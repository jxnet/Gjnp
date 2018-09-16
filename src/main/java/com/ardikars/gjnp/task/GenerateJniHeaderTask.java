/**
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ardikars.gjnp.task;

import com.ardikars.gjnp.extension.GenerateJniHeaderExtension;
import com.ardikars.gjnp.util.StringJoiner;
import org.gradle.api.internal.AbstractTask;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Task for generate JNI header files.
 * @author Ardika Rommy Sanjaya
 * @since 1.0.0
 */
public class GenerateJniHeaderTask extends AbstractTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateJniHeaderTask.class);

    /**
     * Execute javah commands.
     */
    @TaskAction
    public void generateJniHeaderTask() {
        GenerateJniHeaderExtension extension = getProject()
                .getExtensions()
                .findByType(GenerateJniHeaderExtension.class);
        String javah = extension.getJavah();
        if (javah == null || javah.isEmpty()) {
            String defaultJavaHome = System.getProperty("java.home");
            if (defaultJavaHome.endsWith("/jre")) {
                javah = defaultJavaHome.substring(0, defaultJavaHome.lastIndexOf("/jre"));
            } else {
                javah = defaultJavaHome;
            }
            javah += "/bin/javah";
        }
        String destination = extension.getDestination();
        if (destination == null) {
            destination = getProject().getBuildDir().getAbsolutePath() + "/jni/include";
        }
        List<String> defaultClasspath = new ArrayList<>();
        Iterator<File> fileIterator = getProject().getConfigurations().getByName("compile").getFiles().iterator();
        defaultClasspath.addAll(extension.getClassPath());
        while (fileIterator.hasNext()) {
            defaultClasspath.add(fileIterator.next().getAbsolutePath());
        }
        StringJoiner stringJoiner;
        String classpath = org.gradle.internal.os.OperatingSystem.current().isWindows() ?
                new StringJoiner(";").join(defaultClasspath) :
                new StringJoiner(":").join(defaultClasspath);
        List<String> classes = extension.getNativeClasses();
        if (classes == null || classes.isEmpty()) {
            throw new IllegalArgumentException("Classes should be not empty.");
        }
        String nativeClasses = new StringJoiner(" ").join(classes);
        try {
            String command = javah + " "
                    + " -jni -d " + destination
                    + " -classpath " + classpath
                    + " " + nativeClasses + " ";
            Runtime.getRuntime().exec(command);
            LOGGER.debug("Execute: {}", command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

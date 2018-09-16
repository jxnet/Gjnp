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
import org.gradle.api.tasks.AbstractExecTask;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Task for generate JNI header files.
 * @author Ardika Rommy Sanjaya
 * @since 1.0.0
 */
public class GenerateJniHeaderTask extends AbstractExecTask<GenerateJniHeaderTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateJniHeaderTask.class);

    public GenerateJniHeaderTask() {
        super(GenerateJniHeaderTask.class);
    }

    /**
     * Execute javah commands.
     */
    @Override
    @TaskAction
    public void exec() {
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
        if (extension.getClasspath() != null && !extension.getClasspath().isEmpty()) {
            defaultClasspath.addAll(extension.getClasspath());
        }
        defaultClasspath.add(getProject().getBuildDir().getAbsolutePath() + "/classes/java/main");
        Iterator<File> fileIterator = getProject().getConfigurations().getByName("compile").getFiles().iterator();
        while (fileIterator.hasNext()) {
            defaultClasspath.add(fileIterator.next().getAbsolutePath());
        }
        String classpath = org.gradle.internal.os.OperatingSystem.current().isWindows() ?
                new StringJoiner(";").join(defaultClasspath) :
                new StringJoiner(":").join(defaultClasspath);
        List<String> classes = extension.getNativeClasses();
        if (classes == null || classes.isEmpty()) {
            throw new IllegalArgumentException("Classes should be not empty.");
        }
        List<String> commands = new ArrayList<>();
        commands.add(javah);
        commands.add("-jni");
        commands.add("-d");
        commands.add(destination);
        commands.add("-classpath");
        commands.add(classpath);
        commands.addAll(classes);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Command : {}", commands);
        }
        super.setCommandLine(commands);
        super.exec();
    }

}

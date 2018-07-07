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
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.gradle.api.internal.AbstractTask;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateJniHeaderTask extends AbstractTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateJniHeaderTask.class);

    @TaskAction
    public void generateJniHeaderTask() {
        GenerateJniHeaderExtension extension = getProject()
                .getExtensions()
                .findByType(GenerateJniHeaderExtension.class);
        String javah = extension.getJavah();
        String defaultJavaHome = System.getProperty("java.home");
        if (defaultJavaHome.endsWith("/jre")) {
            javah = defaultJavaHome.substring(0, defaultJavaHome.lastIndexOf("/jre"));
        } else {
            javah = defaultJavaHome;
        }
        javah += "/bin/javah";
        Path destination = extension.getDestination();
        if (destination == null) {
            destination = Paths.get(getProject().getBuildDir().getAbsolutePath() + "/jni/include");
        }
        Path classPath = extension.getClassPath();
        if (classPath == null) {
            classPath = Paths.get(getProject().getBuildDir() + "/classes/java/main");
        }
        List<String> classes = extension.getNativeClasses();
        if (classes == null || classes.isEmpty()) {
            throw new IllegalArgumentException("Classes should be not empty.");
        }
        String nativeClasses = String.join(" ", classes);
        try {
            String command = javah + " "
                    + " -jni -d " + destination.toAbsolutePath().toString()
                    + " -classpath " + classPath.toAbsolutePath().toString()
                    + " " + nativeClasses + " ";
            Process process = Runtime.getRuntime().exec(command);
            LOGGER.debug("Execute: {}", command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

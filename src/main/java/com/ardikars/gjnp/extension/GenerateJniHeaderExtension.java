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

package com.ardikars.gjnp.extension;

import java.util.List;

public class GenerateJniHeaderExtension {

    private String javah;
    private String destination;
    private List<String> classpath;
    private List<String> nativeClasses;

    public String getJavah() {
        return javah;
    }

    public void setJavah(String javah) {
        this.javah = javah;
    }

    public List<String> getClasspath() {
        return classpath;
    }

    public void setClassPath(List<String> classpath) {
        this.classpath = classpath;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<String> getNativeClasses() {
        return nativeClasses;
    }

    public void setNativeClasses(List<String> nativeClasses) {
        this.nativeClasses = nativeClasses;
    }

}

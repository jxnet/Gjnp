Gjnp is a gradle plugin for generate JNI Header files.


## How to use.

> Add a plugin to the build.gradle
>
> ```
> plugins {
>     id 'com.ardikars.gjnp' version '1.0.0.Final' apply false
> }
>
> apply plugin: 'com.ardikars.gjnp' 
>
> ```

> Configure a plugin.
> 
>
> ```
> Gnjp {
>     destination = "${rootDir}/gjnp/include" // default: "${buildDir}/jni/include". 
>     nativeClasses = ['com.ardikars.gnjp.JniApp', 'com.ardikars.gjnp.GjnpApp'] // default: Empty list.
>     // classpath = [''] // add more classpath, default: current project classpath + dependencies classpath (jar).
> }
>```



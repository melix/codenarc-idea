The CodeNarc and GMetrics jars which are embedded here don't include the org.codehaus.groovy classes 
which are normally in the distribution. This is done to avoid a classpath messup with IntelliJ IDEA 
which already defines those classes in the embedded Groovy jar.

java.lang.LinkageError: loader constraint violation: loader (instance of com/intellij/ide/plugins/cl/PluginClassLoader) previously initiated loading for a different type with name "org/codehaus/groovy/ast/expr/RegexExpression"
	at java.lang.ClassLoader.defineClass1(Native Method)
	at java.lang.ClassLoader.defineClassCond(ClassLoader.java:631)
	at java.lang.ClassLoader.defineClass(ClassLoader.java:615)
	at java.lang.ClassLoader.defineClass(ClassLoader.java:465)
	at com.intellij.util.lang.UrlClassLoader._defineClass(UrlClassLoader.java:124)
	at com.intellij.util.lang.UrlClassLoader.defineClass(UrlClassLoader.java:120)
	at com.intellij.util.lang.UrlClassLoader._findClass(UrlClassLoader.java:96)
	at com.intellij.ide.plugins.cl.PluginClassLoader.d(PluginClassLoader.java:102)
	at com.intellij.ide.plugins.cl.PluginClassLoader.loadClass(PluginClassLoader.java:63)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:247)


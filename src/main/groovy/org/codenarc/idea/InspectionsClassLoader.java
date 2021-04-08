package org.codenarc.idea;

import java.net.URL;
import java.net.URLClassLoader;

public class InspectionsClassLoader extends URLClassLoader {

    public InspectionsClassLoader() {
        super(new URL[0], CodeNarcInspectionToolProvider.class.getClassLoader());
    }

    public Class<?> defineClass(byte[] data) {
        return super.defineClass(null, data, 0, data.length);
    }

}

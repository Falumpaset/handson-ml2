package de.immomio.config;

import com.google.common.reflect.ClassPath;
import org.springdoc.core.SpringDocUtils;

import java.io.IOException;

/**
 * @author Niklas Lindemann
 */

public abstract class BaseOpenApiDocConfig {
    public void init() {
        final ClassLoader loader = Thread.currentThread()
                .getContextClassLoader();
        try {
            ClassPath classpath = ClassPath.from(loader);
            for (ClassPath.ClassInfo levelClass : classpath.getTopLevelClassesRecursive("de.immomio.controller")) {
                SpringDocUtils.getConfig().addRestControllers(loader.loadClass(levelClass.getName()));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}

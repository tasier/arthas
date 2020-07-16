package com.taobao.arthas.agent;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author beiwei30 on 09/12/2016.
 */
public class ArthasClassloader extends URLClassLoader {
    public ArthasClassloader(URL[] urls) {
        super(urls, ClassLoader.getSystemClassLoader().getParent()); //FIXME ArthasClassloader的父类加载器是ExtClassLoader，下面注释有误 add by binjie
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final Class<?> loadedClass = findLoadedClass(name);
        if (loadedClass != null) {
            return loadedClass;
        }

        // 优先从parent（SystemClassLoader）里加载系统类，避免抛出ClassNotFoundException
        if (name != null && (name.startsWith("sun.") || name.startsWith("java."))) {
            return super.loadClass(name, resolve);
        }
        try {
            Class<?> aClass = findClass(name);
            if (resolve) {
                resolveClass(aClass);
            }
            return aClass;
        } catch (Exception e) {
            // ignore
        }
        return super.loadClass(name, resolve);
    }
}

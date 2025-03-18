package androidx.test.internal.junit5;

import android.util.Log;
import androidx.test.internal.runner.ClassPathScanner;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.platform.commons.util.ClassFilter;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ClasspathScanner implements org.junit.platform.commons.util.ClasspathScanner {

    @Override
    public List<Class<?>> scanForClassesInPackage(String basePackageName, ClassFilter classFilter) {
        System.err.println("Hi from my own Classpath Scanner");
        System.err.println("scan for class in package: " + basePackageName);
        ArrayList<Class<?>> result = new ArrayList<>();
        ClassPathScanner scanner = new ClassPathScanner(ClassPathScanner.getDefaultClasspaths(InstrumentationRegistry.getInstrumentation()));
        try {
            for (String entry : scanner.getClassPathEntries()) {
                System.err.println("classPathEntry: " + entry);
                try {
                    Class<?> clazz = Class.forName(entry);
                    if (classFilter.match(clazz)) {
                        System.err.println("adding: " + clazz);
                        result.add(clazz);
                    }
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public List<Class<?>> scanForClassesInClasspathRoot(URI root, ClassFilter classFilter) {
        System.err.println("Hi from my own Classpath Scanner");
        System.err.println("scan for classes in " + root.getPath());
        return new ArrayList<>();
    }
}

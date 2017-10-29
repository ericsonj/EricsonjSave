package net.ericsonj.save;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.persistence.Entity;

/**
 *
 * @author ejoseph
 */
public class ScannerClass {

    private String directoryPath;

    public ScannerClass() {
    }

    public List<Class> getClasses() throws ClassNotFoundException, IOException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = "";
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            URI uri = new URI(resource.toString());
            if (uri.getPath() != null) {
                dirs.add(new File(uri.getPath()));
            }
        }
        List<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            this.directoryPath = directory.getAbsolutePath();
            List<Class> ls = findClasses(directory);
            for (Class cl : ls) {
                Annotation an = cl.getAnnotation(Entity.class);
                if (an != null) {
                    classes.add(cl);
                }
            }
        }
        return classes;
    }

    private List<Class> findClasses(File directory) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file));
            } else if (file.getName().endsWith(".class")) {
                String classString = file.getAbsolutePath();
                classString = classString.replace(directoryPath, "");
                if (classString.startsWith("/")) {
                    classString = classString.substring(1);
                }
                classString = classString.replace("/", ".").replace(".class", "");
                classes.add(Class.forName(classString));
            }
        }
        return classes;
    }

}

package ladowanie;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class Ladowanie_klas extends ClassLoader
{

    public static List<Class> zaladuj_klasy(String nad_klasa) throws IOException, ClassNotFoundException
    {
        List<Class> klasy_sortujace = new ArrayList<>();

        String sciezka;
        String aktualna_klasa;
        String klasa_o;


        sciezka = "C:\\Users\\Sorin\\Desktop\\cw3javaM\\sorting.jar";
        JarFile jar = new JarFile(sciezka);
        Enumeration<JarEntry> enumeration = jar.entries();

        URL[] urls = {new URL("jar:file:" + sciezka + "!/")};
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (enumeration.hasMoreElements())
        {
            JarEntry jar_wejs = enumeration.nextElement();
            if (jar_wejs.isDirectory() || !jar_wejs.getName().endsWith(".class"))
            {
                continue;
            }

            aktualna_klasa = jar_wejs.getName().substring(0, jar_wejs.getName().length() - 6);


            aktualna_klasa = aktualna_klasa.replace('/', '.');
            Class c = cl.loadClass(aktualna_klasa);

            if (c.getSuperclass() != null)
            {
                klasa_o = nazwa_klasy(c.getSuperclass().getName());
                if (klasa_o.equals(nad_klasa))
                {
                    klasy_sortujace.add(cl.loadClass(aktualna_klasa));
                }
            }
        }
        return klasy_sortujace;
    }

    public static String nazwa_klasy(String fullClassName)
    {
        String nazwa;
        String[] pomoc;

        pomoc = fullClassName.split("\\.");
        nazwa = pomoc[pomoc.length - 1];

        return nazwa;
    }
}


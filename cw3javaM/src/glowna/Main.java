package glowna;

import cw_sort.*;
import ladowanie.Ladowanie_klas;
import pamiec.Mapa;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;

public class Main
{
    //-----------USTAWIENIA PROGRAMU
    public static int zakres_los = 1000;
    public static int liczba_watkow = 20;
    public static int min = 0;
    public static int max = 100000;
    public static int ilosc = 10000;
    public static String rodzaj_klucza = "silne";
    public static String typ_wartosci = "silne";

    //-----------SYNCHRONIZACJA
    public static Object pamiec_podr_synch = new Object();
    public static Object metody_synch = new Object();
    public static Object licznik_synch = new Object();
    public static Object konsola_synch = new Object();

    //-----------LICZNIKI ODNIESIEN DO PAMIECI
    public static int g1 = 0;
    public static int g2 = 0;
    public static int m1 = 0;
    public static int m2 = 0;

    //-----------KONTENERY
    public static List<Method> metody = new ArrayList<>();
    public static List<Long> torba_na_ziarna = new ArrayList<>();

    //-----------ROZNE
    public static final String czerwony_wiadomosc = "\u001B[31m";
    public static final String zielony_wiadomosc = "\u001B[32m";
    public static final String resetuj = "\u001B[0m";
    private static boolean wlacz_wylacz = true;
    public static Mapa pamiec;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    public static void main(String[] args) throws InterruptedException, IOException {



        pamiec = new Mapa(rodzaj_klucza,typ_wartosci);




        Runnable[] runners = new Runnable[liczba_watkow];
        Thread[] threads = new Thread[liczba_watkow];
        for(int i=0; i<liczba_watkow; i++)
        {
            runners[i] = new sortowanie(i);
        }
        for(int i=0; i<liczba_watkow; i++)
        {
            threads[i] = new Thread(runners[i]);
        }
        for(int i=0; i<liczba_watkow; i++)
        {
            threads[i].start();
        }

        try
        {
            glowne_ladowanie();
        } catch (Exception e)
        {
            e.printStackTrace();
        }


        while(wlacz_wylacz==true)
        {
            log_trafien();
        }

        System.in.read();
        wlacz_wylacz = false;
        raport_koncowy();
    }

    public static class sortowanie implements Runnable
    {

        int id_watku;

        public sortowanie(int id_watku)
        {
            this.id_watku=id_watku;
        }



        @Override
        public void run()
        {
            while (wlacz_wylacz==true)
            {
                Random losuj_rozne = new Random();
                Method algorytmy;
                List<IElement> list;
                long losowane_ziarno = losuj_rozne.nextInt(zakres_los);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                if(torba_na_ziarna.contains(losowane_ziarno))
                {
                    trafiony();
                    //System.out.println(zielony_wiadomosc + "Ziarno juz posortowane" + resetuj);
                }
                else
                {
                    nietrafiony();
                    algorytmy = metody.get(losuj_rozne.nextInt(metody.size()));
                    torba_na_ziarna.add(losowane_ziarno);
                    list = losuj_inty(ilosc,min,max,losowane_ziarno);
                    try
                    {
                        algorytmy.invoke(algorytmy.getDeclaringClass().newInstance(), list);
                    } catch (IllegalAccessException e)
                    {
                        e.printStackTrace();
                    } catch (InvocationTargetException e)
                    {
                        e.printStackTrace();
                    } catch (InstantiationException e)
                    {
                        e.printStackTrace();
                    }
                    synchronized (pamiec_podr_synch)
                    {
                        pamiec.posortowane.put(losowane_ziarno,list);
                    }
                    //log_sortowania(algorytmy,id_watku,losowane_ziarno);
                }
            }
        }
    }

    public static void glowne_ladowanie() throws Exception
    {
        Ladowanie_klas sorter = new Ladowanie_klas();
        List<Class> klasy_sortujace;
        klasy_sortujace = sorter.zaladuj_klasy("AbstractIntSorter");
        zaladuj_metody_int(klasy_sortujace);
        klasy_sortujace = sorter.zaladuj_klasy("AbstractFloatSorter");
        zaladuj_metody_float(klasy_sortujace);

    }



    public static void zaladuj_metody_int(List<Class> klasy_sortujace) throws Exception
    {
        synchronized (metody_synch)
        {
            for (int i=0; i<=1;i++)
            {
                Class cl = klasy_sortujace.get(i);
                if(i==1)
                {
                    metody.add(cl.getMethod("solve0", List.class));
                }
                else
                {
                }
            }
        }
    }

    public static void zaladuj_metody_float(List<Class> klasy_sortujace) throws Exception
    {
        synchronized (metody_synch)
        {
            for (Class cl : klasy_sortujace)
            {
                metody.add(cl.getMethod("solve1", List.class));
            }
        }
    }



    public static List<IElement> losuj_inty(int ilosc, int min, int max, long ziarno)
    {
        List<IElement> dane = new ArrayList<>();
        Random generowanie = new Random();
        generowanie.setSeed(ziarno);
        for (int i=0; i<ilosc; i++)
        {
            char litera = (char)(generowanie.nextInt(26) + 'a');
            int wartosc = generowanie.nextInt(max-min+1)+min;
            dane.add(new IntElement(Character.toString(litera),wartosc));
        }

        return dane;
    }

    public static void log_trafien() throws InterruptedException
    {
        Thread.sleep(10000);
        System.out.println(czerwony_wiadomosc + "-----------------------------------------------" + resetuj);
        synchronized (pamiec_podr_synch)
        {
            System.out.println(czerwony_wiadomosc +"Ilosc elementow w pamieci: "+ pamiec.posortowane.size() + resetuj);
        }
        synchronized (licznik_synch)
        {
            System.out.println(czerwony_wiadomosc +"Ogolne odwolania do pamieci (globalnie):" +g1+ resetuj);
            System.out.println(czerwony_wiadomosc +"Ogolne odwolania do pamieci (ten raport):" +g2+ resetuj);
            System.out.println(czerwony_wiadomosc +"Nieudane odwalania do pamieci (globalnie):" +m1+ resetuj);
            System.out.println(czerwony_wiadomosc +"Nieudane odwolania do pamieci (ten raport):" +m2+ resetuj);
            String chybione = DECIMAL_FORMAT.format((float) m1 / g1 * 100);
            System.out.println(czerwony_wiadomosc +"Procent chybien: " + chybione + "%"+ resetuj);
        }
        System.out.println(czerwony_wiadomosc +"-----------------------------------------------" + resetuj);
        zerowanie();

    }

    public static void log_sortowania(Method algorytmy, int id_watku, long losowane_ziarno)
    {
        synchronized (konsola_synch)
        {
            System.out.println("-----------------------------------------------");
            Ladowanie_klas sorter = new Ladowanie_klas();
            System.out.println("Id sortujacego watku: " + id_watku);
            System.out.println("Sortowane ziarno: " + losowane_ziarno);
            System.out.println("Nazwa algorytmu: " + sorter.nazwa_klasy(algorytmy.getDeclaringClass().toString()));
            System.out.println("-----------------------------------------------");
        }
    }

    public static void trafiony()
    {
        synchronized (licznik_synch)
        {
            g1++;
            g2++;
        }
    }

    public static void nietrafiony()
    {
        synchronized (licznik_synch)
        {
            m1++;
            m2++;
            g1++;
            g2++;
        }
    }

    public static void zerowanie()
    {
        synchronized (licznik_synch)
        {
            m2 = 0;
            g2 = 0;
        }
    }

    public static void raport_koncowy()
    {
        int chybione = m1/g1*100;
        System.out.println("Procent chybien: " + chybione + "%");
    }
}

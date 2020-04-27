package pamiec;

import cw_sort.*;
import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.apache.commons.collections4.map.ReferenceMap;
import java.util.*;

public class Mapa
{
    public Mapa(String rodzaj_klucza, String typ_wartosci)
    {
        AbstractReferenceMap.ReferenceStrength klucz = null;
        AbstractReferenceMap.ReferenceStrength wartosc = null;

        if(rodzaj_klucza=="silne")
        {
            klucz = ref_silne(rodzaj_klucza);
        }
        if(rodzaj_klucza=="slabe")
        {
            klucz = ref_slabe(rodzaj_klucza);
        }
        if(rodzaj_klucza=="miekkie")
        {
            klucz = ref_miekkie(rodzaj_klucza);
        }
        
        if(typ_wartosci=="silne")
        {
            wartosc = ref_silne(typ_wartosci);
        }
        if(typ_wartosci=="slabe")
        {
            wartosc = ref_slabe(typ_wartosci);
        }
        if(typ_wartosci=="miekkie")
        {
            wartosc = ref_miekkie(typ_wartosci);
        }
        
        posortowane = Collections.synchronizedMap(new ReferenceMap<>(klucz,wartosc));
    }

    public static Map<Object, Object> posortowane;

    private AbstractReferenceMap.ReferenceStrength ref_silne(String typ)
    {
        return AbstractReferenceMap.ReferenceStrength.HARD;
    }

    private AbstractReferenceMap.ReferenceStrength ref_miekkie(String typ)
    {
        return AbstractReferenceMap.ReferenceStrength.SOFT;
    }

    private AbstractReferenceMap.ReferenceStrength ref_slabe(String typ)
    {
        return AbstractReferenceMap.ReferenceStrength.WEAK;
    }

    public static void wloz_posortowane(List<IElement> list, int ziarno)
    {
        posortowane.put(ziarno,list);
    }

    public static void wyswietl_ziarno(int ziarno)
    {
        System.out.print(posortowane);
        System.out.println();
    }
}

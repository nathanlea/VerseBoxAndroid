package com.fibonaccistudios.nathanlea.versebox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Nathan on 7/20/2016.
 */
public class VerseCardSort {

    public static List<VerseCard> sortbyDate( ) {
        Map<Integer, VerseCard> integerVerseCardMap = new HashMap<>();

        for (int i = 0; i < MainActivity.verseList.size(); i++) {
            int sortBy = 0;

            String s = MainActivity.verseList.get(i).getStartDate();
            String date[] = s.split("/");
            //assert date.length!=3;
            int i1 = Integer.parseInt(date[2]);
            int i2 = Integer.parseInt(date[1]);
            int i3 = Integer.parseInt(date[0]);

            sortBy = i1 * 10000 + i2 * 100 + i3;

            integerVerseCardMap.put(sortBy, MainActivity.verseList.get(i));
        }

        Map<Integer, VerseCard> treeMap = new TreeMap<>(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o2.compareTo(o1);
                    }
                });
        treeMap.putAll(integerVerseCardMap);

        List<VerseCard> t = new ArrayList<>();

        for( Map.Entry<Integer, VerseCard> entry : treeMap.entrySet()) {
            t.add(entry.getValue());
        }

        return t;
    }

    public static List<VerseCard> sortbyBook ( ) {

        Map<Integer, VerseCard> integerVerseCardMap = new HashMap<>();

        for (int i = 0; i < MainActivity.verseList.size(); i++) {
            int sortBy = MainActivity.verseList.get(i).getBookIndex();

            integerVerseCardMap.put(sortBy, MainActivity.verseList.get(i));
        }

        Map<Integer, VerseCard> treeMap = new TreeMap<>(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
        treeMap.putAll(integerVerseCardMap);

        List<VerseCard> t = new ArrayList<>();

        for( Map.Entry<Integer, VerseCard> entry : treeMap.entrySet()) {
            t.add(entry.getValue());
        }
        return t;
    }
}

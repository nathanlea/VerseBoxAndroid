package com.fibonaccistudios.nathanlea.versebox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        Map<Integer,ArrayList<VerseCard>> multiMap = new HashMap<>();
        ArrayList<Integer> index = new ArrayList<>();

        for (int i = 0; i < MainActivity.verseList.size(); i++) {
            boolean exists = false;
            for (Integer t : index) {
                if (t == MainActivity.verseList.get(i).getBookIndex()) {
                    exists = true;
                }
            }
            if (!exists) {
                index.add(MainActivity.verseList.get(i).getBookIndex());
                multiMap.put(MainActivity.verseList.get(i).getBookIndex(), new ArrayList<VerseCard>());
            }
            multiMap.get(MainActivity.verseList.get(i).getBookIndex()).add(MainActivity.verseList.get(i));
        }

        Map<Integer, ArrayList<VerseCard>> treeMap = new TreeMap<>(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1.compareTo(o2);
                    }
                });
        treeMap.putAll(multiMap);
        List<VerseCard> t = new ArrayList<>();

        for( Map.Entry<Integer, ArrayList<VerseCard>> entry : treeMap.entrySet()) {
            for(VerseCard verseCard : entry.getValue()) {
                t.add(verseCard);
            }
        }
        return t;
    }

    public static  Map<String, Integer> buildTextSizeTopic( ) {

        Map<String,ArrayList<VerseCard>> multiMap = new HashMap<>();
        ArrayList<String> topics = new ArrayList<>();

        for (int i = 0; i < MainActivity.verseList.size(); i++) {
            boolean exists = false;
            for(String t : topics) {
                if(t.equals(MainActivity.verseList.get(i).getTopic())) {
                    exists = true;
                }
            }
            if(!exists){
                topics.add(MainActivity.verseList.get(i).getTopic());
                multiMap.put(MainActivity.verseList.get(i).getTopic(), new ArrayList<VerseCard>());
            }
            multiMap.get(MainActivity.verseList.get(i).getTopic()).add(MainActivity.verseList.get(i));
        }

        float total = multiMap.size();

        Map<String, Integer> textSizeTopic = new HashMap<>();

        for(int i = 0; i < topics.size(); i++) {
            float size = multiMap.get(topics.get(i)).size();

            float per = size/total;

            int textSize = (int)(100*per);
            if(textSize<25) { textSize = 25; }
            textSizeTopic.put(topics.get(i), textSize);
        }
        return textSizeTopic;
    }

    public static Map<String,ArrayList<VerseCard>> orderByTopics ( ) {
        Map<String,ArrayList<VerseCard>> multiMap = new HashMap<>();
        ArrayList<String> topic = new ArrayList<>();

        for (int i = 0; i < MainActivity.verseList.size(); i++) {
            boolean exists = false;
            if(!MainActivity.verseList.get(i).getTopic().equals("") ) {
                for (String t : topic) {
                    if (t.equals(MainActivity.verseList.get(i).getTopic())) {
                        exists = true;
                    }
                }
                if (!exists) {
                    topic.add(MainActivity.verseList.get(i).getTopic());
                    multiMap.put(MainActivity.verseList.get(i).getTopic(), new ArrayList<VerseCard>());
                }
                multiMap.get(MainActivity.verseList.get(i).getTopic()).add(MainActivity.verseList.get(i));
            }
        }
        return multiMap;
    }

    public static Map<String,ArrayList<VerseCard>> orderBySections ( ) {
        Map<String,ArrayList<VerseCard>> multiMap = new HashMap<>();
        ArrayList<String> section = new ArrayList<>();

        for (int i = 0; i < MainActivity.verseList.size(); i++) {
            boolean exists = false;
            if(!MainActivity.verseList.get(i).getSection().equals("") ) {
                for (String t : section) {
                    if (t.equals(MainActivity.verseList.get(i).getSection())) {
                        exists = true;
                    }
                }
                if (!exists) {
                    section.add(MainActivity.verseList.get(i).getSection());
                    multiMap.put(MainActivity.verseList.get(i).getSection(), new ArrayList<VerseCard>());
                }
                multiMap.get(MainActivity.verseList.get(i).getSection()).add(MainActivity.verseList.get(i));
            }
        }
        return multiMap;
    }

    public static  Map<String, Integer> buildTextSizeSection( ) {

        Map<String,ArrayList<VerseCard>> multiMap = new HashMap<>();
        ArrayList<String> section = new ArrayList<>();

        for (int i = 0; i < MainActivity.verseList.size(); i++) {
            boolean exists = false;
            if(!MainActivity.verseList.get(i).getSection().equals("") ) {
                for (String t : section) {
                    if (t.equals(MainActivity.verseList.get(i).getSection())) {
                        exists = true;
                    }
                }
                if (!exists) {
                    section.add(MainActivity.verseList.get(i).getSection());
                    multiMap.put(MainActivity.verseList.get(i).getSection(), new ArrayList<VerseCard>());
                }
                multiMap.get(MainActivity.verseList.get(i).getSection()).add(MainActivity.verseList.get(i));
            }
        }

        float total = multiMap.size();

        Map<String, Integer> textSizeTopic = new HashMap<>();

        for(int i = 0; i < section.size(); i++) {
            float size = multiMap.get(section.get(i)).size();

            float per = size/total;

            int textSize = (int)(100*per);
            if(textSize<25) { textSize = 25; }
            textSizeTopic.put(section.get(i), textSize);
        }
        return textSizeTopic;
    }
}

package com.developndesign.telehealthpatient.utils;

public class CommonMethods {
    public static String capitalizeWord(String str) {
        String words[] = str.split("\\s");
        String words1[] = str.split("/");
        StringBuilder capitalizeWord = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty()){
                String first = w.substring(0, 1);
                String afterfirst = w.substring(1);
                capitalizeWord.append(first.toUpperCase()).append(afterfirst).append(" ");
            }
        }
        return capitalizeWord.toString().trim();
    }

}

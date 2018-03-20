package com.bytepace.ditsch.utils;

import java.util.Random;

/**
 * Testing module, returns random word or sentence
 */
public class RandStr {
    private static final int allchance = 100;
    private static final int startvovel = 20;
    private static final int badvovel = 13;
    private static final int stopchance = 30;
    private static final int doublevovel = 4;
    private static final int badletter = 13;
    private static final int doubleletter = 10;
    private static final int minlen = 4;

    private static String[] vovels_rus = {"у", "е", "а", "о", "я", "и"};
    private static String[] badvovels_rus = {"ы", "э", "ю"};
    private static String[] nice_rus = {"к", "н", "г", "л", "р", "п", "д", "в", "м", "т", "с", "б"};
    private static String[] bad_rus = {"ц", "ш", "щ", "з", "ж", "ф", "ч"};
    private static String[] vovels_eng = {"a", "e", "i", "o", "u", "y"};
    private static String[] badvovels_eng = {};
    private static String[] nice_eng = {"r", "t", "p", "s", "d", "f", "g", "h", "k", "l", "c", "v", "b", "n", "m"};
    private static String[] bad_eng = {"q", "w", "j", "z", "x"};

    public static String getWord(boolean english) {
        StringBuilder s = new StringBuilder();


        Random rand = new Random();
        boolean vovel = rand.nextInt(allchance) < startvovel;
        String[] vovels = vovels_rus;
        String[] badvovels = badvovels_rus;
        String[] nice = nice_rus;
        String[] bad = bad_rus;
        if (english) {
            vovels = vovels_eng;
            badvovels = badvovels_eng;
            nice = nice_eng;
            bad = bad_eng;
        }
        int badvovel = RandStr.badvovel;
        if (english) {
            badvovel = -1;
        }
        while (rand.nextInt(allchance) > stopchance || s.length() < minlen) {
            if (vovel) {
                if (rand.nextInt(allchance) > badvovel) {
                    s.append(vovels[rand.nextInt(vovels.length)]);
                } else {
                    s.append(badvovels[rand.nextInt(badvovels.length)]);
                }
                if (rand.nextInt(allchance) > doublevovel) {
                    vovel = false;
                }
            } else {
                if (rand.nextInt(allchance) > badletter) {
                    s.append(nice[rand.nextInt(nice.length)]);
                } else {
                    s.append(bad[rand.nextInt(bad.length)]);
                }
                if (rand.nextInt(allchance) > doubleletter) {
                    vovel = true;
                }
            }

        }
        return s.toString();
    }

    public static String getSentence(boolean english) {
        String s = "";
        Random rand = new Random();
        while (rand.nextInt(allchance) > stopchance || s.length() < minlen) {
            s += getWord(english) + " ";
        }
        return s;
    }
}

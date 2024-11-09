package org.aoc.y2023.day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Trebuchet {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2023\\day1\\input.txt";
    private static final char ZERO = '0';
    private static final char NINE = '9';
    private static final Map<String, Integer> DIGIT_BY_LETTER = new HashMap<>() {{
        put("one", 1);
        put("two", 2);
        put("three", 3);
        put("four", 4);
        put("five", 5);
        put("six", 6);
        put("seven", 7);
        put("eight", 8);
        put("nine", 9);
    }};

    public static void main(String[] args) {
        List<String> input = parseInput();
        int resP1 = 0;
        int resP2 = 0;
        for (String s : input) {
            int firstIndexDigitP1 = getFirstIndexDigitP1(s);
            int lastIndexDigitP1 = getLastIndexDigitP1(s);
            int firstDigitP1 = s.charAt(firstIndexDigitP1) - ZERO;
            int lastDigitP1 = s.charAt(lastIndexDigitP1) - ZERO;
            int firstDigitP2 = getFirstDigitP2(s);
            int lastDigitP2 = getLastDigitP2(s);
            resP1 += firstDigitP1 * 10 + lastDigitP1;
            resP2 += firstDigitP2 * 10 + lastDigitP2;
        }
        System.out.println(resP1);
        System.out.println(resP2);
    }

    private static List<String> parseInput() {
        List<String> input = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                input.add(scanner.nextLine());
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return input;
    }

    private static int getFirstIndexDigitP1(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) >= ZERO && input.charAt(i) <= NINE) {
                return i;
            }
        }
        return -1;
    }

    private static int getLastIndexDigitP1(String input) {
        for (int i = input.length() - 1; i >= 0; i--) {
            if (input.charAt(i) >= ZERO && input.charAt(i) <= NINE) {
                return i;
            }
        }
        return -1;
    }

    private static int getFirstDigitP2(String input) {
        String firstKey = "";
        int firstIndexByChar = input.length();
        for (Map.Entry<String, Integer> entry : DIGIT_BY_LETTER.entrySet()) {
            int index = input.indexOf(entry.getKey());
            if (index != -1 && index < firstIndexByChar) {
                firstIndexByChar = index;
                firstKey = entry.getKey();
            }
        }
        int firstIndexByDigit = getFirstIndexDigitP1(input);
        if (firstIndexByChar != input.length() && firstIndexByChar < firstIndexByDigit) {
            return DIGIT_BY_LETTER.get(firstKey);
        }
        return input.charAt(firstIndexByDigit) - ZERO;
    }

    private static int getLastDigitP2(String input) {
        String lastKey = "";
        int lastIndexByChar = -1;
        for (Map.Entry<String, Integer> entry : DIGIT_BY_LETTER.entrySet()) {
            int index = input.lastIndexOf(entry.getKey());
            if (index != -1 && index > lastIndexByChar) {
                lastIndexByChar = index;
                lastKey = entry.getKey();
            }
        }
        int lastIndexByDigit = getLastIndexDigitP1(input);
        if (lastIndexByChar != -1 && lastIndexByChar > lastIndexByDigit) {
            return DIGIT_BY_LETTER.get(lastKey);
        }
        return input.charAt(lastIndexByDigit) - ZERO;
    }
}

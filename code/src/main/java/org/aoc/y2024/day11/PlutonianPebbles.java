package org.aoc.y2024.day11;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PlutonianPebbles {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day11\\input.txt";
    private static final String SPACE_SEPARATOR = " ";
    private static final int DEPTH_P1 = 25;
    private static final int DEPTH_P2 = 75;
    private static final String ZERO = "0";
    private static final String ONE = "1";
    private static final String MULTIPLE_FACTOR = "2024";
    private static final Map<Element, BigInteger> memory = new HashMap<>();

    public static void main(String[] args) {
        BigInteger resP1 = new BigInteger(ZERO);
        BigInteger resP2 = new BigInteger(ZERO);
        List<String> stones = parseInput();
        for (String stone: stones) {
            resP1 = resP1.add(count(stone, DEPTH_P1));
            resP2 = resP2.add(count(stone, DEPTH_P2));
        }
        System.out.println(resP1);
        System.out.println(resP2);
    }

    private static List<String> parseInput() {
        List<String> input = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                String[] rawComponents = row.split(SPACE_SEPARATOR);
                input.addAll(Arrays.asList(rawComponents));
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return input;
    }

    private static BigInteger count(String s, int depth) {
        if (memory.containsKey(new Element(s, depth))) {
            return memory.get(new Element(s, depth));
        }

        BigInteger res = new BigInteger(ZERO);

        if (depth == 0) {
            return new BigInteger(ONE);
        } else if (s.equals(ZERO)) {
            res = count(ONE, depth - 1);
        } else if (s.length() % 2 == 0) {
            int mid = s.length() / 2;
            res = res.add(count(new BigInteger(s.substring(0, mid)).toString(), depth - 1));
            res = res.add(count(new BigInteger(s.substring(mid)).toString(), depth - 1));
        } else {
            BigInteger intValue = new BigInteger(s);
            res = count(intValue.multiply(new BigInteger(MULTIPLE_FACTOR)).toString(), depth - 1);
        }

        memory.put(new Element(s, depth), res);
        return res;
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class Element {
        private String value;
        private int depth;
    }
}

package org.aoc.y2024.day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HistorianHysteria {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day1\\input.txt";
    private static final String TRIPLE_SPACE_SEPARATOR = "   ";

    public static void main(String[] args) {
        List<List<Integer>> input = parseInput();
        Collections.sort(input.get(0));
        Collections.sort(input.get(1));
        Map<Integer, Long> occurrences = groupNumberOccurrence(input.get(1));
        System.out.println(calculateTotalDistance(input.get(0), input.get(1)));
        System.out.println(calculateSimilarityScore(input.get(0), occurrences));
    }

    private static List<List<Integer>> parseInput() {
        List<List<Integer>> input = new ArrayList<>();
        input.add(new ArrayList<>());
        input.add(new ArrayList<>());

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                String[] rawInputs = row.split(TRIPLE_SPACE_SEPARATOR);
                input.get(0).add(Integer.valueOf(rawInputs[0]));
                input.get(1).add(Integer.valueOf(rawInputs[1]));
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return input;
    }

    private static Map<Integer, Long> groupNumberOccurrence(List<Integer> lst) {
        return lst.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private static int calculateTotalDistance(List<Integer> lst1, List<Integer> lst2) {
        int res = 0;

        for (int i = 0; i < lst1.size(); i++) {
            res += Math.abs(lst1.get(i) - lst2.get(i));
        }

        return res;
    }

    private static long calculateSimilarityScore(List<Integer> lst, Map<Integer, Long> occurrence) {
        long res = 0;

        for (Integer e : lst) {
            res += e * occurrence.getOrDefault(e, 0L);
        }

        return res;
    }
}

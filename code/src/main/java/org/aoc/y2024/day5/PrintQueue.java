package org.aoc.y2024.day5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class PrintQueue {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day5\\input.txt";
    private static final String COMMAS_SEPARATOR = ",";
    private static final String VERTICAL_BAR_SEPARATOR = "\\|";

    public static void main(String[] args) {
        int resP1 = 0;
        int resP2 = 0;
        List<Object> objects = parseInput();
        Map<Integer, Set<Integer>> pageOrderingRules = (Map<Integer, Set<Integer>>) objects.get(0);
        List<List<Integer>> pageNumbersList = (List<List<Integer>>) objects.get(1);

        for (List<Integer> pageNumbers: pageNumbersList) {
            if (isCorrectOrder(pageOrderingRules, pageNumbers)) {
                resP1 += pageNumbers.get(pageNumbers.size() / 2);
            } else {
                List<Integer> correctPageNumbers = getCorrectOrder(pageOrderingRules, pageNumbers);
                resP2 += correctPageNumbers.get(correctPageNumbers.size() / 2);
            }
        }
        System.out.println(resP1);
        System.out.println(resP2);
    }

    private static List<Object> parseInput() {
        Map<Integer, Set<Integer>> pageOrderingRules = new HashMap<>();
        List<List<Integer>> pageNumbersList = new ArrayList<>();
        boolean isReachEmptyRow = false;

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                if (row.isEmpty()) {
                    isReachEmptyRow = true;
                } else {
                    if (isReachEmptyRow) {
                        String[] rawInput = row.split(COMMAS_SEPARATOR);
                        List<Integer> pageNumbers = new ArrayList<>();
                        for (String numberStr: rawInput) {
                            pageNumbers.add(Integer.parseInt(numberStr));
                        }
                        pageNumbersList.add(pageNumbers);
                    } else {
                        String[] rawInput = row.split(VERTICAL_BAR_SEPARATOR);
                        Integer pageKey = Integer.parseInt(rawInput[0]);
                        Integer pageFollowing = Integer.parseInt(rawInput[1]);
                        Set<Integer> curPageFollowings = pageOrderingRules.containsKey(pageKey)
                                ? pageOrderingRules.get(pageKey)
                                : new HashSet<>();
                        curPageFollowings.add(pageFollowing);
                        pageOrderingRules.put(pageKey, curPageFollowings);
                    }
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return List.of(pageOrderingRules, pageNumbersList);
    }

    private static boolean isCorrectOrder(Map<Integer, Set<Integer>> pageOrderingRules, List<Integer> pageNumbers) {
        for (int i = 0; i < pageNumbers.size() - 1; i++) {
            Integer pageNumber = pageNumbers.get(i);
            for (int j = i+1; j < pageNumbers.size(); j++) {
                if (pageOrderingRules.get(pageNumber) == null || !pageOrderingRules.get(pageNumber).contains(pageNumbers.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<Integer> getCorrectOrder(Map<Integer, Set<Integer>> pageOrderingRules, List<Integer> pageNumbers) {
        List<Integer> correctPageNumbers = new ArrayList<>();
        while (true) {
            if (pageNumbers.size() == 1) {
                correctPageNumbers.add(pageNumbers.getFirst());
                break;
            }
            for (int i = 0; i < pageNumbers.size(); i++) {
                AtomicInteger tmp = new AtomicInteger(i);
                Set<Integer> pageFollowings = pageOrderingRules.get(pageNumbers.get(i));
                List<Integer> subPageNumbers = pageNumbers.stream()
                        .filter(num -> pageNumbers.indexOf(num) != tmp.get())
                        .toList();
                if (pageFollowings != null && pageFollowings.containsAll(subPageNumbers)) {
                    correctPageNumbers.add(pageNumbers.get(i));
                    pageNumbers.remove(i);
                    break;
                }
            }
        }
        return correctPageNumbers;
    }
}

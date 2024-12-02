package org.aoc.y2024.day2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RedNosedReports {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day2\\input.txt";
    private static final String SPACE_SEPARATOR = " ";
    private static final int MAX_LEVEL_DIFFER = 3;
    private static final int MIN_LEVEL_DIFFER = 1;

    public static void main(String[] args) {
        List<List<Integer>> input = parseInput();
        int resP1 = 0, resP2 = 0;

        for (List<Integer> lst : input) {
            if (isSafeP1(lst)) {
                resP1++;
            }
            if (isSafeP2(lst)) {
                resP2++;
            }
        }

        System.out.println(resP1);
        System.out.println(resP2);
    }

    private static List<List<Integer>> parseInput() {
        List<List<Integer>> input = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                List<Integer> record = new ArrayList<>();
                String row = scanner.nextLine();
                String[] rawInputs = row.split(SPACE_SEPARATOR);
                for (String e : rawInputs) {
                    record.add(Integer.valueOf(e));
                }
                input.add(record);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return input;
    }

    private static boolean isAscending(List<Integer> lst) {
        for (int i = 0; i < lst.size() - 1; i++) {
            if (lst.get(i) > lst.get(i+1)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isDescending(List<Integer> lst) {
        for (int i = 0; i < lst.size() - 1; i++) {
            if (lst.get(i) < lst.get(i+1)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSafeP1(List<Integer> lst) {
        if (isAscending(lst) || isDescending(lst)) {
            for (int i = 0; i < lst.size() - 1; i++) {
                int abs = Math.abs(lst.get(i+1) - lst.get(i));
                if (abs < MIN_LEVEL_DIFFER || abs > MAX_LEVEL_DIFFER) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean isSafeP2(List<Integer> lst) {
        for (int i = 0; i < lst.size(); i++) {
            List<Integer> cloneLst = new ArrayList<>(lst);
            cloneLst.remove(i);
            if (isSafeP1(cloneLst)) {
                return true;
            }
        }
        return false;
    }
}

package org.aoc.y2024.day4;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CeresSearch {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day4\\input.txt";
    private static final String XMAS = "XMAS";

    public static void main(String[] args) {
        int resP1 = 0;
        int resP2 = 0;
        List<List<Character>> input = parseInput();
        List<Position> xPositions = findChar(input, XMAS.charAt(0));
        for (Position e: xPositions) {
            resP1 += countXMASP1(input, e);
        }
        List<Position> aPositions = findChar(input, XMAS.charAt(2));
        for (Position e: aPositions) {
            resP2 += countXMASP2(input, e);
        }
        System.out.println(resP1);
        System.out.println(resP2);
    }

    private static List<List<Character>> parseInput() {
        List<List<Character>> input = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                List<Character> lst = new ArrayList<>();
                String row = scanner.nextLine();
                for (char c: row.toCharArray()) {
                    lst.add(c);
                }
                input.add(lst);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return input;
    }

    private static List<Position> findChar(List<List<Character>> input, char c) {
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.get(i).size(); j++) {
                if (input.get(i).get(j) == c) {
                    positions.add(new Position(i, j));
                }
            }
        }
        return positions;
    }

    private static int countXMASP1(List<List<Character>> input, Position position) {
        int res = 0;

        if (isCorrectChar(input, position.row, position.col + 1, XMAS.charAt(1))
                && isCorrectChar(input, position.row, position.col + 2, XMAS.charAt(2))
                && isCorrectChar(input, position.row, position.col + 3, XMAS.charAt(3))) {
            res++;
        }

        if (isCorrectChar(input, position.row, position.col - 1, XMAS.charAt(1))
                && isCorrectChar(input, position.row, position.col - 2, XMAS.charAt(2))
                && isCorrectChar(input, position.row, position.col - 3, XMAS.charAt(3))) {
            res++;
        }

        if (isCorrectChar(input, position.row + 1, position.col, XMAS.charAt(1))
                && isCorrectChar(input, position.row + 2, position.col, XMAS.charAt(2))
                && isCorrectChar(input, position.row + 3, position.col, XMAS.charAt(3))) {
            res++;
        }

        if (isCorrectChar(input, position.row - 1, position.col, XMAS.charAt(1))
                && isCorrectChar(input, position.row - 2, position.col, XMAS.charAt(2))
                && isCorrectChar(input, position.row - 3, position.col, XMAS.charAt(3))) {
            res++;
        }

        if (isCorrectChar(input, position.row - 1, position.col + 1, XMAS.charAt(1))
                && isCorrectChar(input, position.row - 2, position.col + 2, XMAS.charAt(2))
                && isCorrectChar(input, position.row - 3, position.col + 3, XMAS.charAt(3))) {
            res++;
        }

        if (isCorrectChar(input, position.row + 1, position.col - 1, XMAS.charAt(1))
                && isCorrectChar(input, position.row + 2, position.col - 2, XMAS.charAt(2))
                && isCorrectChar(input, position.row + 3, position.col - 3, XMAS.charAt(3))) {
            res++;
        }

        if (isCorrectChar(input, position.row + 1, position.col + 1, XMAS.charAt(1))
                && isCorrectChar(input, position.row + 2, position.col + 2, XMAS.charAt(2))
                && isCorrectChar(input, position.row + 3, position.col + 3, XMAS.charAt(3))) {
            res++;
        }

        if (isCorrectChar(input, position.row - 1, position.col - 1, XMAS.charAt(1))
                && isCorrectChar(input, position.row - 2, position.col - 2, XMAS.charAt(2))
                && isCorrectChar(input, position.row - 3, position.col - 3, XMAS.charAt(3))) {
            res++;
        }

        return res;
    }

    private static int countXMASP2(List < List < Character >> input, Position position) {
        int res = 0;

        if (isCorrectChar(input, position.row - 1, position.col - 1, XMAS.charAt(1))
                && isCorrectChar(input, position.row + 1, position.col + 1, XMAS.charAt(3))
                && isCorrectChar(input, position.row + 1, position.col - 1, XMAS.charAt(1))
                && isCorrectChar(input, position.row - 1, position.col + 1, XMAS.charAt(3))) {
            res++;
        } else if (isCorrectChar(input, position.row - 1, position.col - 1, XMAS.charAt(3))
                && isCorrectChar(input, position.row + 1, position.col + 1, XMAS.charAt(1))
                && isCorrectChar(input, position.row + 1, position.col - 1, XMAS.charAt(1))
                && isCorrectChar(input, position.row - 1, position.col + 1, XMAS.charAt(3))) {
            res++;
        } else if (isCorrectChar(input, position.row - 1, position.col - 1, XMAS.charAt(1))
                && isCorrectChar(input, position.row + 1, position.col + 1, XMAS.charAt(3))
                && isCorrectChar(input, position.row + 1, position.col - 1, XMAS.charAt(3))
                && isCorrectChar(input, position.row - 1, position.col + 1, XMAS.charAt(1))) {
            res++;
        } else if (isCorrectChar(input, position.row - 1, position.col - 1, XMAS.charAt(3))
                && isCorrectChar(input, position.row + 1, position.col + 1, XMAS.charAt(1))
                && isCorrectChar(input, position.row + 1, position.col - 1, XMAS.charAt(3))
                && isCorrectChar(input, position.row - 1, position.col + 1, XMAS.charAt(1))) {
            res++;
        }

        return res;
    }

    private static boolean isCorrectChar(List<List<Character>> input, int row, int col, char c) {
        if (row < 0 || col < 0 || row >= input.size() || col >= input.getFirst().size()) {
            return false;
        }
        return input.get(row).get(col) == c;
    }

    @Data
    @AllArgsConstructor
    private static class Position {
        private int row;
        private int col;
    }
}

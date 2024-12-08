package org.aoc.y2024.day8;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ResonantCollinearity {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day8\\input.txt";
    private static final char ZERO = '0';
    private static final char NINE = '9';
    private static final char LOWER_A = 'a';
    private static final char LOWER_Z = 'z';
    private static final char UPPER_A = 'A';
    private static final char UPPER_Z = 'Z';
    private static final char ANTINODE = '#';

    public static void main(String[] args) {
        List<List<Character>> map = parseInput();
        List<Position> positions = getAntennaFromMap(map);
        System.out.println(countP1(map, positions));
        System.out.println(countP2(map, positions));
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

    private static List<Position> getAntennaFromMap(List<List<Character>> map) {
        List < Position > positions = new ArrayList< >();
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                char c = map.get(i).get(j);
                if ((c >= ZERO && c <= NINE) || (c >= LOWER_A && c <= LOWER_Z) || (c >= UPPER_A && c <= UPPER_Z)) {
                    positions.add(new Position(i, j, c));
                }
            }
        }
        return positions;
    }

    private static int countP1(List<List<Character>> map, List<Position> positions) {
        Set<Position> newPositions = new HashSet<>();
        for (int i = 0; i < positions.size() - 1; i++) {
            Position position1 = positions.get(i);
            for (int j = i + 1; j < positions.size(); j++) {
                Position position2 = positions.get(j);
                if (position1.getC() == position2.getC()) {
                    int count = 1;
                    int diffX = position1.getX() - position2.getX();
                    int diffY = position1.getY() - position2.getY();
                    int newX1 = position1.getX() + diffX * count;
                    int newX2 = position2.getX() - diffX * count;
                    int newY1 = position1.getY() + diffY * count;
                    int newY2 = position2.getY() - diffY * count;
                    if (!isOutsideMap(map, newX1, newY1)) {
                        newPositions.add(new Position(newX1, newY1, ANTINODE));
                    }
                    if (!isOutsideMap(map, newX2, newY2)) {
                        newPositions.add(new Position(newX2, newY2, ANTINODE));
                    }
                }
            }
        }
        return newPositions.size();
    }


    private static int countP2(List<List<Character>> map, List<Position> positions) {
        Set<Position> newPositions = new HashSet<>();
        for (int i = 0; i < positions.size() - 1; i++) {
            Position position1 = positions.get(i);
            for (int j = i + 1; j < positions.size(); j++) {
                Position position2 = positions.get(j);
                if (position1.getC() == position2.getC()) {
                    int count = 1;
                    int diffX = position1.getX() - position2.getX();
                    int diffY = position1.getY() - position2.getY();
                    do {
                        boolean isExit = true;
                        int newX1 = position1.getX() + diffX * count;
                        int newX2 = position2.getX() - diffX * count;
                        int newY1 = position1.getY() + diffY * count;
                        int newY2 = position2.getY() - diffY * count;
                        if (!isOutsideMap(map, newX1, newY1)) {
                            newPositions.add(new Position(newX1, newY1, ANTINODE));
                            isExit = false;
                        }
                        if (!isOutsideMap(map, newX2, newY2)) {
                            newPositions.add(new Position(newX2, newY2, ANTINODE));
                            isExit = false;
                        }
                        if (isExit) {
                            break;
                        }
                        count++;
                    } while (true);
                }
            }
        }
        newPositions.addAll(positions);
        return newPositions.size();
    }

    private static boolean isOutsideMap(List <List< Character >> map, int x, int y) {
        return x < 0 || y < 0 || x >= map.size() || y >= map.get(0).size();
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = "c")
    private static class Position {
        private int x;
        private int y;
        private char c;
    }
}

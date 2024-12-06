package org.aoc.y2024.day6;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class GuardGallivant {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day6\\input.txt";
    private static final char UP = '^';
    private static final char DOWN = 'v';
    private static final char LEFT = '>';
    private static final char RIGHT = '<';
    private static final char OBSTACLE = '#';
    private static final int MAX_STEP = 10000;
    
    public static void main(String[] args) {
        List<List<Character>> map = parseInput();
        Position start = findStartPosition(map);
        Set<Position> positions = travelAroundMap(map, start);
        System.out.println(positions.size());
        System.out.println(countObstruction(map, positions, start));
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

    private static Position findStartPosition(List<List<Character>> map) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) == UP) {
                    return new Position(i, j);
                }
            }
        }
        return new Position(-1, -1);
    }

    private static Set<Position> travelAroundMap(List<List<Character>> map, Position start) {
        Set<Position> positions = new HashSet<>();
        int maxRow = map.size();
        int maxCol = map.getFirst().size();
        int curRow = start.getX();
        int curCol = start.getY();
        char curDirection = UP;
        int count = 0;
        positions.add(start);

        while (!isOutsideMap(maxRow, maxCol, curRow, curCol) && count < MAX_STEP) {
            switch (curDirection) {
                case UP:
                    if (isObstacle(map, curRow, curCol)) {
                        positions.remove(new Position(curRow, curCol));
                        curDirection = LEFT;
                        curRow++;
                    } else {
                        curRow--;
                        positions.add(new Position(curRow, curCol));
                    }
                    break;
                case LEFT:
                    if (isObstacle(map, curRow, curCol)) {
                        positions.remove(new Position(curRow, curCol));
                        curDirection = DOWN;
                        curCol--;
                    } else {
                        curCol++;
                        positions.add(new Position(curRow, curCol));
                    }
                    break;
                case RIGHT:
                    if (isObstacle(map, curRow, curCol)) {
                        positions.remove(new Position(curRow, curCol));
                        curDirection = UP;
                        curCol++;
                    } else {
                        curCol--;
                        positions.add(new Position(curRow, curCol));
                    }
                    break;
                case DOWN:
                    if (isObstacle(map, curRow, curCol)) {
                        positions.remove(new Position(curRow, curCol));
                        curDirection = RIGHT;
                        curRow--;
                    } else {
                        curRow++;
                        positions.add(new Position(curRow, curCol));
                    }
            }
            count++;
        }

        positions.remove(new Position(curRow, curCol));
        return count == MAX_STEP ? new HashSet<>() : positions;
    }

    private static int countObstruction(List<List<Character>> map, Set <Position> travelPositions, Position start) {
        int count = 0;
        for (Position travelPosition: travelPositions) {
            char initValue = map.get(travelPosition.x).get(travelPosition.y);
            if (initValue != UP) {
                map.get(travelPosition.x).set(travelPosition.y, OBSTACLE);
                Set<Position> positions = travelAroundMap(map, start);
                if (positions.isEmpty()) {
                    count++;
                }
                map.get(travelPosition.x).set(travelPosition.y, initValue);
            }
        }
        return count;
    }

    private static boolean isOutsideMap(int maxRow, int maxCol, int curRow, int curCol) {
        return curRow < 0 || curCol < 0 || curRow >= maxRow || curCol >= maxCol;
    }

    private static boolean isObstacle(List<List<Character>> map, int x, int y) {
        return map.get(x).get(y) == OBSTACLE;
    }

    @Data
    @AllArgsConstructor
    private static class Position {
        private int x;
        private int y;
    }
}

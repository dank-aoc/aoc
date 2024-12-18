package org.aoc.y2024.day18;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class RamRun {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day18\\input.txt";
    private static final int MAX_ROW = 71;
    private static final int MAX_COL = 71;
    private static final String COMMAS_SEPARATOR = ",";
    private static final char CORRUPTED = '#';
    private static final char SAFE = '.';
    private static final int BYTE = 1024;

    public static void main(String[] args) {
        List<Position> positions = parseInput();
        List<List<Character>> map = setup();
        addCorrupted(map, positions);
        System.out.println(findSmallestPath(map));
        System.out.println(findFirstObstacle(map, positions));
    }

    private static List<Position> parseInput() {
        List<Position> positions = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                String[] rawComponent = row.split(COMMAS_SEPARATOR);
                int x = Integer.parseInt(rawComponent[0]);
                int y = Integer.parseInt(rawComponent[1]);
                positions.add(new Position(y, x));
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return positions;
    }

    private static List<List<Character>> setup() {
        List<List<Character>> map = new ArrayList<>();
        for (int i = 0; i < MAX_ROW; i++) {
            List<Character> lst = new ArrayList<>();
            for (int j = 0; j < MAX_COL; j++) {
                lst.add(SAFE);
            }
            map.add(lst);
        }
        return map;
    }

    private static Position findFirstObstacle(List<List<Character>> map, List<Position> positions) {
        for (int i = BYTE; i < positions.size(); i++) {
            Position position = positions.get(i);
            map.get(position.getX()).set(position.getY(), CORRUPTED);
            int score = findSmallestPath(map);
            if (score == -1) {
                return position;
            }
        }
        return new Position(-1, -1);
    }

    private static void addCorrupted(List<List<Character>> map, List<Position> positions) {
        for (int i = 0; i < BYTE; i++) {
            Position position = positions.get(i);
            map.get(position.getX()).set(position.getY(), CORRUPTED);
        }
    }

    private static int findSmallestPath(List<List<Character>> map) {
        Map<State, Integer> minCost = new HashMap<>();
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparing(State::getScore));

        pq.add(new State(new Position(0, 0), 0));

        while (!pq.isEmpty()) {
            State state = pq.poll();
            Position pos = state.getPosition();
            int score = state.getScore();

            // If we reach the goal, return the score
            if (pos.getX() == MAX_ROW - 1 && pos.getY() == MAX_COL - 1) {
                return score;
            }

            // If we've already reached this state with a lower score, skip it
            if (minCost.containsKey(state) && minCost.get(state) <= score) {
                continue;
            }

            // Update the minimum cost map
            minCost.put(state, score);

            // Move
            Position up = new Position(pos.getX() - 1, pos.getY());
            Position down = new Position(pos.getX() + 1, pos.getY());
            Position left = new Position(pos.getX(), pos.getY() - 1);
            Position right = new Position(pos.getX(), pos.getY() + 1);

            if (isValid(map, up)) {
                pq.add(new State(up, score + 1));
            }
            if (isValid(map, down)) {
                pq.add(new State(down, score + 1));
            }
            if (isValid(map, left)) {
                pq.add(new State(left, score + 1));
            }
            if (isValid(map, right)) {
                pq.add(new State(right, score + 1));
            }
        }

        return -1;
    }

    private static boolean isValid(List<List<Character>> map, Position position) {
        int x = position.getX();
        int y = position.getY();
        return x >= 0 && y >= 0 && x < MAX_ROW && y < MAX_COL && map.get(x).get(y) != CORRUPTED;
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class Position {
        private int x;
        private int y;
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = "score")
    private static class State {
        private Position position;
        private int score;
    }
}

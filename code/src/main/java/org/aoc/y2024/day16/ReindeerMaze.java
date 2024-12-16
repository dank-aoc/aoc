package org.aoc.y2024.day16;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class ReindeerMaze {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day16\\input.txt";
    private static final char START = 'S';
    private static final char END = 'E';
    private static final char LEFT = '>';
    private static final char RIGHT = '<';
    private static final char UP = '^';
    private static final char DOWN = 'v';
    private static final char WALL = '#';
    private static final int FORWARD_COST = 1;
    private static final int TURN_COST = 1000;

    public static void main(String[] args) {
        List<List<Character>> map = parseInput();
        Result result = findSmallestPath(map);
        System.out.println(result.getMinScore());
        System.out.println(result.getTiles().size());
    }

    private static List<List<Character>> parseInput() {
        List<List<Character>> map = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                List<Character> lst = new ArrayList<>();
                String row = scanner.nextLine();
                for (char c : row.toCharArray()) {
                    lst.add(c);
                }
                map.add(lst);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return map;
    }

    private static Result findSmallestPath(List<List<Character>> map) {
        Position start = findStartPosition(map);
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparing(State::getScore));
        int minScore = Integer.MAX_VALUE;
        Map<State, Integer> minCost = new HashMap<>();
        Set<Position> tiles = new HashSet<>();

        pq.add(new State(start, LEFT, 0, null));

        while (!pq.isEmpty()) {
            State state = pq.poll();
            Position position = state.getPosition();
            char direction = state.getDirection();
            int score = state.getScore();

            if (map.get(position.getX()).get(position.getY()) == END && minScore >= score) {
                minScore = score;
                extractPath(tiles, state);
            }
            if (minCost.containsKey(state) && minCost.get(state) < score) {
                continue;
            }

            minCost.put(state, score);
            Position next = getNextPosition(position, direction);
            if (!isWall(map, next)) {
                pq.add(new State(next, direction, score + FORWARD_COST, state));
            }
            if (direction == UP || direction == DOWN) {
                pq.add(new State(position, LEFT, score + TURN_COST, state));
                pq.add(new State(position, RIGHT, score + TURN_COST, state));
            } else {
                pq.add(new State(position, UP, score + TURN_COST, state));
                pq.add(new State(position, DOWN, score + TURN_COST, state));
            }
        }

        return new Result(minScore, tiles);
    }

    private static Position findStartPosition(List<List<Character>> map) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) == START) {
                    return new Position(i, j);
                }
            }
        }
        return new Position(-1, -1);
    }

    private static Position getNextPosition(Position position, char direction) {
        return switch (direction) {
            case UP -> new Position(position.getX() - 1, position.getY());
            case DOWN -> new Position(position.getX() + 1, position.getY());
            case LEFT -> new Position(position.getX(), position.getY() - 1);
            case RIGHT -> new Position(position.getX(), position.getY() + 1);
            default -> position;
        };
    }

    private static boolean isWall(List<List<Character>> map, Position position) {
        return map.get(position.getX()).get(position.getY()) == WALL;
    }

    private static void extractPath(Set<Position> tiles, State state) {
        while (state.getParent() != null) {
            tiles.add(state.getPosition());
            state = state.getParent();
        }
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
    @EqualsAndHashCode(exclude = {"score", "parent"})
    private static class State {
        private Position position;
        private char direction;
        private int score;
        private State parent;
    }

    @Data
    @AllArgsConstructor
    private static class Result {
        private int minScore;
        private Set<Position> tiles;
    }
}

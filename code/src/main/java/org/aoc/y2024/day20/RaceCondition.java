package org.aoc.y2024.day20;

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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class RaceCondition {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day20\\input.txt";
    private static final char START = 'S';
    private static final char END = 'E';
    private static final char WALL = '#';
    private static final int MIN_SAVE_COST = 100;
    private static final Map<Position, Integer> memo = new HashMap<>();

    public static void main(String[] args) {
        List<List<Character>> map = parseInput();
        Position start = findPosition(map, START);
        Position end = findPosition(map, END);
        Result result = findSmallestPath(map, start, end);
        result.getTiles().add(start);
        memo.put(start, result.getMinScore());
        System.out.println(solve(map, result, 2, start, end, true));
        System.out.println(solve(map, result, 20, start, end, false));
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

    private static Position findPosition(List<List<Character>> map, char c) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) == c) {
                    return new Position(i, j);
                }
            }
        }
        return new Position(-1, -1);
    }

    private static boolean isValid(List<List<Character>> map, Position position) {
        int x = position.getX();
        int y = position.getY();
        return x >= 0 && y >= 0 && x < map.size() && y < map.get(0).size() && map.get(x).get(y) != WALL;
    }

    private static Result findSmallestPath(List<List<Character>> map, Position start, Position end) {
        if (map.get(start.getX()).get(start.getY()) == END) {
            return new Result(0, new HashSet<>());
        }
        if (start.getX() == end.getX() && start.getY() == end.getY()) {
            return new Result(0, new HashSet<>());
        }

        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparing(State::getScore));
        Map<State, Integer> minCost = new HashMap<>();
        int minScore = Integer.MAX_VALUE;
        Set<Position> tiles = new HashSet<>();

        pq.add(new State(start, 0, null));

        while (!pq.isEmpty()) {
            State state = pq.poll();
            Position pos = state.getPosition();
            int score = state.getScore();

            if (pos.getX() == end.getX() && pos.getY() == end.getY() && minScore >= score) {
                minScore = score;
                extractPath(tiles, state);
            }
            if (minCost.containsKey(state) && minCost.get(state) < score) {
                continue;
            }

            minCost.put(state, score);
            
            Position up = new Position(pos.getX() - 1, pos.getY());
            Position down = new Position(pos.getX() + 1, pos.getY());
            Position left = new Position(pos.getX(), pos.getY() - 1);
            Position right = new Position(pos.getX(), pos.getY() + 1);

            if (isValid(map, up)) {
                pq.add(new State(up, score + 1, state));
            }
            if (isValid(map, down)) {
                pq.add(new State(down, score + 1, state));
            }
            if (isValid(map, left)) {
                pq.add(new State(left, score + 1, state));
            }
            if (isValid(map, right)) {
                pq.add(new State(right, score + 1, state));
            }
        }

        return new Result(minScore, tiles);
    }

    private static void extractPath(Set<Position> tiles, State state) {
        while (state.getParent() != null) {
            tiles.add(state.getPosition());
            state = state.getParent();
        }
    }

    private static int solve(List<List<Character>> map, Result result, int depth, Position start, Position end, boolean isP1) {
        int minScore = result.getMinScore();
        Set<Position> tiles = result.getTiles();
        AtomicInteger count = new AtomicInteger();
        Map<Integer, Integer> costMap = new HashMap<>();
        Predicate<Integer> distanceCondition = isP1
                ? d -> d == depth
                : d -> d <= depth;

        tiles.forEach(tile -> {
            List<Position> neighbors = findByManhattanDistance(map, tile, distanceCondition);
            Result res1 = findSmallestPath(map, start, tile);
            neighbors.forEach(neighbor -> {
                int ms;
                if (memo.containsKey(neighbor)) {
                    ms = memo.get(neighbor);
                } else {
                    Result res2 = findSmallestPath(map, neighbor, end);
                    memo.put(neighbor, res2.getMinScore());
                    ms = res2.getMinScore();
                }
                if (ms != Integer.MAX_VALUE) {
                    int distance = Math.abs(neighbor.getX() - tile.getX()) + Math.abs(neighbor.getY() - tile.getY());
                    int cost = res1.getMinScore() + ms + distance;
                    if (cost < minScore && minScore - cost >= MIN_SAVE_COST) {
                        count.getAndIncrement();
                        costMap.compute(minScore - cost, (k, v) -> (v == null) ? 1 : v + 1);
                    }
                }
            });
        });

        return count.get();
    }

    private static List<Position> findByManhattanDistance(List<List<Character>> map, Position root, Predicate<Integer> distanceCondition) {
        List<Position> positions = new ArrayList<>();

        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.getFirst().size(); j++) {
                int x = Math.abs(root.getX() - i);
                int y = Math.abs(root.getY() - j);
                if (distanceCondition.test(x + y) && x + y != 0 && isValid(map, new Position(i, j))) {
                    positions.add(new Position(i, j));
                }
            }
        }

        return positions;
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

package org.aoc.y2024.day21;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class KeypadConundrum {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day21\\input.txt";
    private static final String ACTIVATE_BUTTON = "A";
    private static final List<List<Character>> numericKeyboard = initNumericKeyboard();
    private static final List<List<Character>> directionalKeyboard = initDirectionalKeyboard();
    private static final Map<Character, Position> directionalMap = initDirectionalMap();
    private static final Map<Character, Position> numericMap = initNumericMap();
    private static final Map<MemoKey, BigInteger> memo = new HashMap<>();
    private static final Map<CombinationKey, Set<String>> combinations = new HashMap<>();

    public static void main(String[] args) {
        initMemo();
        List<String> codes = parseInput();
        BigInteger resP1 = BigInteger.ZERO;
        BigInteger resP2 = BigInteger.ZERO;
        for (String code : codes) {
            Set<String> paths = findShortestPathToPrintCode(code);
            BigInteger resDepth2 = getShortestLengthByDepth(paths, 2);
            BigInteger resDepth25 = getShortestLengthByDepth(paths, 25);
            resP1 = resP1.add(getComplexities(code, resDepth2));
            resP2 = resP2.add(getComplexities(code, resDepth25));
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
                input.add(row);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return input;
    }

    private static List<List<Character>> initNumericKeyboard() {
        List<List<Character>> keyboard = new ArrayList<>();

        List<Character> firstRow = new ArrayList<>();
        firstRow.add('7');
        firstRow.add('8');
        firstRow.add('9');

        List<Character> secondRow = new ArrayList<>();
        secondRow.add('4');
        secondRow.add('5');
        secondRow.add('6');

        List<Character> thirdRow = new ArrayList<>();
        thirdRow.add('1');
        thirdRow.add('2');
        thirdRow.add('3');

        List<Character> fourthRow = new ArrayList<>();
        fourthRow.add(' ');
        fourthRow.add('0');
        fourthRow.add('A');

        keyboard.add(firstRow);
        keyboard.add(secondRow);
        keyboard.add(thirdRow);
        keyboard.add(fourthRow);

        return keyboard;
    }

    private static Map<Character, Position> initNumericMap() {
        Map<Character, Position> map = new HashMap<>();
        map.put('7', new Position(0, 0));
        map.put('8', new Position(0, 1));
        map.put('9', new Position(0, 2));
        map.put('4', new Position(1, 0));
        map.put('5', new Position(1, 1));
        map.put('6', new Position(1, 2));
        map.put('1', new Position(2, 0));
        map.put('2', new Position(2, 1));
        map.put('3', new Position(2, 2));
        map.put(' ', new Position(3, 0));
        map.put('0', new Position(3, 1));
        map.put('A', new Position(3, 2));
        return map;
    }

    private static Map<Character, Position> initDirectionalMap() {
        Map<Character, Position> map = new HashMap<>();
        map.put(' ', new Position(0, 0));
        map.put('^', new Position(0, 1));
        map.put('A', new Position(0, 2));
        map.put('<', new Position(1, 0));
        map.put('v', new Position(1, 1));
        map.put('>', new Position(1, 2));
        return map;
    }

    private static List<List<Character>> initDirectionalKeyboard() {
        List<List<Character>> keyboard = new ArrayList<>();

        List<Character> firstRow = new ArrayList<>();
        firstRow.add(' ');
        firstRow.add('^');
        firstRow.add('A');

        List<Character> secondRow = new ArrayList<>();
        secondRow.add('<');
        secondRow.add('v');
        secondRow.add('>');

        keyboard.add(firstRow);
        keyboard.add(secondRow);

        return keyboard;
    }

    private static void initMemo() {
        for (Map.Entry<Character, Position> entry1 : directionalMap.entrySet()) {
            for (Map.Entry<Character, Position> entry2 : directionalMap.entrySet()) {
                Set<String> res = findShortestPathFromStartToEnd(directionalKeyboard, entry1.getValue(), entry2.getValue());
                memo.put(new MemoKey(entry1.getValue(), entry2.getValue(), 1), res.isEmpty()
                        ? BigInteger.ZERO
                        : BigInteger.valueOf(res.stream().findFirst().get().length()));
                combinations.put(new CombinationKey(entry1.getValue(), entry2.getValue()), res);
            }
        }
    }

    private static Set<String> findShortestPathToPrintCode(String code) {
        Set<String> res = new HashSet<>();
        Position start = numericMap.get('A');
        for (char c : code.toCharArray()) {
            Position end = numericMap.get(c);
            Set<String> paths = findShortestPathFromStartToEnd(numericKeyboard, start, end);
            if (res.isEmpty()) {
                res = paths;
            } else {
                res = res.stream()
                        .flatMap(a -> paths.stream().map(b -> a + b))
                        .collect(Collectors.toSet());
            }
            start = end;
        }
        return res;
    }

    private static Set<String> findShortestPathFromStartToEnd(List<List<Character>> keyboard, Position start, Position end) {
        Set<String> paths = new HashSet<>();
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparing(State::getScore));
        Map<State, Integer> minCost = new HashMap<>();
        int minScore = Integer.MAX_VALUE;

        pq.add(new State(start, 0, ""));

        if (start.getX() == end.getX() && start.getY() == end.getY()) {
            paths.add(ACTIVATE_BUTTON);
            return paths;
        }

        while (!pq.isEmpty()) {
            State state = pq.poll();
            Position pos = state.getPosition();
            int score = state.getScore();
            String direction = state.getDirection();

            if (pos.getX() == end.getX() && pos.getY() == end.getY() && minScore >= score) {
                minScore = score;
                paths.add(state.getDirection() + ACTIVATE_BUTTON);
            }
            if (minCost.containsKey(state) && minCost.get(state) < score) {
                continue;
            }

            minCost.put(state, score);

            Position up = new Position(pos.getX() - 1, pos.getY());
            Position down = new Position(pos.getX() + 1, pos.getY());
            Position left = new Position(pos.getX(), pos.getY() - 1);
            Position right = new Position(pos.getX(), pos.getY() + 1);

            if (isValid(keyboard, up)) {
                pq.add(new State(up, score + 1, direction + "^"));
            }
            if (isValid(keyboard, down)) {
                pq.add(new State(down, score + 1, direction + "v"));
            }
            if (isValid(keyboard, left)) {
                pq.add(new State(left, score + 1, direction + "<"));
            }
            if (isValid(keyboard, right)) {
                pq.add(new State(right, score + 1, direction + ">"));
            }
        }

        return paths;
    }

    private static BigInteger getShortestLengthByDepth(char source, char des, int depth) {
        Position start = directionalMap.get(source);
        Position end = directionalMap.get(des);
        MemoKey key = new MemoKey(start, end, depth);

        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        Set<String> sequences = combinations.get(new CombinationKey(start, end));
        BigInteger res = getShortestLengthByDepth(sequences, depth - 1);

        memo.put(key, res);

        return res;
    }

    private static BigInteger getShortestLengthByDepth(Set<String> sequences, int depth) {
        BigInteger res = BigInteger.ZERO;

        for (String sequence : sequences) {
            BigInteger tmp = BigInteger.ZERO;
            String newSequence = ACTIVATE_BUTTON + sequence;
            for (int i = 0; i < newSequence.length() - 1; i++) {
                tmp = tmp.add(getShortestLengthByDepth(newSequence.charAt(i), newSequence.charAt(i + 1), depth));
            }
            res = res.equals(BigInteger.ZERO) ? tmp : res.min(tmp);
        }

        return res;
    }

    private static BigInteger getComplexities(String code, BigInteger minCost) {
        return minCost.multiply(new BigInteger(code.substring(0, code.length() - 1)));
    }

    private static boolean isValid(List<List<Character>> keyboard, Position position) {
        int x = position.getX();
        int y = position.getY();
        return x >= 0 && y >= 0 && x < keyboard.size() && y < keyboard.getFirst().size() && keyboard.get(x).get(y) != ' ';
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
    @EqualsAndHashCode(exclude = {"score", "direction"})
    private static class State {
        private Position position;
        private int score;
        private String direction;
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class MemoKey {
        private Position start;
        private Position end;
        private int depth;
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class CombinationKey {
        private Position start;
        private Position end;
    }
}

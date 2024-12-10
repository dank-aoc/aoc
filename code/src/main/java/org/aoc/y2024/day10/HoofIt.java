package org.aoc.y2024.day10;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HoofIt {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day10\\input.txt";
    private static final char ZERO = '0';
    private static final int END_OF_PATH = 10;
    private static List<List<Position>> allHikingTrail = new ArrayList<>();

    public static void main(String[] args) {
        int resP1 = 0;
        int resP2 = 0;
        List<List<Integer>> map = parseInput();
        List<Position> trailheads = getTrailheads(map);
        for (Position trailhead: trailheads) {
            List<Position> hikingTrail = new ArrayList<>();
            hikingTrail.add(trailhead);
            findPath(map, hikingTrail);
            resP1 += getScore();
            resP2 += getRating();
            allHikingTrail = new ArrayList<>();
        }
        System.out.println(resP1);
        System.out.println(resP2);
    }

    private static List<List<Integer>> parseInput() {
        List<List<Integer>> input = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                List<Integer> lst = new ArrayList<>();
                String row = scanner.nextLine();
                for (char c: row.toCharArray()) {
                    lst.add(c - ZERO);
                }
                input.add(lst);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return input;
    }

    private static List<Position> getTrailheads(List<List<Integer>> map) {
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) == 0) {
                    positions.add(new Position(i, j));
                }
            }
        }
        return positions;
    }

    private static void findPath(List<List<Integer>> map, List<Position> hikingTrail) {
        if (hikingTrail.size() == END_OF_PATH) {
            allHikingTrail.add(new ArrayList<>(hikingTrail));
            return;
        }

        Position lastPosition = hikingTrail.getLast();
        Position leftPosition = new Position(lastPosition.getX(), lastPosition.getY() - 1);
        Position rightPosition = new Position(lastPosition.getX(), lastPosition.getY() + 1);
        Position upPosition = new Position(lastPosition.getX() - 1, lastPosition.getY());
        Position downPosition = new Position(lastPosition.getX() + 1, lastPosition.getY());

        if (isNextStep(map, lastPosition, leftPosition)) {
            hikingTrail.add(leftPosition);
            findPath(map, hikingTrail);
            hikingTrail.removeLast();
        }
        if (isNextStep(map, lastPosition, rightPosition)) {
            hikingTrail.add(rightPosition);
            findPath(map, hikingTrail);
            hikingTrail.removeLast();
        }
        if (isNextStep(map, lastPosition, upPosition)) {
            hikingTrail.add(upPosition);
            findPath(map, hikingTrail);
            hikingTrail.removeLast();
        }
        if (isNextStep(map, lastPosition, downPosition)) {
            hikingTrail.add(downPosition);
            findPath(map, hikingTrail);
            hikingTrail.removeLast();
        }
    }

    private static boolean isOutsideMap(List<List<Integer>> map, Position position) {
        return position.getX() < 0 || position.getY() < 0 || position.getX() >= map.size() || position.getY() >= map.get(0).size();
    }

    private static boolean isNextStep(List<List<Integer>> map, Position curPosition, Position nextPosition) {
        int curX = curPosition.getX();
        int curY = curPosition.getY();
        int nextX = nextPosition.getX();
        int nextY = nextPosition.getY();
        return !isOutsideMap(map, curPosition) && !isOutsideMap(map, nextPosition) && map.get(nextX).get(nextY) - map.get(curX).get(curY) == 1;
    }

    private static int getScore() {
        return allHikingTrail.stream()
                .map(List::getLast)
                .collect(Collectors.toSet())
                .size();
    }

    private static int getRating() {
        return allHikingTrail.size();
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class Position {
        private int x;
        private int y;
    }
}

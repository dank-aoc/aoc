package org.aoc.y2024.day14;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class RestroomRedoubt {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day14\\input.txt";
    private static final int MAX_ROW = 103;
    private static final int MAX_COL = 101;
    private static final int SECOND_P1 = 100;
    private static final String SPACE_SEPARATOR = " ";
    private static final String EQUAL_SEPARATOR = "=";
    private static final String COMMAS_SEPARATOR = ",";

    public static void main(String[] args) {
        List<Robot> robots = parseInput();
        System.out.println(getSafetyFactor(new ArrayList<>(robots)));
        System.out.println(getFewestSecond(new ArrayList<>(robots)) + SECOND_P1);
    }

    private static List<Robot> parseInput() {
        List<Robot> robots = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();

                String[] rawComponent = row.split(SPACE_SEPARATOR);
                String[] posArr = rawComponent[0].split(EQUAL_SEPARATOR);
                String[] posDetail = posArr[1].split(COMMAS_SEPARATOR);
                Position position = new Position(Integer.parseInt(posDetail[1]), Integer.parseInt(posDetail[0]));

                String[] velArr = rawComponent[1].split(EQUAL_SEPARATOR);
                String[] velDetail = velArr[1].split(COMMAS_SEPARATOR);
                Velocity velocity = new Velocity(Integer.parseInt(velDetail[1]), Integer.parseInt(velDetail[0]));

                robots.add(new Robot(position, velocity));
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return robots;
    }

    private static Position getPositionAfterOneSecond(Position curPosition, Velocity velocity) {
        int newY = curPosition.getY() + velocity.getVy();
        if (newY < 0) {
            newY += MAX_COL;
        } else if (newY >= MAX_COL) {
            newY -= MAX_COL;
        }

        int newX = curPosition.getX() + velocity.getVx();
        if (newX < 0) {
            newX += MAX_ROW;
        } else if (newX >= MAX_ROW) {
            newX -= MAX_ROW;
        }

        return new Position(newX, newY);
    }

    private static long getSafetyFactor(List<Robot> robots) {
        long count1 = 0;
        long count2 = 0;
        long count3 = 0;
        long count4 = 0;

        for (int i = 1; i <= SECOND_P1; i++) {
            robots.forEach(robot ->
                    robot.setPosition(getPositionAfterOneSecond(robot.getPosition(), robot.getVelocity())));
        }

        for (Robot robot : robots) {
            Position position = robot.getPosition();
            if (isQuadrant1(position)) {
                count1++;
            } else if (isQuadrant2(position)) {
                count2++;
            } else if (isQuadrant3(position)) {
                count3++;
            } else if (isQuadrant4(position)) {
                count4++;
            }
        }

        return count1 * count2 * count3 * count4;
    }

    private static boolean isQuadrant1(Position position) {
        return position.getX() < MAX_ROW / 2 && position.getY() < MAX_COL / 2;
    }

    private static boolean isQuadrant2(Position position) {
        return position.getX() < MAX_ROW / 2 && position.getY() > MAX_COL / 2;
    }

    private static boolean isQuadrant3(Position position) {
        return position.getX() > MAX_ROW / 2 && position.getY() < MAX_COL / 2;
    }

    private static boolean isQuadrant4(Position position) {
        return position.getX() > MAX_ROW / 2 && position.getY() > MAX_COL / 2;
    }

    private static int getFewestSecond(List<Robot> robots) {
        int second = 0;

        do {
            Set<Position> positions = robots.stream()
                    .map(Robot::getPosition)
                    .collect(Collectors.toSet());
            if (positions.size() == robots.size()) {
                break;
            }
            robots.forEach(robot ->
                    robot.setPosition(getPositionAfterOneSecond(robot.getPosition(), robot.getVelocity())));
            second++;
        } while (true);

        return second;
    }

    @Data
    @AllArgsConstructor
    private static class Robot {
        private Position position;
        private Velocity velocity;
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
    private static class Velocity {
        private int vx;
        private int vy;
    }
}

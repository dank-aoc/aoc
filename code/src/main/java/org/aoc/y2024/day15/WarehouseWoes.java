package org.aoc.y2024.day15;


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
import java.util.Stack;

public class WarehouseWoes {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day15\\input.txt";
    private static final char CURRENT_POSITION = '@';
    private static final char WALL = '#';
    private static final char EMPTY = '.';
    private static final char BOX_P1 = 'O';
    private static final char LEFT_PART_BOX_P2 = '[';
    private static final char RIGHT_PART_BOX_P2 = ']';
    private static final char UP = '^';
    private static final char DOWN = 'v';
    private static final char RIGHT = '>';
    private static final char LEFT = '<';
    
    public static void main(String[] args) {
        LanternFish lanternFishP1 = parseInput();
        Position startP1 = findStartPosition(lanternFishP1.getMap());
        for (Character c : lanternFishP1.getMovements()) {
            startP1 = moveP1(lanternFishP1.getMap(), c, startP1);
        }
        System.out.println(getGPSBoxes(lanternFishP1.getMap()));

        LanternFish lanternFishP2 = parseInput();
        lanternFishP2.setMap(resizeMap(lanternFishP2.getMap()));
        Position start = findStartPosition(lanternFishP2.getMap());
        for (Character c : lanternFishP2.getMovements()) {
            start = moveP2(lanternFishP2.getMap(), c, start);
        }
        System.out.println(getGPSBoxes(lanternFishP2.getMap()));
    }

    private static LanternFish parseInput() {
        List<List<Character>> map = new ArrayList<>();
        List<Character> movements = new ArrayList<>();
        boolean isReadMap = true;

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                if (row.isEmpty()) {
                    isReadMap = false;
                } else if (isReadMap) {
                    List<Character> lst = new ArrayList<>();
                    for (char c : row.toCharArray()) {
                        lst.add(c);
                    }
                    map.add(lst);
                } else {
                    for (char c : row.toCharArray()) {
                        movements.add(c);
                    }
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new LanternFish(map, movements);
    }

    private static List<List<Character>> resizeMap(List<List<Character>> map) {
        List<List<Character>> newMap = new ArrayList<>();

        for (List<Character> row : map) {
            List<Character> lst = new ArrayList<>();
            for (Character c : row) {
                if (c == WALL) {
                    lst.add(WALL);
                    lst.add(WALL);
                } else if (c == BOX_P1) {
                    lst.add(LEFT_PART_BOX_P2);
                    lst.add(RIGHT_PART_BOX_P2);
                } else if (c == EMPTY) {
                    lst.add(EMPTY);
                    lst.add(EMPTY);
                } else {
                    lst.add(CURRENT_POSITION);
                    lst.add(EMPTY);
                }
            }
            newMap.add(lst);
        }

        return newMap;
    }

    private static Position findStartPosition(List<List<Character>> map) {
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) == CURRENT_POSITION) {
                    return new Position(i, j);
                }
            }
        }
        return new Position(-1, -1);
    }

    private static Position moveP1(List<List<Character>> map, Character movement, Position position) {
        return switch (movement) {
            case UP -> moveP1Up(map, movement, position);
            case DOWN -> moveP1Down(map, movement, position);
            case RIGHT -> moveP1Right(map, movement, position);
            case LEFT -> moveP1Left(map, movement, position);
            default -> new Position(-1, -1);
        };
    }

    private static Position moveP1Up(List<List<Character>> map, Character movement, Position position) {
        int nextXUp = position.getX() - 1;
        int nextYUp = position.getY();

        if (isWall(map, nextXUp, nextYUp)) {
            return position;
        } else if (!isBox(map, nextXUp, nextYUp)) {
            map.get(nextXUp).set(nextYUp, CURRENT_POSITION);
            map.get(position.getX()).set(position.getY(), EMPTY);
            return new Position(nextXUp, nextYUp);
        } else {
            int consecutiveBoxes = countConsecutiveBoxesP1(map, movement, nextXUp, nextYUp);
            int moveX = nextXUp - consecutiveBoxes - 1;
            if (isWall(map, moveX, nextYUp)) {
                return position;
            } else {
                map.get(moveX).set(nextYUp, BOX_P1);
                map.get(nextXUp).set(nextYUp, CURRENT_POSITION);
                map.get(position.getX()).set(position.getY(), EMPTY);
                return new Position(nextXUp, nextYUp);
            }
        }
    }

    private static Position moveP1Down(List<List<Character>> map, Character movement, Position position) {
        int nextXDown = position.getX() + 1;
        int nextYDown = position.getY();

        if (isWall(map, nextXDown, nextYDown)) {
            return position;
        } else if (!isBox(map, nextXDown, nextYDown)) {
            map.get(nextXDown).set(nextYDown, CURRENT_POSITION);
            map.get(position.getX()).set(position.getY(), EMPTY);
            return new Position(nextXDown, nextYDown);
        } else {
            int consecutiveBoxes = countConsecutiveBoxesP1(map, movement, nextXDown, nextYDown);
            int moveX = nextXDown + consecutiveBoxes + 1;
            if (isWall(map, moveX, nextYDown)) {
                return position;
            } else {
                map.get(moveX).set(nextYDown, BOX_P1);
                map.get(nextXDown).set(nextYDown, CURRENT_POSITION);
                map.get(position.getX()).set(position.getY(), EMPTY);
                return new Position(nextXDown, nextYDown);
            }
        }
    }

    private static Position moveP1Right(List<List<Character>> map, Character movement, Position position) {
        int nextXRight = position.getX();
        int nextYRight = position.getY() + 1;

        if (isWall(map, nextXRight, nextYRight)) {
            return position;
        } else if (!isBox(map, nextXRight, nextYRight)) {
            map.get(nextXRight).set(nextYRight, CURRENT_POSITION);
            map.get(position.getX()).set(position.getY(), EMPTY);
            return new Position(nextXRight, nextYRight);
        } else {
            int consecutiveBoxes = countConsecutiveBoxesP1(map, movement, nextXRight, nextYRight);
            int moveY = nextYRight + consecutiveBoxes + 1;
            if (isWall(map, nextXRight, moveY)) {
                return position;
            } else {
                map.get(nextXRight).set(moveY, BOX_P1);
                map.get(nextXRight).set(nextYRight, CURRENT_POSITION);
                map.get(nextXRight).set(position.getY(), EMPTY);
                return new Position(nextXRight, nextYRight);
            }
        }
    }

    private static Position moveP1Left(List<List<Character>> map, Character movement, Position position) {
        int nextXLeft = position.getX();
        int nextYLeft = position.getY() - 1;

        if (isWall(map, nextXLeft, nextYLeft)) {
            return position;
        } else if (!isBox(map, nextXLeft, nextYLeft)) {
            map.get(nextXLeft).set(nextYLeft, CURRENT_POSITION);
            map.get(position.getX()).set(position.getY(), EMPTY);
            return new Position(nextXLeft, nextYLeft);
        } else {
            int consecutiveBoxes = countConsecutiveBoxesP1(map, movement, nextXLeft, nextYLeft);
            int moveY = nextYLeft - consecutiveBoxes - 1;
            if (isWall(map, nextXLeft, moveY)) {
                return position;
            } else {
                map.get(nextXLeft).set(moveY, BOX_P1);
                map.get(nextXLeft).set(nextYLeft, CURRENT_POSITION);
                map.get(nextXLeft).set(position.getY(), EMPTY);
                return new Position(nextXLeft, nextYLeft);
            }
        }
    }

    private static Position moveP2(List<List<Character>> map, Character movement, Position position) {
        return switch (movement) {
            case UP -> moveP2Up(map, movement, position);
            case DOWN -> moveP2Down(map, movement, position);
            case RIGHT -> moveP2Right(map, movement, position);
            case LEFT -> moveP2Left(map, movement, position);
            default -> new Position(-1, -1);
        };

    }

    private static Position moveP2Up(List<List<Character>> map, Character movement, Position position) {
        int nextXUp = position.getX() - 1;
        int nextYUp = position.getY();

        if (isWall(map, nextXUp, nextYUp)) {
            return position;
        } else if (!isBox(map, nextXUp, nextYUp)) {
            map.get(nextXUp).set(nextYUp, CURRENT_POSITION);
            map.get(position.getX()).set(position.getY(), EMPTY);
            return new Position(nextXUp, nextYUp);
        } else {
            Set<Box> consecutiveBoxes = findAllConsecutiveBoxes(map, movement, nextXUp, nextYUp);
            if (canMoveBoxes(map, consecutiveBoxes, movement)) {
                resetMap(map, consecutiveBoxes);
                moveBox(map, consecutiveBoxes, movement);
                map.get(nextXUp).set(nextYUp, CURRENT_POSITION);
                map.get(position.getX()).set(position.getY(), EMPTY);
                return new Position(nextXUp, nextYUp);
            } else {
                return position;
            }
        }
    }

    private static Position moveP2Down(List<List<Character>> map, Character movement, Position position) {
        int nextXDown = position.getX() + 1;
        int nextYDown = position.getY();

        if (isWall(map, nextXDown, nextYDown)) {
            return position;
        } else if (!isBox(map, nextXDown, nextYDown)) {
            map.get(nextXDown).set(nextYDown, CURRENT_POSITION);
            map.get(position.getX()).set(position.getY(), EMPTY);
            return new Position(nextXDown, nextYDown);
        } else {
            Set<Box> consecutiveBoxes = findAllConsecutiveBoxes(map, movement, nextXDown, nextYDown);
            if (canMoveBoxes(map, consecutiveBoxes, movement)) {
                resetMap(map, consecutiveBoxes);
                moveBox(map, consecutiveBoxes, movement);
                map.get(nextXDown).set(nextYDown, CURRENT_POSITION);
                map.get(position.getX()).set(position.getY(), EMPTY);
                return new Position(nextXDown, nextYDown);
            } else {
                return position;
            }
        }
    }

    private static Position moveP2Right(List<List<Character>> map, Character movement, Position position) {
        int nextXRight = position.getX();
        int nextYRight = position.getY() + 1;

        if (isWall(map, nextXRight, nextYRight)) {
            return position;
        } else if (!isBox(map, nextXRight, nextYRight)) {
            map.get(nextXRight).set(nextYRight, CURRENT_POSITION);
            map.get(position.getX()).set(position.getY(), EMPTY);
            return new Position(nextXRight, nextYRight);
        } else {
            int consecutiveBoxes = countConsecutiveBoxesP2(map, movement, nextXRight, nextYRight);
            int moveY = nextYRight + consecutiveBoxes * 2 + 2;
            if (moveY < 0 || moveY >= map.get(0).size() || isWall(map, nextXRight, moveY)) {
                return position;
            } else {
                for (int i = moveY; i > nextYRight; i--) {
                    char c = map.get(nextXRight).get(i);
                    map.get(nextXRight).set(i, map.get(nextXRight).get(i-1));
                    map.get(nextXRight).set(i-1, c);
                }
                map.get(nextXRight).set(nextYRight, CURRENT_POSITION);
                map.get(nextXRight).set(position.getY(), EMPTY);
                return new Position(nextXRight, nextYRight);
            }
        }
    }

    private static Position moveP2Left(List<List<Character>> map, Character movement, Position position) {
        int nextXLeft = position.getX();
        int nextYLeft = position.getY() - 1;

        if (isWall(map, nextXLeft, nextYLeft)) {
            return position;
        } else if (!isBox(map, nextXLeft, nextYLeft)) {
            map.get(nextXLeft).set(nextYLeft, CURRENT_POSITION);
            map.get(position.getX()).set(position.getY(), EMPTY);
            return new Position(nextXLeft, nextYLeft);
        } else {
            int consecutiveBoxes = countConsecutiveBoxesP2(map, movement, nextXLeft, nextYLeft);
            int moveY = nextYLeft - consecutiveBoxes * 2 - 2;
            if (moveY < 0 || moveY >= map.get(0).size() || isWall(map, nextXLeft, moveY)) {
                return position;
            } else {
                for (int i = moveY; i < nextYLeft; i++) {
                    char c = map.get(nextXLeft).get(i);
                    map.get(nextXLeft).set(i, map.get(nextXLeft).get(i+1));
                    map.get(nextXLeft).set(i+1, c);
                }
                map.get(nextXLeft).set(nextYLeft, CURRENT_POSITION);
                map.get(nextXLeft).set(position.getY(), EMPTY);
                return new Position(nextXLeft, nextYLeft);
            }
        }
    }

    private static Set<Box> findAllConsecutiveBoxes(List<List<Character>> map, Character movement, int x, int y) {
        Set<Box> boxes = new HashSet<>();
        Stack<Box> stack = new Stack<>();

        if (map.get(x).get(y) == LEFT_PART_BOX_P2) {
            addBoxWithLeftPart(x, y, boxes, stack);
        } else if (map.get(x).get(y) == RIGHT_PART_BOX_P2) {
            addBoxWithRightPart(x, y, boxes, stack);
        }

        while (!stack.isEmpty()) {
            Box box = stack.pop();
            Position left = box.getLeft();
            Position right = box.getRight();

            switch (movement) {
                case UP:
                    int nextRowUp = left.getX() - 1;

                    if (map.get(nextRowUp).get(left.getY()) == LEFT_PART_BOX_P2) {
                       addBoxWithLeftPart(nextRowUp, left.getY(), boxes, stack);
                    } else if (map.get(nextRowUp).get(left.getY()) == RIGHT_PART_BOX_P2) {
                        addBoxWithRightPart(nextRowUp, left.getY(), boxes, stack);
                    }

                    if (map.get(nextRowUp).get(right.getY()) == LEFT_PART_BOX_P2) {
                        addBoxWithLeftPart(nextRowUp, right.getY(), boxes, stack);
                    } else if (map.get(nextRowUp).get(right.getY()) == RIGHT_PART_BOX_P2) {
                        addBoxWithRightPart(nextRowUp, right.getY(), boxes, stack);
                    }

                    break;
                case DOWN:
                    int nextRowDown = left.getX() + 1;

                    if (map.get(nextRowDown).get(left.getY()) == LEFT_PART_BOX_P2) {
                        addBoxWithLeftPart(nextRowDown, left.getY(), boxes, stack);
                    } else if (map.get(nextRowDown).get(left.getY()) == RIGHT_PART_BOX_P2) {
                        addBoxWithRightPart(nextRowDown, left.getY(), boxes, stack);
                    }

                    if (map.get(nextRowDown).get(right.getY()) == LEFT_PART_BOX_P2) {
                        addBoxWithLeftPart(nextRowDown, right.getY(), boxes, stack);
                    } else if (map.get(nextRowDown).get(right.getY()) == RIGHT_PART_BOX_P2) {
                        addBoxWithRightPart(nextRowDown, right.getY(), boxes, stack);
                    }
            }
        }

        return boxes;
    }

    private static void addBoxWithLeftPart(int x, int y, Set<Box> boxes, Stack<Box> stack) {
        Position left = new Position(x, y);
        Position right = new Position(x, y+1);
        Box box = new Box(left, right);
        boxes.add(box);
        stack.push(box);
    }

    private static void addBoxWithRightPart(int x, int y, Set<Box> boxes, Stack<Box> stack) {
        Position left = new Position(x, y-1);
        Position right = new Position(x, y);
        Box box = new Box(left, right);
        boxes.add(box);
        stack.push(box);
    }

    private static int countConsecutiveBoxesP1(List<List<Character>> map, Character movement, int x, int y) {
        int count = 0;

        switch (movement) {
            case UP:
                do {
                    int nextX = x - 1;
                    if (isBox(map, nextX, y)) {
                        count++;
                        x = nextX;
                    } else {
                        break;
                    }
                } while (true);
                break;
            case DOWN:
                do {
                    int nextX = x + 1;
                    if (isBox(map, nextX, y)) {
                        count++;
                        x = nextX;
                    } else {
                        break;
                    }
                } while (true);
                break;
            case RIGHT:
                do {
                    int nextY = y + 1;
                    if (isBox(map, x, nextY)) {
                        count++;
                        y = nextY;
                    } else {
                        break;
                    }
                } while (true);
                break;
            case LEFT:
                do {
                    int nextY = y - 1;
                    if (isBox(map, x, nextY)) {
                        count++;
                        y = nextY;
                    } else {
                        break;
                    }
                } while (true);
                break;
        }

        return count;
    }

    private static int countConsecutiveBoxesP2(List<List<Character>> map, Character movement, int x, int y) {
        int count = 0;

        switch (movement) {
            case RIGHT:
                do {
                    int nextY = y + 2;
                    if (nextY < map.get(0).size() && isBox(map, x, nextY)) {
                        count++;
                        y = nextY;
                    } else {
                        break;
                    }
                } while (true);
                break;
            case LEFT:
                do {
                    int nextY = y - 2;
                    if (nextY >= 0 && isBox(map, x, nextY)) {
                        count++;
                        y = nextY;
                    } else {
                        break;
                    }
                } while (true);
                break;
        }

        return count;
    }

    private static boolean isWall(List<List<Character>> map, int x, int y) {
        return map.get(x).get(y) == WALL;
    }

    private static boolean isBox(List<List<Character>> map, int x, int y) {
        return map.get(x).get(y) == BOX_P1 || map.get(x).get(y) == LEFT_PART_BOX_P2 || map.get(x).get(y) == RIGHT_PART_BOX_P2;
    }

    private static boolean canMoveBoxes(List<List<Character>> map, Set<Box> boxes, Character movement) {
        switch (movement) {
            case UP:
                for (Box box : boxes) {
                    Position left = box.getLeft();
                    Position right = box.getRight();
                    if (isWall(map, left.getX() - 1, left.getY()) || isWall(map, right.getX() - 1, right.getY())) {
                        return false;
                    }
                }
                break;
            case DOWN:
                for (Box box : boxes) {
                    Position left = box.getLeft();
                    Position right = box.getRight();
                    if (isWall(map, left.getX() + 1, left.getY()) || isWall(map, right.getX() + 1, right.getY())) {
                        return false;
                    }
                }
        }

        return true;
    }

    private static void resetMap(List<List<Character>> map, Set<Box> boxes) {
        for (Box box : boxes) {
            Position left = box.getLeft();
            Position right = box.getRight();
            map.get(left.getX()).set(left.getY(), EMPTY);
            map.get(right.getX()).set(right.getY(), EMPTY);
        }
    }

    private static void moveBox(List<List<Character>> map, Set<Box> boxes, Character movement) {
        switch (movement) {
            case UP:
                for (Box box : boxes) {
                    Position left = box.getLeft();
                    Position right = box.getRight();
                    map.get(left.getX() - 1).set(left.getY(), LEFT_PART_BOX_P2);
                    map.get(right.getX() - 1).set(right.getY(), RIGHT_PART_BOX_P2);
                }
                break;
            case DOWN:
                for (Box box : boxes) {
                    Position left = box.getLeft();
                    Position right = box.getRight();
                    map.get(left.getX() + 1).set(left.getY(), LEFT_PART_BOX_P2);
                    map.get(right.getX() + 1).set(right.getY(), RIGHT_PART_BOX_P2);
                }
        }
    }

    private static int getGPSBoxes(List<List<Character>> map) {
        int res = 0;

        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j) == LEFT_PART_BOX_P2 || map.get(i).get(j) == BOX_P1) {
                    res += 100 * i + j;
                }
            }
        }

        return res;
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
    @EqualsAndHashCode
    private static class Box {
        private Position left;
        private Position right;
    }

    @Data
    @AllArgsConstructor
    private static class LanternFish {
        private List<List<Character>> map;
        private List<Character> movements;
    }
}

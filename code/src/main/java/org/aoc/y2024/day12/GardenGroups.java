package org.aoc.y2024.day12;

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

public class GardenGroups {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day12\\input.txt";

    public static void main(String[] args) {
        int countP1 = 0;
        int countP2 = 0;
        List<List<Character>> garden = parseInput();
        Set<Character> plants = findAllPlants(garden);
        for (Character plant : plants) {
            List<Set<Position>> regions = findPlantRegions(garden, plant);
            for (Set<Position> region : regions) {
                countP1 += getArea(region) * getPerimeter(region);
                countP2 += getArea(region) * getSide(region);
            }
        }
        System.out.println(countP1);
        System.out.println(countP2);
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

    private static Set<Character> findAllPlants(List<List<Character>> garden) {
        Set<Character> plants = new HashSet<>();
        for (List<Character> row: garden) {
            plants.addAll(row);
        }
        return plants;
    }

    private static List<Set<Position>> findPlantRegions(List<List<Character>> garden, Character plant) {
        List<Set<Position>> regions = new ArrayList<>();
        Set<Position> traversedPositions = new HashSet<>();
        for (int i = 0; i < garden.size(); i++) {
            for (int j = 0; j < garden.get(i).size(); j++) {
                Position position = new Position(i, j);
                if (garden.get(i).get(j).equals(plant) && !traversedPositions.contains(position)) {
                    Set<Position> region = new HashSet<>();
                    region.add(position);
                    findRegion(garden, position, plant, region);
                    traversedPositions.addAll(region);
                    regions.add(region);
                }
            }
        }
        return regions;
    }

    private static void findRegion(List<List<Character>> garden, Position position, Character plant, Set<Position> region) {
        List<Position> relatedPositions = getRelatedPosition(position);
        Position leftPosition = relatedPositions.get(0);
        Position rightPosition = relatedPositions.get(1);
        Position upPosition = relatedPositions.get(2);
        Position downPosition = relatedPositions.get(3);

        if (!region.contains(leftPosition) && !isOutsideGarden(garden, leftPosition) && garden.get(leftPosition.getX()).get(leftPosition.getY()).equals(plant)) {
            region.add(leftPosition);
            findRegion(garden, leftPosition, plant, region);
        }
        if (!region.contains(rightPosition) && !isOutsideGarden(garden, rightPosition) && garden.get(rightPosition.getX()).get(rightPosition.getY()).equals(plant)) {
            region.add(rightPosition);
            findRegion(garden, rightPosition, plant, region);
        }
        if (!region.contains(upPosition) && !isOutsideGarden(garden, upPosition) && garden.get(upPosition.getX()).get(upPosition.getY()).equals(plant)) {
            region.add(upPosition);
            findRegion(garden, upPosition, plant, region);
        }
        if (!region.contains(downPosition) && !isOutsideGarden(garden, downPosition) && garden.get(downPosition.getX()).get(downPosition.getY()).equals(plant)) {
            region.add(downPosition);
            findRegion(garden, downPosition, plant, region);
        }
    }

    private static boolean isOutsideGarden(List<List<Character>> garden, Position position) {
        return position.getX() < 0 || position.getY() < 0 || position.getX() >= garden.size() || position.getY() >= garden.get(0).size();
    }

    private static int getArea(Set<Position> region) {
        return region.size();
    }

    private static int getPerimeter(Set<Position> region) {
        int res = 0;

        for (Position position : region) {
            List<Position> relatedPositions = getRelatedPosition(position);
            Position leftPosition = relatedPositions.get(0);
            Position rightPosition = relatedPositions.get(1);
            Position upPosition = relatedPositions.get(2);
            Position downPosition = relatedPositions.get(3);

            if (!region.contains(leftPosition)) {
                res++;
            }
            if (!region.contains(rightPosition)) {
                res++;
            }
            if (!region.contains(upPosition)) {
                res++;
            }
            if (!region.contains(downPosition)) {
                res++;
            }
        }

        return res;
    }

    private static int getSide(Set<Position> region) {
        int res = 0;

        for (Position position: region) {
            List<Position> relatedPositions = getRelatedPosition(position);
            Position leftPosition = relatedPositions.get(0);
            Position rightPosition = relatedPositions.get(1);
            Position upPosition = relatedPositions.get(2);
            Position downPosition = relatedPositions.get(3);
            Position lowerRightPosition = new Position(position.getX() + 1, position.getY() + 1);
            Position upperRightPosition = new Position(position.getX() - 1, position.getY() + 1);
            Position lowerLeftPosition = new Position(position.getX() + 1, position.getY() - 1);
            Position upperLeftPosition = new Position(position.getX() - 1, position.getY() - 1);

            if (!region.contains(leftPosition) && !region.contains(upPosition)) {
                res++;
            }
            if (!region.contains(leftPosition) && !region.contains(downPosition)) {
                res++;
            }
            if (!region.contains(rightPosition) && !region.contains(upPosition)) {
                res++;
            }
            if (!region.contains(rightPosition) && !region.contains(downPosition)) {
                res++;
            }
            if (region.contains(rightPosition) && region.contains(downPosition) && !region.contains(lowerRightPosition)) {
                res++;
            }
            if (region.contains(rightPosition) && region.contains(upPosition) && !region.contains(upperRightPosition)) {
                res++;
            }
            if (region.contains(leftPosition) && region.contains(downPosition) && !region.contains(lowerLeftPosition)) {
                res++;
            }
            if (region.contains(leftPosition) && region.contains(upPosition) && !region.contains(upperLeftPosition)) {
                res++;
            }
        }

        return res;
    }

    private static List<Position> getRelatedPosition(Position position) {
        Position leftPosition = new Position(position.getX(), position.getY() - 1);
        Position rightPosition = new Position(position.getX(), position.getY() + 1);
        Position upPosition = new Position(position.getX() - 1, position.getY());
        Position downPosition = new Position(position.getX() + 1, position.getY());
        return List.of(leftPosition, rightPosition, upPosition, downPosition);
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode
    private static class Position {
        private int x;
        private int y;
    }
}

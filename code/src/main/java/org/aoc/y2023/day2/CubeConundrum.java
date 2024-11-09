package org.aoc.y2023.day2;

import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CubeConundrum {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2023\\day2\\input.txt";
    private static final String COLON_AND_SPACE_SEPARATOR = ": ";
    private static final String SEMICOLON_AND_SPACE_SEPARATOR = "; ";
    private static final String COMMAS_AND_SPACE_SEPARATOR = ", ";
    private static final String SPACE_SEPARATOR = " ";

    public static void main(String[] args) {
        List<Record> records = parseInput();
        int resP1 = 0;
        int resP2 = 0;

        for (Record record : records) {
            if (isValidRecord(record)) {
                resP1 += record.getId();
            }
            resP2 += getPowerOfMinimumSetOfCubes(record);
        }

        System.out.println(resP1);
        System.out.println(resP2);
    }

    private static List<Record> parseInput() {
        List<Record> records = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                String rawInput = scanner.nextLine();
                String[] rawInputComponents = rawInput.split(COLON_AND_SPACE_SEPARATOR);
                int recordId = Integer.parseInt(rawInputComponents[0].split(SPACE_SEPARATOR)[1]);
                List<CubeSubset> cubeSubsets = getCubeSubsets(rawInputComponents[1]);
                records.add(Record.builder().id(recordId).cubeSubsets(cubeSubsets).build());
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return records;
    }

    private static List<CubeSubset> getCubeSubsets(String input) {
        List<CubeSubset> cubeSubsets = new ArrayList<>();

        String[] rawSubsets = input.split(SEMICOLON_AND_SPACE_SEPARATOR);
        for (String rawSubset : rawSubsets) {
            CubeSubset cubeSubset = CubeSubset.builder().build();
            String[] rawCubes = rawSubset.split(COMMAS_AND_SPACE_SEPARATOR);
            for (String rawCube : rawCubes) {
                String[] rawCubeComponents = rawCube.split(SPACE_SEPARATOR);
                int quantity = Integer.parseInt(rawCubeComponents[0]);
                Color color = Color.toColor(rawCubeComponents[1]);
                cubeSubset.getCubes().add(Cube.builder().quantity(quantity).color(color).build());
            }
            cubeSubsets.add(cubeSubset);
        }

        return cubeSubsets;
    }

    private static boolean isValidRecord(Record record) {
        for (CubeSubset cubeSubset : record.getCubeSubsets()) {
            for (Cube cube : cubeSubset.getCubes()) {
                if (cube.getQuantity() > cube.getColor().getMaxQuantity()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int getPowerOfMinimumSetOfCubes(Record record) {
        int maxRed = 0;
        int maxGreen = 0;
        int maxBlue = 0;

        for (CubeSubset cubeSubset : record.getCubeSubsets()) {
            for (Cube cube : cubeSubset.getCubes()) {
                int quantity = cube.getQuantity();
                Color color = cube.getColor();
                if (color == Color.RED && quantity > maxRed) {
                    maxRed = quantity;
                } else if (color == Color.GREEN && quantity > maxGreen) {
                    maxGreen = quantity;
                } else if (color == Color.BLUE && quantity > maxBlue) {
                    maxBlue = quantity;
                }
            }
        }

        return maxRed * maxGreen * maxBlue;
    }

    @Data
    @Builder
    private static class Record {
        private int id;
        private List<CubeSubset> cubeSubsets;
    }

    @Data
    @Builder
    private static class CubeSubset {
        @Builder.Default
        private List<Cube> cubes = new ArrayList<>();
    }

    @Data
    @Builder
    private static class Cube {
        private int quantity;
        private Color color;
    }

    private enum Color {
        RED(12),
        GREEN(13),
        BLUE(14);

        private final int maxQuantity;

        Color(int maxQuantity) {
            this.maxQuantity = maxQuantity;
        }

        public int getMaxQuantity() {
            return maxQuantity;
        }

        public static Color toColor(String name) {
            for (Color color : Color.values()) {
                if (color.name().equals(name.toUpperCase())) {
                    return color;
                }
            }
            return null;
        }
    }
}
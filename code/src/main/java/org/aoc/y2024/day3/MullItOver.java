package org.aoc.y2024.day3;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MullItOver {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day3\\input.txt";
    private static final String MUL_REGEX = "mul\\((\\d+),(\\d+)\\)";
    private static final String DO_OR_DON_T_REGEX = "do\\(\\)|don't\\(\\)";
    private static final String DO = "do()";

    public static void main(String[] args) {
        List<String> input = parseInput();
        long resP1 = 0, resP2 = 0;

        for (String s : input) {
            resP1 += calculateP1(s);
            resP2 += calculateP2(s);
        }

        System.out.println(resP1);
        System.out.println(resP2);
    }

    private static List<String> parseInput() {
        List<String> input = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                input.add(scanner.nextLine());
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return input;
    }

    private static long calculateP1(String input) {
        long res = 0;
        Pattern pattern = Pattern.compile(MUL_REGEX);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            long num1 = Integer.parseInt(matcher.group(1));
            long num2 = Integer.parseInt(matcher.group(2));
            res += num1 * num2;
        }
        return res;
    }

    private static long calculateP2(String input) {
        long res = 0;
        List<Chunk> chunks = getChunksDoAndDoNot(input);
        for (int i = 0; i < chunks.size() - 1; i++) {
            Chunk curChunk = chunks.get(i);
            Chunk nextChunk = chunks.get(i+1);
            if (curChunk.getValue().equals(DO)) {
                res += calculateP1(input.substring(curChunk.getIndex(), nextChunk.getIndex()));
            }
            if (i+1 == chunks.size() - 1 && nextChunk.getValue().equals(DO)) {
                res += calculateP1(input.substring(nextChunk.getIndex()));
            }
        }
        return res;
    }

    private static List<Chunk> getChunksDoAndDoNot(String input) {
        List<Chunk> chunks = new ArrayList<>();
        chunks.add(new Chunk(0, DO));
        Pattern pattern = Pattern.compile(DO_OR_DON_T_REGEX);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            chunks.add(new Chunk(matcher.start(), matcher.group()));
        }
        return chunks;
    }

    @Data
    @AllArgsConstructor
    private static class Chunk {
        private int index;
        private String value;
    }
}

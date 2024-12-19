package org.aoc.y2024.day19;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LinenLayout {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day19\\input.txt";
    private static final String COMMAS_SPACE_SEPARATOR = ", ";
    private static final Map<String, BigInteger> memo = new HashMap<>();

    public static void main(String[] args) {
        Input input = parseInput();
        List<String> designs = input.getDesigns();
        List<String> patterns = input.getPatterns();
        int countP1 = 0;
        BigInteger countP2 = BigInteger.ZERO;

        for (String design : designs) {
            BigInteger res = count(patterns, design);
            if (!res.equals(BigInteger.ZERO)) {
                countP2 = countP2.add(res);
                countP1++;
            }
        }

        System.out.println(countP1);
        System.out.println(countP2);
    }

    private static Input parseInput() {
        List<String> patterns = new ArrayList<>();
        List<String> designs = new ArrayList<>();
        boolean isReadPatterns = true;

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                if (row.isEmpty()) {
                    isReadPatterns = false;
                } else {
                    if (isReadPatterns) {
                        String[] rawComponent = row.split(COMMAS_SPACE_SEPARATOR);
                        patterns.addAll(Arrays.asList(rawComponent));
                    } else {
                        designs.add(row);
                    }
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new Input(patterns, designs);
    }

    private static BigInteger count(List<String> patterns, String design) {
        if (design.isEmpty()) {
            return BigInteger.ONE;
        }
        if (memo.containsKey(design)) {
            return memo.get(design);
        }

        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < patterns.size(); i++) {
            String pattern = patterns.get(i);
            if (design.startsWith(pattern)) {
                String newPattern = design.substring(pattern.length());
                res = res.add(count(patterns, newPattern));
            }
        }

        memo.put(design, res);

        return res;
    }

    @Data
    @AllArgsConstructor
    private static class Input {
        private List<String> patterns;
        private List<String> designs;
    }
}

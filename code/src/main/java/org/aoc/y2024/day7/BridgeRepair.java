package org.aoc.y2024.day7;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class BridgeRepair {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day7\\input.txt";
    private static final String COLON_SPACE_SEPARATOR = ": ";
    private static final String SPACE_SEPARATOR = " ";
    private static final char[] OPERATORS = {'+', '*', '|'}; // Remove last operator for P1
    private static final Map<Integer, List<List<Character>>> groupOperatorCombinationsByMaxLength = new HashMap<>();
    private static final List<List<Character>> operatorCombinations = new ArrayList<>();

    public static void main(String[] args) {
        BigInteger res = new BigInteger("0");
        List<Equation> equations = parseInput();
        for (Equation equation : equations) {
            int maxOperatorLength = equation.getOperands().size() - 1;
            if (!groupOperatorCombinationsByMaxLength.containsKey(maxOperatorLength)) {
                setUpAllOperatorCombination(maxOperatorLength, new ArrayList<>());
                groupOperatorCombinationsByMaxLength.put(maxOperatorLength, new ArrayList<>(operatorCombinations));
                operatorCombinations.clear();
            }
            if (isMatch(equation)) {
                res = res.add(equation.getResult());
            }
        }
        System.out.println(res);
    }

    private static List<Equation> parseInput() {
        List<Equation> equations = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                String[] rawComponents = row.split(COLON_SPACE_SEPARATOR);
                BigInteger result = new BigInteger(rawComponents[0]);
                List<BigInteger> operands = Stream.of(rawComponents[1].split(SPACE_SEPARATOR))
                        .map(BigInteger::new)
                        .toList();
                equations.add(new Equation(result, operands));
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return equations;
    }

    private static boolean isMatch(Equation equation) {
        BigInteger result = equation.getResult();
        List<BigInteger> operands = equation.getOperands();
        for (List<Character> curOperators: groupOperatorCombinationsByMaxLength.get(operands.size() - 1)) {
            BigInteger curResult = new BigInteger(operands.get(0).toByteArray());
            for (int j = 1; j < operands.size(); j++) {
                if (curOperators.get(j - 1) == OPERATORS[0]) {
                    curResult = curResult.add(operands.get(j));
                } else if (curOperators.get(j - 1) == OPERATORS[1]) {
                    curResult = curResult.multiply(operands.get(j));
                } else {
                    curResult = new BigInteger(curResult.toString().concat(operands.get(j).toString()));
                }
            }
            if (result.equals(curResult)) {
                return true;
            }
        }
        return false;
    }

    private static void setUpAllOperatorCombination(int maxLength, List<Character> curOperatorCombination) {
        if (maxLength == curOperatorCombination.size()) {
            operatorCombinations.add(new ArrayList<>(curOperatorCombination));
            return;
        }
        for (char operator : OPERATORS) {
            curOperatorCombination.add(operator);
            setUpAllOperatorCombination(maxLength, curOperatorCombination);
            curOperatorCombination.removeLast();
        }
    }

    @Data
    @AllArgsConstructor
    private static class Equation {
        private BigInteger result;
        private List<BigInteger> operands;
    }
}

package org.aoc.y2024.day13;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ClawContraption {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day13\\input.txt";
    private static final String COMMAS_SPACE_SEPARATOR = ", ";
    private static final String COLON_SPACE_SEPARATOR = ": ";
    private static final String EQUAL = "=";
    private static final String PLUS = "\\+";
    private static final char X = 'X';
    private static final char Y = 'Y';
    private static final BigInteger COST_BUTTON_A_PRESSED = BigInteger.valueOf(3);
    private static final BigInteger ADDED_UNIT = BigInteger.valueOf(10000000000000L);

    public static void main(String[] args) {
        BigInteger resP1 = BigInteger.ZERO;
        BigInteger resP2 = BigInteger.ZERO;
        List<ClawMachine> clawMachines = parseInput();
        for (ClawMachine clawMachine : clawMachines) {
            resP1 = resP1.add(getFewestTokenP1(clawMachine));
            adjustPrize(clawMachine.getPrize());
            resP2 = resP2.add(getFewestTokenP1(clawMachine));
        }
        System.out.println(resP1);
        System.out.println(resP2);
    }

    private static List<ClawMachine> parseInput() {
        List<ClawMachine> clawMachines = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));
            List<String> tmpInput = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                if (tmpInput.size() < 3) {
                    tmpInput.add(row);
                } else {
                    setupInput(clawMachines, tmpInput);
                    tmpInput.clear();
                }
            }

            setupInput(clawMachines, tmpInput);
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return clawMachines;
    }

    private static void setupInput(List<ClawMachine> clawMachines, List<String> rawInput) {
        Map<Character, Integer> buttonA = buildButtonMap(rawInput.get(0));
        Map<Character, Integer> buttonB = buildButtonMap(rawInput.get(1));
        Map<Character, BigInteger> prize = buildPrize(rawInput.get(2));
        clawMachines.add(new ClawMachine(buttonA, buttonB, prize));
    }

    private static BigInteger getFewestTokenP1(ClawMachine clawMachine) {
        Map<Character, Integer> buttonA = clawMachine.getButtonA();
        Map<Character, Integer> buttonB = clawMachine.getButtonB();
        Map<Character, BigInteger> prize = clawMachine.getPrize();

        BigInteger res = BigInteger.ZERO;
        BigInteger a1 = new BigInteger(buttonA.get(X).toString());
        BigInteger b1 = new BigInteger(buttonB.get(X).toString());
        BigInteger a2 = new BigInteger(buttonA.get(Y).toString());
        BigInteger b2 = new BigInteger(buttonB.get(Y).toString());
        BigInteger c1 = new BigInteger(prize.get(X).toString());
        BigInteger c2 = new BigInteger(prize.get(Y).toString());
        BigDecimal factor1 = new BigDecimal(buttonA.get(X)).divide(new BigDecimal(buttonB.get(X)), MathContext.DECIMAL128);
        BigDecimal factor2 = new BigDecimal(buttonA.get(Y)).divide(new BigDecimal(buttonB.get(Y)), MathContext.DECIMAL128);

        if (!factor1.equals(factor2)) {
            BigInteger mul1 = c1.multiply(b2);
            BigInteger mul2 = c2.multiply(b1);
            BigInteger mul3 = a1.multiply(b2);
            BigInteger mul4 = a2.multiply(b1);
            BigInteger sub1 = mul1.subtract(mul2);
            BigInteger sub2 = mul3.subtract(mul4);
            BigDecimal x = new BigDecimal(sub1).divide(new BigDecimal(sub2), MathContext.DECIMAL128);

            BigInteger mul5 = c1.multiply(a2);
            BigInteger mul6 = c2.multiply(a1);
            BigInteger mul7 = b1.multiply(a2);
            BigInteger mul8 = b2.multiply(a1);
            BigInteger sub3 = mul5.subtract(mul6);
            BigInteger sub4 = mul7.subtract(mul8);
            BigDecimal y = new BigDecimal(sub3).divide(new BigDecimal(sub4), MathContext.DECIMAL128);

            if (x.compareTo(BigDecimal.ZERO) < 0 || y.compareTo(BigDecimal.ZERO) < 0) {
                return res;
            }
            if (x.stripTrailingZeros().scale() <= 0 && y.stripTrailingZeros().scale() <= 0) {
                res = new BigInteger(x.toString()).multiply(COST_BUTTON_A_PRESSED).add(new BigInteger(y.toString()));
            }
        }

        return res;
    }

    private static Map<Character, Integer> buildButtonMap(String input) {
        Map<Character, Integer> res = new HashMap<>();
        String[] components = input.split(COLON_SPACE_SEPARATOR);
        String[] detailButtons = components[1].split(COMMAS_SPACE_SEPARATOR);
        for (String detailButton: detailButtons) {
            String[] arr = detailButton.split(PLUS);
            res.put(arr[0].charAt(0), Integer.parseInt(arr[1]));
        }
        return res;
    }

    private static Map<Character, BigInteger> buildPrize(String input) {
        Map<Character, BigInteger> res = new HashMap<>();
        String[] components = input.split(COLON_SPACE_SEPARATOR);
        String[] detailPrizes = components[1].split(COMMAS_SPACE_SEPARATOR);
        for (String detailPrize: detailPrizes) {
            String[] arr = detailPrize.split(EQUAL);
            res.put(arr[0].charAt(0), BigInteger.valueOf(Integer.parseInt(arr[1])));
        }
        return res;
    }

    private static void adjustPrize(Map<Character, BigInteger> prize) {
        prize.replaceAll((k, v) -> prize.get(k).add(ADDED_UNIT));
    }

    @Data
    @AllArgsConstructor
    private static class ClawMachine {
        private Map<Character, Integer> buttonA;
        private Map<Character, Integer> buttonB;
        private Map<Character, BigInteger> prize;
    }
}

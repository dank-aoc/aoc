package org.aoc.y2024.day17;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ChronospatialComputer {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day17\\input.txt";
    private static final int SHIFT_RIGHT_BIT = 8;
    private static final char REGISTER_A = 'A';
    private static final char REGISTER_B = 'B';
    private static final char REGISTER_C = 'C';
    private static final String COMMAS_SEPARATOR = ",";
    private static final String SPACE_SEPARATOR = " ";
    private static final String COLON_SPACE_SEPARATOR = ": ";

    public static void main(String[] args) {
        Computer computer = parseInput();
        process(computer);

        List<BigInteger> res = new ArrayList<>();
        for (int i = 0; i < SHIFT_RIGHT_BIT; i++) {
            find(BigInteger.valueOf(i), computer.getPrograms().size() - 1, computer.getPrograms(), res);
        }

        System.out.println(res.stream().sorted().findFirst().get());
    }

    private static Computer parseInput() {
        Map<Character, Integer> registers = new HashMap<>();
        List<Integer> programs = new ArrayList<>();
        boolean isReadRegister = true;

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));

            while (scanner.hasNextLine()) {
                String row = scanner.nextLine();
                if (row.isEmpty()) {
                    isReadRegister = false;
                    continue;
                }

                String[] rawComponent = row.split(COLON_SPACE_SEPARATOR);
                if (isReadRegister) {
                    char register = rawComponent[0].split(SPACE_SEPARATOR)[1].charAt(0);;
                    registers.put(register, Integer.parseInt(rawComponent[1]));
                } else {
                    for (String s : rawComponent[1].split(COMMAS_SEPARATOR)) {
                        programs.add(Integer.parseInt(s));
                    }
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return new Computer(registers, programs);
    }

    private static void process(Computer computer) {
        int instructionPointer = 0;
        List<Integer> programs = computer.getPrograms();
        Map<Character, Integer> registers = computer.getRegisters();

        while (instructionPointer < programs.size()) {
            Integer opCode = programs.get(instructionPointer);
            Integer operand = programs.get(instructionPointer + 1);
            boolean isJump = false;

            switch (opCode) {
                case 0:
                    registers.put(REGISTER_A, (int) (registers.get(REGISTER_A) / Math.pow(2, getComboOperand(operand, registers))));
                    break;
                case 1:
                    registers.put(REGISTER_B, registers.get(REGISTER_B) ^ operand);
                    break;
                case 2:
                    registers.put(REGISTER_B, getComboOperand(operand, registers) % 8);
                    break;
                case 3:
                    if (registers.get(REGISTER_A) != 0) {
                        instructionPointer = operand;
                        isJump = true;
                    }
                    break;
                case 4:
                    registers.put(REGISTER_B, registers.get(REGISTER_C) ^ registers.get(REGISTER_B));
                    break;
                case 5:
                    System.out.print(getComboOperand(operand, registers) % 8 + COMMAS_SEPARATOR);
                    break;
                case 6:
                    registers.put(REGISTER_B, (int) (registers.get(REGISTER_A) / Math.pow(2, getComboOperand(operand, registers))));
                    break;
                case 7:
                    registers.put(REGISTER_C, (int) (registers.get(REGISTER_A) / Math.pow(2, getComboOperand(operand, registers))));
            }

            if (!isJump) {
                instructionPointer += 2;
            }
        }
    }

    private static Integer getComboOperand(int operand, Map<Character, Integer> registers) {
        if (operand >= 0 && operand <= 3) {
            return operand;
        } else if (operand == 4) {
            return registers.get(REGISTER_A);
        } else if (operand == 5) {
            return registers.get(REGISTER_B);
        } else if (operand == 6) {
            return registers.get(REGISTER_C);
        }
        return operand;
    }

    // Extract manual directly from input file
    private static int step(BigInteger a) {
        BigInteger b = a.mod(BigInteger.valueOf(8));
        b = b.xor(BigInteger.valueOf(5));
        BigInteger c = a.shiftRight(b.intValue());
        b = b.xor(BigInteger.valueOf(6));
        b = b.xor(c);
        return b.mod(BigInteger.valueOf(8)).intValue();
    }

    private static void find(BigInteger a, int col, List<Integer> programs, List<BigInteger> res) {
        if (programs.get(col) != step(a)) {
            return;
        }

        if (col == 0) {
            res.add(a);
        } else {
            for (int i = 0; i < SHIFT_RIGHT_BIT; i++) {
                find(a.multiply(BigInteger.valueOf(8)).add(BigInteger.valueOf(i)), col - 1, programs, res);
            }
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Computer {
        private Map<Character, Integer> registers;
        private List<Integer> programs;
    }
}

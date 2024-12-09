package org.aoc.y2024.day9;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DiskFragmenter {
    private static final String INPUT_PATH = "C:\\Users\\HUY\\IdeaProjects\\aoc\\code\\src\\main\\java\\org\\aoc\\y2024\\day9\\input.txt";
    private static final String FREE_SPACE = ".";
    private static final String ZERO = "0";

    public static void main(String[] args) {
        String diskMap = parseInput();
        Map<Integer, String> extractedMap = buildExtractedMap(diskMap);
        reconstructP1(extractedMap); // Change to reconstruct P2
        System.out.println(calculateChecksum(extractedMap));
    }

    private static String parseInput() {
        String input = "";

        try {
            Scanner scanner = new Scanner(new File(INPUT_PATH));
            input = scanner.nextLine();
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return input;
    }

    private static Map<Integer, String > buildExtractedMap(String diskMap) {
        Map<Integer, String> res = new HashMap<>();
        int id = 0;
        int index = 0;

        for (int i = 0; i < diskMap.length(); i++) {
            int c = diskMap.charAt(i) - ZERO.charAt(0);
            for (int j = 0; j < c; j++) {
                res.put(index, i % 2 == 0 ? String.valueOf(id) : FREE_SPACE);
                index++;
            }
            if (i % 2 == 0) {
                id++;
            }
        }

        return res;
    }

    private static void reconstructP1(Map<Integer, String> extractedMap) {
        do {
            int firstEmptySpaceIndex = getFirstEmptySpaceIndex(extractedMap);
            int lastFileIndex = getLastFileIndex(extractedMap);
            if (firstEmptySpaceIndex > lastFileIndex) {
                break;
            }
            String fileId = extractedMap.get(lastFileIndex);
            extractedMap.put(firstEmptySpaceIndex, fileId);
            extractedMap.put(lastFileIndex, FREE_SPACE);
        } while (true);
    }

    private static void reconstructP2(Map<Integer, String> extractedMap) {
        int latestId = getLatestId(extractedMap);
        do {
            Pair<Integer, Integer> filePair = getFirstIndexOfFileId(extractedMap, latestId);
            int firstIndex = getFirstIndexOfSpaceFitBlockFiles(extractedMap, filePair.getValue());
            if (firstIndex != Integer.MAX_VALUE && firstIndex < filePair.getKey()) {
                for (int i = 0; i < filePair.getValue(); i++) {
                    extractedMap.put(firstIndex + i, String.valueOf(latestId));
                    extractedMap.put(filePair.getKey() + i, FREE_SPACE);
                }
            }
            latestId--;
        } while (latestId > 0);
    }

    private static int getFirstEmptySpaceIndex(Map<Integer, String> map) {
        int res = Integer.MAX_VALUE;
        for (Map.Entry<Integer, String > entry : map.entrySet()) {
            if (entry.getValue().equals(FREE_SPACE) && res > entry.getKey()) {
                res = entry.getKey();
            }
        }
        return res;
    }

    private static int getLastFileIndex(Map<Integer, String> map) {
        int res = 0;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (!entry.getValue().equals(FREE_SPACE) && res < entry.getKey()) {
                res = entry.getKey();
            }
        }
        return res;
    }

    private static int getLatestId(Map<Integer, String> map) {
        int id = 0;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (!entry.getValue().equals(FREE_SPACE) && id < Integer.parseInt(entry.getValue())) {
                id = Integer.parseInt(entry.getValue());
            }
        }
        return id;
    }

    private static Pair<Integer, Integer> getFirstIndexOfFileId(Map<Integer, String> map, int id) {
        int firstIdx = Integer.MAX_VALUE;
        int count = 0;

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (!entry.getValue().equals(FREE_SPACE)) {
                int value = Integer.parseInt(entry.getValue());
                if (value == id) {
                    count++;
                    if (firstIdx > entry.getKey()) {
                        firstIdx = entry.getKey();
                    }
                }
            }
        }

        return Pair.of(firstIdx, count);
    }

    private static int getFirstIndexOfSpaceFitBlockFiles(Map < Integer, String > map, int numOfBlockFile) {
        int firstIndex = Integer.MAX_VALUE;

        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(FREE_SPACE) && firstIndex > entry.getKey()) {
                boolean isFit = true;
                for (int i = 0; i < numOfBlockFile; i++) {
                    if (map.get(entry.getKey() + i) == null || !map.get(entry.getKey() + i).equals(FREE_SPACE)) {
                        isFit = false;
                        break;
                    }
                }
                if (isFit) {
                    firstIndex = entry.getKey();
                }
            }
        }

        return firstIndex;
    }

    private static BigInteger calculateChecksum(Map<Integer, String> map) {
        BigInteger res = new BigInteger(ZERO);
        for (Map.Entry < Integer, String > entry: map.entrySet()) {
            if (!entry.getValue().equals(FREE_SPACE)) {
                BigInteger index = new BigInteger(String.valueOf(entry.getKey()));
                BigInteger value = new BigInteger(entry.getValue());
                res = res.add(index.multiply(value));
            }
        }
        return res;
    }
}

package com.mastermind.masterminddemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Engine {
    private final int[] secretCode;
    private final int[] currentGuess;
    private final boolean specialMode;
    private final int codeLength;

    public Engine(int length, boolean uniqueColors) {
        this.codeLength = length;
        this.specialMode = uniqueColors;
        this.secretCode = generateSecret();
        this.currentGuess = new int[codeLength];

        // Initialize the guess array with zeros
        Arrays.fill(currentGuess, 0);
    }
    
    //----------------------------------------------------------------------------------------------------//
    private int[] generateSecret() {
        int[] secret = new int[codeLength];
        List<Integer> available = new ArrayList<>();

        // Initialize available colors (1-8) //----------------------------i <= 8 BEFORE
        for (int i = 1; i <= codeLength; i++) {
            available.add(i);
        }
        for (int i = 0; i < codeLength; i++) {
            int randomIndex = (int)(Math.random() * available.size());
            secret[i] = available.get(randomIndex);
            // If colors must be unique, remove the used color
            if (specialMode) {
                available.remove(randomIndex);
            }
        }
        return secret;
    }
    //----------------------------------------------------------------------------------------------------//
    public int[] analyse() {
        int[] result = new int[codeLength];
        boolean[] secretUsed = new boolean[codeLength];
        boolean[] guessUsed = new boolean[codeLength];

        // First pass: find exact matches (position and color)
        for (int i = 0; i < codeLength; i++) {
            if (currentGuess[i] == secretCode[i]) {
                result[i] = 2; // Exact match
                secretUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        // Second pass: find color matches (color only)
        if (specialMode) {
            // For unique colors mode, each color can only be matched once
            for (int i = 0; i < codeLength; i++) {
                if (!guessUsed[i]) {
                    int guessColor = currentGuess[i];

                    // Check close positions: i-1, i+1 (stay within bounds)
                    for (int offset = -1; offset <= 1; offset++) {
                        if (offset == 0) continue; // Skip the current (exact) position

                        int j = i + offset;
                        if (j >= 0 && j < codeLength && !secretUsed[j]) {
                            if (secretCode[j] == guessColor) {
                                result[i] = 1; // Close-position color match
                                secretUsed[j] = true;
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            // For repeated colors mode, count remaining occurrences of each color
            int[] secretColorCounts = new int[9]; // Colors are 1-8, so we use 1-8 indices

            // Count remaining colors in secret
            for (int i = 0; i < codeLength; i++) {
                if (!secretUsed[i]) {
                    secretColorCounts[secretCode[i]]++;
                }
            }

            // Check remaining guess pegs for color matches
            for (int i = 0; i < codeLength; i++) {
                if (!guessUsed[i]) {
                    int colorId = currentGuess[i];
                    if (secretColorCounts[colorId] > 0) {
                        result[i] = 1; // Color match
                        secretColorCounts[colorId]--;
                    }
                }
            }
        }

        return result;
    }
    //----------------------------------------------------------------------------------------------------//
    public boolean checkWin() {
     return true;
    }
}
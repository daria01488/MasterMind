package com.mastermind.masterminddemo.service;

import com.mastermind.masterminddemo.model.GameMode;
import com.mastermind.masterminddemo.transfer.FeedbackTransfer;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 * Engine Class
 * Generating secret code & processing guesses.
 */
@Service
public class GameService {

    private final Random random = new Random();

    /*
     * gameMode: STANDARD or UNIQUE;
     * codeLength: 4-8;
     * Return: secret code.
     */
    public String generateSecretCode(GameMode gameMode, int codeLength) {
        if (gameMode == GameMode.UNIQUE) {
            // UNIQUE mode: shuffle digits (0-codeLength) = secret code.
            if (codeLength == 4) {
                List<String> specialDigits = new ArrayList<>(Arrays.asList("1", "2", "3", "4"));
                Collections.shuffle(specialDigits);
                return String.join("", specialDigits);
            }
            List<String> availableDigits = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
            Collections.shuffle(availableDigits);
            return String.join("", availableDigits.subList(0, codeLength));
        } else {
            // Standard mode - repeats allowed.
            StringBuilder secretCode = new StringBuilder();
            for (int i = 0; i < codeLength; i++) {
                secretCode.append(random.nextInt(10));
            }
            return secretCode.toString();
        }
    }

    /*
     * Processes a guess & generates feedback.
     * Analysis based on the game mode.
     * gameMode: STANDARD or UNIQUE;
     * secretCode; guess;
     * return FeedbackTransfer = guess analysis.
     */
    public FeedbackTransfer processGuess(GameMode gameMode, String secretCode, String guess) {
        if (gameMode == GameMode.UNIQUE) {
            return analyseUniqueMode(secretCode, guess);
        } else {
            return analyseStandardMode(secretCode, guess);
        }
    }

    /**
     * Analyzes a guess for the STANDARD game mode (numbers can repeat).
     * Feedback: correct position ("exact") and correct number in wrong position ("number").
     */
    private FeedbackTransfer analyseStandardMode(String secretCode, String guess) {
        int exactMatches = 0;
        int numberMatches = 0;
        int codeLength = secretCode.length();

        boolean[] secretUsed = new boolean[codeLength];
        boolean[] guessUsed = new boolean[codeLength];

        // First pass: Check for exact matches (correct number in correct position)
        for (int i = 0; i < codeLength; i++) {
            if (secretCode.charAt(i) == guess.charAt(i)) {
                exactMatches++;
                secretUsed[i] = true;
                guessUsed[i] = true;
            }
        }

        // Second pass: Check for number matches (correct number in wrong position)
        Map<Character, Integer> secretCodeCounts = new HashMap<>();
        for(int i = 0; i < codeLength; i++) {
            if (!secretUsed[i]) {
                char c = secretCode.charAt(i);
                secretCodeCounts.put(c, secretCodeCounts.getOrDefault(c, 0) + 1);
            }
        }

        for (int i = 0; i < codeLength; i++) {
            if (!guessUsed[i]) {
                char c = guess.charAt(i);
                if (secretCodeCounts.containsKey(c) && secretCodeCounts.get(c) > 0) {
                    numberMatches++;
                    secretCodeCounts.put(c, secretCodeCounts.get(c) - 1);
                }
            }
        }
        return new FeedbackTransfer(exactMatches, numberMatches, 0);
    }

    /*
     * Analyzes a guess for the UNIQUE game mode (all numbers are unique).
     * Feedback: correct position ("exact") and number shifted one place left/right ("close").
     */
    private FeedbackTransfer analyseUniqueMode(String secretCode, String guess) {
        int exactMatches = 0;
        int closeMatches = 0;
        int codeLength = secretCode.length();

        // First pass: Check for exact matches
        for (int i = 0; i < codeLength; i++) {
            if (secretCode.charAt(i) == guess.charAt(i)) {
                exactMatches++;
            }
        }

        // Second pass: Check for close matches (shifted one place)
        for (int i = 0; i < codeLength; i++) {
            char guessChar = guess.charAt(i);
            // Check left neighbor (if it exists)
            if (i > 0 && guessChar == secretCode.charAt(i - 1)) {
                closeMatches++;
            }
            // Check right neighbor (if it exists)
            if (i < codeLength - 1 && guessChar == secretCode.charAt(i + 1)) {
                closeMatches++;
            }
        }
        return new FeedbackTransfer(exactMatches, 0, closeMatches);
    }
}

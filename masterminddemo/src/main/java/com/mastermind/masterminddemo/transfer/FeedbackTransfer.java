package com.mastermind.masterminddemo.transfer;

public record FeedbackTransfer(
    int exactMatches,
    int numberMatches,
    int closeMatches
) {}
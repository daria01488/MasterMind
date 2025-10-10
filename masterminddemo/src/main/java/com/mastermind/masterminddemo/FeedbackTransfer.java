package com.mastermind.masterminddemo;

public record FeedbackTransfer(
    int correcrPosition,
    int correctNumber,
    int closePosition
) {}
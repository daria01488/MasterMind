package com.mastermind.masterminddemo;

public record GameSettingsTransfer(
    int codeLength,
    boolean gameMode
) {}
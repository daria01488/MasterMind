package com.mastermind.masterminddemo.transfer;

public record GameSettingsTransfer(
    int codeLength,
    boolean gameMode
) {}
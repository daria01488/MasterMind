package com.mastermind.masterminddemo.model;

import com.mastermind.masterminddemo.FeedbackTransfer;

//Hold the data for a single turn in the game.
public record GameTurn(
    String guess,
    FeedbackTransfer feedback
) {}
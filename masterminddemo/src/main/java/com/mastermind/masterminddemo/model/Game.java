package com.mastermind.masterminddemo.model;

import com.mastermind.masterminddemo.FeedbackTransfer;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 'games' table in the database.
 * Each instance of this class corresponds to a single row in the table.
 */
@Entity
@Table(name = "games")
public class Game {

    /**
     * The unique identifier for the game. This is the Primary Key.
     */
    @Id
    @Column(name = "game_id")
    private String id;

    /**
     * The ID of the user who is playing this game. This links to the UserStats entity.
     */
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "secret_code", nullable = false, length = 8)
    private String secretCode;

    @Column(name = "code_length", nullable = false)
    private int codeLength;

    /**
     * The number of attempts the player has made.
     */
    @Column(nullable = false)
    private int attempts = 0;

    /**
     * The current status of the game (e.g., IN_PROGRESS, WON).
     * Stored as a String in the database.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    /**
     * The mode of the game (e.g., STANDARD, UNIQUE).
     * Stored as a String in the database.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "game_mode", nullable = false)
    private GameMode gameMode;

    /**
     * Stores the entire history of guesses and their corresponding feedback.
     * This List is converted to and from a JSONB column in PostgreSQL.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "guess_history", columnDefinition = "jsonb")
    private List<GameTurn> guessHistory = new ArrayList<>();

    /**
     * The timestamp when the game was created. Automatically set on creation.
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * The timestamp when the game was last updated. Automatically set on every update.
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // --- Constructors, Getters, and Setters ---

    public Game() {
        // JPA requires a no-arg constructor
    }

    // You can add a constructor for easier object creation
    public Game(String id, String userId, String secretCode, int codeLength, GameMode gameMode) {
        this.id = id;
        this.userId = userId;
        this.secretCode = secretCode;
        this.codeLength = codeLength;
        this.gameMode = gameMode;
        this.status = GameStatus.IN_PROGRESS; // New games always start in progress
    }
    
    // Standard getters and setters for all fields...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSecretCode() { return secretCode; }
    public void setSecretCode(String secretCode) { this.secretCode = secretCode; }
    public int getCodeLength() { return codeLength; }
    public void setCodeLength(int codeLength) { this.codeLength = codeLength; }
    public int getAttempts() { return attempts; }
    public void setAttempts(int attempts) { this.attempts = attempts; }
    public GameStatus getStatus() { return status; }
    public void setStatus(GameStatus status) { this.status = status; }
    public GameMode getGameMode() { return gameMode; }
    public void setGameMode(GameMode gameMode) { this.gameMode = gameMode; }
    public List<GameTurn> getGuessHistory() { return guessHistory; }
    public void setGuessHistory(List<GameTurn> guessHistory) { this.guessHistory = guessHistory; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper method to add a new turn to the history
    public void addTurn(String guess, FeedbackTransfer feedback) {
        this.guessHistory.add(new GameTurn(guess, feedback));
        this.attempts++;
    }
}

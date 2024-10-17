package com.github.teraprath.tinylib.game.state;

import javax.annotation.Nonnull;

public abstract class GameStateManager {

    private GameState gameState;

    public GameStateManager() {
        this.gameState = GameState.WAITING;
    }

    public void setGameState(@Nonnull GameState gameState) {
        this.gameState = gameState;
        this.onChange(gameState);
    }

    public abstract void onChange(GameState gameState);

    public GameState getGameState() {
        return gameState;
    }
}

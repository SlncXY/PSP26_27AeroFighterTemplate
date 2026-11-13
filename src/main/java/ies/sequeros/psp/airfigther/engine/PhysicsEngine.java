package ies.sequeros.psp.airfigther.engine;

import ies.sequeros.psp.airfigther.GameBoard;

public class PhysicsEngine extends Thread {

    private static final int TARGET_UPS = 60;
    private static final long OPTIMAL_TIME = 1_000_000_000L / TARGET_UPS;
    private final GameBoard gameBoard;
    private final GameContext gameContext;

    public PhysicsEngine(GameBoard gameBoard, GameContext gameContext) {
        this.gameBoard = gameBoard;
        this.gameContext = gameContext;
    }
    @Override
    public void run() {
        long lastUpdateTime = System.nanoTime();

        while (!isInterrupted()
                && gameContext.getStatus() != GameStatus.GAME_OVER) {

            long now = System.nanoTime();
            long delta = now - lastUpdateTime;
            lastUpdateTime = now;

            synchronized (gameBoard) {
                if (gameContext.getStatus() == GameStatus.PLAYING) {
                    gameBoard.update(delta);
                }
            }

            long processingTime = System.nanoTime() - now;
            long sleepTime = (OPTIMAL_TIME - processingTime) / 1_000_000L;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}


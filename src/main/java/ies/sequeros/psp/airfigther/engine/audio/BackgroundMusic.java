package ies.sequeros.psp.airfigther.engine.audio;

import ies.sequeros.psp.airfigther.engine.GameContext;
import ies.sequeros.psp.airfigther.engine.GameStatus;

import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.InputStream;
import java.io.FileNotFoundException;

public class BackgroundMusic extends Thread {

    private AdvancedPlayer player;
    private final GameContext gameContext;


    public BackgroundMusic(GameContext gameContext) {
        this.gameContext = gameContext;
    }
    @Override
    public void run() {
        try (InputStream audio = getClass().getResourceAsStream("/song1.mp3")) {
            if (audio == null) {
                throw new FileNotFoundException("No se encuentra song1.mp3");
            }

            synchronized (this) {
                if (isInterrupted()
                        || gameContext.getStatus() == GameStatus.GAME_OVER) {
                    return;
                }

                player = new AdvancedPlayer(audio);
            }

            player.play();

        } catch (Exception e) {
            if (!isInterrupted()
                    && gameContext.getStatus() != GameStatus.GAME_OVER) {
                e.printStackTrace();
            }
        } finally {
            synchronized (this) {
                if (player != null) {
                    player.close();
                    player = null;
                }
            }
        }
    }
    public synchronized void shutdown() {
        interrupt();

        if (player != null) {
            player.close();
        }
    }
}

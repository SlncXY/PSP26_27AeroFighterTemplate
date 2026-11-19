package ies.sequeros.psp.airfigther.engine.audio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javazoom.jl.player.advanced.AdvancedPlayer;
import ies.sequeros.psp.airfigther.engine.GameStatus;

import java.util.concurrent.TimeUnit;
import java.io.InputStream;

import ies.sequeros.psp.airfigther.engine.GameContext;

public class SoundEffectsPlayer  extends Thread {

    private final GameContext gameContext;
    private final ExecutorService executorService;

    public SoundEffectsPlayer(GameContext gameContext) {
        this.gameContext = gameContext;
        this.executorService = Executors.newFixedThreadPool(10);
    }


    private void playSound(SoundEffect effect) {
        AdvancedPlayer player = null;

        try (InputStream audio = effect.getInputStream()) {
            player = new AdvancedPlayer(audio);
            player.play();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (player != null) {
                player.close();
            }
        }
    }
    @Override
    public void run() {
        try {
            while (!isInterrupted()
                    && gameContext.getStatus() != GameStatus.GAME_OVER) {

                SoundEffect effect = gameContext.getSoundQueue()
                        .poll(100, TimeUnit.MILLISECONDS);

                if (effect != null) {
                    executorService.execute(() -> playSound(effect));
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        } finally {
            executorService.shutdown();
        }
    }
    public void shutdown() {
        interrupt();
    }

}

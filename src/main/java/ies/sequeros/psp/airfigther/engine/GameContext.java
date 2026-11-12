package ies.sequeros.psp.airfigther.engine;

import ies.sequeros.psp.airfigther.engine.audio.SoundEffect;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameContext {
    private volatile GameStatus status = GameStatus.MENU;
    private volatile GameStatus lastStatus = GameStatus.MENU;
    private volatile boolean debugMode = false;
    private final BlockingQueue<SoundEffect> soundQueue = new LinkedBlockingQueue<>();

    public GameContext(GameStatus status, boolean debugMode) {
        this.status = status;
        this.debugMode = debugMode;
    }
    public synchronized GameStatus getStatus() {

        return status;
    }
    public synchronized void setStatus(GameStatus status) {
        if(this.status!=status) {
        this.lastStatus =this.status;
        }
        this.status = status;
    }


    public synchronized GameStatus getLastStatus() {
        return lastStatus;
    }
    public synchronized GameStatus consumeLastStatus() {
        GameStatus tempo=this.lastStatus;
        this.lastStatus=status;
        return tempo;
    }
    public synchronized void setLastStatus(GameStatus lastStatus) {
        this.lastStatus = lastStatus;
    }
    public boolean isDebugMode() {
        return debugMode;
    }
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
    public void putSound(SoundEffect effect) {
        try {
            soundQueue.put(effect); // Inserta de forma segura para hilos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    public BlockingQueue<SoundEffect> getSoundQueue() {
        return soundQueue;
    }
}

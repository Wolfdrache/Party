package com.wolfdrache.serverapi.gamemodes.roundManagement;

public interface IRoundMode {
    boolean isEdited();
    boolean isWaiting();
    boolean isEnding();
    boolean isRunning();
    String getModeName();
}

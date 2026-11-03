package com.wolfdrache.serverapi.gamemodes;

public enum GameCommandTypes {
    JOIN (true),
    LEAVE (false),
    START (false),
    ADMIN (true),
    STATS (true),
    FORCEJOIN (true);

    public final boolean requiresPlugin;
    GameCommandTypes(boolean requiresPlugin) {
        this.requiresPlugin = requiresPlugin;
    }
}

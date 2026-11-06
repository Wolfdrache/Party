package com.wolfdrache.serverapi.gamemodes;

import com.wolfdrache.serverapi.gamemodes.GameService.CommandType;

public enum GameCommandTypes {
    JOIN (CommandType.JOIN),
    LEAVE (CommandType.LEAVE),
    START (CommandType.START),
    ADMIN (CommandType.ADMIN),
    STATS (CommandType.STATS),
    FORCEJOIN (CommandType.FORCEJOIN);

    public final GameService.CommandType commandType;
    GameCommandTypes(CommandType commandType) {
        this.commandType = commandType;
    }
}

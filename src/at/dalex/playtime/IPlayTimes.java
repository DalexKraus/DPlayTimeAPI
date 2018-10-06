package at.dalex.playtime;

import java.util.HashMap;
import java.util.UUID;

/*
 * Copyright 2018 David Kraus. All rights reserved.
 */
interface IPlayTimes {

    HashMap<UUID, Integer> playerPlayTimes = new HashMap<>();
    HashMap<UUID, Integer> playerSessionTimes = new HashMap<>();
}

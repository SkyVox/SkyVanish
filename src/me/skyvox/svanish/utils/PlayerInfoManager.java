package me.skyvox.svanish.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInfoManager {
	private static Map<UUID, UUID> playerInfo = new HashMap<>();
	
	public static Map<UUID, UUID> getPlayerInfo() {
		return playerInfo;
	}
}
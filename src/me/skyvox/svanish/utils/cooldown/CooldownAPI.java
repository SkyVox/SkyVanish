package me.skyvox.svanish.utils.cooldown;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownAPI {
    private static Map<String, CooldownAPI> cooldowns = new HashMap<String, CooldownAPI>();
    private long start;
    private final int time;
    private final UUID id;
    private final String cooldownName;
    
    public CooldownAPI(UUID id, String cooldownName, int time) {
        this.id = id;
        this.cooldownName = cooldownName;
        this.time = time;
    }
    
    public static boolean isOnCooldown(UUID id, String cooldownName) {
        if (getTimeLeft(id, cooldownName) >= 1) {
            return true;
        } else {
            stop(id, cooldownName);
            return false;
        }
    }
    
    private static void stop(UUID id, String cooldownName) {
    	CooldownAPI.cooldowns.remove(id+cooldownName);
    }
    
    private static CooldownAPI getCooldown(UUID id, String cooldownName) {
        return cooldowns.get(id.toString()+cooldownName);
    }
    
    public static int getTimeLeft(UUID id, String cooldownName) {
    	CooldownAPI cooldown = getCooldown(id, cooldownName);
        int f = -1;
        if (cooldown != null) {
            long now = System.currentTimeMillis();
            long cooldownTime = cooldown.start;
            int totalTime = cooldown.time;
            int r = (int) (now - cooldownTime) / 1000;
            f = (r - totalTime) * (-1);
        }
        return f;
    }
    
    public void start() {
        this.start = System.currentTimeMillis();
        cooldowns.put(this.id.toString()+this.cooldownName, this);
    }
}
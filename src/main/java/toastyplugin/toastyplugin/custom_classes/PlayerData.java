package toastyplugin.toastyplugin.custom_classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PlayerData {

    private Rank rank;

    private String className;
    private int level;
    private int exp;
    private PlayerStats playerStats;
    private Set<String> unlockedClasses;
    private Set<String> unlockedDungeons;
    private Map<String, Integer> dungeonRuns;

    public PlayerData() {
        className = "Warrior";
        level = 1;
        exp = 0;
        playerStats = new PlayerStats();
        unlockedClasses = new HashSet<>();
        unlockedClasses.add("Warrior");
        unlockedDungeons = new HashSet<>();
        dungeonRuns = new HashMap<>();
    }

}

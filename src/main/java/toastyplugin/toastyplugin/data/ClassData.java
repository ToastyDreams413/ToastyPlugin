package toastyplugin.toastyplugin.data;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassData {

    public static final Map<String, String> CLASS_WEAPONS = new HashMap<String, String>() {{
        put("Warrior", "Sword");
        put("Berserker", "Sword");
        put("Monk", "Sword");
        put("Swashbuckler", "Sword");
        put("Elementalist", "Wand");
        put("Battlemage", "Wand");
        put("Puppeteer", "Dagger");
        put("Bladedancer", "Dagger");
        put("Alchemist", "Wand");
        put("Pyromancer", "Staff");
        put("Chronomancer", "Staff");
        put("Bard", "Bow");
        put("Ranger", "Bow");
    }};

    public static final Map<String, String> CLASS_ARMOR = new HashMap<String, String>() {{
        put("Warrior", "Heavy Armor");
        put("Berserker", "Medium Armor");
        put("Monk", "Clothes");
        put("Swashbuckler", "Heavy Armor");
        put("Elementalist", "Light Armor");
        put("Battlemage", "Heavy Armor");
        put("Puppeteer", "Light Armor");
        put("Bladedancer", "Medium Armor");
        put("Alchemist", "Light Armor");
        put("Pyromancer", "Clothes");
        put("Chronomancer", "Clothes");
        put("Bard", "Medium Armor");
        put("Ranger", "Light Armor");
    }};

    public static final Set<String> CLASS_NAMES = new HashSet<String>() {{
        add("Warrior");
        add("Berserker");
        add("Monk");
        add("Swashbuckler");
        add("Elementalist");
        add("Battlemage");
        add("Puppeteer");
        add("Bladedancer");
        add("Alchemist");
        add("Pyromancer");
        add("Chronomancer");
        add("Bard");
        add("Ranger");
    }};

    public static final Map<String, Integer> CLASS_TO_MENU_MODEL_NUMBER = new HashMap<String, Integer>() {{
        put("Warrior", 100);
        put("Berserker", 101);
        put("Monk", 102);
        put("Swashbuckler", 103);
        put("Elementalist", 104);
        put("Battlemage", 105);
        put("Puppeteer", 106);
        put("Bladedancer", 107);
        put("Alchemist", 108);
        put("Pyromancer", 109);
        put("Chronomancer", 110);
        put("Bard", 111);
        put("Ranger", 112);
    }};

    public static final Map<Integer, String> MENU_MODEL_NUMBER_TO_CLASS = new HashMap<Integer, String>() {{
        put(100, "Warrior");
        put(101, "Berserker");
        put(102, "Monk");
        put(103, "Swashbuckler");
        put(104, "Elementalist");
        put(105, "Battlemage");
        put(106, "Puppeteer");
        put(107, "Bladedancer");
        put(108, "Alchemist");
        put(109, "Pyromancer");
        put(110, "Chronomancer");
        put(111, "Bard");
        put(112, "Ranger");
    }};

}

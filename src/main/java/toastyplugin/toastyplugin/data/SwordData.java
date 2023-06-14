package toastyplugin.toastyplugin.data;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class SwordData {

    public static final Map<String, Double> SWORD_DAMAGE = new HashMap<String, Double>() {{
        put("Starter Sword", 5.0);
        put("Rusty Sword", 6.0);
    }};

    public static final Map<String, Double> SWORD_RANGE = new HashMap<String, Double>() {{
        put("Starter Sword", 5.0);
        put("Rusty Sword", 5.0);
    }};

    public static final Map<String, String> SWORD_DISPLAY_NAME = new HashMap<String, String>() {{
        put("Starter Sword", ChatColor.GRAY + "Starter Sword");
        put("Rusty Sword", ChatColor.DARK_GRAY + "Rusty Sword");
    }};

    public static final Map<String, String> SWORD_LORE = new HashMap<String, String>() {{
        put("Starter Sword", "A simple starter sword.");
        put("Rusty Sword", "A rusty sword.");
    }};

    public static final Map<String, Integer> SWORD_MODEL_NUMBER = new HashMap<String, Integer>() {{
        put("Starter Sword", 1);
        put("Rusty Sword", 2);
    }};

}

package toastyplugin.toastyplugin.data;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SwordData {

    public static final HashMap<String, Double> SWORD_DAMAGE = new HashMap<String, Double>() {{
        put("Starter Sword", 5.0);
    }};

    public static final HashMap<String, Double> SWORD_RANGE = new HashMap<String, Double>() {{
        put("Starter Sword", -1.0);
    }};

    public static final HashMap<String, String> SWORD_DISPLAY_NAME = new HashMap<String, String>() {{
        put("Starter Sword", ChatColor.GRAY + "Starter Sword");
    }};

    public static final HashMap<String, String> SWORD_LORE = new HashMap<String, String>() {{
        put("Starter Sword", "A simple starter sword");
    }};

    public static final HashMap<String, Integer> SWORD_MODEL_NUMBER = new HashMap<String, Integer>() {{
        put("Starter Sword", 1);
    }};

}
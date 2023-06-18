package toastyplugin.toastyplugin.data;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.HashMap;
import java.util.Map;

public class WandData {

    public static final Map<String, Double> WAND_DAMAGE = new HashMap<String, Double>() {{
        put("Starter Wand", 5.0);
        put("Honey Wand", 6.0);
        put("Wand of Despair", 20.0);
    }};

    public static final Map<String, Color> WAND_SHOT_COLOR = new HashMap<String, Color>() {{
        put("Starter Wand", Color.WHITE);
        put("Honey Wand", Color.YELLOW);
        put("Wand of Despair", Color.BLACK);
    }};

    public static final Map<String, Double> WAND_VELOCITY = new HashMap<String, Double>() {{
        put("Starter Wand", 0.8);
        put("Honey Wand", 0.8);
        put("Wand of Despair", 0.5);
    }};

    public static final Map<String, Integer> WAND_COOLDOWN = new HashMap<String, Integer>() {{
        put("Starter Wand", 800);
        put("Honey Wand", 600);
        put("Wand of Despair", 1000);
    }};


    public static final Map<String, Double> WAND_RANGE = new HashMap<String, Double>() {{
        put("Starter Wand", 10.0);
        put("Honey Wand", 12.0);
        put("Wand of Despair", 8.0);
    }};

    public static final Map<String, String> WAND_DISPLAY_NAME = new HashMap<String, String>() {{
        put("Starter Wand", ChatColor.WHITE + "Starter Wand");
        put("Honey Wand", ChatColor.DARK_GRAY + "Honey Wand");
        put("Wand of Despair", ChatColor.DARK_RED + "Wand of Despair");
    }};

    public static final Map<String, String> WAND_LORE = new HashMap<String, String>() {{
        put("Starter Wand", "A simple starter wand");
        put("Honey Wand", "A wand made of honey. Surprisingly, it's somewhat effective.");
        put("Wand of Despair", "Just holding this wand makes you want to crawl back into bed under your covers.");
    }};

    public static final Map<String, Integer> WAND_MODEL_NUMBER = new HashMap<String, Integer>() {{
        put("Starter Wand", 1);
        put("Honey Wand", 2);
        put("Wand of Despair", 3);
    }};

}

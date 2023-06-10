package toastyplugin.toastyplugin.data;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class WandData {

    public static final Map<String, Double> WAND_DAMAGE = new HashMap<String, Double>() {{
        put("Starter Wand", 5.0);
    }};

    public static final Map<String, Double> WAND_VELOCITY = new HashMap<String, Double>() {{
        put("Starter Wand", 0.8);
    }};

    public static final Map<String, Integer> WAND_COOLDOWN = new HashMap<String, Integer>() {{
        put("Starter Wand", 800);
    }};


    public static final Map<String, Double> WAND_RANGE = new HashMap<String, Double>() {{
        put("Starter Wand", 10.0);
    }};

    public static final Map<String, String> WAND_DISPLAY_NAME = new HashMap<String, String>() {{
        put("Starter Wand", ChatColor.GRAY + "Starter Wand");
    }};

    public static final Map<String, String> WAND_LORE = new HashMap<String, String>() {{
        put("Starter Wand", "A simple starter wand");
    }};

    public static final Map<String, Integer> WAND_MODEL_NUMBER = new HashMap<String, Integer>() {{
        put("Starter Wand", 1);
    }};

}

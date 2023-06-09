package toastyplugin.toastyplugin.data;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BowData {

    public static final HashMap<String, Double> BOW_DAMAGE = new HashMap<String, Double>() {{
        put("Starter Bow", 10.0);
    }};

    public static final HashMap<String, String> BOW_DISPLAY_NAME = new HashMap<String, String>() {{
        put("Starter Bow", ChatColor.GRAY + "Starter Bow");
    }};

    public static final HashMap<String, String> BOW_LORE = new HashMap<String, String>() {{
        put("Starter Bow", "A simple starter bow");
    }};

    public static final HashMap<String, Integer> BOW_MODEL_NUMBER = new HashMap<String, Integer>() {{
        put("Starter Sword", 1);
    }};

}

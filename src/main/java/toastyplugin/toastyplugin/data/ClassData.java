package toastyplugin.toastyplugin.data;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClassData {

    public static final Map<String, String> CLASS_CAN_USE = new HashMap<String, String>() {{
        put("Warrior", "Sword");
        put("Warrior", "Gauntlet");
    }};

    public static final Set<String> CLASS_NAMES = new HashSet<String>() {{
        add("Warrior");
    }};

}

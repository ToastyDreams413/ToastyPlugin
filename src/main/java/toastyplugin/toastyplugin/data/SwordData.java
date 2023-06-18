package toastyplugin.toastyplugin.data;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public class SwordData {

    public static final Map<String, Double> SWORD_DAMAGE = new HashMap<String, Double>() {{
        put("Starter Sword", 5.0);
        put("Rusty Sword", 6.0);
        put("Decaying Drumstick", 7.0);
        put("Flimsy Rapier", 8.0);
        put("Arachna's Fang", 9.0);
        put("Cracked Stone Hammer", 10.0);
        put("Cutlass of the Seas", 11.0);
        put("Crystal Javelin", 12.0);
        put("Sword of Electrifying Power", 13.0);
    }};

    public static final Map<String, Double> SWORD_RANGE = new HashMap<String, Double>() {{
        put("Starter Sword", 3.5);
        put("Rusty Sword", 3.5);
        put("Decaying Drumstick", 3.5);
        put("Flimsy Rapier", 3.5);
        put("Arachna's Fang", 3.5);
        put("Cracked Stone Hammer", 3.5);
        put("Cutlass of the Seas", 3.5);
        put("Crystal Javelin", 7.0);
        put("Sword of Electrifying Power", 3.5);
    }};

    public static final Map<String, String> SWORD_DISPLAY_NAME = new HashMap<String, String>() {{
        put("Starter Sword", ChatColor.WHITE + "Starter Sword");
        put("Rusty Sword", ChatColor.WHITE + "Rusty Sword");
        put("Decaying Drumstick", ChatColor.DARK_GRAY + "Decaying Drumstick");
        put("Flimsy Rapier", ChatColor.DARK_GRAY + "Flimsy Rapier");
        put("Arachna's Fang", ChatColor.DARK_GRAY + "Arachna's Fang");
        put("Cracked Stone Hammer", ChatColor.DARK_GRAY + "Cracked Stone Hammer");
        put("Cutlass of the Seas", ChatColor.DARK_AQUA + "Cutlass of the Seas");
        put("Crystal Javelin", ChatColor.DARK_RED + "Crystal Javelin");
        put("Sword of Electrifying Power", ChatColor.DARK_PURPLE + "Sword of Electrifying Power");
    }};

    public static final Map<String, String> SWORD_LORE = new HashMap<String, String>() {{
        put("Starter Sword", "A starter sword. Honestly, it's pretty garbage.");
        put("Rusty Sword", "Once quite a potent sword. However, it's very rusty now.");
        put("Decaying Drumstick", "A decaying drumstick that you obtained by defeating the Mother Hen. Honestly, pretty disgusting.");
        put("Flimsy Rapier", "A flimsy rapier.");
        put("Arachna's Fang", "A trophy obtained by defeating Arachna. It's quite sharp.");
        put("Cracked Stone Hammer", "Once one of the most powerful weapons in existence. Unfortunately, most of it was loss when it was cracked.");
        put("Cutlass of the Seas", "This mighty sword was once brandished by Captain Blackbeard himself.");
        put("Crystal Javelin", "A javelin that can be thrown pretty far, and was once the main weapon of the Crystal Assassin.");
        put("Sword of Electrifying Power", "This dominant sword was once wielded by Zeus himself. Legend says one strike can defeat almost anything.");
    }};

    public static final Map<String, Integer> SWORD_MODEL_NUMBER = new HashMap<String, Integer>() {{
        put("Starter Sword", 1);
        put("Rusty Sword", 2);
        put("Decaying Drumstick", 3);
        put("Flimsy Rapier", 4);
        put("Arachna's Fang", 5);
        put("Cracked Stone Hammer", 6);
        put("Cutlass of the Seas", 7);
        put("Crystal Javelin", 8);
        put("Sword of Electrifying Power", 9);
    }};

}

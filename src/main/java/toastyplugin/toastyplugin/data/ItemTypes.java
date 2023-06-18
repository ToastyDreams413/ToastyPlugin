package toastyplugin.toastyplugin.data;

import java.util.HashMap;
import java.util.Map;

public class ItemTypes {

    public static final Map<String, String> ITEM_TYPE = new HashMap<String, String>() {{
        put("Starter Sword", "sword");
        put("Rusty Sword", "sword");
        put("Decaying Drumstick", "sword");
        put("Flimsy Rapier", "sword");
        put("Arachna's Fang", "sword");
        put("Cracked Stone Hammer", "sword");
        put("Cutlass of the Seas", "sword");
        put("Crystal Javelin", "sword");
        put("Sword of Electrifying Power", "sword");

        put("Starter Wand", "wand");
        put("Honey Wand", "wand");
        put("Wand of Despair", "wand");

        put("Starter Bow", "bow");

        put("Main Menu", "other");
    }};

}

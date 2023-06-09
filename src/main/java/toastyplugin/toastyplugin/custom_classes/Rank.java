package toastyplugin.toastyplugin.custom_classes;

import org.bukkit.ChatColor;

public class Rank {

    private String name;
    private String displayName;
    private int value;

    public Rank(String name) {
        this.name = name;
        if (name.equals("admin")) {
            value = 10;
            displayName = ChatColor.BLUE + "" + ChatColor.BOLD + "[Admin]";
        }
        else if (name.equals("head admin")) {
            value = 20;
            displayName = ChatColor.RED + "" + ChatColor.BOLD + "[" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "H" + ChatColor.YELLOW + "" + ChatColor.BOLD + "e" + ChatColor.GOLD + "" + ChatColor.BOLD + "a" + ChatColor.GREEN + "" + ChatColor.BOLD + "d " + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "A" + ChatColor.AQUA + "" + ChatColor.BOLD + "d" + ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "m" + ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "i" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "n" + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "]";
        }
        else if (name.equals("wifey")) {
            value = 19;
            displayName = ChatColor.RED + "" + ChatColor.BOLD + "[Wifey]";
        }
        else {
            value = 0;
            displayName = "";
        }
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getValue() { return value; }

}

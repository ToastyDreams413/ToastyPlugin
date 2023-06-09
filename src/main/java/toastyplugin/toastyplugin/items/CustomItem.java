package toastyplugin.toastyplugin.items;

import org.bukkit.inventory.ItemStack;
import toastyplugin.toastyplugin.data.ItemTypes;
import toastyplugin.toastyplugin.items.other.MainMenu;
import toastyplugin.toastyplugin.items.weapons.CustomBow;
import toastyplugin.toastyplugin.items.weapons.CustomSword;
import toastyplugin.toastyplugin.items.weapons.CustomWand;

public class CustomItem {

    public static ItemStack createCustomItem(String itemName) {
        if (ItemTypes.ITEM_TYPE.get(itemName).equals("sword")) {
            return CustomSword.createCustomSword(itemName);
        }
        else if (ItemTypes.ITEM_TYPE.get(itemName).equals("wand")) {
            return CustomWand.createCustomWand(itemName);
        }
        else if (ItemTypes.ITEM_TYPE.get(itemName).equals("bow")) {
            return CustomBow.createCustomBow(itemName);
        }
        else if (ItemTypes.ITEM_TYPE.get(itemName).equals("other")) {
            return MainMenu.createOtherItem(itemName);
        }
        return null;
    }
}

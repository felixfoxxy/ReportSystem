package dev.felixfoxxy.reportsystem.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiUtils {
	public static String getInfo() {
		String ret = "";
		ret += "&5=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
		ret += "&aPlugin: ReportSystem\n";
		ret += "&6Author: FelixFoxx\n";
		ret += "&9Website: https://felixfoxxy.dev/\n";
		ret += "&5=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
		return ChatColor.translateAlternateColorCodes('&', ret);
	}
	
	public static ItemStack GetFillerItem() {
		ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta fm = filler.getItemMeta();
		fm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&0 "));
		filler.setItemMeta(fm);
		return filler;
	}
}

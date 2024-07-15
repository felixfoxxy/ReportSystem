package dev.felixfoxxy.reportsystem.guis;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import dev.felixfoxxy.reportsystem.ReportSystem;
import dev.felixfoxxy.reportsystem.util.GuiUtils;
import dev.felixfoxxy.reportsystem.util.Report;

public class GuiManageReport {
	public static void Show(Player p, Report rep) {
		Inventory inv = Bukkit.createInventory(null, 9 * 3, ReportSystem.getInstance().lang.getProperty("GuiManageReportTitle") + rep.Timestamp);
		
		for(int i = 0; i < 7; i++) {
			inv.setItem(i, GuiUtils.GetFillerItem());
		}
		
		ItemStack close = new ItemStack(Material.BARRIER);
		ItemMeta cm = close.getItemMeta();
		cm.setDisplayName(ReportSystem.getInstance().lang.getProperty("GuiClose"));
		close.setItemMeta(cm);
		inv.setItem(8, close);
		
		ItemStack del = new ItemStack(Material.COMPOSTER);
		ItemMeta delm = del.getItemMeta();
		delm.setDisplayName(ReportSystem.getInstance().lang.getProperty("GuiDelete"));
		del.setItemMeta(delm);
		inv.setItem(7, del);
		
		for(int i = 0; i < 9; i++) {
			ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
			ItemMeta fm = filler.getItemMeta();
			fm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&0 "));
			filler.setItemMeta(fm);
			inv.setItem((9 * 2) + i, filler);
		}
		
		inv.setItem(9, GuiUtils.GetFillerItem());
		
		ItemStack state_free = new ItemStack(Material.REDSTONE_BLOCK);
		ItemMeta state_freem = state_free.getItemMeta();
		state_freem.setDisplayName(ReportSystem.getInstance().lang.getProperty("GuiFree"));
		state_free.setItemMeta(state_freem);
		inv.setItem(10, state_free);
		
		ItemStack state_claim = new ItemStack(Material.GOLD_BLOCK);
		ItemMeta state_claimm = state_claim.getItemMeta();
		state_claimm.setDisplayName(ReportSystem.getInstance().lang.getProperty("GuiClaimed"));
		state_claim.setItemMeta(state_claimm);
		inv.setItem(11, state_claim);
		
		ItemStack state_comp = new ItemStack(Material.EMERALD_BLOCK);
		ItemMeta state_compm = state_comp.getItemMeta();
		state_compm.setDisplayName(ReportSystem.getInstance().lang.getProperty("GuiCompleted"));
		state_comp.setItemMeta(state_compm);
		inv.setItem(12, state_comp);
		
		inv.setItem(13, GuiUtils.GetFillerItem());
		
		ItemStack ban = new ItemStack(Material.ANVIL);
		ItemMeta banm = ban.getItemMeta();
		banm.setDisplayName(ReportSystem.getInstance().lang.getProperty("GuiBan"));
		ban.setItemMeta(banm);
		inv.setItem(14, ban);
		
		inv.setItem(15, GuiUtils.GetFillerItem());
		
		List<String> lore = new ArrayList<String>();
		lore.add(ReportSystem.getInstance().lang.getProperty("GuiPropState") + rep.State.toString().toUpperCase());
		lore.add(ReportSystem.getInstance().lang.getProperty("GuiPropCreator") + Bukkit.getServer().getOfflinePlayer(rep.Player).getName());
		lore.add(ReportSystem.getInstance().lang.getProperty("GuiPropDate") + rep.Timestamp.split("_")[0]);
		lore.add(ReportSystem.getInstance().lang.getProperty("GuiPropTime") + rep.Timestamp.split("_")[1]);
		lore.add(ReportSystem.getInstance().lang.getProperty("GuiPropReason") + rep.Reason);
		lore.add(ReportSystem.getInstance().lang.getProperty("GuiPropFile") + rep.Timestamp + ".json");
		
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta headm = (SkullMeta)head.getItemMeta();
		headm.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6" + Bukkit.getServer().getOfflinePlayer(rep.Player).getName()));
		headm.setOwningPlayer(Bukkit.getServer().getOfflinePlayer(rep.Player));
		headm.setLore(lore);
		head.setItemMeta(headm);
		inv.setItem(16, head);
		
		inv.setItem(17, GuiUtils.GetFillerItem());
		
		p.openInventory(inv);
	}
}

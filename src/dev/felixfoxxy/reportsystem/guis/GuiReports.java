package dev.felixfoxxy.reportsystem.guis;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;

import dev.felixfoxxy.reportsystem.ReportSystem;
import dev.felixfoxxy.reportsystem.util.FileUtils;
import dev.felixfoxxy.reportsystem.util.GuiUtils;
import dev.felixfoxxy.reportsystem.util.Report;

public class GuiReports {
	public static void Show(Player p, int pid) {

		Inventory inv = Bukkit.createInventory(null, 9 * 6, ReportSystem.getInstance().lang.getProperty("GuiReportsTitle") + pid);
		
		for(int i = 0; i < 8; i++) {
			inv.setItem(i, GuiUtils.GetFillerItem());
		}
		
		for(int i = 0; i < 7; i++) {
			inv.setItem((9 * 5) + i, GuiUtils.GetFillerItem());
		}
		
		ItemStack close = new ItemStack(Material.BARRIER);
		ItemMeta cm = close.getItemMeta();
		cm.setDisplayName(ReportSystem.getInstance().lang.getProperty("GuiClose"));
		close.setItemMeta(cm);
		inv.setItem(8, close);
		
		ItemStack ppage = new ItemStack(Material.ARROW);
		ItemMeta ppm = ppage.getItemMeta();
		ppm.setDisplayName(ReportSystem.getInstance().lang.getProperty("GuiPrevPage"));
		ppage.setItemMeta(ppm);
		inv.setItem((9 * 5) + 7, ppage);
		
		ItemStack npage = new ItemStack(Material.SPECTRAL_ARROW);
		ItemMeta npm = npage.getItemMeta();
		npm.setDisplayName(ReportSystem.getInstance().lang.getProperty("GuiNextPage"));
		npage.setItemMeta(npm);
		inv.setItem((9 * 5) + 8, npage);
		
		File[] reportFiles = new File(ReportSystem.getInstance().properties.getProperty("ReportsPath")).listFiles();
		if(reportFiles != null) {
			for(int i = (9 * 4) * pid; i < (9 * 4) * pid + (9 * 4); i++) {
				if(!(i <= reportFiles.length -1))
					break;
				try {
					File f = reportFiles[i];
					Report rep = new Gson().fromJson(FileUtils.readFromInputStream(new FileInputStream(f)), Report.class);
					Material mat = Material.PAPER;
					String state = "";
					
					switch(rep.State) {
						case FREE:
							mat = Material.REDSTONE_BLOCK;
							state = ReportSystem.getInstance().lang.getProperty("GuiFree");
							break;
						case CLAIMED:
							mat = Material.GOLD_BLOCK;
							state = ReportSystem.getInstance().lang.getProperty("GuiClaimed");
							break;
						case COMPLETED:
							mat = Material.EMERALD_BLOCK;
							state = ReportSystem.getInstance().lang.getProperty("GuiCompleted");
							break;
					}
					
					String spc = "&0";
					for(int s = 0; s < i; s++)
						spc += " ";
					spc = ChatColor.translateAlternateColorCodes('&', spc);
					
					List<String> lore = new ArrayList<String>();
					lore.add(ReportSystem.getInstance().lang.getProperty("GuiPropState") + state);
					lore.add(ReportSystem.getInstance().lang.getProperty("GuiPropCreator") + Bukkit.getServer().getOfflinePlayer(rep.Creator).getName() + spc);
					lore.add(ReportSystem.getInstance().lang.getProperty("GuiPropDate") + rep.Timestamp.split("_")[0]);
					lore.add(ReportSystem.getInstance().lang.getProperty("GuiPropTime") + rep.Timestamp.split("_")[1]);
					lore.add(ReportSystem.getInstance().lang.getProperty("GuiPropReason") + rep.Reason);
					lore.add(ReportSystem.getInstance().lang.getProperty("GuiPropFile") + rep.Timestamp + ".json");
					
					ItemStack itm = new ItemStack(mat);
					ItemMeta meta = itm.getItemMeta();
					meta.setLore(lore);
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a" + Bukkit.getServer().getOfflinePlayer(rep.Player).getName()));
					itm.setItemMeta(meta);
					inv.addItem(itm);
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		
		p.openInventory(inv);
	}
}

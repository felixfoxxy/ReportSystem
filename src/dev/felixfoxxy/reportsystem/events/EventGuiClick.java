package dev.felixfoxxy.reportsystem.events;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.google.gson.Gson;

import dev.felixfoxxy.reportsystem.ReportSystem;
import dev.felixfoxxy.reportsystem.guis.GuiManageReport;
import dev.felixfoxxy.reportsystem.util.FileUtils;
import dev.felixfoxxy.reportsystem.util.Report;
import dev.felixfoxxy.reportsystem.util.ReportState;

public class EventGuiClick implements Listener{	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if(e.getView().getTitle().trim().startsWith(ReportSystem.getInstance().lang.getProperty("GuiReportsTitle"))){
			if(!p.hasPermission(ReportSystem.getInstance().properties.getProperty("ReportsPermission"))) {
				p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ReportsGUIPermission"));
				p.closeInventory();
				return;
			}
			
			GuiReports(e);
		}
		else if(e.getView().getTitle().trim().startsWith(ReportSystem.getInstance().lang.getProperty("GuiManageReportTitle"))){
			if(!p.hasPermission(ReportSystem.getInstance().properties.getProperty("ManagePermission"))) {
				p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ManagePermission"));
				p.closeInventory();
				return;
			}
			
			GuiManageReport(e);
		}
	}
	
	void GuiReports(InventoryClickEvent e) {
		if(e.getCurrentItem() == null)
			return;
		
		Player p = (Player) e.getWhoClicked();
		String itmName = e.getCurrentItem().getItemMeta().getDisplayName().trim();
		
		if(itmName.substring(2) != "" && Bukkit.getServer().getPlayer(itmName.substring(2)) != null) {
			if(p.hasPermission(ReportSystem.getInstance().properties.getProperty("ManagePermission"))) {
				for(String l : e.getCurrentItem().getItemMeta().getLore()) {
					if(l.replace(ReportSystem.getInstance().lang.getProperty("GuiPropFile"), "").trim().endsWith(".json")) {
						try {
							Report rep = new Gson().fromJson(FileUtils.readFromInputStream(new FileInputStream(ReportSystem.getInstance().properties.getProperty("ReportsPath") + l.split(" ")[1].substring(2).trim())), Report.class);
							p.closeInventory();
							GuiManageReport.Show(p, rep);
						}catch(Exception ex) {
							ex.printStackTrace();
							p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ChatColor.translateAlternateColorCodes('&', "&4Error getting Report(Check Console)!"));
						}
						break;
					}
				}
			}
			else {
				p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ManagePermission"));
				p.closeInventory();
			}
		}
		else if(itmName.startsWith(ReportSystem.getInstance().lang.getProperty("GuiPrevPage"))) {
			int pid = Integer.parseInt(e.getView().getTitle().replace(ReportSystem.getInstance().lang.getProperty("GuiReportsTitle"), "").trim());
			if(pid != 0) {
				p.closeInventory();
				dev.felixfoxxy.reportsystem.guis.GuiReports.Show(p, pid -1);
			}
		}
		else if(itmName.startsWith(ReportSystem.getInstance().lang.getProperty("GuiNextPage"))) {
			int pid = Integer.parseInt(e.getView().getTitle().replace(ReportSystem.getInstance().lang.getProperty("GuiReportsTitle"), "").trim());
			p.closeInventory();
			dev.felixfoxxy.reportsystem.guis.GuiReports.Show(p, pid +1);
		}
		else if(itmName.startsWith(ReportSystem.getInstance().lang.getProperty("GuiClose"))) {
			p.closeInventory();
		}
		
		e.setCancelled(true);
	}
	
	void GuiManageReport(InventoryClickEvent e) {
		if(e.getCurrentItem() == null) {
			e.setCancelled(true);
			return;
		}
		
		Player p = (Player) e.getWhoClicked();
		String itmName = e.getCurrentItem().getItemMeta().getDisplayName().trim();
		
		String fn = e.getView().getTitle().trim().replace(ReportSystem.getInstance().lang.getProperty("GuiManageReportTitle"), "");
		Report rep = null;
		
		try {
			rep = new Gson().fromJson(FileUtils.readFromInputStream(new FileInputStream(ReportSystem.getInstance().properties.getProperty("ReportsPath") + fn + ".json")), Report.class);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		if(itmName.startsWith(ReportSystem.getInstance().lang.getProperty("GuiClose"))) {
			p.closeInventory();
		}
		else if(itmName.startsWith(ReportSystem.getInstance().lang.getProperty("GuiDelete"))) {
			if(p.hasPermission(ReportSystem.getInstance().properties.getProperty("DeletePermission"))){
				try {
					new File(ReportSystem.getInstance().properties.getProperty("ReportsPath") + rep.Timestamp + ".json").delete();
					p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ReportDeleted"));
				}catch(Exception ex) {
					ex.printStackTrace();
					p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ExceptionDelete"));
				}
			}
			else
				p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("DeletePermission"));
			p.closeInventory();
		}
		else if(itmName.startsWith(ReportSystem.getInstance().lang.getProperty("GuiFree"))) {
			if(p.hasPermission(ReportSystem.getInstance().properties.getProperty("FreePermission"))){
				rep.State = ReportState.FREE;
				try {
					File rf = new File(ReportSystem.getInstance().properties.getProperty("ReportsPath") + rep.Timestamp + ".json");
					rf.createNewFile();
				    BufferedWriter writer = new BufferedWriter(new FileWriter(rf));
				    writer.write(new Gson().toJson(rep));
				    writer.close();
				    p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ReportFreed"));
				}catch(Exception ex) {
					ex.printStackTrace();
					p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ExceptionSetState"));
				}
			}
			else
				p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("FreePermission"));
			p.closeInventory();
		}
		else if(itmName.startsWith(ReportSystem.getInstance().lang.getProperty("GuiClaimed"))) {
			if(p.hasPermission(ReportSystem.getInstance().properties.getProperty("ClaimPermission"))){
				rep.State = ReportState.CLAIMED;
				try {
					File rf = new File(ReportSystem.getInstance().properties.getProperty("ReportsPath") + rep.Timestamp + ".json");
					rf.createNewFile();
				    BufferedWriter writer = new BufferedWriter(new FileWriter(rf));
				    writer.write(new Gson().toJson(rep));
				    writer.close();
				    p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ReportClaimed"));
				}catch(Exception ex) {
					ex.printStackTrace();
					p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ExceptionSetState"));
				}
			}
			else
				p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ClaimPermission"));
			p.closeInventory();
		}
		else if(itmName.startsWith(ReportSystem.getInstance().lang.getProperty("GuiCompleted"))) {
			if(p.hasPermission(ReportSystem.getInstance().properties.getProperty("CompletePermission"))){
				rep.State = ReportState.COMPLETED;
				try {
					File rf = new File(ReportSystem.getInstance().properties.getProperty("ReportsPath") + rep.Timestamp + ".json");
					rf.createNewFile();
				    BufferedWriter writer = new BufferedWriter(new FileWriter(rf));
				    writer.write(new Gson().toJson(rep));
				    writer.close();
				    p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ReportCompleted"));
				}catch(Exception ex) {
					ex.printStackTrace();
					p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ExceptionSetState"));
				}
			}
			else
				p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("CompletePermission"));
			
			p.closeInventory();
		}
		else if(itmName.startsWith(ReportSystem.getInstance().lang.getProperty("GuiBan"))) {
			if(p.hasPermission(ReportSystem.getInstance().properties.getProperty("BanPermission"))){
				String reason = ReportSystem.getInstance().lang.getProperty("BanMessage").replace("%PREFIX%", ReportSystem.getInstance().lang.getProperty("Prefix")).replace("%REASON%", rep.Reason).replace("%BANBY%", p.getName()).replace("%BANBYUUID%", p.getUniqueId().toString()).replace("%REPORTID%", rep.Timestamp);
				Bukkit.getOfflinePlayer(rep.Player).ban(reason, new Date(Long.MAX_VALUE), reason);
				try {
					if(ReportSystem.getInstance().properties.getProperty("CompleteReportOnBan").toLowerCase().trim().startsWith("true")) {
						rep.State = ReportState.COMPLETED;
						File rf = new File(ReportSystem.getInstance().properties.getProperty("ReportsPath") + rep.Timestamp + ".json");
						rf.createNewFile();
					    BufferedWriter writer = new BufferedWriter(new FileWriter(rf));
					    writer.write(new Gson().toJson(rep));
					    writer.close();
					}
				}catch(Exception ex) {
					ex.printStackTrace();
					p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ExceptionSetState"));
				}
			    p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("PlayerBanned"));
				p.closeInventory();
			}
			else
				p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("BanPermission"));
			p.closeInventory();
		}
		e.setCancelled(true);
	}
}

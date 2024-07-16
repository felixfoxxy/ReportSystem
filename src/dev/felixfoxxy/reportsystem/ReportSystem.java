package dev.felixfoxxy.reportsystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;
import java.io.FileOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import dev.felixfoxxy.reportsystem.commands.CommandReport;
import dev.felixfoxxy.reportsystem.commands.CommandReportSystem;
import dev.felixfoxxy.reportsystem.commands.CommandReports;
import dev.felixfoxxy.reportsystem.events.*;
import dev.felixfoxxy.reportsystem.util.GuiUtils;

public class ReportSystem extends JavaPlugin{
	public static String ConfigPath = "plugins/ReportSystem/ReportSystem.config";
	public static SimpleDateFormat TimestampFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	public Properties properties = new Properties();
	public Properties lang = new Properties();
	
	public static ReportSystem instance;
	public static ReportSystem getInstance() {
		return instance;
	}
	
	public void loadCfg() {
		try {
			if(new File(ConfigPath).createNewFile()) {
				properties.setProperty("ReportsPath", "reports/");
				properties.setProperty("LangPath", "plugins/ReportSystem/ReportSystem-Lang.config");
				properties.setProperty("ReportPermission", "reportsystem.report");
				properties.setProperty("ReportsPermission", "reportsystem.reports");
				properties.setProperty("InfoPermission", "reportsystem.info");
				properties.setProperty("HelpPermission", "reportsystem.help");
				properties.setProperty("ReloadPermission", "reportsystem.reload");
				properties.setProperty("FreePermission", "reportsystem.report.free");
				properties.setProperty("ClaimPermission", "reportsystem.report.claim");
				properties.setProperty("CompletePermission", "reportsystem.report.complete");
				properties.setProperty("BanPermission", "reportsystem.report.ban");
				properties.setProperty("DeletePermission", "reportsystem.report.delete");
				properties.setProperty("ManagePermission", "reportsystem.report.manage");
				properties.setProperty("CompleteReportOnBan", "true");
				properties.setProperty("TimestampFormat", "yyyy-MM-dd_HH-mm-ss");
				//ReportSystem.getInstance().properties.getProperty("")
				properties.store(new FileOutputStream(ConfigPath), null);
			}
			properties.load(new FileInputStream(ConfigPath));
			TimestampFormat = new SimpleDateFormat(properties.getProperty("TimestampFormat"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadLang() {
		try {
			if(new File(properties.getProperty("LangPath")).createNewFile()) {
				lang.setProperty("Prefix", "&8[&4&lReportSystem&r&8] &r");
				lang.setProperty("ReportPermission", "&cYou need the Permission \"reportsystem.report\" to use this Command!");
				lang.setProperty("ReportsPermission", "&cYou need the Permission \"reportsystem.reports\" to use this Command!");
				lang.setProperty("ReportsGUIPermission", "&cYou need the Permission \"reportsystem.reports\" to use this GUI!");
				lang.setProperty("InfoPermission", "&cYou need the Permission \"reportsystem.info\" to use this Command!");
				lang.setProperty("HelpPermission", "&cYou need the Permission \"reportsystem.help\" to use this Command!");
				lang.setProperty("ReloadPermission", "&cYou need the Permission \"reportsystem.reload\" to use this Command!");
				lang.setProperty("Reported", "&aPlayer reported!");
				lang.setProperty("ReportError", "&4Error reporting Player!\nPlease report this issue to an Admin!");
				lang.setProperty("InvalidPlayer", "&4Invalid Player!");
				lang.setProperty("InvalidUsage", "&4Invalid Usage!\nUse /report [PLAYER] [REASON]");
				lang.setProperty("ReportFreed", "&4Report Freed!");
				lang.setProperty("ReportClaimed", "&6Report Claimed!");
				lang.setProperty("ReportCompleted", "&aReport Completed!");
				lang.setProperty("PlayerBanned", "&cPlayer Banned!");
				lang.setProperty("ReportDeleted", "&4Report Deleted!");
				lang.setProperty("ExceptionDelete", "&4Report Deleted!");
				lang.setProperty("ExceptionSetState", "&4Error changing Report state(Check Console)!");
				lang.setProperty("BanMessage", "\n\n%PREFIX%\n&cYou have been banned!\n&cReason: %REASON%\n&5Banned by: &6%BANBY% &8(&6%BANBYUUID%&8)\n&aReport ID: %REPORTID%\n&0                                                                                                                                                                                                      &0");
				lang.setProperty("HelpMessage", "&6Usage:\n&2/report [PLAYER] [REASON] -- Report a Player\n/reports -- Shows all Reports\n/reportsystem -- Shows Plugin Info\n/reportsystem info -- Shows Plugin Info\n/reportsystem help -- Shows Help Message\n/reportsystem reload -- Reloads the Plugin");
				lang.setProperty("ReloadMessage", "&aConfigs reloaded!");
				lang.setProperty("GuiReportsTitle", "&4Reports &8/ &2");
				lang.setProperty("GuiManageReportTitle", "&2Report: &c");
				lang.setProperty("GuiClose", "&4Close");
				lang.setProperty("GuiCompleted", "&aCompleted");
				lang.setProperty("GuiClaimed", "&6Claimed");
				lang.setProperty("GuiFree", "&4Free");
				lang.setProperty("GuiPrevPage", "&bPrevious Page");
				lang.setProperty("GuiNextPage", "&bNext Page");
				lang.setProperty("GuiBan", "&cBAN");
				lang.setProperty("GuiDelete", "&4Delete");
				lang.setProperty("GuiPropState", "&5State: ");
				lang.setProperty("GuiPropCreator", "&6Creator: ");
				lang.setProperty("GuiPropDate", "&bDate: ");
				lang.setProperty("GuiPropTime", "&2Time: ");
				lang.setProperty("GuiPropReason", "&9Reason: ");
				lang.setProperty("GuiPropFile", "&cFile: ");
				lang.setProperty("DeletePermission", "&cYou need the Permission \"reportsystem.report.delete\" to delete Reports!");
				lang.setProperty("BanPermission", "&cYou need the Permission \"reportsystem.report.ban\" to ban Players!");
				lang.setProperty("CompletePermission", "&cYou need the Permission \"reportsystem.report.complete\" to complete Reports!");
				lang.setProperty("ClaimPermission", "&cYou need the Permission \"reportsystem.report.claim\" to claim Reports!");
				lang.setProperty("FreePermission", "&cYou need the Permission \"reportsystem.report.free\" to free Reports!");
				lang.setProperty("ManagePermission", "&cYou need the Permission \"reportsystem.report.manage\" to free Reports!");
				lang.store(new FileOutputStream(properties.getProperty("LangPath")), null);
			}
			lang.load(new FileInputStream(properties.getProperty("LangPath")));
			for (Map.Entry<Object, Object> entry : lang.entrySet()) {
				lang.setProperty((String)entry.getKey(), ChatColor.translateAlternateColorCodes('&', (String)entry.getValue()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onEnable() {
		instance = this;
		try {
			Files.createDirectories(Paths.get("plugins/ReportSystem/"));
			loadCfg();
			loadLang();
			Files.createDirectories(Paths.get(properties.getProperty("ReportsPath")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		getCommand("report").setExecutor(new CommandReport());
		getCommand("reports").setExecutor(new CommandReports());
		getCommand("reportsystem").setExecutor(new CommandReportSystem());
		Bukkit.getPluginManager().registerEvents(new EventGuiClick(), this);
		Bukkit.getConsoleSender().sendMessage(GuiUtils.getInfo());
	}
}

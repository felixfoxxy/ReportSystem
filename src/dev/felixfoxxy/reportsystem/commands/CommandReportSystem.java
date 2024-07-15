package dev.felixfoxxy.reportsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.felixfoxxy.reportsystem.ReportSystem;
import dev.felixfoxxy.reportsystem.util.GuiUtils;

public class CommandReportSystem implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		Player p = (Player)sender;
		if(args.length == 1) {
			switch(args[0].toLowerCase()) {
				case "info":
					if(!p.hasPermission(ReportSystem.getInstance().properties.getProperty("InfoPermission"))) {
						p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("InfoPermission"));
						return false;
					}
					p.sendMessage(GuiUtils.getInfo());
					break;
				case "help":
					if(!p.hasPermission(ReportSystem.getInstance().properties.getProperty("HelpPermission"))) {
						p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("HelpPermission"));
						return false;
					}
					p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("HelpMessage"));
					break;
				case "reload":
					if(!p.hasPermission(ReportSystem.getInstance().properties.getProperty("ReloadPermission"))) {
						p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ReloadPermission"));
						return false;
					}
					
					ReportSystem.getInstance().loadCfg();
					ReportSystem.getInstance().loadLang();
					
					p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ReloadMessage"));
					break;
			}
		}
		else {
			if(!p.hasPermission(ReportSystem.getInstance().properties.getProperty("InfoPermission"))) {
				p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("InfoPermission"));
				return false;
			}
			p.sendMessage(GuiUtils.getInfo());
		}
		return false;
	}
}

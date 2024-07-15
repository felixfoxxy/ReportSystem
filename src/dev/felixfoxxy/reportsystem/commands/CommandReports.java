package dev.felixfoxxy.reportsystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.felixfoxxy.reportsystem.ReportSystem;
import dev.felixfoxxy.reportsystem.guis.GuiReports;

public class CommandReports implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		Player p = (Player)sender;
		if(!p.hasPermission(ReportSystem.getInstance().properties.getProperty("ReportsPermission"))) {
			p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ReportsPermission"));
			return false;
		}
		
		GuiReports.Show(p, 0);
		
		return false;
	}
}

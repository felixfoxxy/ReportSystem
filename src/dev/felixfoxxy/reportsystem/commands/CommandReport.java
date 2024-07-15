package dev.felixfoxxy.reportsystem.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.gson.Gson;

import dev.felixfoxxy.reportsystem.ReportSystem;
import dev.felixfoxxy.reportsystem.util.Report;
import dev.felixfoxxy.reportsystem.util.ReportState;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;

public class CommandReport implements CommandExecutor{
	@SuppressWarnings({ "deprecation"})
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		Player p = (Player)sender;
		if(!p.hasPermission(ReportSystem.getInstance().properties.getProperty("ReportPermission"))) {
			p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ReportPermission"));
			return false;
		}
		if(args.length >= 2) {
			if(Bukkit.getServer().getPlayer(args[0]) != null) {
				String reason = "";
				for(int i = 1; i < args.length; i++)
					reason += args[i] + " ";
				reason = reason.trim();
				
				Report rep = new Report();
				rep.Player = Bukkit.getServer().getOfflinePlayer(args[0]).getUniqueId();
				rep.Reason = reason;
				rep.Timestamp = ReportSystem.TimestampFormat.format(new Timestamp(System.currentTimeMillis()));
				rep.State = ReportState.FREE;
				rep.Creator = p.getUniqueId();
				
				try {
					File rf = new File(ReportSystem.getInstance().properties.getProperty("ReportsPath") + rep.Timestamp + ".json");
					rf.createNewFile();
				    BufferedWriter writer = new BufferedWriter(new FileWriter(rf));
				    writer.write(new Gson().toJson(rep));
				    writer.close();
				    p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("Reported"));
				}catch(Exception ex) {
					ex.printStackTrace();
					p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("ReportError"));
				}
			}else {
				p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("InvalidPlayer"));
			}
		}else {
			p.sendMessage(ReportSystem.getInstance().lang.getProperty("Prefix") + ReportSystem.getInstance().lang.getProperty("InvalidUsage"));
		}
		return false;
	}
}
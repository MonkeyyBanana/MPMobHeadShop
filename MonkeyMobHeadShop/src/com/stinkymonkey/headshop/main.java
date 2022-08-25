package com.stinkymonkey.headshop;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class main extends JavaPlugin implements CommandExecutor {
	public static HashMap<String, Double> price = new HashMap<String, Double>();
	public static HashMap<Integer, String> slot = new HashMap<Integer, String>();
	
	private static gui gi;
	public static Economy econ = null;
	@Override
	public void onEnable() {
		setupEconomy();
		loadConfig();
		setUpConfig();
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new listener(this), this);
		
		this.getCommand("headshop").setExecutor(this);
		gi = new gui(this);
		this.getGui().creatGui();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public void loadConfig() {
		File pluginFolder = new File("plugins" + System.getProperty("file.separator") + this.getDescription().getName());
		if (pluginFolder.exists() == false) {
    		pluginFolder.mkdir();
    		System.out.println("[Monkey-MHS] CREATED A FOLDER");
    	}
		
		File configFile = new File("plugins" + System.getProperty("file.separator") + this.getDescription().getName() + System.getProperty("file.separator") + "config.yml");
		if (configFile.exists() == false) {
    		this.saveDefaultConfig();
    		System.out.println("[Monkey-MHS] CREATED A CONFIG");
		}
    	
    	try {
    		this.getConfig().load(configFile);
    		System.out.println("[Monkey-MHS] LOADED CONFIG");
    	} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[Monkey-MHS] FAILED TO LOAD CONFIG");
    	}
	}
	
	int n = 1;
	public void setUpConfig() {
		while (this.getConfig().contains("head" + Integer.toString(n))) {
			price.put(this.getConfig().getString("head" + Integer.toString(n) + ".name"), this.getConfig().getDouble("head" + Integer.toString(n) + ".price"));
			slot.put (this.getConfig().getInt("head" + Integer.toString(n) + ".slot"), this.getConfig().getString("head" + Integer.toString(n) + ".name"));
			n++;
		}
	}
	
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null)
			return false;
		econ = rsp.getProvider();
		return econ != null;
	}
	
	public gui getGui() {
		return gi;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("headshop")) {
			if (args.length == 0) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					if (p.hasPermission("MonkeyMHS.gui")) {
						p.openInventory(this.getGui().headShop);
					} else {
						p.sendMessage(ChatColor.RED + "You don't have permission!");
					}
				} else {
					System.out.println("[Monkey-MHS] YOU CAN NOT ACCESS THE GUI FROM CONSOLE!");
				}
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					n = 1;
					getGui().headShop.clear();
					this.reloadConfig();
					price.clear();
					slot.clear();
					setUpConfig();
					getGui().creatGui();
					if (sender instanceof Player) {
						Player p = (Player) sender;
						p.sendMessage(ChatColor.DARK_RED + "Monkey Mob Head Shop Has Been Reloaded!");
					}
					System.out.println("[Monkey-MHS] SUCCESSFULLY RELOADED!");
				}
			}
		}
		return false;
	}
}

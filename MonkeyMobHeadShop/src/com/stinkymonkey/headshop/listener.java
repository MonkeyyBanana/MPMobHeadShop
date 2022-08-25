package com.stinkymonkey.headshop;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class listener implements Listener{
	private main Main;
	public listener (main Main) {
		this.Main = Main;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getCurrentItem() == null) return;
		if (event.getCurrentItem().getItemMeta() == null) return;
		if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
		Player p = (Player) event.getWhoClicked();
		if (event.getInventory().equals(Main.getGui().headShop)) {
			event.setCancelled(true);
			if (main.slot.containsKey(event.getSlot())) {
				String skullName = main.slot.get(event.getSlot());
				boolean checker = false;
				ItemStack newItem = null;
				UUID skinu = p.getUniqueId();
				ItemStack iteml = p.getInventory().getItemInHand();
				int amount = 0;
				for (ItemStack item : p.getInventory().getContents()) {
					try {
						if (item.hasItemMeta()) {
							ItemMeta meta = item.getItemMeta();
							if (meta.hasLore()) {
								List<String> lore = meta.getLore();
								List<String> newLore = new ArrayList<String>();
								for (String str : lore) {
									newLore.add(ChatColor.stripColor(str));
								}
								if (lore.size() > 5) {
									if (newLore.contains("Hidden(0):" + MobType.valueOf(skullName).getDisplayName()) && newLore.contains("MobHunting Reward")) {
										checker = true;
										newItem = item;
										amount += item.getAmount();
									}
								}
							}
						}
					} catch (Exception e) {
						
					}
				}
				if (event.getClick().equals(ClickType.LEFT) && checker == true) {
					for (int i = 1; i <= 64; i++) {
						newItem.setAmount(i);
						while(p.getInventory().contains(newItem)) {
							p.getInventory().remove(newItem);
						}
					}
					newItem.setAmount(amount - 1);
					p.getInventory().addItem(newItem);
					main.econ.depositPlayer(p, main.price.get(skullName));
					p.sendMessage(ChatColor.GREEN + "Successfully Sold Heads For $" + String.format("%,.2f", main.price.get(skullName)));
					p.playSound(p.getLocation(), Sound.valueOf("BLOCK_NOTE_BLOCK_PLING"), 3.0F, 3.0F);
				} else if (event.getClick().equals(ClickType.RIGHT) && checker == true) {
					if (MobType.BonusMob.getSkinUUID().equals(skinu)) {
						if (newItem.getAmount() == 3) {
							p.getInventory().addItem(iteml);
						}
					} else {
						main.econ.depositPlayer(p, amount * main.price.get(skullName));
					}
					for (int i = 1; i <= 64; i++) {
						newItem.setAmount(i);
						while (p.getInventory().contains(newItem)) {
							p.getInventory().remove(newItem);
						}
					}
					p.sendMessage(ChatColor.GREEN + "Successfully Sold Heads For $" + String.format("%,.2f",  (amount * main.price.get(skullName))));
					p.playSound(p.getLocation(), Sound.valueOf("BLOCK_NOTE_BLOCK_PLING"), 3.0F, 3.0F);
				} else {
					p.sendMessage(ChatColor.RED + "No Heads To Sell!");
					p.playSound(p.getLocation(), Sound.valueOf("ENTITY_ITEM_BREAK"), 3.0F, 3.0F);
				}
			}
		}
	}
}

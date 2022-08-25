package com.stinkymonkey.headshop;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.md_5.bungee.api.ChatColor;

public class gui {
	private main Main;
	public gui (main Main) {
		this.Main = Main;
	}
	
	Inventory headShop;
	
	@SuppressWarnings("deprecation")
	public void creatGui() {
		headShop = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', Main.getConfig().getString("settings.guiName")));
		for (Map.Entry<Integer, String> str : main.slot.entrySet()) {
			headShop.setItem(str.getKey(), getMobSkull(MobType.valueOf(str.getValue()), str));
		}
	}
	
	public ItemStack getMobSkull(MobType mobtype, Map.Entry<Integer, String> str) {
		String encodedTexture = mobtype.getTextureValue();
		String randomUUID = mobtype.getSkinUUID().toString();
		String displayName = mobtype.getDisplayName();
		if (encodedTexture == null) {
			return null;
		}
		ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1,(short)3);

		SkullMeta meta = (SkullMeta)skull.getItemMeta();
		GameProfile profile = new GameProfile(UUID.fromString(randomUUID), null);
		profile.getProperties().put("textures", new Property("textures", encodedTexture));
		Field profileField = null;
		try {
			profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(meta, profile);
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}
		List<String> metaList = new ArrayList<String>();
		metaList.add(ChatColor.GREEN + "$" + String.format("%,.2f", main.price.get(str.getValue())));
		metaList.add(ChatColor.AQUA + "Left Click To Sell One");
		metaList.add(ChatColor.AQUA + "Right Click To Sell All");
		meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + str.getValue());
		meta.setLore(metaList);
		skull.setItemMeta(meta);
		return skull;
	}
	
}

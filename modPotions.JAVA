package com.modPotions.bukkit;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.Potion.Tier;
import org.bukkit.potion.PotionType;


/*
 * Author: Nicholas R. Boyd
 * Last Edited: 9/30/14
 */
public class main extends JavaPlugin implements Listener
{
	ItemStack potionOfFlight;
	ItemStack potionOfHaste;
	
	public void onEnable()
	{
		//creates the item "Potion of Flight"
		potionOfFlight = new ItemStack(Material.POTION);
		ItemMeta metaF = potionOfFlight.getItemMeta();
		metaF.setDisplayName("Potion Of Flight");
		ArrayList<String> loreF = new ArrayList<String>();
		loreF.add("Allows Flight and a Small Speed Boost");
		loreF.add(ChatColor.GRAY + "Flight: (1:00)");
		loreF.add(ChatColor.GRAY + "Speed: (1:30)");
		loreF.add("");
		loreF.add(ChatColor.DARK_PURPLE + "When Applied:");
		loreF.add(ChatColor.DARK_AQUA + "+20% Speed");
		metaF.setLore(loreF);
		potionOfFlight.setItemMeta(metaF);
		//Sets the recipe for the Potion Of Flight
		ShapedRecipe flyingPotion = new ShapedRecipe(potionOfFlight);
		flyingPotion.shape("FFF","FPF","FFF");
		flyingPotion.setIngredient('F', Material.FEATHER);
		flyingPotion.setIngredient('P', Material.GLASS_BOTTLE);
		getServer().addRecipe(flyingPotion);
		this.getServer().getPluginManager().registerEvents(this, this);
		
		
		
		//creates the item "Potion of Haste"
		potionOfHaste = new ItemStack(Material.POTION);
		ItemMeta metaH = potionOfHaste.getItemMeta();
		metaH.setDisplayName("Potion Of Haste");
		ArrayList<String> loreH = new ArrayList<String>();
		loreH.add("Quickens Mining Speed");
		loreH.add(ChatColor.GRAY + "Haste: (1:00)");
		metaH.setLore(loreH);
		potionOfHaste.setItemMeta(metaH);
		//Sets the recipe for the Potion Of Haste
		ShapedRecipe hastePotion = new ShapedRecipe(potionOfHaste);
		hastePotion.shape("GGG","GPG","GGG");
		hastePotion.setIngredient('G', Material.GOLD_NUGGET);
		hastePotion.setIngredient('P', Material.GLASS_BOTTLE);
		getServer().addRecipe(hastePotion);
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void consumeEvent(PlayerItemConsumeEvent cnsm)
	{
		if(cnsm.getItem().getType() != potionOfFlight.getType()) {return;}
		final Player p = cnsm.getPlayer();
		final ItemStack i = cnsm.getItem();
		if(i.isSimilar(potionOfFlight))
		{
			p.setAllowFlight(true);
			p.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("ModPotions"), new Runnable(){
				public void run()
				{
					try
					{
						if(p.getGameMode() != GameMode.CREATIVE)
						{
							p.setAllowFlight(false);
						}
					} catch(Exception ex) {}
				}
			}, 1200);
		}
		
		if(i.isSimilar(potionOfHaste))
		{
			p.addPotionEffect(PotionEffectType.FAST_DIGGING.createEffect(1200, 0));
		}
	}
}

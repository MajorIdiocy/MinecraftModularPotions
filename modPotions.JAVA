package com.modPotions.bukkit;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.Sign;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitScheduler;

/*
 * Author: Nicholas R. Boyd
 * Last Edited: 9/30/14
 */
public class main extends JavaPlugin implements Listener {
	ArrayList<Block> stands;
	ArrayList<ItemStack> potions;
	ArrayList<Player> interacts;

	public void onEnable() {
		stands = new ArrayList<Block>(100);
		potions = new ArrayList<ItemStack>(100);
		getServer().getPluginManager().registerEvents(this, this);
	}

	/*
	 * Establishes a chest as a brewing stand when a sign is placed on it that
	 * says "[Brewing Stand]" on the first line
	 * 
	 * @param event Checks to see if a player is creating a stand whenever they
	 * change a sign.
	 */
	@EventHandler
	public void newStand(SignChangeEvent event) {
		if (event.getBlock().getType() == Material.SIGN_POST
				|| event.getBlock().getType() == Material.WALL_SIGN
				|| event.getBlock().getType() == Material.SIGN) {
			Sign s = (Sign) event.getBlock().getState().getData();
			Block attached = event.getBlock().getRelative(s.getAttachedFace()); // Finds the block the sign is attached to
			if (attached.getType().equals(Material.CHEST) 						//if it's a chest, adds it to the stands arraylist
					&& event.getLine(0).contains("[Brewing Stand]")) {
				stands.add(event.getBlock().getRelative(s.getAttachedFace()));
			}
			event.getPlayer().sendMessage("Stand Created!");
		}
	}

	/*
	 * Creates a potion if a stand has necessary materials, and some item that
	 * adds potion effects
	 * 
	 * @param event Checks every time the player taps an item
	 */
	@EventHandler
	public void potionCreation(PlayerInteractEvent event) {

		if (stands.contains(event.getClickedBlock())) // Checks to see if the chest is a stand
		{
			Chest x = (Chest) event.getClickedBlock().getState();
			Inventory chest = x.getInventory();
			ArrayList<PotionEffectType> effects = new ArrayList<PotionEffectType>(
					50);
			ArrayList<Boolean> modifiers = new ArrayList<Boolean>();

			if (chest.contains(Material.NETHER_STALK)
					&& chest.contains(Material.GLASS_BOTTLE)) // Checks for required materials
			{
				if (chest.contains(Material.GOLDEN_CARROT)) {
					effects.add(PotionEffectType.NIGHT_VISION);
				}
				if (chest.contains(Material.CARROT)) {
					effects.add(PotionEffectType.INVISIBILITY);
				}
				if (chest.contains(Material.SLIME_BALL)) {
					effects.add(PotionEffectType.JUMP);
				}
				if (chest.contains(Material.MAGMA_CREAM)) {
					effects.add(PotionEffectType.FIRE_RESISTANCE);
				}
				if (chest.contains(Material.SUGAR)) {
					effects.add(PotionEffectType.SPEED);
				}
				if (chest.contains(Material.RAW_FISH)) {
					effects.add(PotionEffectType.WATER_BREATHING);
				}
				if (chest.contains(Material.SPECKLED_MELON)) {
					effects.add(PotionEffectType.HEAL);
				}
				if (chest.contains(Material.SPIDER_EYE)) {
					effects.add(PotionEffectType.POISON);
				}
				if (chest.contains(Material.GHAST_TEAR)) {
					effects.add(PotionEffectType.REGENERATION);
				}
				if (chest.contains(Material.BLAZE_POWDER)) {
					effects.add(PotionEffectType.INCREASE_DAMAGE);
				}
				if (chest.contains(Material.APPLE)) {
					effects.add(PotionEffectType.ABSORPTION);
				}
				if (chest.contains(Material.POISONOUS_POTATO)) {
					effects.add(PotionEffectType.BLINDNESS);
				}
				if (chest.contains(Material.INK_SACK)) {
					effects.add(PotionEffectType.CONFUSION);
				}
				if (chest.contains(Material.LEATHER)) {
					effects.add(PotionEffectType.DAMAGE_RESISTANCE);
				}
				if (chest.contains(Material.GOLD_INGOT)) {
					effects.add(PotionEffectType.FAST_DIGGING);
				}
				if (chest.contains(Material.ARROW)) {
					effects.add(PotionEffectType.HARM);
				}
				if (chest.contains(Material.GOLD_BLOCK)) {
					effects.add(PotionEffectType.HEALTH_BOOST);
				}
				if (chest.contains(Material.WHEAT)) {
					effects.add(PotionEffectType.HUNGER);
				}
				if (chest.contains(Material.POTATO)) {
					effects.add(PotionEffectType.SATURATION);
				}
				if (chest.contains(Material.BROWN_MUSHROOM)) {
					effects.add(PotionEffectType.SLOW);
				}
				if (chest.contains(Material.RED_MUSHROOM)) {
					effects.add(PotionEffectType.SLOW_DIGGING);
				}
				if (chest.contains(Material.FERMENTED_SPIDER_EYE)) {
					effects.add(PotionEffectType.WEAKNESS);
				}
				if (chest.contains(Material.COAL)) {
					effects.add(PotionEffectType.WITHER);
				}

				if (chest.contains(Material.SULPHUR)) {
					modifiers.add(true);
				} else {
					modifiers.add(false);
				}
				if (chest.contains(Material.GLOWSTONE_DUST)) {
					modifiers.add(true);
				} else {
					modifiers.add(false);
				}
				if (chest.contains(Material.REDSTONE)) {
					modifiers.add(true);
				} else {
					modifiers.add(false);
				}
			}

			if (!effects.isEmpty()) {
				potionBrew(effects, modifiers, x, event.getPlayer());
			}
		}
	}

	/*
	 * The actual process used to create the new dynamic potion Effects opposite
	 * to the desired potion occur while brewing (ie poison may effect a player
	 * while brewing any potion of health) All possible potion effects that are
	 * used in the potion are stored in its lore Parameters use final so they
	 * can be accessed inside of the runnables
	 * 
	 * @param effects - effects to be added to a potion.
	 * 
	 * @param modifiers - whether the potion is a splash, or extended.
	 * 
	 * @param stand - the chest in which a potion is being brewed.
	 * 
	 * @param p - the player who started the potion creation event.
	 */
	public void potionBrew(final ArrayList<PotionEffectType> effects,
			final ArrayList<Boolean> modifiers, final Chest stand,
			final HumanEntity p) {
		Bukkit.broadcastMessage("Potion Brewing Has Begun");
		// Finds what poteffects have been saved , and determines their
		// 'opposites'
		final ArrayList<PotionEffectType> eventTypes = new ArrayList<PotionEffectType>(
				50);
		if (effects.contains(PotionEffectType.HUNGER)
				|| effects.contains(PotionEffectType.SATURATION)) {
			eventTypes.add(PotionEffectType.HUNGER);
		}
		if (effects.contains(PotionEffectType.BLINDNESS)
				|| effects.contains(PotionEffectType.NIGHT_VISION)
				|| effects.contains(PotionEffectType.INVISIBILITY)
				|| effects.contains(PotionEffectType.CONFUSION)) {
			eventTypes.add(PotionEffectType.BLINDNESS);
		}
		if (effects.contains(PotionEffectType.HEAL)
				|| effects.contains(PotionEffectType.REGENERATION)
				|| effects.contains(PotionEffectType.ABSORPTION)
				|| effects.contains(PotionEffectType.HEALTH_BOOST)
				|| effects.contains(PotionEffectType.DAMAGE_RESISTANCE)) {
			eventTypes.add(PotionEffectType.POISON);
		}
		if (effects.contains(PotionEffectType.JUMP)
				|| effects.contains(PotionEffectType.SPEED)
				|| effects.contains(PotionEffectType.WATER_BREATHING)
				|| effects.contains(PotionEffectType.FAST_DIGGING)) {
			eventTypes.add(PotionEffectType.SLOW);
		}
		if (effects.contains(PotionEffectType.FIRE_RESISTANCE)) {
			eventTypes.add(PotionEffectType.CONFUSION);
		}
		if (effects.contains(PotionEffectType.POISON)
				|| effects.contains(PotionEffectType.HARM)
				|| effects.contains(PotionEffectType.WEAKNESS)
				|| effects.contains(PotionEffectType.WITHER)) {
			eventTypes.add(PotionEffectType.ABSORPTION);
		}
		if (effects.contains(PotionEffectType.INCREASE_DAMAGE)) {
			eventTypes.add(PotionEffectType.WITHER);
		}

		final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

		// Every 3 seconds, there is a chance one of the negative effects will
		// activate
		scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {

				double eventHappenChance = Math.random();
				double whichEventRandom = Math.random();
				if (eventHappenChance > 0.75) {
					int index = (int) (whichEventRandom * eventTypes.size());
					p.addPotionEffect(eventTypes.get(index)
							.createEffect(600, 0));
				}

			}
		}, 0L, 20L);

		// Delays potion creation
		scheduler.scheduleSyncDelayedTask(this, new Runnable() {
			@Override
			public void run() {
				// Item Creation
				ItemStack newPot = new Potion(PotionType.STRENGTH)
						.toItemStack(1);
				if (modifiers.get(0)) {
					newPot = new Potion(PotionType.STRENGTH).splash()
							.toItemStack(1);
				}
				PotionMeta meta = (PotionMeta) newPot.getItemMeta();

				// Name Creation
				String[] name = new String[3];
				if (effects.contains(PotionEffectType.HEAL)
						|| effects.contains(PotionEffectType.REGENERATION)
						|| effects.contains(PotionEffectType.ABSORPTION)
						|| effects.contains(PotionEffectType.HEALTH_BOOST)
						|| effects.contains(PotionEffectType.DAMAGE_RESISTANCE)) {
					name[0] = "Elixer";
				} // First part of name
				else if (effects.contains(PotionEffectType.HUNGER)
						|| effects.contains(PotionEffectType.SATURATION)) {
					name[0] = "Draught";
				} else {
					name[0] = "Potion";
				}

				name[1] = " of";

				if (effects.contains(PotionEffectType.POISON)
						|| effects.contains(PotionEffectType.HARM)
						|| effects.contains(PotionEffectType.WEAKNESS)
						|| effects.contains(PotionEffectType.WITHER)) {
					name[2] = " Harming";
				} // Last Part of name
				else if (effects.contains(PotionEffectType.JUMP)
						|| effects.contains(PotionEffectType.SPEED)
						|| effects.contains(PotionEffectType.WATER_BREATHING)
						|| effects.contains(PotionEffectType.FAST_DIGGING)) {
					name[2] = " Movement";
				} else if (effects.contains(PotionEffectType.BLINDNESS)
						|| effects.contains(PotionEffectType.NIGHT_VISION)
						|| effects.contains(PotionEffectType.INVISIBILITY)
						|| effects.contains(PotionEffectType.CONFUSION)) {
					name[2] = " Vision";
				} else {
					name[2] = " Something";
				}
				StringBuffer name1 = new StringBuffer();
				for (int i = 0; i < name.length; i++) {
					name1.append(name[i]);
				}
				String nameFinal = name1.toString();
				meta.setDisplayName(nameFinal);

				int t = (modifiers.get(1)) ? 1 : 0;
				int r = (modifiers.get(2)) ? 12000 : 6000;
				for (PotionEffectType pet : effects) {
					meta.addCustomEffect(pet.createEffect(r, t), true);
				}
				// Lore Creation (future stuff)

				newPot.setItemMeta(meta);

				Chest toAdd = (Chest) stand;
				toAdd.getInventory().clear();
				toAdd.getBlockInventory().addItem(newPot);
				potions.add(newPot);
				Bukkit.broadcastMessage("Potion Done");
				scheduler.cancelAllTasks(); // Ends the task above, which
											// determines negative effects
				return;
			}
		}, 400L);
	}
}

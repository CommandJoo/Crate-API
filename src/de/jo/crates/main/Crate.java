package de.jo.crates.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Crate implements Listener {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	List<Inventory> invs = new ArrayList();
	public static ItemStack[] contents;
	private int itemIndex = 0;
	private String title;
	private ItemStack randItem;
	private ItemStack hopperItem;
	private Sound clickSound;
	private Sound winningSound;
	private Sound loosingSound;
	private Sound whenItemClickedSound;

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Crate() {
	}

	public Crate(String title) {
		this.title = title;
	}

	public Crate(String title, ItemStack[] items) {
		this.title = title;
		contents = items;
	}

	public Crate(String title, ItemStack[] items, ItemStack randItem) {
		this.title = title;
		contents = items;
		this.randItem = randItem;
	}

	public Crate(String title, ItemStack[] items, ItemStack randItem, ItemStack hopperItem) {
		this.title = title;
		contents = items;
		this.randItem = randItem;
		this.hopperItem = hopperItem;
	}

	public Crate(String title, ItemStack[] items, ItemStack randItem, ItemStack hopperItem, Sound clickSound) {
		this.title = title;
		contents = items;
		this.hopperItem = hopperItem;
		this.randItem = randItem;
		this.clickSound = clickSound;
	}

	public Crate(String title, ItemStack[] items, ItemStack randItem, ItemStack hopperItem, Sound clickSound,
			Sound winningSound, Sound loosingSound) {
		this.title = title;
		contents = items;
		this.randItem = randItem;
		this.hopperItem = hopperItem;
		this.clickSound = clickSound;
		this.loosingSound = loosingSound;
		this.winningSound = winningSound;
	}

	public Crate(String title, ItemStack[] items, ItemStack randItem, ItemStack hopperItem, Sound clickSound,
			Sound winningSound, Sound loosingSound, Sound whenItemClickedSound) {
		this.title = title;
		contents = items;
		this.randItem = randItem;
		this.hopperItem = hopperItem;
		this.clickSound = clickSound;
		this.winningSound = winningSound;
		this.loosingSound = loosingSound;
		this.whenItemClickedSound = whenItemClickedSound;
	}

	public Crate(String title, ItemStack[] items, ItemStack randItem, ItemStack hopperItem, Sound clickSound,
			Sound winningSound, Sound whenItemClickedSound, Player player) {
		this.title = title;
		contents = items;
		this.randItem = randItem;
		this.hopperItem = hopperItem;
		this.clickSound = clickSound;
		this.winningSound = winningSound;
		this.whenItemClickedSound = whenItemClickedSound;
		spin(player);
	}

	public void spin(Player player) {
		Inventory inv = Bukkit.createInventory(null, 27, this.title);
		verteilen(inv);
		this.invs.add(inv);
		player.openInventory(inv);

		Random r = new Random();
		final double sekunden = 7.0D + 5.0D * r.nextDouble();
		new BukkitRunnable() {
			boolean done = false;
			double delay = 0.0D;
			int ticks = 0;

			public void run() {
				if (done) {
					return;
				}

				this.ticks += 1;
				this.delay += 1.0D / (20.0D * sekunden);
				if (this.ticks > this.delay * 10.0D) {
					this.ticks = 0;
					for (int itemstacks = 9; itemstacks < 18; itemstacks++) {
						inv.setItem(itemstacks,
								Crate.contents[((itemstacks + Crate.this.itemIndex) % Crate.contents.length)]);
					}

					Crate.this.itemIndex += 1;
					if (Crate.this.clickSound != null) {
						player.playSound(player.getLocation(), Crate.this.clickSound, 1.0F, 1.0F);
					}
					if (this.delay >= 0.5D) {
						done = true;
						new BukkitRunnable() {
							public void run() {
								try {
									ItemStack item = inv.getItem(13);
									if (!(item.getType() == Crate.this.randItem.getType())) {
										player.getInventory().addItem(new ItemStack[] { item });
										if (Crate.this.winningSound != null) {
											player.playSound(player.getLocation(), Crate.this.winningSound, 1.0F, 1.0F);
										}
									} else {
										if (Crate.this.loosingSound != null) {
											player.playSound(player.getLocation(), Crate.this.loosingSound, 1.0F, 1.0F);
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
									cancel();
								}
								player.updateInventory();
								player.closeInventory();
								cancel();

							}
						}.runTaskLater(Main.getPlugin(Main.class), 50L);
						cancel();
					}
				}
			}
		}.runTaskTimer(Main.instance(), 0L, 1L);
	}

	public void verteilen(Inventory inv) {
		if ((this.randItem != null) && (this.randItem.getType() != Material.AIR)) {
			for (int i = 0; i < 27; i++) {
				inv.setItem(i, this.randItem);
			}
		}
		if ((this.hopperItem != null) && (this.hopperItem.getType() != Material.AIR)) {
			inv.setItem(4, this.hopperItem);
		}
		if (contents == null) {
			ItemStack[] items = new ItemStack[9];

			ItemStack leer = new ItemStack(Material.BARRIER);
			ItemMeta leerMeta = leer.getItemMeta();
			leerMeta.setDisplayName("§cError");
			leer.setItemMeta(leerMeta);

			items[0] = leer;
			items[1] = leer;
			items[2] = leer;
			items[3] = leer;
			items[4] = leer;
			items[5] = leer;
			items[6] = leer;
			items[7] = leer;
			items[8] = leer;
			items[9] = leer;
			contents = items;
		}
		int startIndex = ThreadLocalRandom.current().nextInt(contents.length);
		for (int index = 0; index < startIndex; index++) {
			for (int itemstacks = 9; itemstacks < 18; itemstacks++) {
				inv.setItem(itemstacks, contents[((itemstacks + this.itemIndex) % contents.length)]);
			}
			this.itemIndex += 1;
		}
	}

	@EventHandler
	private void onInvClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();

		if (e.getCurrentItem() == null) {
			return;
		}
		if (e.getCurrentItem().getItemMeta() == null) {
			return;
		}
		if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
			return;
		}

		if (e.getView().getTitle().equalsIgnoreCase(this.title)) {

			if (e.getRawSlot() < e.getInventory().getSize()) {
				e.setCancelled(true);

				if (this.whenItemClickedSound != null) {
					p.playSound(p.getLocation(), this.whenItemClickedSound, 1.0F, 1.0F);
				}
			}
		}
	}
}

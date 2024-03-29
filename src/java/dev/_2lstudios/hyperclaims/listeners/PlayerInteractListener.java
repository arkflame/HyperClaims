package dev._2lstudios.hyperclaims.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import dev._2lstudios.hyperclaims.player.ProtectionPlayer;
import dev._2lstudios.hyperclaims.player.ProtectionPlayerManager;
import dev._2lstudios.hyperclaims.utils.ProtectionUtil;
import dev._2lstudios.worldsentinel.region.RegionManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.Listener;

public class PlayerInteractListener implements Listener {
    private final Economy economy;
    private final RegionManager regionManager;
    private final ProtectionPlayerManager pPlayerManager;
    private final ItemStack wandItemStack;

    public PlayerInteractListener(final Economy economy, final RegionManager regionManager,
            final ProtectionPlayerManager pPlayerManager, final ItemStack wandItemStack) {
        this.economy = economy;
        this.regionManager = regionManager;
        this.pPlayerManager = pPlayerManager;
        this.wandItemStack = wandItemStack;
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final ItemStack itemStack = event.getItem();
        if (itemStack != null && itemStack.isSimilar(this.wandItemStack)) {
            event.setCancelled(true);
            final Player player = event.getPlayer();
            final Action action = event.getAction();
            if (action == Action.LEFT_CLICK_AIR) {
                final ProtectionPlayer pPlayer = this.pPlayerManager.getPlayer(player);
                ProtectionUtil.claim(this.economy, this.regionManager, pPlayer, player);
            } else if (action != Action.RIGHT_CLICK_AIR) {
                final Block clickedBlock = event.getClickedBlock();
                if (clickedBlock == null) {
                    return;
                }
                final Location clickedBlockLocation = clickedBlock.getLocation();
                final String worldName = clickedBlockLocation.getWorld().getName();
                if (!worldName.equals("world") && !worldName.equals("world_nether")) {
                    player.sendMessage(ChatColor.RED + "No puedes claimear en este mundo!");
                } else if (action == Action.LEFT_CLICK_BLOCK) {
                    if (!player.isSneaking()) {
                        final ProtectionPlayer pPlayer2 = this.pPlayerManager.getPlayer(player);
                        pPlayer2.setPosition1(clickedBlockLocation);
                        player.sendMessage(ChatColor.GREEN + "Posicion 1 seleccionada correctamente!");
                    } else {
                        final ProtectionPlayer pPlayer2 = this.pPlayerManager.getPlayer(player);
                        ProtectionUtil.claim(this.economy, this.regionManager, pPlayer2, player);
                    }
                } else if (action == Action.RIGHT_CLICK_BLOCK) {
                    if (!player.isSneaking()) {
                        final ProtectionPlayer pPlayer2 = this.pPlayerManager.getPlayer(player);
                        pPlayer2.setPosition2(clickedBlockLocation);
                        player.sendMessage(ChatColor.GREEN + "Posicion 2 seleccionada correctamente!");
                    } else {
                        final ProtectionPlayer pPlayer2 = this.pPlayerManager.getPlayer(player);
                        pPlayer2.setPosition1(null);
                        pPlayer2.setPosition2(null);
                    }
                }
            }
        }
    }
}

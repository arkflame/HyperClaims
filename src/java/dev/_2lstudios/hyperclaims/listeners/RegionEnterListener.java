package dev._2lstudios.hyperclaims.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev._2lstudios.hyperclaims.utils.StringCollectionUtil;
import dev._2lstudios.worldsentinel.events.RegionEnterEvent;
import dev._2lstudios.worldsentinel.region.Region;
import dev._2lstudios.worldsentinel.region.RegionFlags;

public class RegionEnterListener implements Listener {
    private void sendEnterLeaveMessage(final Player player, final Region region, final String action,
            final String playerName) {
        if (region != null) {
            final RegionFlags flags = region.getFlags();
            if (StringCollectionUtil.containsEquals(flags.getCollection("extra_flags"), "ProtectionWands")) {
                ChatColor chatColor;
                if (flags.getCollection("owners").contains(playerName)) {
                    chatColor = ChatColor.GREEN;
                } else {
                    chatColor = ChatColor.RED;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e" + action + " region " + chatColor
                        + flags.getString("name") + "&e! Owners: &b" + flags.getCollection("owners")));
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onRegionEnter(final RegionEnterEvent event) {
        final Region oldRegion = event.getOldRegion();
        final Region newRegion = event.getNewRegion();
        final Player player = event.getPlayer();
        final String playerName = player.getName();
        this.sendEnterLeaveMessage(player, oldRegion, "Saliste de la", playerName);
        this.sendEnterLeaveMessage(player, newRegion, "Entraste a la", playerName);
    }
}

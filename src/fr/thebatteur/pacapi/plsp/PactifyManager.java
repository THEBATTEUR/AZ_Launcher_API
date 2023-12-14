/*
 * Class issue du repo https://github.com/PactifyLauncherExamples/PactifyPlugin
 */
package fr.thebatteur.pacapi.plsp;

import fr.thebatteur.pacapi.plsp.PLSPConfFlag;
import fr.thebatteur.pacapi.plsp.PactifyPlayer;
import fr.thebatteur.pacapi.utils.BukkitUtil;
import fr.thebatteur.pacapi.utils.PacketOutBuffer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import pactify.client.api.mcprotocol.NotchianPacketBuffer;
import pactify.client.api.mcprotocol.util.NotchianPacketUtil;
import pactify.client.api.plsp.PLSPPacket;
import pactify.client.api.plsp.PLSPPacketHandler;
import pactify.client.api.plsp.PLSPProtocol;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class PactifyManager implements Listener, Closeable {
    private Plugin plugin;

    private int serverVersion;

    public Plugin getPlugin() {
        return this.plugin;
    }

    public int getServerVersion() {
        return this.serverVersion;
    }

    private final Map<UUID, PactifyPlayer> players = new HashMap<>();
    private List<PLSPConfFlag> defaultFlags;

    public PactifyManager(Plugin plugin) {
        this.plugin = plugin;
        this.serverVersion = BukkitUtil.findServerVersion();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "PLSP");
        Bukkit.getLogger().info(String.valueOf(getClass().getSimpleName()) + " class loaded");
    }

    public PactifyManager(Plugin plugin, List<PLSPConfFlag> defaultFlags) {
        this.plugin = plugin;
        this.serverVersion = BukkitUtil.findServerVersion();
        this.defaultFlags = defaultFlags;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "PLSP");
        Bukkit.getLogger().info(String.valueOf(getClass().getSimpleName()) + " class loaded");
    }

    public PactifyPlayer getPlayer(Player player) {
        return this.players.get(player.getUniqueId());
    }

    private void playerQuit(Player player) {
        PactifyPlayer pactifyPlayer = this.players.remove(player.getUniqueId());
        if (pactifyPlayer != null)
            pactifyPlayer.free(true);
    }

    public void sendPLSPMessage(Player player, PLSPPacket<PLSPPacketHandler.ClientHandler> message) {
        try {
            PacketOutBuffer buf = new PacketOutBuffer();
            PLSPProtocol.PacketData<?> packetData = PLSPProtocol.getClientPacketByClass(message.getClass());
            NotchianPacketUtil.writeString((NotchianPacketBuffer) buf, packetData.getId(), 32767);
            message.write((NotchianPacketBuffer) buf);
            player.sendPluginMessage(this.plugin, "PLSP", buf.toBytes());
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.WARNING, "Exception sending PLSP message to " + ((player != null) ? player.getName() : "null") + ":", e);
        }
    }

    public void close() throws IOException {
        HandlerList.unregisterAll(this);
        this.plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(this.plugin, "PLSP");
    }

    //Listeners ---------------------------

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerLogin(PlayerLoginEvent event) {
        event.getPlayer().setMetadata("PACAPI:hostname",
                (MetadataValue) new FixedMetadataValue(this.plugin, event.getHostname()));
        PactifyPlayer pactifyPlayer;
        this.players.put(event.getPlayer().getUniqueId(), pactifyPlayer = new PactifyPlayer(this, event.getPlayer()));
        pactifyPlayer.init();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerLoginMonitor(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED)
            playerQuit(event.getPlayer());
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        PactifyPlayer pactifyPlayer = getPlayer(event.getPlayer());
        pactifyPlayer.join(defaultFlags);
        this.players.forEach(((uuid, pactifyPlayer1) -> {
            if (pactifyPlayer1.isTransformed())
                sendPLSPMessage(pactifyPlayer.getPlayer(), pactifyPlayer1.getTransformation().toMobPacket(uuid));
            if (pactifyPlayer1.isScaled() || pactifyPlayer1.isntDefaultOpacity())
                sendPLSPMessage(pactifyPlayer.getPlayer(), pactifyPlayer1.getTransformation().toScalePacket(pactifyPlayer1.getPlayer()));
        }));
    }



    @EventHandler(priority = EventPriority.HIGHEST)
    private void onPlayerQuit(PlayerQuitEvent event) {
        playerQuit(event.getPlayer());
    }
}

/*
 * Class issue du repo https://github.com/PactifyLauncherExamples/PactifyPlugin
 */
package fr.thebatteur.pacapi.plsp;

import fr.thebatteur.pacapi.utils.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import pactify.client.api.plsp.PLSPPacket;
import pactify.client.api.plsp.PLSPPacketHandler;
import pactify.client.api.plsp.packet.client.PLSPPacketReset;
import pactify.client.api.plsp.packet.client.PLSPPacketVignette;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PactifyPlayer {

    private static final Pattern PACTIFY_HOSTNAME_PATTERN = Pattern.compile("[\000\002]PAC([0-9A-F]{5})[\000\002]");

    private final PactifyManager service;

    private final Player player;
    private PactifyTransformation transformation;

    private boolean joined;

    private int launcherProtocolVersion;

    public PactifyPlayer(PactifyManager service, Player player) {
        this.service = service;
        this.player = player;
        this.transformation = new PactifyTransformation(EntityType.PLAYER);
    }

    public void init() {
        List<MetadataValue> hostnameMeta = this.player.getMetadata("PACAPI:hostname");
        if (!hostnameMeta.isEmpty()) {
            String hostname = ((MetadataValue)hostnameMeta.get(0)).asString();
            Matcher m = PACTIFY_HOSTNAME_PATTERN.matcher(hostname);
            if (m.find())
                this.launcherProtocolVersion = Math.max(1, Integer.parseInt(m.group(1), 16));
        } else {
            this.service.getPlugin().getLogger().warning("Unable to verify the launcher of " + this.player.getName() +
                    ": it probably logged when the plugin was disabled!");
        }
        BukkitUtil.addChannel(this.player, "PLSP");
    }

    public void join(List<PLSPConfFlag> activatedFlags) {
        this.joined = true;
        this.service.sendPLSPMessage(this.player, (PLSPPacket<PLSPPacketHandler.ClientHandler>) new PLSPPacketReset());
        this.setFlags(activatedFlags, true);
    }

    public void setFlag(PLSPConfFlag flag, boolean activated) {
        this.service.sendPLSPMessage(this.player, flag.toPacket(activated));
    }

    public void setFlags(List<PLSPConfFlag> flags, boolean activated) {
        for (PLSPConfFlag flag : flags) {
            this.service.sendPLSPMessage(player, flag.toPacket(activated));
        }
    }

    public void setFlags(List<PLSPConfFlag> flags, List<Boolean> activated) {
        for (int i = 0; i < flags.size(); i++) {
            Optional<Boolean> activatedFlag = Optional.ofNullable(activated.get(i));
            this.service.sendPLSPMessage(player, flags.get(i).toPacket(activatedFlag.orElseGet(() -> activated.get(activated.size() - 1))));
        }
    }

    public void resetPLSPPackets() {
        this.service.sendPLSPMessage(this.player, new PLSPPacketReset());
    }

    public void setVignette(Color color) {
        this.service.sendPLSPMessage(this.player, new PLSPPacketVignette(true, color.getRed()/255.0F, color.getGreen()/255.0F, color.getBlue()/255.0F));
    }

    public void transformIntoMob(EntityType entityType) {
        this.transformation.setEntityType(entityType);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            this.service.sendPLSPMessage(onlinePlayer, this.transformation.toMobPacket(player.getUniqueId()));
            this.service.sendPLSPMessage(onlinePlayer, this.transformation.toScalePacket(player));
        }
    }

    public void rescalePlayer(float scale) {
        this.transformation.setScale(scale);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            this.service.sendPLSPMessage(onlinePlayer, transformation.toScalePacket(this.player));
        }
    }

    public void editOpacity(float opacity) {
        this.transformation.setOpacity(opacity);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            this.service.sendPLSPMessage(onlinePlayer, transformation.toScalePacket(this.player));
        }
    }

    public PactifyTransformation getTransformation() {
        return this.transformation;
    }

    public boolean isTransformed() {
        return this.transformation.getEntityType() != EntityType.PLAYER;
    }

    public boolean isScaled() {
        return Math.abs(this.transformation.getScale()-1.0) <= 0.001F;
    }

    public boolean isntDefaultOpacity() {
        return Math.abs(this.transformation.getOpacity()-1.0) <= 0.001F;
    }

    public boolean hasSupPrefix() {
        return this.transformation.getSupPrefix() != null && !this.transformation.getSupPrefix().isEmpty();
    }

    public void free(boolean onQuit) {
        if (!onQuit)
            this.service.sendPLSPMessage(this.player, (PLSPPacket<PLSPPacketHandler.ClientHandler>)new PLSPPacketReset());
    }

    public void setTransformation(PactifyTransformation transformation) {
        this.transformation = transformation;
    }

    public void applyTransformation() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            this.service.sendPLSPMessage(onlinePlayer, this.transformation.toMobPacket(this.player.getUniqueId()));
            this.service.sendPLSPMessage(onlinePlayer, this.transformation.toScalePacket(this.player));
        }
    }

    public PactifyManager getService() {
        return this.service;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isJoined() {
        return this.joined;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public void setLauncherProtocolVersion(int launcherProtocolVersion) {
        this.launcherProtocolVersion = launcherProtocolVersion;
    }

    public boolean hasLauncher() {
        return (this.launcherProtocolVersion > 0);
    }

    public int getLauncherProtocolVersion() {
        return this.launcherProtocolVersion;
    }

    public String toString() {
        return "PactifyPlayer(service=" + getService() + ", player=" + getPlayer() + ", joined=" + isJoined() + ", launcherProtocolVersion=" + getLauncherProtocolVersion() + ")";
    }
}

package fr.thebatteur.pacapi.plsp;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import pactify.client.api.plprotocol.metadata.ImmutablePactifyScaleMetadata;
import pactify.client.api.plprotocol.metadata.PactifyModelMetadata;
import pactify.client.api.plprotocol.metadata.PactifyTagMetadata;
import pactify.client.api.plsp.packet.client.PLSPPacketEntityMeta;
import pactify.client.api.plsp.packet.client.PLSPPacketPlayerModel;

import java.util.UUID;

public class PactifyTransformation {

    private EntityType entityType;
    private float scale;
    private float opacity;
    private String supPrefix;

    public PactifyTransformation(EntityType entityType) {
        this.entityType = entityType;
        this.scale = 1.0F;
        this.opacity = 1.0F;
    }

    public PactifyTransformation(EntityType entityType, float scale) {
        this.entityType = entityType;
        this.scale = scale;
        this.opacity = -1.0F;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public float getScale() {
        return scale;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public String getSupPrefix() {
        return supPrefix;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public void setSupPrefix(String supPrefix) {
        this.supPrefix = supPrefix;
    }

    public PLSPPacketPlayerModel toMobPacket(UUID uuid) {
        return new PLSPPacketPlayerModel(uuid, new PactifyModelMetadata(this.entityType.getTypeId()));
    }

    public PLSPPacketEntityMeta toScalePacket(Player player) {
        PLSPPacketEntityMeta packet = new PLSPPacketEntityMeta();
        ImmutablePactifyScaleMetadata ipsm = new ImmutablePactifyScaleMetadata(this.getScale(), this.getScale(), this.getScale(), this.getScale(), this.getScale(), true);
        packet.setEntityId(player.getEntityId());
        packet.setScale(ipsm);
        PactifyTagMetadata ptm = new PactifyTagMetadata();
        if (this.supPrefix != null && !this.supPrefix.isEmpty())
            ptm.setText(this.supPrefix);
        ptm.setScale(this.scale);
        packet.setOpacity(this.opacity);
        packet.setSupTag(ptm);
        return packet;
    }
}

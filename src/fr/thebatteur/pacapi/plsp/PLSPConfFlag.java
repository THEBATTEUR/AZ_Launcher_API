package fr.thebatteur.pacapi.plsp;

import pactify.client.api.plsp.packet.client.PLSPPacketConfFlag;

public enum PLSPConfFlag {

    ATTACK_COOLDOWN,
    PLAYER_PUSH,
    LARGE_HITBOX,
    SWORD_BLOCKING,
    HIT_AND_BLOCK,
    OLD_ENCHANTEMENTS,
    PVP_HIT_PRIORITY,
    SEE_CHUNKS,
    SIDEBAR_SCORES,
    SMOOTH_EXPERIENCE_BAR,
    SORT_TAB_LIST_BY_NAMES;

    public PLSPPacketConfFlag toPacket() {
        return this.toPacket(true);
    }

    public PLSPPacketConfFlag toPacket(boolean activated) {
        return new PLSPPacketConfFlag(this.toString(), activated);
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}

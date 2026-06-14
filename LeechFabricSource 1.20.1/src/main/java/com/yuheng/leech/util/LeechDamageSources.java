package com.yuheng.leech.util;

import com.yuheng.leech.LeechMod;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class LeechDamageSources {
    public static final RegistryKey<DamageType> LEECH_TRUE = RegistryKey.of(
            RegistryKeys.DAMAGE_TYPE,
            LeechMod.id("leech_true")
    );

    private LeechDamageSources() {
    }

    public static DamageSource trueDamage(PlayerEntity player) {
        return new DamageSource(
                player.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(LEECH_TRUE),
                player,
                player
        );
    }
}

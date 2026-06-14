package com.yuheng.leech.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("lastDamageTaken")
    float leech$getLastDamageTaken();

    @Accessor("lastDamageTaken")
    void leech$setLastDamageTaken(float value);
}

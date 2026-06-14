package com.yuheng.leech.net;

import com.yuheng.leech.LeechMod;
import com.yuheng.leech.item.LeechSwordItem;
import com.yuheng.leech.storage.LeechPersistentInventory;
import com.yuheng.leech.storage.LeechStorageState;
import com.yuheng.leech.util.LeechUtil;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public final class LeechPackets {
    public static final Identifier AIR_JUMP = LeechMod.id("air_jump");
    public static final Identifier SUMMON_SWORD = LeechMod.id("summon_sword");
    public static final Identifier CLEAR_LEVITATION = LeechMod.id("clear_levitation");
    public static final Identifier OPEN_STORAGE = LeechMod.id("open_storage");

    private LeechPackets() {
    }

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(AIR_JUMP, (server, player, handler, buf, responseSender) ->
                server.execute(() -> handleAirJump(player))
        );

        ServerPlayNetworking.registerGlobalReceiver(SUMMON_SWORD, (server, player, handler, buf, responseSender) ->
                server.execute(() -> handleSummonSword(player))
        );

        ServerPlayNetworking.registerGlobalReceiver(CLEAR_LEVITATION, (server, player, handler, buf, responseSender) ->
                server.execute(() -> handleClearLevitation(player))
        );

        ServerPlayNetworking.registerGlobalReceiver(OPEN_STORAGE, (server, player, handler, buf, responseSender) ->
                server.execute(() -> handleOpenStorage(player))
        );
    }

    private static void handleAirJump(ServerPlayerEntity player) {
        if (!LeechSwordItem.isLeechSwordMainHand(player)) {
            return;
        }
        if (player.isOnGround()) {
            return;
        }
        if (player.getItemCooldownManager().isCoolingDown(LeechMod.LEECH_SWORD)) {
            return;
        }

        Vec3d velocity = player.getVelocity();
        player.setVelocity(velocity.x, 0.42D, velocity.z);
        player.fallDistance = 0.0F;
        player.velocityModified = true;
        player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
        player.getItemCooldownManager().set(LeechMod.LEECH_SWORD, 3);
    }

    private static void handleSummonSword(ServerPlayerEntity player) {
        if (LeechUtil.hasLeechSword(player)) {
            return;
        }

        ItemStack stack = LeechSwordItem.createDefaultStack();
        if (!player.getInventory().insertStack(stack)) {
            player.dropItem(stack, false);
        }
    }

    private static void handleClearLevitation(ServerPlayerEntity player) {
        if (!LeechUtil.hasLeechSword(player)) {
            return;
        }

        player.removeStatusEffect(StatusEffects.LEVITATION);
        player.fallDistance = 0.0F;
    }

    private static void handleOpenStorage(ServerPlayerEntity player) {
        if (!LeechSwordItem.isLeechSwordMainHand(player)) {
            return;
        }

        LeechStorageState state = LeechStorageState.get(player.getServer());
        LeechPersistentInventory inventory = new LeechPersistentInventory(state.getInventory(player.getUuid()), state);

        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                (syncId, playerInventory, playerEntity) -> GenericContainerScreenHandler.createGeneric9x6(syncId, playerInventory, inventory),
                Text.translatable("container.leech.storage")
        ));
    }
}

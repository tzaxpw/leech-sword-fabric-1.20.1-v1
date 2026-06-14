package com.yuheng.leech.util;

import com.yuheng.leech.item.LeechSwordItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public final class LeechUtil {
    private LeechUtil() {
    }

    public static boolean hasLeechSword(PlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() instanceof LeechSwordItem) {
                return true;
            }
        }
        return false;
    }
}

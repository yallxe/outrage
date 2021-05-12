package net.superblaubeere27.clientbase.modules.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;

public class AutoTotem extends Module {
    public AutoTotem() {
        super("AutoTotem", "auto totem but bugs if your inventory is full ://", ModuleCategory.COMBAT);
    }

    @EventTarget
    public void handler(MotionUpdateEvent event) {
        ItemStack offhand_item = mc.player.getHeldItemOffhand();
        if (offhand_item.getItem() != Items.TOTEM_OF_UNDYING && mc.player.getHealth() <= 5) {
            for (ItemStack itemStack : mc.player.inventory.mainInventory) {
                if (itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
                    mc.player.setHeldItem(EnumHand.OFF_HAND, itemStack);
                }
            }
        }
    }
}

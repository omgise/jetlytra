package xyz.e3ndr.jetlytra.util;

import baubles.api.BaublesApi;
import baubles.common.container.InventoryBaubles;
import ganymedes01.etfuturum.compat.CompatBaublesExpanded;
import net.minecraft.entity.player.EntityPlayer;

public class BaubleUtil {

    public static void syncWingsBaubles(EntityPlayer player) {
        if (player.worldObj.isRemote) {
            return; // Only sync from server to client.
        }

        InventoryBaubles baubles = (InventoryBaubles) BaublesApi.getBaubles(player);
        if (baubles == null) {
            return;
        }

        for (int slotId : CompatBaublesExpanded.wingSlotIDs) {
            baubles.syncSlotToClients(slotId);
        }
    }

}

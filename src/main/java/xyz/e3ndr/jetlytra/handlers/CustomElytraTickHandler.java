package xyz.e3ndr.jetlytra.handlers;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import ganymedes01.etfuturum.api.elytra.IElytraPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import xyz.e3ndr.jetlytra.items.CustomElytra;

public class CustomElytraTickHandler {
    public static final Map<EntityPlayer, Boolean> propelState = new HashMap<EntityPlayer, Boolean>();

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        EntityLivingBase entity = event.entityLiving;

        if (!(entity instanceof IElytraPlayer)) {
            return; // not a player, skip
        }

        if (!((IElytraPlayer) entity).etfu$isElytraFlying()) {
            return; // not flying, skip
        }

        ItemStack stack = CustomElytra.getCustomElytra(entity);
        if (stack == null) {
            return; // no custom elytra, skip
        }

        boolean isPropelling = propelState.getOrDefault(entity, false);
        if (!isPropelling) {
            return; // not propelling, skip
        }

        CustomElytra elytra = (CustomElytra) stack.getItem();

        if (elytra.onBoostTick(stack)) {
            // Verbatim:
            // https://github.com/Roadhog360/Et-Futurum-Requiem/blob/master/src/main/java/ganymedes01/etfuturum/entities/EntityBoostingFireworkRocket.java#L49C117-L53C116
            Vec3 vec3d = entity.getLookVec();
            entity.motionX += vec3d.xCoord * 0.1D + (vec3d.xCoord * 1.5D - entity.motionX) * 0.5D;
            entity.motionY += vec3d.yCoord * 0.1D + (vec3d.yCoord * 1.5D - entity.motionY) * 0.5D;
            entity.motionZ += vec3d.zCoord * 0.1D + (vec3d.zCoord * 1.5D - entity.motionZ) * 0.5D;
            entity.velocityChanged = true;
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerLoggedOutEvent evt) {
        propelState.remove(evt.player);
    }

}

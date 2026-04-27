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
        IElytraPlayer elytraPlayer;
        {

            if (!(entity instanceof IElytraPlayer)) {
                return; // not a player, skip
            }

            elytraPlayer = (IElytraPlayer) entity;
        }

        if (!elytraPlayer.etfu$isElytraFlying()) {
            return; // not flying, skip
        }

        ItemStack stack = CustomElytra.getCustomElytra(event.entityLiving);
        if (stack == null) {
            return; // no custom elytra, skip
        }

        boolean isPropelling = propelState.getOrDefault(elytraPlayer, false);
        if (!isPropelling) {
            return; // not propelling, skip
        }

        CustomElytra elytra = (CustomElytra) stack.getItem();

        if (elytra.onBoostTick(stack)) {
            // Inspired from:
            // https://github.com/Roadhog360/Et-Futurum-Requiem/blob/master/src/main/java/ganymedes01/etfuturum/entities/EntityBoostingFireworkRocket.java#L49C117-L53C116
            Vec3 vec3d = entity.getLookVec();

            double dx = vec3d.xCoord * 0.1 + (vec3d.xCoord * 1.5 - entity.motionX) * 0.5;
            double dy = vec3d.yCoord * 0.1 + (vec3d.yCoord * 1.5 - entity.motionY) * 0.5;
            double dz = vec3d.zCoord * 0.1 + (vec3d.zCoord * 1.5 - entity.motionZ) * 0.5;

            entity.motionX += dx * elytra.acceleration;
            entity.motionY += dy * elytra.acceleration;
            entity.motionZ += dz * elytra.acceleration;
            entity.velocityChanged = true;
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerLoggedOutEvent evt) {
        propelState.remove(evt.player);
    }

}

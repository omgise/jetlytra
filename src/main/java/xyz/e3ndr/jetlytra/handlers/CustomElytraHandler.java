package xyz.e3ndr.jetlytra.handlers;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import ganymedes01.etfuturum.api.elytra.IElytraPlayer;
import ganymedes01.etfuturum.compat.ModsList;
import ganymedes01.etfuturum.configuration.configs.ConfigModCompat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import xyz.e3ndr.jetlytra.items.CustomElytra;
import xyz.e3ndr.jetlytra.util.BaubleUtil;

public class CustomElytraHandler {
    public static final Map<EntityPlayer, Double> propelRates = new HashMap<>();

    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event) {
        EntityLivingBase entity = event.entityLiving;
        if (!(entity instanceof IElytraPlayer)) {
            return; // not a player, skip
        }

        IElytraPlayer elytraPlayer = (IElytraPlayer) entity;

        if (!elytraPlayer.etfu$isElytraFlying()) {
            return; // not flying, skip
        }

        ItemStack stack = CustomElytra.getCustomElytra(event.entityLiving);
        if (stack == null) {
            return; // no custom elytra, skip
        }

        CustomElytra elytra = (CustomElytra) stack.getItem();

        if (ModsList.BAUBLES_EXPANDED.isLoaded() && ConfigModCompat.elytraBaublesExpandedCompat != 0) {
            // Sync Baubles|Expanded wing baubles to client to prevent desync between server
            // and client which causes the HUD to not update and show stale information
            // about remaining flight time.
            BaubleUtil.syncWingsBaubles((EntityPlayer) entity);
        }

        double propellingRate = propelRates.getOrDefault(elytraPlayer, 0d);
        if (propellingRate == 0) {
            return; // not propelling, skip
        }

        if (elytra.onBoostTick(stack, propellingRate)) {
            // Inspired from:
            // https://github.com/Roadhog360/Et-Futurum-Requiem/blob/master/src/main/java/ganymedes01/etfuturum/entities/EntityBoostingFireworkRocket.java#L49C117-L53C116
            Vec3 vec3d = entity.getLookVec();

            double dx = vec3d.xCoord * 0.1 + (vec3d.xCoord * 1.5 - entity.motionX) * 0.5;
            double dy = vec3d.yCoord * 0.1 + (vec3d.yCoord * 1.5 - entity.motionY) * 0.5;
            double dz = vec3d.zCoord * 0.1 + (vec3d.zCoord * 1.5 - entity.motionZ) * 0.5;

            entity.motionX += dx * elytra.maxAcceleration * propellingRate;
            entity.motionY += dy * elytra.maxAcceleration * propellingRate;
            entity.motionZ += dz * elytra.maxAcceleration * propellingRate;
            entity.velocityChanged = true;
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
        propelRates.remove(event.player);
    }

    @SubscribeEvent
    public void onPlayerAttacked(LivingAttackEvent event) {
        EntityLivingBase entity = event.entityLiving;
        if (!(entity instanceof IElytraPlayer)) {
            return; // not a player, skip
        }

        if (!event.source.damageType.equals("flyIntoWall")) {
            return; // not collision damage, skip
        }

        ItemStack stack = CustomElytra.getCustomElytra(event.entityLiving);
        if (stack == null) {
            return; // no custom elytra, skip
        }

        CustomElytra elytra = (CustomElytra) stack.getItem();

        if (elytra.canAbsorbCollision(stack, event.ammount)) {
            event.setCanceled(true);
        }
    }

}

package xyz.e3ndr.jetlytra.net;

import java.lang.reflect.Field;
import java.util.EnumMap;

import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import ganymedes01.etfuturum.EtFuturum;
import ganymedes01.etfuturum.configuration.configs.ConfigMixins;
import ganymedes01.etfuturum.elytra.IElytraPlayer;
import ganymedes01.etfuturum.items.equipment.ItemArmorElytra;
import ganymedes01.etfuturum.network.StartElytraFlyingMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;

/**
 * We have to override their implementation because they hardcode
 * 
 * <pre>
 * itemstack.getItem() == ModItems.ELYTRA.get()
 * </pre>
 * 
 * instead of the usual instanceof check.
 */
public class EFStartElytraFlyingHandlerOverride implements IMessageHandler<StartElytraFlyingMessage, IMessage> {

    @SuppressWarnings("unchecked")
    public static void doOverride() throws Exception {
        EnumMap<Side, FMLEmbeddedChannel> channels;
        {
            Field channelsField = SimpleNetworkWrapper.class.getDeclaredField("channels");
            channelsField.setAccessible(true);
            channels = (EnumMap<Side, FMLEmbeddedChannel>) channelsField.get(EtFuturum.networkWrapper);
        }

        FMLEmbeddedChannel channel = channels.get(Side.SERVER);
        channel.pipeline().remove("ganymedes01.etfuturum.network.StartElytraFlyingHandler");

        EtFuturum.networkWrapper.registerMessage(EFStartElytraFlyingHandlerOverride.class, StartElytraFlyingMessage.class, 6, Side.SERVER);
    }

    @Override
    public IMessage onMessage(StartElytraFlyingMessage message, MessageContext ctx) {
        // Their logic, verbaitim, except for the item check.
        EntityPlayer player = ((NetHandlerPlayServer) ctx.netHandler).playerEntity;
        if (!player.onGround && (ConfigMixins.enableNewElytraTakeoffLogic || player.motionY < 0.0D) && !((IElytraPlayer) player).etfu$isElytraFlying() && !player.isInWater()) {
            ItemStack itemstack = ItemArmorElytra.getElytra(player);

            if (itemstack != null && itemstack.getItem() instanceof ItemArmorElytra && !ItemArmorElytra.isBroken(itemstack)) {
                ((IElytraPlayer) player).etfu$setElytraFlying(true);
            }
        } else {
            ((IElytraPlayer) player).etfu$setElytraFlying(true);
            ((IElytraPlayer) player).etfu$setElytraFlying(false);
        }
        return null;
    }

}

package xyz.e3ndr.jetlytra.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import ganymedes01.etfuturum.api.elytra.IElytraPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import xyz.e3ndr.jetlytra.ModMessages;
import xyz.e3ndr.jetlytra.items.CustomElytra;
import xyz.e3ndr.jetlytra.net.MessagePropelling;

public class ClientHandler {
    private static final Minecraft mc = Minecraft.getMinecraft();

    private int propellingTicks = 0;
    private double propellingRate = 0;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        if (event.phase != Phase.START) {
            return;
        }

        if (mc.thePlayer == null) {
            return;
        }

        if (!((IElytraPlayer) mc.thePlayer).etfu$isElytraFlying()) {
            return; // not flying, skip
        }

        ItemStack stack = CustomElytra.getCustomElytra(mc.thePlayer);
        if (stack == null) {
            return; // no custom elytra, skip
        }

        CustomElytra elytra = (CustomElytra) stack.getItem();

        boolean isPropelling = mc.gameSettings.keyBindForward.getIsKeyPressed();
        if (!isPropelling) {
            this.propellingTicks = 0;
        } else {
            this.propellingTicks++;
        }

        double newRate = Math.min(1, (double) this.propellingTicks / elytra.accelerationTicks);
        if (newRate == this.propellingRate) {
            return; // No change in propulsion rate, skip sending message
        }

        this.propellingRate = newRate;

        MessagePropelling message = new MessagePropelling();
        message.propellingRate = this.propellingRate;
        ModMessages.network.sendToServer(message);
    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event) {
        if (event.phase != Phase.END) {
            return;
        }

        if (mc.thePlayer == null) {
            return;
        }

        if (!((IElytraPlayer) mc.thePlayer).etfu$isElytraFlying()) {
            return; // not flying, skip
        }

        ItemStack stack = CustomElytra.getCustomElytra(mc.thePlayer);
        if (stack == null) {
            return; // no custom elytra, skip
        }

        CustomElytra elytra = (CustomElytra) stack.getItem();

        String hudInfo = elytra.hudInfo(stack, this.propellingRate);
        if (hudInfo == null) {
            return; // no info to display, skip
        }

        GL11.glPushMatrix();
        mc.entityRenderer.setupOverlayRendering();

        ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();

        int textWidth = mc.fontRenderer.getStringWidth(hudInfo);
        int x = (width - textWidth) / 2;
        int y = height / 2 + 16; // below the crosshair

        mc.fontRenderer.drawStringWithShadow(hudInfo, x, y, 0xFFFFFF);

        GL11.glPopMatrix();
    }

}

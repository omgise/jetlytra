package xyz.e3ndr.jetlytra.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import net.minecraft.client.Minecraft;
import xyz.e3ndr.jetlytra.ModMessages;
import xyz.e3ndr.jetlytra.net.MessagePropelling;

public class ClientKeyHandler {
    private static final Minecraft mc = Minecraft.getMinecraft();

    private boolean lastPropellingState = false;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent e) {
        if (e.phase != Phase.START) {
            return;
        }
        if (mc.thePlayer == null) {
            return;
        }

        boolean isPropelling = mc.gameSettings.keyBindForward.getIsKeyPressed();
        if (isPropelling == this.lastPropellingState) {
            return;
        }

        this.lastPropellingState = isPropelling;

        MessagePropelling message = new MessagePropelling();
        message.isPropelling = isPropelling;
        ModMessages.network.sendToServer(message);
    }

}

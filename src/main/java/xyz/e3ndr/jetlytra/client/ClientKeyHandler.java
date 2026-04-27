package xyz.e3ndr.jetlytra.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import net.minecraft.client.Minecraft;
import xyz.e3ndr.jetlytra.ModMessages;
import xyz.e3ndr.jetlytra.net.MessagePropelling;

public class ClientKeyHandler {
    private static final int PROPULSION_START_TICKS = 40; // ticks, how long it takes to reach full propulsion rate. 20t = 1s.

    private static final Minecraft mc = Minecraft.getMinecraft();

    private int propellingTicks = 0;
    private double propellingRate = 0;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent e) {
        if (e.phase != Phase.START) {
            return;
        }
        if (mc.thePlayer == null) {
            return;
        }

        boolean isPropelling = mc.gameSettings.keyBindForward.getIsKeyPressed();
        if (!isPropelling) {
            this.propellingTicks = 0;
        } else {
            this.propellingTicks++;
        }

        double newRate = Math.min(1, (double) this.propellingTicks / PROPULSION_START_TICKS);
        if (newRate == this.propellingRate) {
            return; // No change in propulsion rate, skip sending message
        }

        this.propellingRate = newRate;

        MessagePropelling message = new MessagePropelling();
        message.propellingRate = this.propellingRate;
        ModMessages.network.sendToServer(message);
    }

}

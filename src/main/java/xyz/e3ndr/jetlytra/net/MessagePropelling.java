package xyz.e3ndr.jetlytra.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import xyz.e3ndr.jetlytra.handlers.CustomElytraHandler;

public class MessagePropelling implements IMessage, IMessageHandler<MessagePropelling, IMessage> {
    public double propellingRate;

    public MessagePropelling() {}

    @Override
    public void fromBytes(ByteBuf buf) {
        this.propellingRate = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(this.propellingRate);
    }

    @Override
    public IMessage onMessage(MessagePropelling msg, MessageContext ctx) {
        EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
        if (entityPlayer == null) {
            return null;
        }

        CustomElytraHandler.propelRates.put(entityPlayer, msg.propellingRate);
        return null;
    }

}

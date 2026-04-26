package xyz.e3ndr.jetlytra.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import xyz.e3ndr.jetlytra.handlers.CustomElytraTickHandler;

public class MessagePropelling implements IMessage, IMessageHandler<MessagePropelling, IMessage> {
    public boolean isPropelling;

    public MessagePropelling() {}

    @Override
    public void fromBytes(ByteBuf buf) {
        this.isPropelling = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.isPropelling);
    }

    @Override
    public IMessage onMessage(MessagePropelling msg, MessageContext ctx) {
        EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
        if (entityPlayer == null) {
            return null;
        }

        CustomElytraTickHandler.propelState.put(entityPlayer, msg.isPropelling);
        return null;
    }

}

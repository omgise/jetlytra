package xyz.e3ndr.jetlytra;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import xyz.e3ndr.jetlytra.net.MessagePropelling;

public class ModMessages {
    public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("jetlytra");

    public static void init() {
        network.registerMessage(
            MessagePropelling.class,
            MessagePropelling.class,
            0,
            Side.SERVER
        );
    }

}

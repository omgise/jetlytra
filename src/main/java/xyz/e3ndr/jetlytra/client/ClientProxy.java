package xyz.e3ndr.jetlytra.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import xyz.e3ndr.jetlytra.CommonProxy;
import xyz.e3ndr.jetlytra.ModBase;

/**
 * Override as needed.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        ModBase.LOG.info("Doing Client preInit.");
        FMLCommonHandler.instance().bus().register(new ClientKeyHandler());
    }

}

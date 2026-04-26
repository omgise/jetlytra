package xyz.e3ndr.jetlytra;

import com.google.common.base.Throwables;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import xyz.e3ndr.jetlytra.handlers.CustomElytraTickHandler;
import xyz.e3ndr.jetlytra.net.EFStartElytraFlyingHandlerOverride;

public class CommonProxy {

    /**
     * Run before anything else. Read your config, create blocks, items, etc, and
     * register them with the GameRegistry.
     */
    public void preInit(FMLPreInitializationEvent event) {
        ModBase.LOG.info("Hello world! Doing preInit.");

        ModMessages.init();
        ModRegistry.preInit();

        try {
            EFStartElytraFlyingHandlerOverride.doOverride();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * Do your mod setup. Build whatever data structures you care about. Register
     * recipes.
     */
    public void init(FMLInitializationEvent event) {
        ModBase.LOG.info("Doing init.");

        ModRegistry.init();
        MinecraftForge.EVENT_BUS.register(new CustomElytraTickHandler());
    }

    /**
     * Handle interaction with other mods, complete your setup based on this.
     */
    public void postInit(FMLPostInitializationEvent event) {
        ModBase.LOG.info("Doing postInit.");

    }

}

package xyz.e3ndr.jetlytra;

import java.util.ArrayList;
import java.util.List;

import cofh.lib.util.helpers.EnergyHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xyz.e3ndr.jetlytra.items.FluxElytra;
import xyz.e3ndr.jetlytra.recipe.RecipeFluxElytra;

public class ModRegistry {

    public static final CreativeTabs tab = new ModTab(CreativeTabs.getNextID(), "JetLytra");

    // https://oldcofh.github.io/docs/thermal-expansion/storage/flux-capacitors/#tiers
    // @formatter:off
    public static final Item itemLeadstoneFluxLytra = new FluxElytra("leadstone_flux_elytra",     80_000,    200, 0.1 );
    public static final Item itemHardenedFluxLytra  = new FluxElytra("hardened_flux_elytra",     400_000,    800, 0.15);
    public static final Item itemRedstoneFluxLytra  = new FluxElytra("redstone_flux_elytra",   4_000_000,  8_000, 0.3 );
    public static final Item itemResonantFluxLytra  = new FluxElytra("resonant_flux_elytra",  20_000_000, 32_000, 0.5 );
    // @formatter:on

    public static void preInit() {
        // static initializer for items
    }

    public static void init() {
        Item teCapacitor = GameRegistry.findItem("ThermalExpansion", "capacitor");
        Item teMaterial = GameRegistry.findItem("ThermalFoundation", "material");
        Item teDynamo = GameRegistry.findItem("ThermalExpansion", "Dynamo");

        Item efElytra = GameRegistry.findItem("etfuturum", "elytra");

        List<String> missingItems = new ArrayList<>();
        if (teCapacitor == null) {
            missingItems.add("ThermalDynamics:capacitor");
        }
        if (teMaterial == null) {
            missingItems.add("ThermalFoundation:material");
        }
        if (teDynamo == null) {
            missingItems.add("ThermalExpansion:Dynamo");
        }
        if (efElytra == null) {
            missingItems.add("etfuturum:elytra");
        }

        if (!missingItems.isEmpty()) {
            throw new RuntimeException("Missing required items for crafting recipes: " + String.join(", ", missingItems));
        }

        ItemStack leadstoneFluxElytraStack = EnergyHelper.setDefaultEnergyTag(new ItemStack(itemLeadstoneFluxLytra, 1, 0), 0);
        ItemStack hardenedFluxElytraStack = EnergyHelper.setDefaultEnergyTag(new ItemStack(itemHardenedFluxLytra, 1, 0), 0);
        ItemStack redstoneFluxElytraStack = EnergyHelper.setDefaultEnergyTag(new ItemStack(itemRedstoneFluxLytra, 1, 0), 0);
        ItemStack resonantFluxElytraStack = EnergyHelper.setDefaultEnergyTag(new ItemStack(itemResonantFluxLytra, 1, 0), 0);

        // @formatter:off
        GameRegistry.addRecipe(new RecipeFluxElytra(
            leadstoneFluxElytraStack,
            "ICI",
            "GEG",
            "DPD",

            'I', "ingotLead",
            'C', new ItemStack(teCapacitor, 1,   2), // leadstone flux capacitor
            'G', new ItemStack(teMaterial,  1, 131), // lead gear
            'E', new ItemStack(efElytra,    1,   0),
            'D', new ItemStack(teDynamo,    1,   0), // steam dynamo
            'P', new ItemStack(teMaterial,  1, 512)  // pyrotheum dust
        ));
        GameRegistry.addRecipe(new RecipeFluxElytra(
            hardenedFluxElytraStack,
            "ICI",
            "GEG",
            "DBD",

            'I', "ingotInvar",
            'C', new ItemStack(teCapacitor, 1,   3), // hardened flux capacitor
            'G', new ItemStack(teMaterial,  1, 136), // invar gear
            'E', leadstoneFluxElytraStack,
            'D', new ItemStack(teMaterial,  1, 514), // aerotheum dust
            'B', new ItemStack(teMaterial,  1, 137)  // bronze gear
        ));
        GameRegistry.addRecipe(new RecipeFluxElytra(
            redstoneFluxElytraStack, 
            "ICI",
            "GEG",
            "DBD",

            'I', "ingotElectrum",
            'C', new ItemStack(teCapacitor, 1,   4), // redstone flux capacitor
            'G', new ItemStack(teMaterial,  1, 135), // electrum gear
            'E', hardenedFluxElytraStack,
            'D', new ItemStack(teMaterial,  1, 515), // petrotheum dust
            'B', new ItemStack(teMaterial,  1, 138)  // signalum gear
        ));
        GameRegistry.addRecipe(new RecipeFluxElytra(
            resonantFluxElytraStack,
            "ICI",
            "GEG",
            "DBD",

            'I', "ingotEnderium",
            'C', new ItemStack(teCapacitor, 1,   5), // resonant flux capacitor
            'G', new ItemStack(teMaterial,  1, 140), // enderium gear
            'E', redstoneFluxElytraStack,
            'D', new ItemStack(teMaterial,  1, 513), // cryotheum dust
            'B', new ItemStack(teMaterial,  1, 139)  // lumium gear
        ));
        // @formatter:off
    }

}

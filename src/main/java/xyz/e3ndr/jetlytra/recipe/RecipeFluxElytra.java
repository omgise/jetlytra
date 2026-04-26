package xyz.e3ndr.jetlytra.recipe;

import cofh.api.energy.IEnergyContainerItem;
import cofh.lib.util.helpers.EnergyHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeFluxElytra extends ShapedOreRecipe {

    public RecipeFluxElytra(ItemStack result, Object... recipe) {
        super(result, recipe);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack result = super.getCraftingResult(inv);

        if (result == null) {
            return null;
        }

        // Keep the charge of the capacitor used in the recipe

        ItemStack capacitorStack = inv.getStackInSlot(1);
        IEnergyContainerItem capacitor = (IEnergyContainerItem) capacitorStack.getItem();

        EnergyHelper.setDefaultEnergyTag(result, capacitor.getEnergyStored(capacitorStack));
        return result;
    }

}

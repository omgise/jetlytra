package xyz.e3ndr.jetlytra;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModTab extends CreativeTabs {
    private final String name;

    public ModTab(int id, String name) {
        super(id, name);
        this.name = name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        return new ItemStack(ModRegistry.itemLeadstoneFluxLytra, 1, 0);
    }

    @Override
    public String getTranslatedTabLabel() {
        return this.name;
    }

    @Override
    public Item getTabIconItem() {
        return null;
    }

}

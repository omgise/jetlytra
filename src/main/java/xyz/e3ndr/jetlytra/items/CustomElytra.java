package xyz.e3ndr.jetlytra.items;

import cpw.mods.fml.common.registry.GameRegistry;
import ganymedes01.etfuturum.items.equipment.ItemArmorElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import xyz.e3ndr.jetlytra.ModRegistry;

public abstract class CustomElytra extends ItemArmorElytra {

    public final double acceleration;

    public CustomElytra(String id, double acceleration) {
        super();
        this.setUnlocalizedNameWithPrefix(id);
        this.setTextureName(id);
        this.setMaxStackSize(1);
        this.setCreativeTab(ModRegistry.tab);

        this.acceleration = acceleration;

        GameRegistry.registerItem(this, id);
    }

    public static ItemStack getCustomElytra(EntityLivingBase entity) {
        ItemStack stack = ItemArmorElytra.getElytra(entity);
        if (stack != null && stack.getItem() instanceof CustomElytra) {
            return stack;
        } else {
            return null;
        }
    }

    /* ---------------------------------------------------------------- */
    /* ---------------------------------------------------------------- */
    /* ---------------------------------------------------------------- */

    /**
     * @return true, if there was enough energy to boost. false if the boost failed
     *         (e.g. not enough energy).
     */
    public abstract boolean onBoostTick(ItemStack container);

    /**
     * @return true, if the elytra can absorb the collision damage. false if the
     *         collision should be handled normally (e.g. not enough energy).
     */
    public abstract boolean canAbsorbCollision(ItemStack container, float damage);

    /* ---------------------------------------------------------------- */
    /* ---------------------------------------------------------------- */
    /* ---------------------------------------------------------------- */

    @Override
    public String getTextureDomain() {
        return "jetlytra";
    }

    @Override
    public String getNameDomain() {
        return "jetlytra";
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        // We don't want the broken_elytra texture when we run out of energy.
        return this.itemIcon;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repairMaterial) {
        return false;
    }

}

package xyz.e3ndr.jetlytra.items;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cofh.api.energy.IEnergyContainerItem;
import cofh.lib.util.helpers.EnergyHelper;
import cofh.lib.util.helpers.StringHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class FluxElytra extends CustomElytra implements IEnergyContainerItem {
    private static final int FLIGHT_CONSUMPTION_RATE = 100; // RF/second
    private static final int BOOST_CONSUMPTION_RATE = 200; // RF/tick key held down
    private static final int COLLISION_ABSORB_RATE = 300; // RF/damage

    private static final int HOUR_IN_SECONDS = (int) TimeUnit.HOURS.toSeconds(1);
    private static final int MINUTE_IN_SECONDS = (int) TimeUnit.MINUTES.toSeconds(1);

    private final int capacity;
    private final int maxReceive;

    public FluxElytra(String id, int maxEnergy, int maxInput, double acceleration) {
        super(id, acceleration);
        this.capacity = maxEnergy;
        this.maxReceive = maxInput;
    }

    /* ---------------------------------------------------------------- */
    /* ---------------------------------------------------------------- */
    /* ---------------------------------------------------------------- */

    @Override
    public boolean onBoostTick(ItemStack container, double propellingRate) {
        int energyNeeded = (int) Math.ceil(BOOST_CONSUMPTION_RATE * propellingRate);
        return this.consume(container, energyNeeded) > 0;
    }

    @Override
    public boolean canAbsorbCollision(ItemStack container, float damage) {
        int energyNeeded = (int) Math.ceil(damage * COLLISION_ABSORB_RATE);
        return this.consume(container, energyNeeded) > 0;
    }

    @Override
    public String hudInfo(ItemStack stack, double propellingRate) {
        int consumptionRate = (int) Math.ceil(BOOST_CONSUMPTION_RATE * propellingRate);
        consumptionRate += FLIGHT_CONSUMPTION_RATE / 20; // add the base flight consumption rate.

        int storedEnergy = this.getEnergyStored(stack);
//        int percentCharge = storedEnergy * 100 / this.capacity;

        int flightTimeRemaining = Math.max(storedEnergy / (consumptionRate * 20), 0); // in seconds

        String flightTimeString;
        if (flightTimeRemaining > HOUR_IN_SECONDS) {
            flightTimeString = String.format("%dh", flightTimeRemaining / HOUR_IN_SECONDS);
        } else if (flightTimeRemaining > MINUTE_IN_SECONDS) {
            flightTimeString = String.format("%dm", flightTimeRemaining / MINUTE_IN_SECONDS);
        } else {
            flightTimeString = String.format("%ds", flightTimeRemaining);
        }

        return String.format("%dRF/t - %s", consumptionRate, flightTimeString);
    }

    /* ---------------------------------------------------------------- */
    /* ---------------------------------------------------------------- */
    /* ---------------------------------------------------------------- */

    @Override
    public void setDamage(ItemStack stack, int damage) {
        // Instead of actually damaging the item, we will just ignore this and
        // update the energy. _Probably_, only EtFuturum will call this method. In their
        // code, they tell the game to damage the item by 1. So.... we'll assume that's
        // the only case this method can get called :p
        this.consume(stack, FLIGHT_CONSUMPTION_RATE);
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return true; // Always show the damage bar, even when not actually damaged.
    }

    @SuppressWarnings({
            "rawtypes",
            "unchecked"
    })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(item, 1, 0), 0));
        list.add(EnergyHelper.setDefaultEnergyTag(new ItemStack(item, 1, 0), this.capacity));
    }

    @SuppressWarnings({
            "rawtypes",
            "unchecked"
    })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean check) {
        super.addInformation(stack, player, list, check);

        if (StringHelper.displayShiftForDetail && !StringHelper.isShiftKeyDown()) {
            list.add(StringHelper.shiftForDetails());
        }
        if (!StringHelper.isShiftKeyDown()) {
            return;
        }
        if (stack.stackTagCompound == null) {
            EnergyHelper.setDefaultEnergyTag(stack, 0);
        }
        list.add(StringHelper.localize("info.cofh.charge") + ": " + stack.stackTagCompound.getInteger("Energy") + " / " + this.capacity + " RF");
    }

    @Override
    public int getDisplayDamage(ItemStack container) {
        return this.getDamage(container);
    }

    @Override
    public int getDamage(ItemStack container) {
        return this.capacity - this.getEnergyStored(container);
    }

    @Override
    public int getMaxDamage() {
        return this.capacity;
    }

    /* ---------------------------------------------------------------- */
    /* ---------------------------------------------------------------- */
    /* ---------------------------------------------------------------- */

    private int consume(ItemStack container, int amount) {
        int stored = this.getEnergyStored(container);
        int toConsume = Math.min(stored, amount);

        int newStored = stored - toConsume;
        this.setEnergy(container, newStored);
        return newStored;
    }

    private void setEnergy(ItemStack container, int energy) {
        if (!container.hasTagCompound()) {
            container.setTagCompound(new NBTTagCompound());
        }
        container.getTagCompound().setInteger("Energy", energy);
    }

    /**
     * Adds energy to a container item. Returns the quantity of energy that was
     * accepted. This should always return 0 if the item cannot be externally
     * charged.
     *
     * @param  container  ItemStack to be charged.
     * @param  maxReceive Maximum amount of energy to be sent into the item.
     * @param  simulate   If TRUE, the charge will only be simulated.
     * 
     * @return            Amount of energy that was (or would have been, if
     *                    simulated) received by the item.
     */
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        int energy = this.getEnergyStored(container);
        int energyReceived = Math.min(this.capacity - energy, Math.min(this.maxReceive, maxReceive));

        if (!simulate) {
            energy += energyReceived;
            this.setEnergy(container, energy);
        }
        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        return 0; // Cannot be discharged.
    }

    /**
     * Get the amount of energy currently stored in the container item.
     */
    @Override
    public int getEnergyStored(ItemStack container) {
        if (container.getTagCompound() == null || !container.getTagCompound().hasKey("Energy")) {
            return 0;
        }
        return container.getTagCompound().getInteger("Energy");
    }

    /**
     * Get the max amount of energy that can be stored in the container item.
     */
    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return this.capacity;
    }

}

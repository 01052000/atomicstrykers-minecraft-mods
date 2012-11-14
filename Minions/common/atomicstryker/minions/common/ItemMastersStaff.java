package atomicstryker.minions.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Side;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumAction;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

/**
 * Minion control Item class. Nothing to see here really.
 * 
 * 
 * @author AtomicStryker
 */

public class ItemMastersStaff extends Item
{
	private long lastTime;
	private final long coolDown = 100L;
	
	public ItemMastersStaff(int var1)
    {
        super(var1);
        this.maxStackSize = 1;
        System.out.println("Minions Master Staff created! ID: "+shiftedIndex);
        
        this.setCreativeTab(CreativeTabs.tabCombat);
        setTextureFile("/atomicstryker/minions/client/textures/MinionItems.png");
        setIconIndex(0);
        
        lastTime = System.currentTimeMillis();
    }

	@Override
    public void onPlayerStoppedUsing(ItemStack var1, World var2, EntityPlayer var3, int ticksHeld)
    {
    	int ticksLeftFromMax = this.getMaxItemUseDuration(var1) - ticksHeld;
    	float pointStrength = (float)ticksLeftFromMax / 20.0F;
    	pointStrength = (pointStrength * pointStrength + pointStrength * 2.0F) / 3.0F;

    	if (System.currentTimeMillis() > lastTime+coolDown)
    	{
    		lastTime = System.currentTimeMillis();
        	if (pointStrength > 1.0F)
        	{
        		// full power!
        	    MinionsCore.proxy.OnMastersGloveRightClickHeld(var1, var2, var3);
        	}
        	else
        	{
        		// shorter tap
        	    MinionsCore.proxy.OnMastersGloveRightClick(var1, var2, var3);
        	}
    	}
    }

	@Override
    public ItemStack onFoodEaten(ItemStack var1, World var2, EntityPlayer var3)
    {
        return var1;
    }

	@Override
    public int getMaxItemUseDuration(ItemStack var1)
    {
        return 72000;
    }

	@Override
    public EnumAction getItemUseAction(ItemStack var1)
    {
        return EnumAction.block;
    }

	@Override
    public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3)
    {
    	var3.setItemInUse(var1, this.getMaxItemUseDuration(var1));
        return var1;
    }
}
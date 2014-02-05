package atomicstryker.ropesplus.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemRope extends Item
{
    
    public ItemRope(int i)
    {
        super(i);
        setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon("ropesplus:itemRope");
    }
    
}
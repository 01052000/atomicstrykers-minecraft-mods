package com.sirolf2009.necromancy.block;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import com.sirolf2009.necromancy.Necromancy;

public class BlockBlood extends BlockFluidClassic
{

    public BlockBlood(Fluid fluid)
    {
        super(fluid, Material.water);
        setCreativeTab(Necromancy.tabNecromancy);
    }

    @Override
    public IIcon getIcon(int side, int meta)
    {
        return Blocks.flowing_water.getIcon(side, meta);
    }

    @Override
    public int colorMultiplier(IBlockAccess iblockaccess, int x, int y, int z)
    {
        return 0x660000;
    }
}
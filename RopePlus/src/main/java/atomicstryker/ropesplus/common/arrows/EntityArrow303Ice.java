package atomicstryker.ropesplus.common.arrows;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityArrow303Ice extends EntityArrow303
{
    
    public EntityLivingBase victim;
    public float freezeFactor;
    public int freezeTimer;
    
    public EntityArrow303Ice(World world)
    {
        super(world);
    }

    public EntityArrow303Ice(World world, EntityLivingBase entityLivingBase, float power)
    {
        super(world, entityLivingBase, power);
    }

    @Override
    public void entityInit()
    {
        super.entityInit();
        name = "Frost Arrow";
        craftingResults = 4;
        tip = Items.snowball;
        item = new ItemStack(itemId, 1, 0);
        icon = "ropesplus:icearrow";
    }

    @Override
    public boolean onHitTarget(Entity entity)
    {
        if (!(entity instanceof EntityLivingBase) || victim != null)
        {
            return false;
        }
        
        @SuppressWarnings("rawtypes")
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, entity.boundingBox.expand(3D, 3D, 3D));
        for (int i = 0; i < list.size(); i++)
        {
            Entity entity1 = (Entity) list.get(i);
            if (!(entity1 instanceof EntityArrow303Ice))
            {
                continue;
            }
            EntityArrow303Ice entityarrow303ice = (EntityArrow303Ice) entity1;
            if (entityarrow303ice.victim == entity)
            {
                entityarrow303ice.freezeTimer += getFreezeTimer((EntityLivingBase) entity);
                entityarrow303ice.setDead();
                entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) shooter), 4);
                return super.onHitTarget(entity);
            }
        }

        entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) shooter), 4);
        freezeMob((EntityLivingBase) entity);
        return super.onHitTarget(entity);
    }

    private int getFreezeTimer(EntityLivingBase EntityLivingBase)
    {
        return ((EntityLivingBase instanceof EntityPlayer) ? 5 : 10 * 20);
    }

    private void freezeMob(EntityLivingBase EntityLivingBase)
    {
        victim = EntityLivingBase;
        freezeFactor = ((EntityLivingBase instanceof EntityPlayer) ? 0.5F : 0.1F);
        freezeTimer = getFreezeTimer(EntityLivingBase);
        motionX = motionY = motionZ = 0.0D;
    }

    private void unfreezeMob()
    {
        victim = null;
    }

    @Override
    public void setDead()
    {
        if (victim != null)
        {
            unfreezeMob();
        }
        super.setDead();
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (victim != null)
        {
            if (victim.isDead || victim.deathTime > 0)
            {
                setDead();
                return;
            }
            isDead = false;
            inGround = false;
            posX = victim.posX;
            posY = victim.boundingBox.minY + (double) victim.height * 0.5D;
            posZ = victim.posZ;
            setPosition(posX, posY, posZ);
            victim.motionX *= freezeFactor;
            victim.motionY *= freezeFactor;
            victim.motionZ *= freezeFactor;
            freezeTimer--;
            if (freezeTimer <= 0)
            {
                setDead();
            }
        }
    }

    @Override
    public boolean onHitBlock(int curX, int curY, int curZ)
    {
        for (int iX = curX - 1; iX <= curX + 1; iX++)
        {
            for (int iY = curY - 1; iY <= curY + 1; iY++)
            {
                for (int iZ = curZ - 1; iZ <= curZ + 1; iZ++)
                {
                    Block b = worldObj.getBlock(iX, iY, iZ);
                    if (b.getMaterial() == Material.water && worldObj.getBlockMetadata(iX, iY, iZ) == 0)
                    {
                        worldObj.setBlock(iX, iY, iZ, Blocks.ice, 0, 3);
                        continue;
                    }
                    if (b.getMaterial() == Material.lava && worldObj.getBlockMetadata(iX, iY, iZ) == 0)
                    {
                        worldObj.setBlock(iX, iY, iZ, Blocks.cobblestone, 0, 3);
                        continue;
                    }
                    if (b == Blocks.fire)
                    {
                        worldObj.setBlock(iX, iY, iZ, Blocks.air, 0, 3);
                        continue;
                    }
                    if (b == Blocks.torch)
                    {
                        b.func_149690_a(worldObj, iX, iY, iZ, worldObj.getBlockMetadata(iX, iY, iZ), 1.0F, 0);
                        worldObj.setBlock(iX, iY, iZ, Blocks.air, 0, 3);
                    }
                }
            }
        }

        return super.onHitBlock(curX, curY, curZ);
    }
    
    @Override
    public void tickFlying()
    {
        super.tickFlying();
        
        for (int i = 0; i < 4; ++i)
        {
            this.worldObj.spawnParticle("snowballpoof",
                    this.posX + this.motionX * (double) i / 4.0D,
                    this.posY + this.motionY * (double) i / 4.0D,
                    this.posZ + this.motionZ * (double) i / 4.0D,
                    -this.motionX, -this.motionY + 0.2D, -this.motionZ);
        }
    }
    
    @Override
    public IProjectile getProjectileEntity(World par1World, IPosition par2IPosition)
    {
        EntityArrow303Ice entityarrow = new EntityArrow303Ice(par1World);
        entityarrow.setPosition(par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
        return entityarrow;
    }

}
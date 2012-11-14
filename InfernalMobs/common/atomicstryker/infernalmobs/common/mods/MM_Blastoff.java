package atomicstryker.infernalmobs.common.mods;

import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import atomicstryker.infernalmobs.common.InfernalMobsCore;
import atomicstryker.infernalmobs.common.MobModifier;

public class MM_Blastoff extends MobModifier
{
    public MM_Blastoff(EntityLiving mob)
    {
        this.mob = mob;
        this.modName = "Blastoff";
    }
    
    public MM_Blastoff(EntityLiving mob, MobModifier prevMod)
    {
        this.mob = mob;
        this.modName = "Blastoff";
        this.nextMod = prevMod;
    }
    
    private long lastAbilityUse = 0L;
    private final static long coolDown = 15000L;
    
    @Override
    public boolean onUpdate()
    {
        if (mob.getAttackTarget() != null
        && mob.getAttackTarget() instanceof EntityPlayer)
        {
            tryAbility(mob.getAttackTarget());
        }
        
        return super.onUpdate();
    }
    
    @Override
    public int onHurt(DamageSource source, int damage)
    {
        if (source.getEntity() != null
        && source.getEntity() instanceof EntityLiving)
        {
            tryAbility((EntityLiving) source.getEntity());
        }
        
        return super.onHurt(source, damage);
    }

    private void tryAbility(EntityLiving target)
    {
        long time = System.currentTimeMillis();
        if (time > lastAbilityUse+coolDown)
        {
            lastAbilityUse = time;
            mob.worldObj.playSoundAtEntity(mob, "mob.slimeattack", 1.0F, (mob.worldObj.rand.nextFloat() - mob.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
            
            if (target.worldObj.isRemote || !(target instanceof EntityPlayer))
            {
                target.addVelocity(0, 1.1D, 0);
            }
            else
            {
                InfernalMobsCore.instance().sendVelocityPacket((EntityPlayer)target, 0D, 1.1D, 0D);
            }
        }
    }
    
    @Override
    public Class[] getModsNotToMixWith()
    {
        return modBans;
    }
    private static Class[] modBans = { MM_Webber.class };
}
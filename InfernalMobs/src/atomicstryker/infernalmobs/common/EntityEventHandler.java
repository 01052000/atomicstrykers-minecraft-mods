package atomicstryker.infernalmobs.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;

public class EntityEventHandler
{
    /**
     * Links the Forge Event Handler to the registered Entity MobModifier Events (if present)
     */

    @ForgeSubscribe
    public void onEntityJoinedWorld (EntityJoinWorldEvent event)
    {
        if (event.entity instanceof EntityLiving)
        {
            String savedMods = event.entity.getEntityData().getString(InfernalMobsCore.instance().getNBTTag());
            if (!savedMods.equals(""))
            {
                InfernalMobsCore.instance().addEntityModifiersByString((EntityLiving) event.entity, savedMods);
            }
            else
            {
                InfernalMobsCore.instance().processEntitySpawn((EntityLiving) event.entity);
            }
        }
    }

    @ForgeSubscribe
    public void onEntityLivingDeath(LivingDeathEvent event)
    {
        if (!event.entity.worldObj.isRemote)
        {
            MobModifier mod = InfernalMobsCore.getMobModifiers(event.entityLiving);
            if (mod != null)
            {
                if(mod.onDeath())
                {
                    event.setCanceled(true);
                }
            }
        }
    }

    @ForgeSubscribe
    public void onEntityLivingSetAttackTarget(LivingSetAttackTargetEvent event)
    {
        if (!event.entity.worldObj.isRemote)
        {
            MobModifier mod = InfernalMobsCore.getMobModifiers(event.entityLiving);
            if (mod != null)
            {
                mod.onSetAttackTarget(event.target);
            }
        }
    }

    @ForgeSubscribe
    public void onEntityLivingAttacked(LivingAttackEvent event)
    {
        /* fires both client and server before hurt, but we dont need this */
    }
    
    @ForgeSubscribe
    public void onEntityLivingHurt(LivingHurtEvent event)
    {
        MobModifier mod = InfernalMobsCore.getMobModifiers(event.entityLiving);
        if (mod != null)
        {
            event.ammount = mod.onHurt(event.entityLiving, event.source, event.ammount);
        }

        /*
         * We use the Hook two-sided, both with the Mob as possible target and attacker
         */
        Entity attacker = event.source.getEntity();
        if (attacker != null
        && attacker instanceof EntityLiving)
        {
            mod = InfernalMobsCore.getMobModifiers((EntityLiving) attacker);
            if (mod != null)
            {
                event.ammount = mod.onAttack(event.entityLiving, event.source, event.ammount);
            }
        }
    }

    @ForgeSubscribe
    public void onEntityLivingFall(LivingFallEvent event)
    {
        if (!event.entity.worldObj.isRemote)
        {
            MobModifier mod = InfernalMobsCore.getMobModifiers(event.entityLiving);
            if (mod != null)
            {
                event.setCanceled(mod.onFall(event.distance));
            }
        }
    }
    
    @ForgeSubscribe
    public void onEntityLivingJump(LivingEvent.LivingJumpEvent event)
    {
        if (!event.entity.worldObj.isRemote)
        {
            MobModifier mod = InfernalMobsCore.getMobModifiers(event.entityLiving);
            if (mod != null)
            {
                mod.onJump(event.entityLiving);
            }
        }
    }

    @ForgeSubscribe
    public void onEntityLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        if (!event.entityLiving.worldObj.isRemote)
        {
            MobModifier mod = InfernalMobsCore.getMobModifiers(event.entityLiving);
            if (mod != null)
            {
                mod.onUpdate(event.entityLiving);
            }
        }
    }

    @ForgeSubscribe
    public void onEntityLivingDrops(LivingDropsEvent event)
    {
        if (!event.entity.worldObj.isRemote)
        {
            MobModifier mod = InfernalMobsCore.getMobModifiers(event.entityLiving);
            if (mod != null)
            {
                mod.onDropItems(event.entityLiving, event.source, event.drops, event.lootingLevel, event.recentlyHit, event.specialDropValue);
                InfernalMobsCore.removeEntFromElites(event.entityLiving);
            }
        }
    }
}
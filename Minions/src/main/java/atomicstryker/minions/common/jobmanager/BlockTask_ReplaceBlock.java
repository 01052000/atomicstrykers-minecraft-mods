package atomicstryker.minions.common.jobmanager;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;
import atomicstryker.minions.common.entity.EntityMinion;

/**
 * Blocktask for mining a single Block, then replacing it with another
 * 
 * 
 * @author AtomicStryker
 */

public class BlockTask_ReplaceBlock extends BlockTask_MineBlock
{
	public final Block blockToPlace;
	public final int metaToPlace;
	
    public BlockTask_ReplaceBlock(Minion_Job_Manager boss, EntityMinion input, int ix, int iy, int iz, Block blockOrdered, int metaOrdered)
    {
    	super(boss, input, ix, iy, iz);
    	blockToPlace = blockOrdered;
    	metaToPlace = metaOrdered;
    }
    
    public void onFinishedTask()
    {
    	super.onFinishedTask();
    	
    	BlockEvent.BreakEvent event = ForgeHooks.onBlockBreakEvent(worker.worldObj, worker.worldObj.getWorldInfo().getGameType(), (EntityPlayerMP) worker.master, posX, posY, posZ);
        if (!event.isCanceled())
        {
            worker.worldObj.setBlock(posX, posY, posZ, blockToPlace, metaToPlace, 3);
        }
    }
}
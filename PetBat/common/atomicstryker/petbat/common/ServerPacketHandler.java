package atomicstryker.petbat.common;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import atomicstryker.ForgePacketWrapper;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ServerPacketHandler implements IPacketHandler
{

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
        int packetType = ForgePacketWrapper.readPacketID(data);
        
        if (packetType == 1)
        {
            Class[] decodeAs = {String.class};
            Object[] packetReadout = ForgePacketWrapper.readPacketData(data, decodeAs);
            
            String batName = (String) packetReadout[0];
            EntityPlayer p = (EntityPlayer) player;
            
            if (p.getCurrentEquippedItem() != null
            && p.getCurrentEquippedItem().itemID == PetBatMod.instance().itemPocketedBat.shiftedIndex)
            {
                ItemPocketedPetBat.writeBatNameToItemStack(p.getCurrentEquippedItem(), batName);
            }
        }
    }

}
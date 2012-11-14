package atomicstryker.multimine.common.fmlmagic;

import java.util.Iterator;

import static org.objectweb.asm.Opcodes.*;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.PlayerControllerMP;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import cpw.mods.fml.relauncher.IClassTransformer;

public class MMTransformer implements IClassTransformer
{
    /* Obfuscated Names for NetServerHandler Transformation */
    
    /* net/minecraft/src/Packet14BlockDig */
    private final String packet14BlockDigNameO = "ej";
    /* net.minecraft.src.NetServerHandler */
    private final String netServerHandlerClassNameO = "iv";
    /* net/minecraft/src/NetServerHandler */
    private final String netServerHandlerJavaClassNameO = "iv";
    /* handleBlockDig */
    private final String netServerHandlertargetMethodNameO = "a";
    /* playerEntity */
    private final String netServerHandlerEntIDFieldNameO = "d";
    /* net/minecraft/src/EntityPlayerMP */
    private final String entityPlayerMPJavaClassNameO = "iq";
    /* entityId */
    private final String entityPlayerMPEntIDFieldNameO = "k";
    
    
    /* Obfuscated Names for PlayerControllerMP Transformation */
    
    /* net.minecraft.src.PlayerControllerMP */
    private final String playerControllerMPClassNameO = "ayg";
    /* net/minecraft/src/NetServerHandler */
    private final String playerControllerMPJavaClassNameO = "ayg";
    /* onPlayerDamageBlock */
    private final String playerControllerMPtargetMethodNameO = "c";
    /* currentBlockX */
    private final String playerControllerMPcurrentBlockXFieldNameO = "c";
    /* currentBlockY */
    private final String playerControllerMPcurrentBlockYFieldNameO = "d";
    /* currentBlockZ */
    private final String playerControllerMPcurrentBlockZFieldNameO = "e";
    /* curBlockDamageMP */
    private final String playerControllerMPcurrentBlockDamageFieldNameO = "g";
    
    
    /* MCP Names for PlayerControllerMP Transformation */
    private final String playerControllerMPClassName = "net.minecraft.src.PlayerControllerMP";
    private final String playerControllerMPJavaClassName = "net/minecraft/src/PlayerControllerMP";
    private final String playerControllerMPtargetMethodName = "onPlayerDamageBlock";
    private final String playerControllerMPcurrentBlockXFieldName = "currentBlockX";
    private final String playerControllerMPcurrentBlockYFieldName = "currentBlockY";
    private final String playerControllerMPcurrentBlockZFieldName = "currentblockZ";
    private final String playerControllerMPcurrentBlockDamageFieldName = "curBlockDamageMP";
    
    /* MCP Names for NetServerHandler Transformation */
    private final String packet14BlockDigName = "net/minecraft/src/Packet14BlockDig";
    private final String netServerHandlerClassName = "net.minecraft.src.NetServerHandler";
    private final String netServerHandlerJavaClassName = "net/minecraft/src/NetServerHandler";
    private final String netServerHandlertargetMethodName = "handleBlockDig";
    private final String netServerHandlerEntIDFieldName = "playerEntity";
    private final String entityPlayerMPJavaClassName = "net/minecraft/src/EntityPlayerMP";
    private final String entityPlayerMPEntIDFieldName = "entityId";
    
    @Override
    public byte[] transform(String name, byte[] bytes)
    {
        //System.out.println("transforming: "+name);
        if (name.equals(netServerHandlerClassNameO))
        {
            return handleNetServerHandlerObfuscated(bytes);
        }
        else if (name.equals(playerControllerMPClassNameO))
        {
            return handlePlayerControllerMPObfuscated(bytes);
        }
        else if (name.equals(netServerHandlerClassName))
        {
            return handleNetServerHandler(bytes);
        }
        else if (name.equals(playerControllerMPClassName))
        {
            return handlePlayerControllerMP(bytes);
        }
        
        return bytes;
    }
    
    private byte[] handlePlayerControllerMPObfuscated(byte[] bytes)
    {
        System.out.println("**************** Multi Mine transform running on PlayerControllerMP *********************** ");
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        // find method to inject into
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
            MethodNode m = methods.next();
            if (m.name.equals(playerControllerMPtargetMethodNameO) && m.desc.equals("(IIII)V"))
            {
                System.out.println("In target method! Patching!");
                
                // find injection point in method, there is a single IFLT instruction we use as target
                for (int index = 0; index < m.instructions.size(); index++)
                {
                    // find block ID local variable node and from that, local variable index
                    int blockIDvar = 5;
                    if (m.instructions.get(index).getType() == AbstractInsnNode.VAR_INSN
                    && m.instructions.get(index).getOpcode() == ISTORE)
                    {
                        System.out.println("Found local variable ISTORE Node at "+index);
                        VarInsnNode blockIDNode = (VarInsnNode)m.instructions.get(index);
                        blockIDvar = blockIDNode.var;
                        System.out.println("Block ID is in local variable "+blockIDvar);
                    }
                    
                    if (m.instructions.get(index).getOpcode() == IFLT)
                    {
                        System.out.println("Found IFLT Node at "+index);
                        
                        int offset = 1;
                        while (m.instructions.get(index-offset).getOpcode() != ALOAD)
                        {
                            offset++;
                        }
                        
                        System.out.println("Found ALOAD Node at offset -"+offset+" from IFLT Node");
                        
                        // make an exit label node
                        LabelNode lmm1Node = new LabelNode(new Label());
                        
                        // make new instruction list
                        InsnList toInject = new InsnList();
                        
                        // construct instruction nodes for list
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new FieldInsnNode(GETFIELD, playerControllerMPJavaClassNameO, playerControllerMPcurrentBlockDamageFieldNameO, "F"));
                        toInject.add(new LdcInsnNode(0.1F));
                        toInject.add(new InsnNode(FCMPL));
                        toInject.add(new JumpInsnNode(IFLT, lmm1Node));
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new FieldInsnNode(GETFIELD, playerControllerMPJavaClassNameO, playerControllerMPcurrentBlockDamageFieldNameO, "F"));
                        toInject.add(new LdcInsnNode(0.5F));
                        toInject.add(new InsnNode(FCMPG));
                        toInject.add(new JumpInsnNode(IFGE, lmm1Node));
                        toInject.add(new MethodInsnNode(INVOKESTATIC, "atomicstryker/multimine/client/MultiMineClient", "instance", "()Latomicstryker/multimine/client/MultiMineClient;"));
                        toInject.add(new VarInsnNode(ILOAD, blockIDvar));
                        toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "atomicstryker/multimine/client/MultiMineClient", "getIsEnabledForServerAndBlockId", "(I)Z"));
                        toInject.add(new JumpInsnNode(IFEQ, lmm1Node));
                        toInject.add(new MethodInsnNode(INVOKESTATIC, "atomicstryker/multimine/client/MultiMineClient", "instance", "()Latomicstryker/multimine/client/MultiMineClient;"));
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new FieldInsnNode(GETFIELD, playerControllerMPJavaClassNameO, playerControllerMPcurrentBlockXFieldNameO, "I"));
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new FieldInsnNode(GETFIELD, playerControllerMPJavaClassNameO, playerControllerMPcurrentBlockYFieldNameO, "I"));
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new FieldInsnNode(GETFIELD, playerControllerMPJavaClassNameO, playerControllerMPcurrentBlockZFieldNameO, "I"));
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new FieldInsnNode(GETFIELD, playerControllerMPJavaClassNameO, playerControllerMPcurrentBlockDamageFieldNameO, "F"));
                        toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "atomicstryker/multimine/client/MultiMineClient", "onClientMinedBlockTenthCompleted", "(IIIF)V"));
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new InsnNode(FCONST_0));
                        toInject.add(new FieldInsnNode(PUTFIELD, playerControllerMPJavaClassNameO, playerControllerMPcurrentBlockDamageFieldNameO, "F"));
                        toInject.add(new InsnNode(RETURN));
                        toInject.add(lmm1Node);
                        
                        m.instructions.insertBefore(m.instructions.get(index-offset), toInject);
                        System.out.println("Patching Complete!");
                        break;
                    }
                }
            }
        }
        
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }
    
    private byte[] handleNetServerHandlerObfuscated(byte[] bytes)
    {
        System.out.println("**************** Multi Mine transform running on NetServerHandler *********************** ");
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        // find method to inject into
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
            MethodNode m = methods.next();
            if (m.name.equals(netServerHandlertargetMethodNameO) && m.desc.equals("(L"+packet14BlockDigNameO+";)V"))
            {
                System.out.println("In target method! Patching!");
                
                // make an exit label node
                LabelNode lmm2Node = new LabelNode(new Label());
                
                // make new instruction list
                InsnList toInject = new InsnList();
                
                // construct instruction nodes for list
                toInject.add(new MethodInsnNode(INVOKESTATIC, "atomicstryker/multimine/common/MultiMineServer", "instance", "()Latomicstryker/multimine/common/MultiMineServer;"));
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new FieldInsnNode(GETFIELD, netServerHandlerJavaClassNameO, netServerHandlerEntIDFieldNameO, "L"+entityPlayerMPJavaClassNameO+";"));
                toInject.add(new FieldInsnNode(GETFIELD, entityPlayerMPJavaClassNameO, entityPlayerMPEntIDFieldNameO, "I"));
                toInject.add(new VarInsnNode(ALOAD, 1));
                toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "atomicstryker/multimine/common/MultiMineServer", "getShouldIgnoreBlockDigPacket", "(IL"+packet14BlockDigNameO+";)Z"));
                toInject.add(new JumpInsnNode(IFEQ, lmm2Node));
                toInject.add(new InsnNode(RETURN));
                toInject.add(lmm2Node);
                
                // inject new instruction list into method instruction list
                m.instructions.insert(toInject);
                
                System.out.println("Patching Complete!");
                break;
            }
        }
        
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }
    
    private byte[] handlePlayerControllerMP(byte[] bytes)
    {
        System.out.println("**************** Multi Mine transform running on PlayerControllerMP *********************** ");
        
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        // find method to inject into
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
            MethodNode m = methods.next();
            if (m.name.equals(playerControllerMPtargetMethodName) && m.desc.equals("(IIII)V"))
            {
                System.out.println("In target method! Patching!");
                
                // find injection point in method, there is a single IFLT instruction we use as target
                for (int index = 0; index < m.instructions.size(); index++)
                {
                    // find block ID local variable node and from that, local variable index
                    int blockIDvar = 5;
                    if (m.instructions.get(index).getType() == AbstractInsnNode.VAR_INSN
                    && m.instructions.get(index).getOpcode() == ISTORE)
                    {
                        System.out.println("Found local variable ISTORE Node at "+index);
                        VarInsnNode blockIDNode = (VarInsnNode)m.instructions.get(index);
                        blockIDvar = blockIDNode.var;
                        System.out.println("Block ID is in local variable "+blockIDvar);
                    }
                    
                    if (m.instructions.get(index).getOpcode() == IFLT)
                    {
                        System.out.println("Found IFLT Node at "+index);
                        
                        int offset = 1;
                        while (m.instructions.get(index-offset).getOpcode() != ALOAD)
                        {
                            offset++;
                        }
                        
                        System.out.println("Found ALOAD Node at offset -"+offset+" from IFLT Node");
                        
                        // make an exit label node
                        LabelNode lmm1Node = new LabelNode(new Label());
                        
                        // make new instruction list
                        InsnList toInject = new InsnList();
                        
                        // construct instruction nodes for list
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new FieldInsnNode(GETFIELD, playerControllerMPJavaClassName, playerControllerMPcurrentBlockDamageFieldName, "F"));
                        toInject.add(new LdcInsnNode(0.1F));
                        toInject.add(new InsnNode(FCMPL));
                        toInject.add(new JumpInsnNode(IFLT, lmm1Node));
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new FieldInsnNode(GETFIELD, playerControllerMPJavaClassName, playerControllerMPcurrentBlockDamageFieldName, "F"));
                        toInject.add(new LdcInsnNode(0.5F));
                        toInject.add(new InsnNode(FCMPG));
                        toInject.add(new JumpInsnNode(IFGE, lmm1Node));
                        toInject.add(new MethodInsnNode(INVOKESTATIC, "atomicstryker/multimine/client/MultiMineClient", "instance", "()Latomicstryker/multimine/client/MultiMineClient;"));
                        toInject.add(new VarInsnNode(ILOAD, blockIDvar));
                        toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "atomicstryker/multimine/client/MultiMineClient", "getIsEnabledForServerAndBlockId", "(I)Z"));
                        toInject.add(new JumpInsnNode(IFEQ, lmm1Node));
                        toInject.add(new MethodInsnNode(INVOKESTATIC, "atomicstryker/multimine/client/MultiMineClient", "instance", "()Latomicstryker/multimine/client/MultiMineClient;"));
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new FieldInsnNode(GETFIELD, playerControllerMPJavaClassName, playerControllerMPcurrentBlockXFieldName, "I"));
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new FieldInsnNode(GETFIELD, playerControllerMPJavaClassName, playerControllerMPcurrentBlockYFieldName, "I"));
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new FieldInsnNode(GETFIELD, playerControllerMPJavaClassName, playerControllerMPcurrentBlockZFieldName, "I"));
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new FieldInsnNode(GETFIELD, playerControllerMPJavaClassName, playerControllerMPcurrentBlockDamageFieldName, "F"));
                        toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "atomicstryker/multimine/client/MultiMineClient", "onClientMinedBlockTenthCompleted", "(IIIF)V"));
                        toInject.add(new VarInsnNode(ALOAD, 0));
                        toInject.add(new InsnNode(FCONST_0));
                        toInject.add(new FieldInsnNode(PUTFIELD, playerControllerMPJavaClassName, playerControllerMPcurrentBlockDamageFieldName, "F"));
                        toInject.add(new InsnNode(RETURN));
                        toInject.add(lmm1Node);
                        
                        m.instructions.insertBefore(m.instructions.get(index-offset), toInject);
                        System.out.println("Patching Complete!");
                        break;
                    }
                }
            }
        }
        
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }
    
    private byte[] handleNetServerHandler(byte[] bytes)
    {
        System.out.println("**************** Multi Mine transform running on NetServerHandler *********************** ");
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        
        // find method to inject into
        Iterator<MethodNode> methods = classNode.methods.iterator();
        while(methods.hasNext())
        {
            MethodNode m = methods.next();
            if (m.name.equals(netServerHandlertargetMethodName) && m.desc.equals("(L"+packet14BlockDigName+";)V"))
            {
                System.out.println("In target method! Patching!");
                
                // make an exit label node
                LabelNode lmm2Node = new LabelNode(new Label());
                
                // make new instruction list
                InsnList toInject = new InsnList();
                
                // construct instruction nodes for list
                toInject.add(new MethodInsnNode(INVOKESTATIC, "atomicstryker/multimine/common/MultiMineServer", "instance", "()Latomicstryker/multimine/common/MultiMineServer;"));
                toInject.add(new VarInsnNode(ALOAD, 0));
                toInject.add(new FieldInsnNode(GETFIELD, netServerHandlerJavaClassName, netServerHandlerEntIDFieldName, "L"+entityPlayerMPJavaClassName+";"));
                toInject.add(new FieldInsnNode(GETFIELD, entityPlayerMPJavaClassName, entityPlayerMPEntIDFieldName, "I"));
                toInject.add(new VarInsnNode(ALOAD, 1));
                toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "atomicstryker/multimine/common/MultiMineServer", "getShouldIgnoreBlockDigPacket", "(IL"+packet14BlockDigName+";)Z"));
                toInject.add(new JumpInsnNode(IFEQ, lmm2Node));
                toInject.add(new InsnNode(RETURN));
                toInject.add(lmm2Node);
                
                // inject new instruction list into method instruction list
                m.instructions.insert(toInject);
                
                System.out.println("Patching Complete!");
                break;
            }
        }
        
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}
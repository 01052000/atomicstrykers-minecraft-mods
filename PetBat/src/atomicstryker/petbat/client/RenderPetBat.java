package atomicstryker.petbat.client;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import atomicstryker.petbat.common.EntityPetBat;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPetBat extends RenderLiving
{
    private ModelPetBat renderModel;
    private ResourceLocation tex = new ResourceLocation("petbat", "textures/model/petbat.png");
    private ResourceLocation texGlis = new ResourceLocation("petbat", "textures/model/petbat_glister.png");
    
    public RenderPetBat()
    {
        super(new ModelPetBat(), 0.25F);
        renderModel = new ModelPetBat();
    }
    
    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    @Override
    protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2)
    {
        this.scaleBat((EntityPetBat)par1EntityLiving, par2);
    }

    @Override
    protected void rotateCorpse(EntityLivingBase par1EntityLiving, float par2, float par3, float par4)
    {
        this.rotateRenderedModel((EntityPetBat)par1EntityLiving, par2, par3, par4);
    }
    
    /**
     * Passes the specialRender and renders it
     */
    @Override
    protected void func_130008_a(EntityLivingBase par1EntityLiving, double par2, double par4, double par6)
    {
        String name = ((EntityPetBat)par1EntityLiving).getDisplayName();
        if (!name.equals(""))
        {
            renderLivingLabel(par1EntityLiving, name, par2, par4-1D, par6, 64);
        }
        super.func_130008_a(par1EntityLiving, par2, par4, par6);
    }
    
    @Override
    protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3)
    {
        if (par2 == 2 && ((EntityPetBat)par1EntityLiving).getBatLevel() > 5)
        {
            setRenderPassModel(renderModel);
            return 15;
        }
        
        return -1;
    }

    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    @Override
    protected void renderLivingAt(EntityLivingBase par1EntityLiving, double par2, double par4, double par6)
    {
        this.renderPetBatAt((EntityPetBat)par1EntityLiving, par2, par4, par6);
    }

    @Override
    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderBat((EntityPetBat)par1EntityLiving, par2, par4, par6, par8, par9);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderBat((EntityPetBat)par1Entity, par2, par4, par6, par8, par9);
    }

    private void renderBat(EntityPetBat par1EntityPetBat, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRenderLiving(par1EntityPetBat, par2, par4, par6, par8, par9);
    }

    private void scaleBat(EntityPetBat par1EntityPetBat, float par2)
    {
        GL11.glScalef(0.35F, 0.35F, 0.35F);
    }

    private void renderPetBatAt(EntityPetBat par1EntityPetBat, double par2, double par4, double par6)
    {
        super.renderLivingAt(par1EntityPetBat, par2, par4, par6);
    }

    private void rotateRenderedModel(EntityPetBat par1EntityPetBat, float par2, float par3, float par4)
    {
        if (!par1EntityPetBat.getIsBatHanging())
        {
            GL11.glTranslatef(0.0F, MathHelper.cos(par2 * 0.3F) * 0.1F, 0.0F);
        }
        else
        {
            GL11.glTranslatef(0.0F, -0.1F, 0.0F);
        }

        super.rotateCorpse(par1EntityPetBat, par2, par3, par4);
    }

    @Override
    protected ResourceLocation func_110775_a(Entity entity)
    {
        return ((EntityPetBat)entity).glister ? texGlis : tex;
    }
}
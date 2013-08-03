package net.minecraft.src;

import java.util.Random;
/*//FMLdelete
import net.minecraft.entity.*;
import net.minecraft.item.*;
*///FMLdelete
public class ItemResurrectionFeather extends Item {
	private Random rand;

	public ItemResurrectionFeather(int par1) {
		super(par1);
		this.setMaxStackSize(1);
		rand = new Random();
	}

	@Override
	public int getColorFromItemStack(ItemStack itemstack, int par1) {
		return 0x22ffff8c;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack, EntityLiving entity) {
		if (whiteListCheck(entity)
				&& !ngListCheck(entity)) {
				EntityCreature entityCreature = (EntityCreature) entity;
			if (entityCreature.health <= 0) {
				entityCreature.setEntityHealth(entityCreature.getMaxHealth());
				entityCreature.isDead = false;
				entityCreature.deathTime = 0;
				--itemstack.stackSize;
				for (int var1 = 0; var1 < 20; ++var1)
				{
					double var2 = this.rand.nextGaussian() * 1.0D;
					double var4 = this.rand.nextGaussian() * 1.0D;
					double var6 = this.rand.nextGaussian() * 1.0D;
					entityCreature.worldObj.spawnParticle("instantSpell", entityCreature.posX + (double)(this.rand.nextFloat() * entityCreature.width * 2.0F) - (double)entityCreature.width, entityCreature.posY + 0.5D + (double)(this.rand.nextFloat() * entityCreature.height), entityCreature.posZ + (double)(this.rand.nextFloat() * entityCreature.width * 2.0F) - (double)entityCreature.width, var2, var4, var6);
				}
			}
		}
		return true;
	}

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack var1, EntityLiving var2, EntityLiving var3)
    {
        var2.heal(1);
        this.itemInteractionForEntity(var1, var2);
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
    	entityplayer.swingItem();
    	return itemstack;
    }

	public String getClassName(String s) {
		if (s == null) return null;
		Package pac = getClass().getPackage();
		if (pac != null) s = pac.getName().concat(".").concat(s);
		return s;
	}

	public boolean whiteListCheck(EntityLiving entity) {
		boolean flag = false;
		for (int i = 0 ; i < mod_ResurrectionFeather.whiteList.size() && !flag ; i++) {
			String s = (String)mod_ResurrectionFeather.whiteList.get(i);
			try {
				Class c = Class.forName(getClassName(s));
				if (c.isInstance(entity)) flag = true;
			} catch (ClassNotFoundException e) {
				if (entity.texture.indexOf(s.concat(".")) != -1) flag = true;
			}
		}
		return flag;
	}

	public boolean ngListCheck(EntityLiving entity) {
		boolean flag = false;
		for (int i = 0 ; i < mod_ResurrectionFeather.ngList.size() && !flag ; i++) {
			String s = (String)mod_ResurrectionFeather.ngList.get(i);
			try {
				Class c = Class.forName(getClassName(s));
				if (c.isInstance(entity)) flag = true;
			} catch (ClassNotFoundException e) {
				if (entity.texture.indexOf(s.concat(".")) != -1) flag = true;
			}
		}
		return flag;
	}
}
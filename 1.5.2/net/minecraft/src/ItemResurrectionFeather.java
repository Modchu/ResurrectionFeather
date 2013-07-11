package net.minecraft.src;

import java.util.Random;

public class ItemResurrectionFeather extends Item {
	private Random rand;

	public ItemResurrectionFeather(int par1) {
		super(par1);
		setMaxStackSize(1);
		rand = new Random();
	}

	@Override
	public int getColorFromItemStack(ItemStack itemstack, int par1) {
		return mod_ResurrectionFeather.useOriginalIcon ? 16777215 : 0x22ffff8c;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack, EntityLiving entity) {
		if (whiteListCheck(entity)
				&& !ngListCheck(entity)) {
				EntityCreature entityCreature = (EntityCreature) entity;
			if (entityCreature.getHealth() <= 0) {
				entityCreature.setEntityHealth(entityCreature.getMaxHealth());
				entityCreature.isDead = false;
				entityCreature.deathTime = 0;
				--itemstack.stackSize;
				for (int var1 = 0; var1 < 20; ++var1)
				{
					double var2 = rand.nextGaussian() * 1.0D;
					double var4 = rand.nextGaussian() * 1.0D;
					double var6 = rand.nextGaussian() * 1.0D;
					entityCreature.worldObj.spawnParticle("instantSpell", entityCreature.posX + (double)(rand.nextFloat() * entityCreature.width * 2.0F) - (double)entityCreature.width, entityCreature.posY + 0.5D + (double)(rand.nextFloat() * entityCreature.height), entityCreature.posZ + (double)(rand.nextFloat() * entityCreature.width * 2.0F) - (double)entityCreature.width, var2, var4, var6);
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
        itemInteractionForEntity(var1, var2);
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

	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		itemIcon = par1IconRegister.registerIcon(mod_ResurrectionFeather.useOriginalIcon ? mod_ResurrectionFeather.itemName : "feather");
	}
}
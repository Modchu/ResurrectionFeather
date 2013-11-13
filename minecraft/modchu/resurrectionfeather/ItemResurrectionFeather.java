package modchu.resurrectionfeather;import java.util.Random;import modchu.resurrectionfeathercore.ResurrectionFeatherEntityCreature;import net.minecraft.client.renderer.texture.IconRegister;import net.minecraft.entity.EntityLivingBase;import net.minecraft.entity.player.EntityPlayer;import net.minecraft.item.Item;import net.minecraft.item.ItemStack;import net.minecraft.world.World;public class ItemResurrectionFeather extends Item {	private Random rand;	public ItemResurrectionFeather(int par1) {		super(par1);		setMaxStackSize(1);		rand = new Random();	}	@Override	public int getColorFromItemStack(ItemStack itemstack, int par1) {		return ResurrectionFeather.useOriginalIcon ? 16777215 : 0x22ffff8c;	}	@Override	public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer entityPlayer, EntityLivingBase entity) {		if (ResurrectionFeatherEntityCreature.resurrectionEntity(entity)) --itemstack.stackSize;		return true;	}	@Override	public boolean hitEntity(ItemStack var1, EntityLivingBase var2, EntityLivingBase var3) {		var2.heal(1);		return var2 instanceof EntityPlayer ? itemInteractionForEntity(var1, (EntityPlayer) var2, var3) : itemInteractionForEntity(var1, null, var3);	}	@Override	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {		if (ResurrectionFeatherEntityCreature.allResurrectionTime < 1				&& ResurrectionFeatherEntityCreature.allResurrectionWaitTime < 1) {			entityplayer.swingItem();			ResurrectionFeatherEntityCreature.item = this;			ResurrectionFeatherEntityCreature.itemstack = itemstack;			ResurrectionFeatherEntityCreature.allResurrectionWaitTime = ResurrectionFeather.allResurrectionWaitTime;			ResurrectionFeatherEntityCreature.allResurrectionTime = ResurrectionFeather.allResurrectionTime;		}		return itemstack;	}	@Override	public void registerIcons(IconRegister par1IconRegister)	{		itemIcon = par1IconRegister.registerIcon(ResurrectionFeather.useOriginalIcon ? iconString : "feather");	}}
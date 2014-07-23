package modchu.resurrectionfeather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import modchu.lib.Modchu_Debug;
import modchu.lib.Modchu_Main;
import modchu.lib.Modchu_Reflect;
import modchu.lib.characteristic.Modchu_AS;

public class ResurrectionFeatherEntityCreature {
	public static List entityWhiteListData = new ArrayList();
	public static List entityNgListData = new ArrayList();
	public static HashMap<Object, Long> entitydeathTimeMap = new HashMap();
	public static HashMap<Object, Long> entityResurrectionTimeMap = new HashMap();
	public static List<String> whiteList = new ArrayList<String>();
	public static List<String> ngList = new ArrayList<String>();
	public static int onDeathTimeDespawn = 20;
	//public static int allResurrectionWaitTime = 0;
	//public static long allResurrectionTime = 0;
	public static long tempResurrectionTime;
	public static long tempAllResurrectionTime;
	public static int tempCurrentItem;
	private static Random rand = new Random();
	public static Object tempItemstack;
	public static boolean allResurrectionFlag = false;

	public static void livingEventLivingUpdateEvent(Object event) {
		//if (modc_ResurrectionFeather.debug) Modchu_Debug.mDebug("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent 1");
		Object entityLiving = Modchu_Reflect.getFieldObject(event.getClass(), "entityLiving", event);
		if (entityLiving != null) ;else {
			//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent entityLiving == null !!");
			return;
		}
		long systemTime = Modchu_AS.getLong(Modchu_AS.minecraftSystemTime);
		//if (modc_ResurrectionFeather.debug) Modchu_Debug.mdDebug("allResurrectionTime = "+(tempAllResurrectionTime + modc_ResurrectionFeather.allResurrectionWaitTime - systemTime), 1);
		if (Modchu_AS.getFloat(Modchu_AS.entityLivingBaseGetHealth, entityLiving) <= 0.0F) {
			//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent call.");
			onDeathUpdate(entityLiving, true);
		} else {
			//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent "+(Modchu_AS.getFloat(Modchu_AS.entityLivingBaseGetHealth, entityLiving)));
		}
		if (allResurrectionFlag
				&& tempAllResurrectionTime + modc_ResurrectionFeather.allResurrectionWaitTime < systemTime) {
			Object thePlayer = Modchu_AS.get(Modchu_AS.minecraftThePlayer);
			boolean isRemote = Modchu_AS.getBoolean(Modchu_AS.worldIsRemote, thePlayer);
			if (thePlayer != null) {
				Object itemStack = Modchu_AS.get(Modchu_AS.entityPlayerInventoryGetStackInSlot, thePlayer, tempCurrentItem);
				Modchu_Debug.mDebug("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent 1 isRemote="+isRemote+" itemStack="+itemStack+" tempCurrentItem="+tempCurrentItem);
				if (itemStack != null) {
					Object o = Modchu_Main.getModchuItem(Modchu_AS.get(Modchu_AS.itemStackGetItem, tempItemstack));
					Modchu_Debug.mDebug("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent 2 isRemote="+isRemote+" o="+o);
					if (o != null) {
						int stackSize = itemStack != null ? Modchu_AS.getInt(Modchu_AS.itemStackStackSize, itemStack) : 1;
						Modchu_Debug.mDebug("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent 3 isRemote="+isRemote+" stackSize="+stackSize);
						if (stackSize > 0) Modchu_AS.set(Modchu_AS.itemStackStackSize, itemStack, 0);
						Modchu_AS.set(Modchu_AS.entityPlayerInventorySetInventorySlotContents, thePlayer, tempCurrentItem, null);
						Modchu_Debug.mDebug("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent 4 setInventorySlotContents isRemote="+isRemote+" tempCurrentItem="+tempCurrentItem);
					}
				}
				allResurrectionFlag = false;
			}
			int stackSize = tempItemstack != null ? Modchu_AS.getInt(Modchu_AS.itemStackStackSize, tempItemstack) : 1;
			Modchu_Debug.mDebug("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent 3 isRemote="+isRemote+" stackSize="+stackSize);
			if (tempItemstack != null
					&& stackSize > 0) Modchu_AS.set(Modchu_AS.itemStackStackSize, tempItemstack, 0);
			tempItemstack = null;
			Modchu_Debug.mDebug1("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent end. ");
		}
	}

	public static int onDeathUpdate(Object entityLivingBase, boolean isDead) {
		//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() isRemote="+(Modchu_AS.getBoolean(Modchu_AS.worldIsRemote, entityLivingBase)));
		int i = -1;
		if (entityLivingBase != null) ;else {
			//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() entityCreature == null !!");
			return i;
		}
		if (!isDead
				&& Modchu_AS.getFloat(Modchu_AS.entityLivingBaseGetHealth, entityLivingBase) > 0.0F) {
			//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate !isDead return.");
			return 2;
		}
		if (listCheck(whiteList, entityWhiteListData, entityLivingBase)
				&& !listCheck(ngList, entityNgListData, entityLivingBase)) {
			long deathTime = entitydeathTimeMap.containsKey(entityLivingBase) ? entitydeathTimeMap.get(entityLivingBase) : 0;
			long resurrectionTime = entityResurrectionTimeMap.containsKey(entityLivingBase) ? entityResurrectionTimeMap.get(entityLivingBase) : -1;
			long systemTime = Modchu_AS.getLong(Modchu_AS.minecraftSystemTime);
			boolean resurrectionFlag = resurrectionTime + 1000 > systemTime;
			if (tempAllResurrectionTime + modc_ResurrectionFeather.allResurrectionWaitTime > systemTime) {
				if (tempItemstack != null
						&& !resurrectionFlag) {
					resurrectionFlag = Modchu_AS.getBoolean(Modchu_AS.itemItemInteractionForEntity, Modchu_AS.get(Modchu_AS.itemStackGetItem, tempItemstack), tempItemstack, Modchu_AS.get(Modchu_AS.minecraftThePlayer), entityLivingBase);
				}
			}
			if (resurrectionFlag) {
				tempResurrectionTime = systemTime;
				entityResurrectionTimeMap.put(entityLivingBase, systemTime);
				entitydeathTimeMap.put(entityLivingBase, (long) 0);
				isDead = false;
				i = 2;
			}
			if (isDead) {
				++deathTime;
				//if (modc_ResurrectionFeather.debug) Modchu_Debug.mdDebug("onDeathUpdate() run. ("+deathTime+" / "+onDeathTimeDespawn+")");
				//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("onDeathUpdate() run. onDeathTimeDespawn="+onDeathTimeDespawn);

				if (deathTime >= onDeathTimeDespawn) {
					i = 1;
					Modchu_AS.set(Modchu_AS.entityLivingBaseDeathTime, entityLivingBase, 19);
					//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() run. i = 1 deathTime="+deathTime+" onDeathTimeDespawn="+onDeathTimeDespawn);
					//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() run. dead set.");
				} else {
					i = 0;
					if (Modchu_AS.getInt(Modchu_AS.entityLivingBaseDeathTime, entityLivingBase) > 0) Modchu_AS.set(Modchu_AS.entityLivingBaseDeathTime, entityLivingBase, 0);
					//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() run. i = 0 deathTime="+deathTime+" onDeathTimeDespawn="+onDeathTimeDespawn+" setDeathTime="+Modchu_AS.getInt(Modchu_AS.entityLivingBaseDeathTime, entityLivingBase));
					//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() run. not dead set.");
				}

				if (deathTime > (onDeathTimeDespawn + 100)) {
					deathTime = onDeathTimeDespawn - 1;
				}
				entitydeathTimeMap.put(entityLivingBase, deathTime);
			} else {
				//if (modc_ResurrectionFeather.debug) Modchu_Debug.dDebug(null);
			}
		} else {
			//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() listCheck out. entityLivingBase="+entityLivingBase);
		}
		//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() end. return i="+i);
		return i;
	}

	private static void removeDeathMap(Object entityLivingBase) {
		if (!Modchu_AS.getBoolean(Modchu_AS.worldIsRemote, entityLivingBase)) entitydeathTimeMap.remove(entityLivingBase);
	}

	public static boolean listCheck(List<String> list, List dataList, Object entityLivingBase) {
		String s;
		Class c;
		boolean flag = false;
		boolean simpleNameCheck = true;
		//boolean white = list.equals(whiteList);
		if (list.size() == 0) {
			Modchu_Debug.lDebug1("ResurrectionFeatherEntityCreature listCheck list.size() == 0 !!");
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			s = list.get(i);
			if (s.startsWith("[")
					&& s.endsWith("]")) {
				simpleNameCheck = false;
				s = s.substring(1, s.length() - 1);
				//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("listCheck simpleNameCheck == false. s="+s);
			} else {
				//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("listCheck simpleNameCheck == true. s="+s);
				simpleNameCheck = true;
			}
			if (dataList.contains(entityLivingBase)) {
				//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("listCheck map ok. return true.");
				flag = true;
				break;
			} else {
				int i1 = s.lastIndexOf("@Tame");
				boolean isTamedCheck = i1 > -1;
				//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("listCheck s="+s+" isTamedCheck="+isTamedCheck);
				if (isTamedCheck) {
					s = s.substring(0, i1);
				}
				try {
					c = Class.forName(s);
					//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("listCheck 2 c="+c);
					if (c.isInstance(entityLivingBase)) {
						//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("listCheck isInstance 3 ok.");
						if (isTamedCheck) {
							flag = Modchu_AS.getBoolean(Modchu_AS.isTamed, entityLivingBase);
							//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("listCheck isInstance 4 ok. flag="+flag);
						} else {
							//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("listCheck isInstance 4 else");
							flag = true;
						}
						if (flag) dataList.add(entityLivingBase);
						//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("listCheck isInstance 5 flag="+flag);
						return flag;
					}
				} catch (ClassNotFoundException e1) {
				}
			}
			if (!flag
					&& simpleNameCheck
					&& entityLivingBase.getClass().getSimpleName().indexOf(s) > -1) {
				//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("listCheck getSimpleName ok. return true.");
				flag = true;
				break;
			}
		}
		//if (modc_ResurrectionFeather.debug) Modchu_Debug.Debug("listCheck flag="+flag+" entityLivingBase.getClass().getSimpleName()="+entityLivingBase.getClass().getSimpleName());
		return flag;
	}

	public static boolean resurrectionEntity(Object entityLivingBase) {
		//if (modc_ResurrectionFeather.debug) Modchu_Debug.mDebug("resurrectionEntity isRemote="+(Modchu_AS.getBoolean(Modchu_AS.worldIsRemote, entityLivingBase)));
		Modchu_AS.set(Modchu_AS.entityLivingBaseSetHealth, entityLivingBase, Modchu_AS.getFloat(Modchu_AS.entityLivingBaseGetMaxHealth, entityLivingBase));
		Modchu_AS.set(Modchu_AS.entityIsDead, entityLivingBase, false);
		Modchu_AS.set(Modchu_AS.entityLivingBaseDeathTime, entityLivingBase, 0);
		//removeDeathMap(entityLivingBase);
		entitydeathTimeMap.put(entityLivingBase, (long) 0);
		//if (modc_ResurrectionFeather.debug) Modchu_Debug.mdDebug(null);
		for (int var1 = 0; var1 < 30; ++var1) {
			double var2 = rand.nextGaussian() * 1.0D;
			double var4 = rand.nextGaussian() * 1.0D;
			double var6 = rand.nextGaussian() * 1.0D;
			double posX = Modchu_AS.getDouble(Modchu_AS.entityPosX, entityLivingBase);
			double posY = Modchu_AS.getDouble(Modchu_AS.entityPosY, entityLivingBase);
			double posZ = Modchu_AS.getDouble(Modchu_AS.entityPosZ, entityLivingBase);
			double width = Modchu_AS.getDouble(Modchu_AS.entityWidth, entityLivingBase);
			double height = Modchu_AS.getDouble(Modchu_AS.entityHeight, entityLivingBase);
			Modchu_AS.set(Modchu_AS.worldSpawnParticle, entityLivingBase, "instantSpell", posX + (double)(rand.nextFloat() * width * 2.0F) - (double)width, posY + 0.1D + (double)(rand.nextFloat() * height), posZ + (double)(rand.nextFloat() * width * 2.0F) - (double)width, var2, var4, var6);
		}
		if (modc_ResurrectionFeather.resurrectionWarp) {
			double posX = Modchu_AS.getDouble(Modchu_AS.entityPosX);
			double posY = Modchu_AS.getDouble(Modchu_AS.entityPosY);
			double posZ = Modchu_AS.getDouble(Modchu_AS.entityPosZ);
			Modchu_AS.set(Modchu_AS.entitySetPosition, entityLivingBase, posX, posY, posZ);
		}
		return true;
	}

	public static boolean allResurrection(Object entityPlayer, Object itemStack) {
		if (!modc_ResurrectionFeather.allResurrection) return false;
		long systemTime = Modchu_AS.getLong(Modchu_AS.minecraftSystemTime);
		if (tempAllResurrectionTime + modc_ResurrectionFeather.allResurrectionWaitTime < systemTime) {
			tempItemstack = itemStack;
			tempAllResurrectionTime = systemTime;
			allResurrectionFlag = true;
			tempCurrentItem = Modchu_AS.getInt(Modchu_AS.entityPlayerInventoryCurrentItem, entityPlayer);
			//if (modc_ResurrectionFeather.debug) Modchu_Debug.mDebug("allResurrection tempCurrentItem="+tempCurrentItem);
			return true;
		}
		return false;
	}
}
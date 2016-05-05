package modchu.resurrectionfeather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import modchu.lib.Modchu_AS;
import modchu.lib.Modchu_Debug;
import modchu.lib.Modchu_Main;

public class ResurrectionFeatherEntityCreature {
	public static List entityWhiteListData = new ArrayList();
	public static List entityNgListData = new ArrayList();
	public static HashMap<Object, Long> entitydeathTimeMap = new HashMap();
	//public static HashMap<Object, Long> entityResurrectionTimeMap = new HashMap();
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
	private static boolean consumptionFlag = false;

	public static void livingEventLivingUpdateEvent(Object entityLiving) {
		boolean debug = false;
		//if (debug) Modchu_Debug.mDebug("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent 1");
		if (entityLiving != null); else {
			//if (debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent entityLiving == null !!");
			return;
		}
		if (Modchu_AS.getFloat(Modchu_AS.entityLivingBaseGetHealth, entityLiving) <= 0.0F) {
			//if (debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent call.");
			onDeathUpdate(entityLiving, true);
		} else {
			//if (debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent "+(Modchu_AS.getFloat(Modchu_AS.entityLivingBaseGetHealth, entityLiving)));
		}
		long systemTime = Modchu_AS.getLong(Modchu_AS.minecraftSystemTime);
		if (debug) Modchu_Debug.mdDebug("allResurrectionTime = "+(tempAllResurrectionTime + modc_ResurrectionFeather.allResurrectionWaitTime - systemTime), 1);
		if (allResurrectionFlag
				&& tempAllResurrectionTime + modc_ResurrectionFeather.allResurrectionWaitTime < systemTime) {
			Modchu_Debug.mDebug1("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent allResurrectionFlag = false");
			tempItemstack = null;
			allResurrectionFlag = false;
		}
		//Modchu_Debug.mDebug1("ResurrectionFeatherEntityCreature livingEventLivingUpdateEvent end. ");
	}

	public static int onDeathUpdate(Object entityLivingBase, boolean isDead) {
		boolean debug = false;
		if (debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() isRemote="+(Modchu_AS.getBoolean(Modchu_AS.worldIsRemote, entityLivingBase)));
		int i = -1;
		if (entityLivingBase != null); else {
			if (debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() entityCreature == null !!");
			return i;
		}
		if (!isDead
				&& Modchu_AS.getFloat(Modchu_AS.entityLivingBaseGetHealth, entityLivingBase) > 0.0F) {
			if (debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate !isDead return.");
			return 2;
		}
		if (listCheck(whiteList, entityWhiteListData, entityLivingBase)
				&& !listCheck(ngList, entityNgListData, entityLivingBase)) {
			long deathTime = entitydeathTimeMap.containsKey(entityLivingBase) ? entitydeathTimeMap.get(entityLivingBase) : 0;
			//long resurrectionTime = entityResurrectionTimeMap.containsKey(entityLivingBase) ? entityResurrectionTimeMap.get(entityLivingBase) : -1;
			long systemTime = Modchu_AS.getLong(Modchu_AS.minecraftSystemTime);
			//boolean resurrectionFlag = resurrectionTime + 1000 > systemTime;
			boolean resurrectionFlag = false;
			if (allResurrectionFlag
					&& tempAllResurrectionTime + modc_ResurrectionFeather.allResurrectionWaitTime > systemTime) {
				//if (!resurrectionFlag) {
					boolean isRemote = Modchu_AS.getBoolean(Modchu_AS.worldIsRemote, entityLivingBase);
					resurrectionFlag = resurrectionEntity(entityLivingBase);
				//}
			}
			if (resurrectionFlag) {
				tempResurrectionTime = systemTime;
				//entityResurrectionTimeMap.put(entityLivingBase, systemTime);
				entitydeathTimeMap.put(entityLivingBase, (long) 0);
				isDead = false;
				if (debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate IsCreativeMode="+Modchu_AS.getBoolean(Modchu_AS.entityPlayerCapabilitiesIsCreativeMode));
				i = 2;
				boolean isRemote = Modchu_AS.getBoolean(Modchu_AS.worldIsRemote, entityLivingBase);
				if (!isRemote
						&& consumptionFlag) {
					Object thePlayer = Modchu_AS.getList(Modchu_AS.worldPlayerEntities, Modchu_AS.getObjectArray(Modchu_AS.minecraftServerGetServerWorldServers)[0]).get(0);
					Object itemStack = Modchu_AS.get(Modchu_AS.entityPlayerInventoryGetStackInSlot, thePlayer, tempCurrentItem);
					if (debug) Modchu_Debug.mDebug("ResurrectionFeatherEntityCreature onDeathUpdate 1 isRemote="+isRemote+" itemStack="+itemStack+" tempCurrentItem="+tempCurrentItem);
					if (itemStack != null
							&& tempItemstack != null
							&& Modchu_AS.get(Modchu_AS.itemStackGetItem, itemStack).getClass() == Modchu_AS.get(Modchu_AS.itemStackGetItem, tempItemstack).getClass()) {
						Object o = Modchu_Main.getModchuCharacteristicObjectMaster(Modchu_AS.get(Modchu_AS.itemStackGetItem, tempItemstack));
						//Modchu_Debug.mDebug("ResurrectionFeatherEntityCreature onDeathUpdate 2 isRemote="+isRemote+" o="+o);
						if (o != null) {
							int stackSize = Modchu_AS.getInt(Modchu_AS.itemStackStackSize, itemStack);
							if (debug) Modchu_Debug.mDebug("ResurrectionFeatherEntityCreature onDeathUpdate 3 isRemote="+isRemote+" stackSize="+stackSize);
							--stackSize;
							if (stackSize > 0) Modchu_AS.set(Modchu_AS.itemStackStackSize, itemStack, stackSize);
							if (stackSize <= 0) Modchu_AS.set(Modchu_AS.entityPlayerInventorySetInventorySlotContents, thePlayer, tempCurrentItem, null);
							if (debug) Modchu_Debug.mDebug("ResurrectionFeatherEntityCreature onDeathUpdate 4 setInventorySlotContents isRemote="+isRemote+" tempCurrentItem="+tempCurrentItem);
						}
					} else {
						allResurrectionFlag = false;
						isDead = true;
						if (debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate consumptionFlag else itemStack.getItem().getClass()="+Modchu_AS.get(Modchu_AS.itemStackGetItem, itemStack).getClass());
						i = 0;
					}
					consumptionFlag = false;
				}
			 }
			if (isDead) {
				++deathTime;
				if (debug) {
					Modchu_Debug.mdDebug("onDeathUpdate() run. ("+deathTime+" / "+onDeathTimeDespawn+")");
					Modchu_Debug.Debug("onDeathUpdate() run. onDeathTimeDespawn="+onDeathTimeDespawn);
				}

				if (deathTime >= onDeathTimeDespawn) {
					i = 1;
					Modchu_AS.set(Modchu_AS.entityLivingBaseDeathTime, entityLivingBase, 19);
					if (debug) {
						Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() run. i = 1 deathTime="+deathTime+" onDeathTimeDespawn="+onDeathTimeDespawn);
						Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() run. dead set.");
					}
				} else {
					i = 0;
					if (Modchu_AS.getInt(Modchu_AS.entityLivingBaseDeathTime, entityLivingBase) > 0) Modchu_AS.set(Modchu_AS.entityLivingBaseDeathTime, entityLivingBase, 0);
					if (debug) {
						Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() run. i = 0 deathTime="+deathTime+" onDeathTimeDespawn="+onDeathTimeDespawn+" setDeathTime="+Modchu_AS.getInt(Modchu_AS.entityLivingBaseDeathTime, entityLivingBase));
						Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() run. not dead set.");
					}
				}

				if (deathTime > (onDeathTimeDespawn + 100)) {
					deathTime = onDeathTimeDespawn - 1;
				}
				entitydeathTimeMap.put(entityLivingBase, deathTime);
			} else {
				if (debug) Modchu_Debug.dDebug(null);
			}
		} else {
			if (debug) {
				Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() listCheck out. entityLivingBase="+entityLivingBase);
				Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() listCheck out. entityLivingBase.getClass()="+entityLivingBase.getClass());
			}
		}
		if (debug) Modchu_Debug.Debug("ResurrectionFeatherEntityCreature onDeathUpdate() end. return i="+i);
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
				//if (debug) Modchu_Debug.Debug("listCheck simpleNameCheck == false. s="+s);
			} else {
				//if (debug) Modchu_Debug.Debug("listCheck simpleNameCheck == true. s="+s);
				simpleNameCheck = true;
			}
			if (dataList.contains(entityLivingBase)) {
				//if (debug) Modchu_Debug.Debug("listCheck map ok. return true.");
				flag = true;
				break;
			} else {
				int i1 = s.lastIndexOf("@Tame");
				boolean isTamedCheck = i1 > -1;
				//if (debug) Modchu_Debug.Debug("listCheck s="+s+" isTamedCheck="+isTamedCheck);
				if (isTamedCheck) {
					s = s.substring(0, i1);
				}
				try {
					c = Class.forName(s);
					//if (debug) Modchu_Debug.Debug("listCheck 2 c="+c);
					if (c.isInstance(entityLivingBase)) {
						//if (debug) Modchu_Debug.Debug("listCheck isInstance 3 ok.");
						if (isTamedCheck) {
							flag = Modchu_AS.getBoolean(Modchu_AS.isTamed, entityLivingBase);
							//if (debug) Modchu_Debug.Debug("listCheck isInstance 4 ok. flag="+flag);
						} else {
							//if (debug) Modchu_Debug.Debug("listCheck isInstance 4 else");
							flag = true;
						}
						if (flag) dataList.add(entityLivingBase);
						//if (debug) Modchu_Debug.Debug("listCheck isInstance 5 flag="+flag);
						return flag;
					}
				} catch (ClassNotFoundException e1) {
				}
			}
			if (!flag
					&& simpleNameCheck
					&& entityLivingBase.getClass().getSimpleName().indexOf(s) > -1) {
				//if (debug) Modchu_Debug.Debug("listCheck getSimpleName ok. return true.");
				flag = true;
				break;
			}
		}
		//if (debug) Modchu_Debug.Debug("listCheck flag="+flag+" entityLivingBase.getClass().getSimpleName()="+entityLivingBase.getClass().getSimpleName());
		return flag;
	}

	public static boolean resurrectionEntity(Object entityLivingBase) {
		//if (debug) Modchu_Debug.mDebug("resurrectionEntity isRemote="+(Modchu_AS.getBoolean(Modchu_AS.worldIsRemote, entityLivingBase)));
		Modchu_AS.set(Modchu_AS.entityLivingBaseSetHealth, entityLivingBase, Modchu_AS.getFloat(Modchu_AS.entityLivingBaseGetMaxHealth, entityLivingBase));
		Modchu_AS.set(Modchu_AS.entityIsDead, entityLivingBase, false);
		Modchu_AS.set(Modchu_AS.entityLivingBaseDeathTime, entityLivingBase, 0);
		//removeDeathMap(entityLivingBase);
		entitydeathTimeMap.put(entityLivingBase, (long) 0);
		//if (debug) Modchu_Debug.mdDebug(null);
		for (int var1 = 0; var1 < 30; ++var1) {
			double var2 = rand.nextGaussian() * 1.0D;
			double var4 = rand.nextGaussian() * 1.0D;
			double var6 = rand.nextGaussian() * 1.0D;
			double posX = Modchu_AS.getDouble(Modchu_AS.entityPosX, entityLivingBase);
			double posY = Modchu_AS.getDouble(Modchu_AS.entityPosY, entityLivingBase);
			double posZ = Modchu_AS.getDouble(Modchu_AS.entityPosZ, entityLivingBase);
			float width = Modchu_AS.getFloat(Modchu_AS.entityWidth, entityLivingBase);
			float height = Modchu_AS.getFloat(Modchu_AS.entityHeight, entityLivingBase);
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
		boolean debug = false;
		if (debug) Modchu_Debug.mDebug("allResurrection");
		if (!modc_ResurrectionFeather.allResurrection) return false;
		//Modchu_Debug.mDebug("allResurrection 1");
		long systemTime = Modchu_AS.getLong(Modchu_AS.minecraftSystemTime);
		//Modchu_Debug.mDebug("allResurrection 2 systemTime="+systemTime);
		if (tempAllResurrectionTime + modc_ResurrectionFeather.allResurrectionWaitTime < systemTime) {
			//Modchu_Debug.mDebug("allResurrection 3");
			tempItemstack = itemStack;
			tempAllResurrectionTime = systemTime;
			allResurrectionFlag = true;
			boolean isRemote = Modchu_AS.getBoolean(Modchu_AS.worldIsRemote, entityPlayer);
			if (isRemote) tempCurrentItem = Modchu_AS.getInt(Modchu_AS.entityPlayerInventoryCurrentItem, entityPlayer);
			if (!Modchu_AS.getBoolean(Modchu_AS.entityPlayerCapabilitiesIsCreativeMode)) consumptionFlag = true;
			if (debug) Modchu_Debug.mDebug("allResurrection tempCurrentItem="+tempCurrentItem);
			Modchu_Debug.mDebug("allResurrection return true end.");
			return true;
		}
		Modchu_Debug.mDebug("allResurrection return false");
		return false;
	}
}
package modchu.resurrectionfeather;import java.io.BufferedReader;import java.io.BufferedWriter;import java.io.File;import java.io.FileReader;import java.io.FileWriter;import java.util.ArrayList;import java.util.List;import modchu.lib.Modchu_AS;import modchu.lib.Modchu_CastHelper;import modchu.lib.Modchu_Config;import modchu.lib.Modchu_Debug;import modchu.lib.Modchu_IItem;import modchu.lib.Modchu_Main;import modchu.lib.Modchu_Reflect;public class modc_ResurrectionFeather {	public static int onDeathTimeDespawn = 300;	public static int allResurrectionWaitTime = 300;	public static boolean allResurrection = false;	public static boolean useOriginalIcon = true;	public static boolean resurrectionWarp = true;	// ~164	public static int resurrectionFeatherID = 17650;	public static final String versionString = "10";	public static Modchu_IItem itemInstance;	public static modc_ResurrectionFeather baseModInstance;	private static File whiteListfile;	private static File ngListfile;	public static List<String> whiteList = new ArrayList<String>();	public static List<String> ngList = new ArrayList<String>();	private static File mainCfgfile;	public static String itemName;	public static boolean debug = Modchu_Debug.mDebug;	//public static boolean debug = true;	public String getVersion() {		return versionString;	}	public void load() {		Modchu_Main.eventRegister(this);		if (Modchu_Main.getMinecraftVersion() < 164) {			String modchuLibEventVersion = Modchu_CastHelper.String(Modchu_Reflect.getFieldObject("EntityCreature", "modchuLibEventVersion"));			if (modchuLibEventVersion != null) Modchu_Main.modchuLibEventRegister(this);			else {				Modchu_Main.setRuntimeException("ResurrectionFeather required mod not install error !! required mod [ addModchuLibEvent Mod ]");				return;			}		}		whiteListfile = new File(Modchu_Main.cfgdir, ("ResurrectionFeather_whiteList.cfg"));		ngListfile = new File(Modchu_Main.cfgdir, ("ResurrectionFeather_NGList.cfg"));		mainCfgfile = new File(Modchu_Main.cfgdir, ("ResurrectionFeather.cfg"));		baseModInstance = this;		loadcfg();		itemName = "ResurrectionFeather";		itemInstance = (Modchu_IItem) Modchu_AS.get(Modchu_AS.itemSetTextureName, Modchu_AS.get(Modchu_AS.itemSetCreativeTab, Modchu_AS.get(Modchu_AS.itemSetUnlocalizedName, Modchu_Main.newModchuCharacteristicObject("Modchu_Item", ItemResurrectionFeather.class, resurrectionFeatherID), itemName), Modchu_AS.get(Modchu_AS.creativeTabsTabMaterials)), itemName);		Modchu_Main.languageRegistryAddName(itemInstance, itemName.toLowerCase());		Modchu_Main.registerItem(itemInstance, itemName.toLowerCase());		Modchu_Main.addRecipe(Modchu_Reflect.newInstance("ItemStack", new Class[]{ Modchu_Reflect.loadClass("Item"), int.class }, new Object[]{ itemInstance, 1 }),				new Object[] { " Y ", "YXY", " Y ", Character.valueOf('X'),			Modchu_AS.get(Modchu_AS.getItem, "feather"), Character.valueOf('Y'), Modchu_AS.get(Modchu_AS.getItem, "gold_ingot") });		Modchu_Main.addRecipe(Modchu_Reflect.newInstance("ItemStack", new Class[]{ Modchu_Reflect.loadClass("Item"), int.class }, new Object[]{ itemInstance, 10 }),				new Object[] { "XYX", "YXY", "XYX", Character.valueOf('X'),			Modchu_AS.get(Modchu_AS.getItem, "feather"), Character.valueOf('Y'), Modchu_AS.get(Modchu_AS.getBlock, "gold_block") });		loadList();		ResurrectionFeatherEntityCreature.whiteList = whiteList;		ResurrectionFeatherEntityCreature.ngList = ngList;;		ResurrectionFeatherEntityCreature.onDeathTimeDespawn = onDeathTimeDespawn;	}	public boolean modEnabled() {		return true;	}	public void init(Object event) {		//Modchu_Debug.lDebug("modc_ResurrectionFeather init");		Modchu_Main.itemModelMesherRegister(itemInstance, 0, "modchulib:"+itemName.toLowerCase(), "inventory");		//Modchu_Debug.lDebug("modc_ResurrectionFeather init end.");	}	public void livingEventLivingUpdateEvent(Object event) {		Object entityLiving = Modchu_Reflect.getFieldObject(event.getClass(), "entityLiving", event);		ResurrectionFeatherEntityCreature.livingEventLivingUpdateEvent(entityLiving);	}	public void livingDeathEvent(Object event) {		//if (debug) Modchu_Debug.Debug("modc_ResurrectionFeather livingDeathEvent");		Object entityLiving = Modchu_Reflect.getFieldObject(event.getClass(), "entityLiving", event);		//if (debug) Modchu_Debug.Debug("modc_ResurrectionFeather livingDeathEvent entityLiving="+entityLiving);		//if (debug) Modchu_Debug.Debug("modc_ResurrectionFeather livingDeathEvent isRemote="+(Modchu_AS.getBoolean(Modchu_AS.worldIsRemote, entityLiving)));		Modchu_Reflect.invokeMethod(event.getClass(), "setCanceled", new Class[]{ boolean.class }, event, new Object[]{ ResurrectionFeatherEntityCreature.onDeathUpdate(entityLiving, true) != -1 });		//if (debug) Modchu_Debug.Debug("modc_ResurrectionFeather livingDeathEvent isCanceled="+(Modchu_Reflect.invokeMethod(event.getClass(), "isCanceled", event)));	}	public Object[] entityCreatureOnLivingUpdate(Object[] o) {		Object entityLiving = o != null				&& o.length > 0 ? o[0] : null;		ResurrectionFeatherEntityCreature.livingEventLivingUpdateEvent(entityLiving);		return new Object[]{ false };	}	public Object[] entityCreatureOnDeathUpdate(Object[] o) {		Object entityLiving = o != null				&& o.length > 0 ? o[0] : null;		boolean b = ResurrectionFeatherEntityCreature.onDeathUpdate(entityLiving, true) != -1;		return new Object[]{ b };	}	public static List<String> writerList(String[] s, File file, List<String> list) {		//Listファイル書き込み		try {			BufferedWriter bwriter = new BufferedWriter(new FileWriter(file));			for (int i = 0; i < s.length ; i++)			{				//Modchu_Debug.mDebug("s[i]="+s[i]);				if (s[i] != null) {					bwriter.write(s[i]);					list.add(s[i]);					bwriter.newLine();				}			}			bwriter.close();			Modchu_Debug.Debug("file new file create.");		} catch (Exception e) {			Modchu_Debug.Debug("file writer fail.");			e.printStackTrace();			Modchu_Debug.Debug(" ");		}		return list;	}	public static void loadList() {		// List読み込み		if (Modchu_Main.cfgdir.exists()) {			if (!whiteListfile.exists()) {				// whiteListファイルが無い = 新規作成				String s[] = {						"net.minecraft.entity.passive.EntityTameable@Tame", "net.minecraft.entity.passive.EntityHorse@Tame"				};				whiteList = writerList(s, whiteListfile, whiteList);			} else {				// whiteListファイルがある				try {					BufferedReader breader = new BufferedReader(new FileReader(whiteListfile));					String rl;					while ((rl = breader.readLine()) != null) {						whiteList.add(rl);					}					breader.close();					Modchu_Debug.mDebug("whiteList load end.");				} catch (Exception e) {					Modchu_Debug.Debug("whiteList file load fail.");		            e.printStackTrace();					Modchu_Debug.Debug(" ");				}			}			if (!ngListfile.exists()) {				// NGListファイルが無い = 新規作成				String s[] = {						"EntityMob"				};				ngList = writerList(s, ngListfile, ngList);			} else {				// NGListファイルがある				try {					BufferedReader breader = new BufferedReader(new FileReader(ngListfile));					String rl;					while ((rl = breader.readLine()) != null) {						ngList.add(rl);					}					breader.close();					Modchu_Debug.mDebug("NGList load end.");				} catch (Exception e) {					Modchu_Debug.Debug("NGList file load fail.");		            e.printStackTrace();					Modchu_Debug.Debug(" ");				}			}		}	}	public static void loadcfg() {		// cfg読み込み		if (Modchu_Main.cfgdir.exists()) {			ArrayList list = new ArrayList();			if (!mainCfgfile.exists()) {				// cfgファイルが無い = 新規作成				String s[] = {						"onDeathTimeDespawn=2500",						"allResurrection=true",						"allResurrectionWaitTime=300",						"useOriginalIcon=true",						"resurrectionWarp=true",				};				if (Modchu_Main.getMinecraftVersion() < 170) list.add("resurrectionFeatherID=17650");				for(String s1 : s) {					list.add(s1);				}				Modchu_Config.writerConfig(mainCfgfile, list);			} else {				// cfgファイルがある				onDeathTimeDespawn = Modchu_CastHelper.Int(Modchu_Config.loadConfig(mainCfgfile, "onDeathTimeDespawn", onDeathTimeDespawn));				allResurrectionWaitTime = Modchu_CastHelper.Int(Modchu_Config.loadConfig(mainCfgfile, "allResurrectionWaitTime", allResurrectionWaitTime));				allResurrection = Modchu_CastHelper.Boolean(Modchu_Config.loadConfig(mainCfgfile, "allResurrection", allResurrection));				useOriginalIcon = Modchu_CastHelper.Boolean(Modchu_Config.loadConfig(mainCfgfile, "useOriginalIcon", useOriginalIcon));				resurrectionWarp = Modchu_CastHelper.Boolean(Modchu_Config.loadConfig(mainCfgfile, "resurrectionWarp", resurrectionWarp));				if (Modchu_Main.getMinecraftVersion() < 170) resurrectionFeatherID = Modchu_CastHelper.Int(Modchu_Config.loadConfig(mainCfgfile, "resurrectionFeatherID", resurrectionFeatherID));				cfgMaxMinCheck();				String k[] = {						"onDeathTimeDespawn",						"allResurrection",						"allResurrectionWaitTime",						"useOriginalIcon",						"resurrectionWarp"				};				String k1[] = {						""+onDeathTimeDespawn,						""+allResurrection,						""+allResurrectionWaitTime,						""+useOriginalIcon,						""+resurrectionWarp				};				ArrayList list2 = new ArrayList();				if (Modchu_Main.getMinecraftVersion() < 170) {					list.add("resurrectionFeatherID");					list2.add(""+resurrectionFeatherID);				}				for(String s1 : k) {					list.add(s1);				}				for(String s1 : k1) {					list2.add(s1);				}				Modchu_Config.writerSupplementConfig(mainCfgfile, list, list2);			}		}	}	public static void cfgMaxMinCheck() {		if (onDeathTimeDespawn < 0) onDeathTimeDespawn = 0;		if (onDeathTimeDespawn > 2147483647) onDeathTimeDespawn = 2147483647;	}}
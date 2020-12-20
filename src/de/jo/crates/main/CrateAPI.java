package de.jo.crates.main;
import org.bukkit.plugin.java.JavaPlugin;

public class CrateAPI extends JavaPlugin {
	
	private static CrateAPI pl;
	
	@Override
	public void onEnable() {
		pl = this;
	}	
	
	public static CrateAPI instance() {
		return pl;
	}
	
}

package de.jo.crates.main;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private static Main pl;
	
	@Override
	public void onEnable() {
		
		
		pl = this;
		
	}	
	
	public static Main instance() {
		return pl;
	}
	
}

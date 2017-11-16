package com.ben.configapi;

import java.io.File;

import com.ben.configapi.critical.ConfigManager;

public class ConfigAPI {

	@Deprecated
	public static ConfigManager config;
	
	@Deprecated
	public static void start(String file) {
		config = new ConfigManager(new File(file));
	}
	
	public static ConfigManager createConfig(String file) {
		return new ConfigManager(new File(file));
	}
	
	public static ConfigManager createConfig(File file) {
		return new ConfigManager(file);
	}
}

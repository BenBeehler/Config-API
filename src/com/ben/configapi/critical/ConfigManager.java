package com.ben.configapi.critical;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigManager {
	
	private File config;
	
	public ConfigManager(File file) {
		this.setFile(file);
		
		if(!config.exists()) {
			try {
				if(config.createNewFile()) {
					//new file
				}
			} catch (IOException e) {
			}
		}
	}
	
	public static ArrayList<String> getContent(File file){
		ArrayList<String> list = 
				new ArrayList<String>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String in;
			while((in = br.readLine()) != null) {
				list.add(in + "\n");
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public void setValueToPath(String path, Object value) {
		ArrayList<String> content = getContent(this.config);
		ArrayList<String> newContent = 
				new ArrayList<String>();
		
		try {
			clearFile(this.config);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String data = path + ": " + value.toString();
		
		for(String line : content) {
			String[] split = line.split(" ");
			String p = split[0];
			
			if(p.equals(path + ":")) {
				//do nothing
			} else if(line.trim().equals("")) {
				//do nothing
			} else {
				newContent.add(line);
			}
		}
		
		newContent.add(data);
		
		newContent.stream().forEach(l -> {
			appendData(this.config, l);
			
		});
	}
	
	public String getValueFromPath(String path) {
		ArrayList<String> list = getContent(this.config);
		
		String value = "0";
		
		for(String string : list) {
			String[] split = string.split(" ");
			String p = split[0];
			
			if(p.equals(path + ":")) {
				value = string.replaceFirst(p + " ", "");
			}
		}
		
		return value;
	}
	
	public boolean pathExists(String path) {
		ArrayList<String> content = getContent(this.config);
		
		for(int i = 0; i < content.size(); i++) {
			String line = content.get(i);
			
			String p = line.split(":")[0];
			
			if(p.trim().equals(path.trim())) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean pathHasValue(String path, Object value) {
		ArrayList<String> content = getContent(this.config);
		
		for(int i = 0; i < content.size(); i++) {
			String line = content.get(i);
			
			String p = line.split(":")[0];
			String v = line.replace(p, "").trim();
			
			if(p.trim().equals(path.trim())) {
				if(v.equals(value.toString())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public HashMap<String, String> getHashMap(String path) {
		String val = this.getValueFromPath(path);
		return parseMap(val);
	}
	
	public int getInteger(String path) {
		if(isInteger(getValueFromPath(path).trim())) {
			return Integer.parseInt(getValueFromPath(path).trim());
		} else {
			return 0;
		}
	}
	
	public HashMap<String, String> parseMap(String string) {
		if(isMap(string)) {
			HashMap<String, String> value = new HashMap<>();
			String[] array=string.split("");
			array[0]="";
			array[array.length-1]="";
			StringBuilder builder=new StringBuilder();
			for(String str:array) {
				builder.append(str);
			}
			string=builder.toString();
			array=string.split(",");
			for(String str:array) {
				if(str.contains("=")) {
					String split[]=str.split("=");
					if(split.length>1) {
						StringBuilder sb=new StringBuilder();
						for(int i=1; i<split.length;i++) {
							sb.append(split[i]);
						}
						
						value.put(split[0].trim(), sb.toString());
					}
				}
			}
			return value;
		} else {
			return new HashMap<String, String>();
		}
	}
	
	public double getDouble(String path) {
		if(isDouble(getValueFromPath(path).trim())) {
			return Double.parseDouble(getValueFromPath(path).trim());
		} else {
			return 0;
		}
	}
	
	public float getFloat(String path) {
		if(isFloat(getValueFromPath(path).trim())) {
			return Float.parseFloat(getValueFromPath(path).trim());
		} else {
			return 0;
		}
	}
	
	public boolean getBoolean(String path) {
		if(isBoolean(getValueFromPath(path).trim())) {
			if(getValueFromPath(path).trim().toLowerCase().equals("true")) {
				return true;
			} else if(getValueFromPath(path).trim().toLowerCase().equals("false")) {
				return false;
			} else {
				return false; //This will never ever get called
			}
		} else {
			return false;
		}
	}
	
	public List<String> getStringList(String path) {
		if(isList(getValueFromPath(path).trim())) {
			String stringValue = getValueFromPath(path);
			
			String[] charArray = stringValue.split("");
			
			charArray[0] = "";
			charArray[charArray.length - 1] = "";
			
			StringBuilder sb = new StringBuilder();
			
			for(String ch : charArray) {
				sb.append(ch);
			}
			
			List<String> value = new ArrayList<>();
			
			for(String entry : sb.toString().split(",")) {
				value.add(entry);
			}
			
			return value;
		} else {
			List<String> empty = new ArrayList<>();
			return empty;
		}
	}
	
	public void appendData(File file, String data) {
		try(FileWriter fw = new FileWriter(file, true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println(data);
			} catch (IOException e) {
		}
	}
	
	public static void clearFile(File file) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(file);
		writer.print("");
		writer.close();
	}

	public File getFile() {
		return config;
	}

	public void setFile(File config) {
		this.config = config;
	}
	
	public boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean isFloat(String value) {
		try {
			Float.parseFloat(value);
			
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean isBoolean(String value) {
		if(value.trim().toLowerCase().equals("true") ||
				value.trim().toLowerCase().equals("false")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isList(String value) {
		value = value.trim();
		
		if(value.startsWith("[") &&
				value.endsWith("]")) {
			return true;
		} else {
			return false;
		}
	} 
	
	public boolean isMap(String string) {
		string=string.trim();
		
		if(string.startsWith("{") && string.endsWith("}")) {
			return true;
		}
		
		return false;
	}
}

package glacier.parser;

import java.util.HashMap;

public class ShortcutManager {
	HashMap<String, Shortcut> iedshortcuts = new HashMap<>();
	HashMap<String, Shortcut> transshortcuts = new HashMap<>();
	HashMap<String, Shortcut> funcshortcuts = new HashMap<>();
	
	public class Shortcut {
		public String name;
		public String type;
		public String scname;
		public Shortcut(String scname, String type, String name) {
			this.name = name;
			this.type = type;
			this.scname = scname;
		}
	}
	
	public void addIEDShortcut(Shortcut sc) {
		iedshortcuts.put(sc.scname, sc);
	}
	
	
	public void addTransShortcut(Shortcut sc) {
		transshortcuts.put(sc.scname, sc);
	}
	
	public void addFuncShortcut(Shortcut sc) {
		funcshortcuts.put(sc.scname, sc);
	}
	
	public Shortcut getIEDShortcut(String scname) {
		if(iedshortcuts.containsKey(scname)) {
			return iedshortcuts.get(scname);
		}
		return null;
	}
	
	public Shortcut getTransShortcut(String scname) {
		if(transshortcuts.containsKey(scname)) {
			return transshortcuts.get(scname);
		}
		return null;
	}
	

	public void loadDefaultShortcuts() {
		// Geometry Data
		addIEDShortcut(new Shortcut("pos", "vec3", "position"));
		addIEDShortcut(new Shortcut("normal", "vec3", "normal"));
		addIEDShortcut(new Shortcut("texCoord", "vec2", "texCoord0"));
		// Camera-Matrices
		addTransShortcut(new Shortcut("world", "mat4", "worldTrans"));
		addTransShortcut(new Shortcut("view", "mat4", "viewTrans"));
		addTransShortcut(new Shortcut("proj", "mat4", "projTrans"));
		addTransShortcut(new Shortcut("normal", "mat3", "normalTrans"));
		addTransShortcut(new Shortcut("mvp", "mat4", "mvpTrans"));
		// Material
		// Functions
		addFuncShortcut(new Shortcut("norm", "vec4", "normalize"));
	}

	public Shortcut getFuncShortcut(String text) {
		if(funcshortcuts.containsKey(text)) {
			return funcshortcuts.get(text);
		}
		return null;
	}

}

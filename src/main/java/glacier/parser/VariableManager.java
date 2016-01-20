package glacier.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import glacier.builder.cdefinitions.Definition;

public class VariableManager {
	public enum GlobalType {
		IN, OUT, TRANS, UNI, MAT
	}

	// Vertex Data
	private HashMap<String, Definition> vertInReg = new HashMap<>();
	private Set<Definition> vertInSet = new HashSet<>();
	private HashMap<String, Definition> vertOutReg = new HashMap<>();
	private Set<Definition> vertOutSet = new HashSet<>();
	private HashMap<String, Definition> vertUniformReg = new HashMap<>();
	private Set<Definition> vertUniformSet = new HashSet<>();
	private HashMap<String, Definition> vertTransReg = new HashMap<>();
	private Set<Definition> vertTransSet = new HashSet<>();
	private HashMap<String, Definition> vertMatReg = new HashMap<>();
	private Set<Definition> vertMatSet = new HashSet<>();
	// Fragment Data
	private HashMap<String, Definition> fragOutReg = new HashMap<>();
	private Set<Definition> fragOutSet = new HashSet<>();
	private HashMap<String, Definition> fragUniformReg = new HashMap<>();
	private Set<Definition> fragUniformSet = new HashSet<>();
	private HashMap<String, Definition> fragTransReg = new HashMap<>();
	private Set<Definition> fragTransSet = new HashSet<>();
	private HashMap<String, Definition> fragMatReg = new HashMap<>();
	private Set<Definition> fragMatSet = new HashSet<>();
	
	public Set<Definition> getGlobalSet(GlobalType type, boolean vert) {
		switch (type) {
		case IN:
			return vert ? vertInSet : vertOutSet;
		case TRANS:
			return vert ? vertTransSet : fragTransSet;
		case OUT:
			return vert ? vertOutSet : fragOutSet;
		case UNI:
			return vert ? vertUniformSet : fragUniformSet;
		case MAT:
			return vert ? vertMatSet : fragMatSet;
		default:
			return null;
		}
	}
	
	public Set<Definition> getGlobalSet(GlobalType type) {
		Set<Definition> defs = new HashSet<>();
		switch (type) {
		case IN:
			defs.addAll(vertInSet);
			defs.addAll(vertOutSet);
			break;
		case TRANS:
			defs.addAll(vertTransSet);
			defs.addAll(fragTransSet);
			break;
		case OUT:
			defs.addAll(vertOutSet);
			defs.addAll(fragOutSet);
			break;
		case UNI:
			defs.addAll(vertUniformSet);
			defs.addAll(fragUniformSet);
			break;
		case MAT:
			defs.addAll(vertMatSet);
			defs.addAll(fragMatSet);
			break;
		default:
			break;
		}
		return defs;
	}

	/**
	 * Saves the given global Variable Definition
	 * 
	 * @param type
	 *            Type of the global var
	 * @param vert
	 *            whether it's from the vertex shader or frag
	 * @param def
	 *            The definition to save
	 */
	public void saveGlobal(GlobalType type, boolean vert, Definition def) {
		// Vertex Shader Variable
		switch (type) {
		case IN:
			if(vert) {
				if(! vertInReg.containsKey(def.getName())) {
					vertInReg.put(def.getName(), def);
					vertInSet.add(def);
				}
			} else {
				throw new UnsupportedOperationException("Fragment should not have in");
			}
			break;
		case TRANS:
			if(vert) {
				if(! vertTransReg.containsKey(def.getName())) {
					vertTransReg.put(def.getName(), def);
					vertTransSet.add(def);
				}
			} else {
				if(! fragTransReg.containsKey(def.getName())) {
					fragTransReg.put(def.getName(), def);
					fragTransSet.add(def);
				}
			}
			break;
		case OUT:
			if(vert) {
				if(! vertOutReg.containsKey(def.getName())) {
					vertOutReg.put(def.getName(), def);
					vertOutSet.add(def);
				}
			} else {
				if(! fragOutReg.containsKey(def.getName())) {
					fragOutReg.put(def.getName(), def);
					fragOutSet.add(def);
				}
			}
			break;
		case UNI:
			System.out.println("Saving uni: " + def.getName());
			if(vert) {
				if(! vertUniformReg.containsKey(def.getName())) {
					vertUniformReg.put(def.getName(), def);
					vertUniformSet.add(def);
				}
			} else {
				if(! fragUniformReg.containsKey(def.getName())) {
					fragUniformReg.put(def.getName(), def);
					fragUniformSet.add(def);
				}
			}
			break;
		case MAT:
			System.out.println("Saving uni: " + def.getName());
			if(vert) {
				if(! vertMatReg.containsKey(def.getName())) {
					vertMatReg.put(def.getName(), def);
					vertMatSet.add(def);
				}
			} else {
				if(! fragMatReg.containsKey(def.getName())) {
					fragMatReg.put(def.getName(), def);
					fragMatSet.add(def);
				}
			}
			break;
		default:
			throw new UnsupportedOperationException("Global Type not implemented");
		}
	}
	
	/**
	 * Returns a saved global of the given parameters
	 * @param type THe GlobalTypr of the variable
	 * @param vert Which Shaderpart
	 * @param name Name of the variable
	 * @return The saved Definition
	 */
	public Definition getGlobal(GlobalType type, boolean vert, String name) {
		switch(type) {
		case IN:
			if(vert) {
				return vertInReg.get(name);
			} else {
				return vertOutReg.get(name);
			}
		case TRANS:
			if(vert) {
				return vertTransReg.get(name);
			} else {
				return fragTransReg.get(name);
			}
		case OUT:
			if(vert) {
				return vertOutReg.get(name);
			} else {
				return fragOutReg.get(name);
			}
		case UNI:
			if(vert) {
				return vertUniformReg.get(name);
			} else {
				return fragUniformReg.get(name);
			}
		case MAT:
			if(vert) {
				return vertMatReg.get(name);
			} else {
				return fragMatReg.get(name);
			}
		default:
			throw new UnsupportedOperationException("Global Type not implemented");
		}
	}
	
	public boolean globalExists(GlobalType type, boolean vert, String name) {
		return getGlobal(type, vert, name) != null;
	}
	
	public int getRenderTargetCount() {
		return fragOutSet.size();
	}
}

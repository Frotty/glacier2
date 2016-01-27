package glacier.parser;

import glacier.builder.cdefinitions.TransDef;
import glacier.builder.cdefinitions.VertexInDef;
import glacier.builder.cdefinitions.Definition;
import glacier.builder.cdefinitions.MaterialDef;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.HashMap;

public class VarManager {
    private HashMap<ParserRuleContext, HashMap<String, Definition>> userVarMap = new HashMap<>();
    private HashMap<DefaultScope, HashMap<String, Definition>> defaultVarMap = new HashMap<>();

    public VarManager() {
        populateDefaulScopes();
    }

    private <KEY> void saveVar(HashMap<KEY, HashMap<String, Definition>> map, KEY key, Definition definition) {
        if (!map.containsKey(key)) {
            map.put(key, new HashMap<>());
        }
        map.get(key).put(definition.getName(), definition);
    }

    public void saveVar(ParserRuleContext ruleContext, Definition definition) {
        System.out.println("saving: " + definition.getName() + " for scope " + ruleContext);
        saveVar(userVarMap, ruleContext, definition);
    }

    public boolean varExists(ParserRuleContext ruleContext, String name) {
        return userVarMap.containsKey(ruleContext) ? userVarMap.get(ruleContext).containsKey(name) : false;
    }

    public boolean varExists(DefaultScope scope, String name) {
        return defaultVarMap.containsKey(scope) ? defaultVarMap.get(scope).containsKey(name) : false;
    }

    public Definition getVar(DefaultScope scope, String name) {
        return defaultVarMap.get(scope).get(name);
    }

    public Definition getVar(ParserRuleContext ruleContext, String name) {
        return userVarMap.get(ruleContext).get(name);
    }

    public static enum DefaultScope {
        VERT_IN, MAT, TRANS
    }

    private void populateDefaulScopes() {
        for (VertexInDef def : VertexInDef.values()) {
            saveVar(defaultVarMap, DefaultScope.VERT_IN, def);
        }
        for (MaterialDef def : MaterialDef.values()) {
            saveVar(defaultVarMap, DefaultScope.MAT, def);
        }
        for (TransDef def : TransDef.values()) {
            saveVar(defaultVarMap, DefaultScope.TRANS, def);
        }
    }
}

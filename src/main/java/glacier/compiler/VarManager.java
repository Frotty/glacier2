package glacier.compiler;

import antlr4.GlacierParser;
import glacier.builder.cdefinitions.Definition;
import glacier.builder.cdefinitions.MaterialDef;
import glacier.builder.cdefinitions.TransDef;
import glacier.builder.cdefinitions.VertexInDef;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collection;
import java.util.HashMap;

public class VarManager {
    private HashMap<ParserRuleContext, HashMap<String, Definition>> userVarMap = new HashMap<>();
    private HashMap<GlobalScope, HashMap<String, Definition>> defaultVarMap = new HashMap<>();

    public VarManager() {
        populateDefaulScopes();
    }

    private <KEY> boolean saveVar(HashMap<KEY, HashMap<String, Definition>> map, KEY key, Definition definition) {
        if (!map.containsKey(key)) {
            map.put(key, new HashMap<>());
        }
        if (!map.get(key).containsKey(definition.getName())) {
            map.get(key).put(definition.getName(), definition);
            return true;
        }
        return false;
    }

    public void saveVar(GlobalScope scope, Definition definition) {
        System.out.println("saving: " + definition.getName() + " for scope " + scope);
        saveVar(defaultVarMap, scope, definition);
    }

    public void saveVar(ParserRuleContext ruleContext, Definition definition) {
        System.out.println("saving: " + definition.getName() + " for scope " + ruleContext);
        saveVar(userVarMap, ruleContext, definition);
    }

    public boolean varExists(ParserRuleContext ruleContext, String name) {
        return userVarMap.containsKey(ruleContext) ? userVarMap.get(ruleContext).containsKey(name) : false;
    }

    public boolean varExists(GlobalScope scope, String name) {
        return defaultVarMap.containsKey(scope) ? defaultVarMap.get(scope).containsKey(name) : false;
    }

    public boolean globalAccessValid(GlacierParser.ExprContext expr, ParserRuleContext shaderParent) {
        GlobalScope scope = GlobalScope.getFromName(expr.ieD.getText(), shaderParent instanceof GlacierParser.VertexShaderContext);
        if (scope != null) {
            return varExists(scope, expr.varName.getText());
        }
        return varExists(shaderParent, expr.varName.getText());
    }

    public Definition getVar(GlobalScope scope, String name) {
        return defaultVarMap.get(scope).get(name);
    }

    public Collection<Definition> getVars(GlobalScope scope) {
        return defaultVarMap.get(scope).values();
    }

    public Definition getVar(ParserRuleContext ruleContext, String name) {
        return userVarMap.get(ruleContext).get(name);
    }

    public void incrementUsage(GlacierParser.ExprContext expr, ParserRuleContext shaderParent) {
        boolean inVertShader = shaderParent instanceof GlacierParser.VertexShaderContext;
        GlobalScope scope = GlobalScope.getFromName(expr.ieD.getText(), inVertShader);
        if (scope != null) {
            getVar(scope, expr.varName.getText()).incrementUsage(inVertShader);
        } else {
            getVar(shaderParent, expr.varName.getText()).incrementUsage(inVertShader);
        }
    }

    public Definition getVar(String globalScope, String name, ParserRuleContext shaderPart) {
        return getVar(GlobalScope.getFromName(globalScope, shaderPart instanceof GlacierParser.VertexShaderContext), name);
    }


    public static enum GlobalScope {
        VERT_IN("in", ""), MAT("mat", "mat"), TRANS("trans", "trans"), VERT_OUT("out", ""), FRAG_OUT("", "out"), UNI("uni", "uni");

        private final String vertName;
        private final String fragName;

        public static GlobalScope getFromName(String name, boolean inVertShader) {
            for (GlobalScope scope : GlobalScope.values()) {
                if (inVertShader ? scope.vertName.equals(name) : scope.fragName.equals(name)) {
                    return scope;
                }
            }
            return null;
        }

        GlobalScope(String vertName, String fragName) {
            this.vertName = vertName;
            this.fragName = fragName;
        }
    }

    private void populateDefaulScopes() {
        for (VertexInDef def : VertexInDef.values()) {
            saveVar(GlobalScope.VERT_IN, def);
        }
        for (MaterialDef def : MaterialDef.values()) {
            saveVar(GlobalScope.MAT, def);
        }
        for (TransDef def : TransDef.values()) {
            saveVar(GlobalScope.TRANS, def);
        }
    }
}

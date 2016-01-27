package glacier.builder.cdefinitions;

public enum VertexInDef implements Definition {
    POS("pos", "vec3"), NORMAL("normal", "vec3"), TEXCOORD("texCoord", "vec2");

    private final String name;
    private final String type;

    VertexInDef(String name, String type) {
        this.name = name;
        this.type = type;
    }

    int usages = 0;

    @Override
    public int getUsages() {
        return usages;
    }

    @Override
    public void incrementUsage() {
        usages++;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String generateLocVarDef() {
        return "";
    }

    @Override
    public String generateInit() {
        return "";
    }

    @Override
    public String generateBlock() {
        return "";
    }

    @Override
    public String generateInstance() {
        return "";
    }

    @Override
    public String generateShaderInDef() {
        return "";
    }

    @Override
    public String generateShaderOutDef() {
        return "";
    }

    @Override
    public String generateShaderUniDef() {
        return "";
    }

    @Override
    public String generateLocVarSet() {
        return null;
    }


}

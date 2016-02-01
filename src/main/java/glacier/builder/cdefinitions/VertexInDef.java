package glacier.builder.cdefinitions;

public enum VertexInDef implements Definition {
    POS("pos", "position", "vec3"), NORMAL("normal", "normal", "vec3"), TEXCOORD("texCoord", "textCoord0", "vec2"), COLOR("color", "color", "vce4");

    private final String name;
    private final String attributeName;
    private final String type;


    VertexInDef(String name, String attributeName, String type) {
        this.name = name;
        this.attributeName = attributeName;
        this.type = type;
    }

    private int fragUsages = 0;
    private int vertUsages = 0;


    @Override
    public int getUsages(boolean vert) {
        return vert ? vertUsages : fragUsages;
    }

    @Override
    public void incrementUsage(boolean vert) {
        if (vert) {
            vertUsages++;
        } else {
            fragUsages++;
        }
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
    public String generateShaderDef() {
        return "in " + getType() + " a_" + attributeName + ";\n" ;
    }

    @Override
    public String generateShaderAccess() {
        return "a_" + attributeName;
    }

    @Override
    public String generateLocVarSet() {
        return null;
    }


}

package glacier.builder.cdefinitions;

public class VertexOutDef implements Definition {
    private final String name;
    private final String type;


    public VertexOutDef(String name, String type) {
        this.name = name;
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
        return "out " + getType() + " v_" + name + ";\n" ;
    }

    @Override
    public String generateShaderAccess() {
        return "v_" + name;
    }

    @Override
    public String generateLocVarSet() {
        return null;
    }


}

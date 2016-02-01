package glacier.builder.cdefinitions;


public enum MaterialDef implements Definition {
    DIFFUSETEXTURE() {
        @Override
        public String generateShaderDef() {
            return "uniform sampler2D m_diffuseTexture;";
        }

        @Override
        public String generateShaderAccess() {
            return "m_diffuseTexture";
        }

    };

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
    public String generateLocVarSet() {
        return null;
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
        return "";
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

}

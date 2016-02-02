package glacier.builder.cdefinitions;


public enum TransDef implements Definition {
    WORLD("u_MvpTrans", "m_mvpTrans", false, true),
    NORMAL("u_NormalTrans", "m_normalTrans", false, false),
    PROJ("u_ProjTrans", "m_projTrans", false, false),
    VIEW("u_ViewTrans", "m_viewTrans", false, false),
    MVP("u_WorldTrans", "m_worldTrans", true, false);
    private final boolean needsM3;
    private final boolean needsM4;
    private final String locVarName;
    private final String uniformName;

    TransDef(String locVarName, String uniformName, boolean needsM3, boolean needsM4) {
        this.locVarName = locVarName;
        this.uniformName = uniformName;
        this.needsM3 = needsM3;
        this.needsM4 = needsM4;
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
    public String generateShaderAccess() {
        return uniformName;
    }


    @Override
    public String generateLocVarSet() {
        return "\t\t" + locVarName + " = program.getUniformLocation(\"" + uniformName + "\");\n";
    }

    @Override
    public String generateLocVarDef() {
        if (needsM3) {
            return "\tprivate int " + locVarName + ";\n\tprivate final Matrix3 tempMat3 = new Matrix3();\n";
        } else if (needsM4) {
            return "\tprivate int " + locVarName + ";";

        } else {
            return "\tprivate int " + locVarName + ";\n\tprivate final Matrix3 tempMat4 = new Matrix4();\n";
        }
    }

    @Override
    public String generateInit() {
        return "";
    }

    @Override
    public String generateBlock() {
        switch (this) {
            case MVP:
                return "";
            case NORMAL:
                return "";
            case PROJ:
                return "\t\tprogram.setUniformMatrix(u_ProjTrans, camera.projection);\n";
            case VIEW:
                return "\t\tprogram.setUniformMatrix(u_ViewTrans, camera.view);\n";
            case WORLD:
                return "";
            default:
                break;
        }
        return "";
    }

    @Override
    public String generateInstance() {
        switch (this) {
            case MVP:
                return "\t\ttempMat4.set(camera.combined).mul(renderable.worldTransform);\n\t\tprogram.setUniformMatrix(u_MvpTrans, tempMat4);\n";
            case NORMAL:
                return "\t\ttempMat3.set(renderable.worldTransform).inv().transpose();\n\t\tprogram.setUniformMatrix(u_NormalTrans, tempMat3);\n";
            case PROJ:
                return "";
            case VIEW:
                return "";
            case WORLD:
                return "\t\tprogram.setUniformMatrix(u_WorldTrans, renderable.worldTransform);\n";
            default:
                break;

        }
        return "";
    }

    @Override
    public String generateShaderDef() {
        switch (this) {
            case MVP:
                return "uniform mat4 t_mvpTrans;";
            case NORMAL:
                return "uniform mat3 t_normalTrans;";
            case PROJ:
                return "uniform mat4 t_projTrans;";
            case VIEW:
                return "uniform mat4 t_viewTrans;";
            case WORLD:
                return "uniform mat4 t_worldTrans;";
            default:
                break;

        }
        return null;
    }

    @Override
    public String getName() {
        switch (this) {
            case MVP:
                return "mvpTrans";
            case NORMAL:
                return "normalTrans";
            case PROJ:
                return "projTrans";
            case VIEW:
                return "viewTrans";
            case WORLD:
                return "worldTrans";
            default:
                break;

        }
        return null;
    }

    @Override
    public String getType() {
        // TODO Auto-generated method stub
        return null;
    }

}

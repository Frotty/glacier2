package glacier.builder.cdefinitions;

public interface Definition {
    String getName();

    String getType();

    String generateLocVarSet();

    String generateLocVarDef();

    String generateInit();

    String generateBlock();

    String generateInstance();

    String generateShaderDef();

    String generateShaderAccess();

    int getUsages(boolean vert);

    void incrementUsage(boolean vert);

}

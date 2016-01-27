package glacier.builder.cdefinitions;

public interface Definition {
    String getName();

    abstract String getType();

    abstract String generateLocVarSet();

    abstract String generateLocVarDef();

    abstract String generateInit();

    abstract String generateBlock();

    abstract String generateInstance();

    abstract String generateShaderInDef();

    abstract String generateShaderOutDef();

    abstract String generateShaderUniDef();

    abstract int getUsages();

    abstract void incrementUsage();

}

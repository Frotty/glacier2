package glacier.builder.cdefinitions;

public interface Definition {
	public abstract String getName();
	public abstract String getType();
	public abstract String generateLocVarSet();
	public abstract String generateLocVarDef();
	public abstract String generateInit();
	public abstract String generateBlock();
	public abstract String generateInstance();
	public abstract String generateShaderInDef();
	public abstract String generateShaderOutDef();
	public abstract String generateShaderUniDef();
}

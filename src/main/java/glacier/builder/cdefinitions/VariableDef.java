package glacier.builder.cdefinitions;

public class VariableDef implements Definition {
	public String name;
	public String type;
	public int uses = 0;

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
	
	public VariableDef(String name,	String type) {
		this.name = name;
		this.type = type;
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
	public String generateLocVarSet() {
		return null;
	}

	@Override
	public String generateLocVarDef() {
		return null;
	}

	@Override
	public String generateInit() {
		return null;
	}

	@Override
	public String generateBlock() {
		return null;
	}

	@Override
	public String generateInstance() {
		return null;
	}

	@Override
	public String generateShaderDef() {
		return null;
	}

	@Override
	public String generateShaderAccess() {
		return "l_" + name;
	}

}

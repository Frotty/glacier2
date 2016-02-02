package glacier.builder.cdefinitions;

public class UniformDef implements Definition {
	private final String name;
	private String type;
	private String nameL;
	private String nameU;
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

	public UniformDef(String type, String name) {
		this.type = type;
		this.name = name;
		this.nameL = "u_" + name.substring(0, 1).toLowerCase() + name.substring(1);
		this.nameU = "u_" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public String generateLocVarDef() {
		return "\tprivate int " + nameU + ";\n";
	}

	public String generateLocVarSet() {
		return "\t\t" + nameU + " = program.getUniformLocation(\"" + nameL + "\");\n";
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
		return "uniform " + type + " " + nameL + ";\n";
	}

	@Override
	public String generateShaderAccess() {
		return "u_" + getName();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return null;
	}
}

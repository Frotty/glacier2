package glacier.builder.cdefinitions;

public enum AttributeDef implements Definition {
	POS, NORMAL, TEXCOORD;
	
	@Override
	public String getType() {
		switch(this) {
		case NORMAL:
			return "vec3";
		case POS:
			return "vec3";
		case TEXCOORD:
			return "vec2";
		default:
			return "";
		}
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
	public String getName() {
		return "";
	}

	@Override
	public String generateLocVarSet() {
		// TODO Auto-generated method stub
		return null;
	}


}

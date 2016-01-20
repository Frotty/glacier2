package glacier.builder.cdefinitions;


public enum MaterialDef implements Definition {
	DIFFUSETEXTURE() {
		@Override
		public String generateShaderUniDef() {
			return "uniform sampler2D m_diffuseTexture;";
		}
	}, DIFFUSECOLOR;


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
	public String generateShaderInDef() {
		return "";
	}

	@Override
	public String generateShaderOutDef() {
		return "";
	}

	@Override
	public String generateShaderUniDef() {
		return null;
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

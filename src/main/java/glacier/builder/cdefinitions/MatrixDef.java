package glacier.builder.cdefinitions;


public enum MatrixDef implements Definition {
	WORLD, NORMAL, PROJ, VIEW, MVP;

	@Override
	public String generateLocVarSet() {
		switch (this) {
		case MVP:
			return "\t\tu_MvpTrans = program.getUniformLocation(\"m_mvpTrans\");\n";
		case NORMAL:
			return "\t\tu_NormalTrans = program.getUniformLocation(\"m_normalTrans\");\n";
		case PROJ:
			return "\t\tu_ProjTrans = program.getUniformLocation(\"m_projTrans\");\n";
		case VIEW:
			return "\t\tu_ViewTrans = program.getUniformLocation(\"m_viewTrans\");\n";
		case WORLD:
			return "\t\tu_WorldTrans = program.getUniformLocation(\"m_worldTrans\");\n";
		default:
			break;
		}
		return null;
	}

	@Override
	public String generateLocVarDef() {
		switch (this) {
		case MVP:
			return "\tprivate int u_MvpTrans;\n\tprivate final Matrix4 tempMat4 = new Matrix4();\n";
		case NORMAL:
			return "\tprivate int u_NormalTrans;\n";
		case PROJ:
			return "\tprivate int u_ProjTrans;\n";
		case VIEW:
			return "\tprivate int u_ViewTrans;\n";
		case WORLD:
			return "\tprivate int u_WorldTrans;\n\tprivate final Matrix3 tempMat3 = new Matrix3();\n";
		default:
			break;
		}
		return "";
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
	public String generateShaderInDef() {
		return "";
	}

	@Override
	public String generateShaderOutDef() {
		return "";
	}

	@Override
	public String generateShaderUniDef() {
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
			return "t_mvpTrans";
		case NORMAL:
			return "t_normalTrans";
		case PROJ:
			return "t_projTrans";
		case VIEW:
			return "t_viewTrans";
		case WORLD:
			return "t_worldTrans";
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

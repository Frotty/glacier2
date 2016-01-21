package glacier.visitors;

import antlr4.GlacierBaseVisitor;
import antlr4.GlacierParser.*;
import glacier.builder.cdefinitions.UniformDef;
import glacier.builder.cdefinitions.VariableDef;
import glacier.error.GlacierErrorType;
import glacier.parser.CompilationResult;
import glacier.parser.VariableManager;
import glacier.parser.VariableManager.GlobalType;

public class EvalVisitor extends GlacierBaseVisitor<String> {
    private final CompilationResult compilationResult;
    private VariableManager varManager;
    private boolean vert = true;
    public HeaderVisitor headerV;

    public EvalVisitor(VariableManager variableManager, CompilationResult compilationResult) {
        varManager = variableManager;
        this.compilationResult = compilationResult;
    }

    @Override
    public String visitShaderProg(ShaderProgContext ctx) {
        compilationResult.lastProcessedToken = ctx.start;
        if (VisitorUtil.hasSize(ctx.nameDeclaration())) {
            if (ctx.nameDeclaration().shaderName != null) {
                compilationResult.name = ctx.nameDeclaration().shaderName.getText() + "Gen";
                compilationResult.lastProcessedToken = ctx.nameDeclaration().stop;
                compilationResult.log("Name: " + compilationResult.name);
            } else {
                compilationResult.error(ctx.nameDeclaration(), GlacierErrorType.INVALID_NAME);
            }
        } else {
            compilationResult.error(compilationResult.lastProcessedToken, GlacierErrorType.MISSING_NAME);
        }
        if (VisitorUtil.hasSize(ctx.glacierHeader())) {
            compilationResult.log("Header Visitor..");
            headerV = new HeaderVisitor(compilationResult);
            headerV.visit(ctx);
        } else {
            compilationResult.error(compilationResult.lastProcessedToken, GlacierErrorType.MISSING_HEADER);
        }
        if (VisitorUtil.hasSize(ctx.vertexShader())) {
            compilationResult.lastProcessedToken = ctx.vertexShader().start;
            visit(ctx.vertexShader());
            compilationResult.lastProcessedToken = ctx.vertexShader().stop;
        } else {
            compilationResult.error(ctx.vertexShader(), GlacierErrorType.MISSING_VERTEX);
        }
        vert = false;
        if (VisitorUtil.hasSize(ctx.fragmentShader())) {
            compilationResult.lastProcessedToken = ctx.fragmentShader().start;
            visit(ctx.fragmentShader());
            compilationResult.lastProcessedToken = ctx.fragmentShader().stop;
        } else {
            compilationResult.error(ctx.fragmentShader(), GlacierErrorType.MISSING_FRAGMENT);
        }
        return null;
    }

    @Override
    public String visitOutBlock(OutBlockContext ctx) {
        System.out.println("out");
        if (VisitorUtil.hasSize(ctx.outArgs)) {
            for (VarDefContext outdef : ctx.outArgs.vardefs) {
                System.out.println("added: " + outdef.varName.getText());
                VariableDef vdef;
                if (outdef.varType != null) {
                    vdef = new VariableDef(outdef.varName.getText(), outdef.varType.getText());
                } else {
                    vdef = new VariableDef(outdef.varName.getText(), "vec4");
                }
                varManager.saveGlobal(GlobalType.OUT, vert, vdef);
            }
        }
        return "";
    }

    @Override
    public String visitUniformsBlock(UniformsBlockContext ctx) {
        if (VisitorUtil.hasSize(ctx.uniformArgs)) {
            for (VarDefContext unidef : ctx.uniformArgs.vardefs) {
                UniformDef def = new UniformDef(unidef.varType.getText(), unidef.varName.getText());
                varManager.saveGlobal(GlobalType.UNI, vert, def);
            }
        }
        return "uniBlock";
    }

    @Override
    public String visitExprMemberVar(ExprMemberVarContext ctx) {
        if (ctx.ieDirect != null) {
            // Is Implicit Access
            switch (ctx.ieDirect.getText()) {
                case "in":
                    if (varManager.globalExists(GlobalType.IN, vert, ctx.varname.getText())) {
                        return toOutInVar(ctx.varname.getText());
                    } else {
                        throw new UnsupportedOperationException("Undefine in variable " + ctx.varname.getText());
                    }
                case "out":
                    if (varManager.globalExists(GlobalType.OUT, vert, ctx.varname.getText())) {
                        return toOutInVar(ctx.varname.getText());
                    } else {
                        throw new UnsupportedOperationException("Undefined out variable: " + ctx.varname.getText());
                    }
                default:
                    compilationResult.error(ctx, GlacierErrorType.INVALID_IEDIRECTIVE);
            }
        }
        return "";
    }

    private String toOutInVar(String text) {
        switch (text) {
            case "pos":
                return "v_position";
            case "normal":
                return "v_normal";
            case "texCoord":
                return "v_texCoord0";
            default:
                return "v_" + text;
        }
    }

//	public String getVarType(String iED, String text) {
//		switch (iED) {
//		case "in":
//			if (inReg.containsKey(text)) {
//				return inReg.get(text).type;
//			}
//		case "out":
//			if (outReg.containsKey(text)) {
//				return outReg.get(text).type;
//			}
//		case "mats":
//			if (matReg.containsKey(text)) {
//				switch (text) {
//				case "mvp":
//					return MatrixDef.MVP.generateShaderUniDef().substring(8, 12);
//				case "view":
//					return MatrixDef.VIEW.generateShaderUniDef().substring(8, 12);
//				case "normal":
//					return MatrixDef.NORMAL.generateShaderUniDef().substring(8, 12);
//				case "world":
//					return MatrixDef.WORLD.generateShaderUniDef().substring(8, 12);
//				case "proj":
//					return MatrixDef.PROJ.generateShaderUniDef().substring(8, 12);
//				}
//			}
//		}
//		return "unknown";
//	}

}

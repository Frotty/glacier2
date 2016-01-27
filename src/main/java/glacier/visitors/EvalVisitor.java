package glacier.visitors;

import antlr4.GlacierParser.*;
import glacier.builder.cdefinitions.UniformDef;
import glacier.builder.cdefinitions.VariableDef;
import glacier.error.GlacierErrorType;
import glacier.parser.CompilationResult;
import glacier.parser.VarManager;
import org.antlr.v4.runtime.ParserRuleContext;

public class EvalVisitor extends ExtendedVisitor {
    private final CompilationResult compilationResult;
    private final EvalResult evalResult;
    private final VarManager varManager;

    private boolean vert = true;

    public EvalVisitor(VarManager variableManager, CompilationResult compilationResult) {
        varManager = variableManager;
        this.compilationResult = compilationResult;
        this.evalResult = new EvalResult();
    }

    @Override
    public String visitShaderProg(ShaderProgContext ctx) {
        compilationResult.lastProcessedToken = ctx.start;
        if (VisitorUtil.hasSize(ctx.nameDeclaration())) {
            compilationResult.log("Name exists.. <" + ctx.nameDeclaration().getText() + ">");
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
            compilationResult.log("Header Visitor.. <" + ctx.glacierHeader().getText() + ">");
            HeaderVisitor headerV = new HeaderVisitor(evalResult, compilationResult);
            headerV.visit(ctx.glacierHeader());
        } else {
            compilationResult.error(compilationResult.lastProcessedToken, GlacierErrorType.MISSING_HEADER);
        }
        if (VisitorUtil.hasSize(ctx.vertexShader().shaderBlock())) {
            compilationResult.log("vertex shader exists");
            compilationResult.lastProcessedToken = ctx.vertexShader().start;
            visit(ctx.vertexShader());
            compilationResult.lastProcessedToken = ctx.vertexShader().stop;
        } else {
            compilationResult.error(ctx.vertexShader(), GlacierErrorType.MISSING_VERTEX);
        }
        vert = false;
        if (VisitorUtil.hasSize(ctx.fragmentShader())) {
            compilationResult.log("fragment shader exists : <" + ctx.fragmentShader().getText() + ">");
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
        if (VisitorUtil.hasSize(ctx.outArgs)) {
            System.out.println("visit: " + ctx.outArgs.vardefs.size());
            for (VarDefContext outdef : ctx.outArgs.vardefs) {
                System.out.println("added: " + outdef.varName.getText());
                VariableDef vdef;
                if (outdef.varType != null) {
                    vdef = new VariableDef(outdef.varName.getText(), outdef.varType.getText());
                } else {
                    vdef = new VariableDef(outdef.varName.getText(), "vec4");
                }
                varManager.saveVar(getShaderPart(ctx), vdef);
            }
        }
        return "";
    }

    @Override
    public String visitUniformsBlock(UniformsBlockContext ctx) {
        System.out.println("visit uni");
        if (VisitorUtil.hasSize(ctx.uniformArgs)) {
            for (VarDefContext unidef : ctx.uniformArgs.vardefs) {
                UniformDef def = new UniformDef(unidef.varType.getText(), unidef.varName.getText());
                varManager.saveVar(getShaderPart(ctx), def);
            }
        }
        return "";
    }

    @Override
    public String visitExprMemberVar(ExprMemberVarContext ctx) {
        ParserRuleContext shaderPart = getShaderPart(ctx);
        if (ctx.ieDirect != null) {
            System.out.println("Visiting: " + ctx.getText() + " : " + shaderPart + " varname: <" + ctx.varname.getText() + ">");
            // Is Implicit Access
            String directive = ctx.ieDirect.getText().trim().toLowerCase();
            switch (directive) {
                case "in":
                    if (varManager.varExists(VarManager.DefaultScope.VERT_IN, ctx.varname.getText())) {
                        varManager.getVar(VarManager.DefaultScope.VERT_IN, ctx.varname.getText()).incrementUsage();
                    } else {
                        compilationResult.error(ctx, GlacierErrorType.VAR_NOT_FOUND, ctx.varname.getText());
                    }
                    break;
                case "out":
                    if (varManager.varExists(shaderPart, ctx.varname.getText())) {
                        varManager.getVar(shaderPart, ctx.varname.getText()).incrementUsage();
                    } else {
                        compilationResult.error(ctx, GlacierErrorType.VAR_NOT_FOUND, ctx.varname.getText(), shaderPart.toString());
                    }
                    break;
                default:
                    compilationResult.error(ctx, GlacierErrorType.INVALID_IEDIRECTIVE, directive);
                    break;
            }
        }
        return "";
    }

}

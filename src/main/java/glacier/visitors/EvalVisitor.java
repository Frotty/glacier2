package glacier.visitors;

import antlr4.GlacierParser.*;
import glacier.builder.cdefinitions.UniformDef;
import glacier.builder.cdefinitions.VariableDef;
import glacier.error.GlacierErrorType;
import glacier.parser.CompilationResult;
import glacier.parser.VarManager;
import org.antlr.v4.runtime.ParserRuleContext;

public class EvalVisitor extends ExtendedVisitor<String> {
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
            compilationResult.log("Name exists..");
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
            HeaderVisitor headerV = new HeaderVisitor(evalResult, compilationResult);
            headerV.visit(ctx.glacierHeader());
        } else {
            compilationResult.error(compilationResult.lastProcessedToken, GlacierErrorType.MISSING_HEADER);
        }
        if (VisitorUtil.hasSize(ctx.vertexShader().shaderBlock())) {
            compilationResult.log("vertex shader exists");
            compilationResult.lastProcessedToken = ctx.vertexShader().start;
            super.visit(ctx.vertexShader().shaderBlock());
            compilationResult.lastProcessedToken = ctx.vertexShader().stop;
        } else {
            compilationResult.error(ctx.vertexShader(), GlacierErrorType.MISSING_VERTEX);
        }
        vert = false;
        if (VisitorUtil.hasSize(ctx.fragmentShader()) && VisitorUtil.hasSize(ctx.fragmentShader().shaderBlock())) {
            compilationResult.log("fragment shader exists");
            compilationResult.lastProcessedToken = ctx.fragmentShader().start;
            super.visit(ctx.fragmentShader().shaderBlock());
            compilationResult.lastProcessedToken = ctx.fragmentShader().stop;
        } else {
            compilationResult.error(ctx.fragmentShader(), GlacierErrorType.MISSING_FRAGMENT);
        }
        return null;
    }

    @Override
    public String visitShaderBlock(ShaderBlockContext ctx) {
        compilationResult.log("visit shaderBlock");
        super.visitShaderBlock(ctx);
        return "" ;
    }

    @Override
    public String visitFunctionBlock(FunctionBlockContext ctx) {
        compilationResult.log("visit Func");
        return super.visitFunctionBlock(ctx);
    }

    @Override
    public String visitStatementsBlock(StatementsBlockContext ctx) {
        compilationResult.log("visit StatementBlock");
        return super.visitStatementsBlock(ctx);
    }

    @Override
    public String visitStatement(StatementContext ctx) {
        compilationResult.log("visit Statement");
        return super.visitStatement(ctx);
    }

    @Override
    public String visitExpr(ExprContext ctx) {
        compilationResult.log("visit Expression: <" + ctx.getText() + ">");
        if(VisitorUtil.hasSize(ctx.ieD)) {
            if(varManager.globalAccessValid(ctx, getShaderPart(ctx))) {
                varManager.incrementUsage(ctx, getShaderPart(ctx));
                compilationResult.log("Valid Global Var Access");
            }
        }
        return super.visitExpr(ctx);
    }

    @Override
    public String visitExprPrimary(ExprPrimaryContext ctx) {
        compilationResult.log("visit Primary Expression: <" + ctx.getText() + ">");
        return super.visitExprPrimary(ctx);
    }

    @Override
    public String visitLocalVarDef(LocalVarDefContext ctx) {
        compilationResult.log("Saving local var: " + ctx.name.getText());
        varManager.saveVar(ctx.getParent(), new VariableDef(ctx.name.getText(), ctx.typeName.getText()));
        return super.visitLocalVarDef(ctx);
    }

    @Override
    public String visitExprAssignable(ExprAssignableContext ctx) {
        compilationResult.log("visit ExprAssignable");
        return super.visitExprAssignable(ctx);
    }

    @Override
    public String visitExprVarAccess(ExprVarAccessContext ctx) {
        compilationResult.log("visit Var Access");
        return super.visitExprVarAccess(ctx);
    }

    @Override
    public String visitStmtSet(StmtSetContext ctx) {
        compilationResult.log("visit Stmt Set");
        return super.visitStmtSet(ctx);
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
        compilationResult.log("exprmbr");
        ParserRuleContext shaderPart = getShaderPart(ctx);
        if (ctx.ieDirect != null) {
            System.out.println("Visiting: " + ctx.getText() + " : " + shaderPart + " varname: <" + ctx.varname.getText() + ">");
            // Is Implicit Access
            String directive = ctx.ieDirect.getText().trim().toLowerCase();
            switch (directive) {
                case "in":
                    if (varManager.varExists(VarManager.GlobalScope.VERT_IN, ctx.varname.getText())) {
                        varManager.getVar(VarManager.GlobalScope.VERT_IN, ctx.varname.getText()).incrementUsage(vert);
                    } else {
                        compilationResult.error(ctx, GlacierErrorType.VAR_NOT_FOUND, ctx.varname.getText());
                    }
                    break;
                case "out":
                    if (varManager.varExists(shaderPart, ctx.varname.getText())) {
                        varManager.getVar(shaderPart, ctx.varname.getText()).incrementUsage(shaderPart instanceof VertexShaderContext);
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

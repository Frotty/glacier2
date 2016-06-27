package glacier.print;

import antlr4.GlacierParser.*;
import glacier.builder.cdefinitions.Definition;
import glacier.compiler.CompilationResult;
import glacier.compiler.VarManager;
import glacier.visitors.VisitorUtil;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;

public class GLSLPrintVisitor extends PrintVisitor<java.lang.String> {
    private ArrayList<String> outVars = new ArrayList<>();
    private int layoutCount = 0;
    private VarManager varManager;
    private CompilationResult result;


    public GLSLPrintVisitor(VarManager varManager, CompilationResult result) {
        this.varManager = varManager;
        this.result = result;
    }

    @Override
    public String visitVertexShader(VertexShaderContext ctx) {
        print("// Vertex-Shader generated by Glacier\n#version 300 es\nprecision mediump float;\n\n");
        for (Definition varDef : varManager.getVars(VarManager.GlobalScope.VERT_IN)) {
            result.log("check: " + varDef.getName());
            if (varDef.getUsages(true) > 0) {
                result.log("has usages: " + varDef.generateShaderDef());
                print(varDef.generateShaderDef());
            }
        }
        return super.visitVertexShader(ctx);
    }

    @Override
    public String visitFragmentShader(FragmentShaderContext ctx) {
        print("// Fragment-Shader generated by Glacier\n#version 300 es\nprecision mediump float;\n\n");
        return super.visitFragmentShader(ctx);
    }

    @Override
    public String visitOutBlock(OutBlockContext ctx) {
        return super.visitOutBlock(ctx);
    }

    public void visitUniformDef(VarDefContext ctx) {
        // Uniforms
        print("uniform " + ctx.varType.getText() + " u_" + ctx.varName.getText() + ";");
    }

    public void visitOutVarDef(VarDefContext ctx, boolean frag) {
        if (frag) {
            // Fragment-Shader Rendertarget
            print("layout(location = " + layoutCount + ") out vec4 rt_" + ctx.varName.getText() + ";");
            layoutCount++;
        } else {
            // Varying in Vertex
            if (ctx.varType != null) {
                print("out " + ctx.varType.getText() + " v_" + ctx.varName.getText() + ";");
                outVars.add(ctx.varType.getText() + " v_" + ctx.varName.getText() + ";");
            } else {
                print("out vec4 v_" + ctx.varName.getText() + ";");
                outVars.add("vec4 v_" + ctx.varName.getText() + ";");
            }
        }
    }

    @Override
    public String visitVarDef(VarDefContext ctx) {
        // Check The Block-Type
        ParserRuleContext parent = ctx.getParent().getParent();
        boolean frag = parent.getParent().getParent() instanceof FragmentShaderContext;
        if (parent instanceof OutBlockContext) {
            visitOutVarDef(ctx, frag);
        } else if (parent instanceof UniformsBlockContext) {
            visitUniformDef(ctx);
        }
        newline();
        return "";
    }

    @Override
    public String visitExprFunctionCall(ExprFunctionCallContext funcCall) {
//		System.out.println("called: " + funcCall.funcName.getText());
        print(funcCall.funcName.getText() + "(");

        visit(funcCall.params);
        print(")");
        return "";
    }

    @Override
    public String visitExprList(ExprListContext exprList) {
        for (ExprContext expr : exprList.exprs) {
            visit(expr);
            print(", ");
        }
        removeLastChars(2);
        return "";
    }

    @Override
    public String visitExprPrimary(ExprPrimaryContext expr) {
        if (expr.funcCall != null) {
            visit(expr.funcCall);
        } else if (expr.varname != null) {
            result.log("visit varname: " + expr.varname.getText());
            if (expr.varname.getText().length() > 1 && expr.varname.getText().charAt(1) == '_') {
                print(expr.varname.getText());
            } else {
                print("l_" + expr.varname.getText());
            }
        } else if (expr.atom != null) {
//			System.out.println("<<<<<+ " + expr.atom.getText());
            print(expr.atom.getText());
        }
        return "";
    }

    @Override
    public String visitExpr(ExprContext expr) {
        result.log("visit Expr: " + expr.getText());
//		System.out.println("expr<<<<<<<" + expr.getText());
        if (expr.left != null) {
//			System.out.println("expr1<<<<<<<" + expr.left.getText());
            visit(expr.left);
            if (expr.op.getText().equals("*")) {
                // Fix for "mul(mat4, vec3)"
                print(" " + expr.op.getText() + " ");
//                if (typeVisitor.visit(expr.left).equals("mat4") && typeVisitor.visit(expr.right).equals("vec3")) {
//                    print("vec4(");
//                    visit(expr.right);
//                    print(", 1.0)");
//                } else {
//                    visit(expr.right);
//                }
            } else {
                print(" " + expr.op.getText() + " ");
                visit(expr.right);
            }
        } else if (expr.op != null) {
            print(" " + expr.op.getText() + " ");
            visit(expr.right);
        } else if (expr.funcName != null) {
//			System.out.println("expr2<<<<<<<" + expr.funcName.getText());
            print(expr.funcName.getText() + "( ");
            visit(expr.params);
            print(")");
        } else if (expr.primary != null) {
//			System.out.println("expr3<<<<<<<" + expr.primary.getText());
            visit(expr.primary);
        } else if (expr.receiver != null) {
//			System.out.println("expr4<<<<<<<" + expr.receiver.getText());
            visit(expr.receiver);
            if (expr.dotsVar != null) {
                print("." + expr.varName.getText());
            }
        } else if (expr.ieD != null) {
            result.log("  -> is GlobalScopeAccess");
            String accessName = varManager.getVar(VarManager.GlobalScope.getFromName(expr.ieD.getText(),
                    getShaderPart(expr) instanceof VertexShaderContext),
                    expr.varName.getText()).generateShaderAccess();
            result.log("    -> access via: " + accessName);
            print(accessName);
        } else {
        }


        return "";
    }

    @Override
    public String visitStmtSet(StmtSetContext stmt) {
        result.log("visit SetStmt: " + stmt.getText());
        visit(stmt.left);
        print(" " + stmt.assignOp.getText() + " ");
        visit(stmt.right);
        print(";");
        newline();
        return "";
    }

    @Override
    public String visitExprAssignable(ExprAssignableContext ctx) {
        result.log("visit ExprAssignable: " + ctx.getText());
        return super.visitExprAssignable(ctx);
    }


    @Override
    public String visitExprMemberVar(ExprMemberVarContext expr) {
        if (VisitorUtil.hasSize(expr.ieDirect)) {
            result.log("visit IED: " + expr.ieDirect.getText() + " vert: " + (getShaderPart(expr) instanceof VertexShaderContext));
            VarManager.GlobalScope fromName = VarManager.GlobalScope.getFromName(expr.ieDirect.getText(), true);

            print(varManager.getVar(expr.ieDirect.getText(), expr.varname.getText(), getShaderPart(expr)).generateShaderAccess());
//            if (isFragment && expr.ieDirect.getText().equals("in")) {
//                print(expr.varname.getText());
//            } else if (expr.ieDirect.getText().equals("out")) {
//                print(expr.varname.getText());
//            } else if (expr.ieDirect.getText().equals("trans")) {
//                print(tsc.name);
//            } else if (sc != null) {
//                print(sc.name);
//            } else {
//                print(expr.varname.getText());
//            }
        }
        return "";
    }

    @Override
    public String visitExprVarAccess(ExprVarAccessContext expr) {
//        Shortcut sc = scman.getIEDShortcut(expr.varname.getText());
//        Shortcut msc = scman.getTransShortcut(expr.varname.getText());
//        if (sc != null) {
//            print(sc.name);
//        } else if (expr.varname.getText().equals("gl_Pos")) {
//            print("gl_Position");
//        } else if (msc != null) {
//            print(msc.name);
//        } else {
//            print("l_" + expr.varname.getText());
//        }
        return "";
    }

    @Override
    public String visitLocalVarDef(LocalVarDefContext ldef) {
        print(ldef.typeName.getText() + " l_" + ldef.name.getText());
        // Has initial expression?
        if (ldef.initial != null) {
            print(" = ");
            visit(ldef.initial);
            print(";");
            newline();
        } else {
            print(";");
            newline();
        }
        return "";
    }

    @Override
    public String visitFunctionBlock(FunctionBlockContext ctx) {
        print(ctx.returnType != null ? ctx.returnType.getText() + " " : "");
        print(ctx.funcName.getText());
        visit(ctx.arguements);
        print(" {");
        increaseIndent();
        newline();
        visit(ctx.body);
        decreaseIndent();
        newline();
        print("}");
        newline();
        newline();
        return "";
    }

    @Override
    public String visitStmtReturn(StmtReturnContext ctx) {
        print("return ");
        visit(ctx.expr());
        print(";");
        return "";
    }

    @Override
    public String visitArguments(ArgumentsContext ctx) {
        print("(");
        for (VarDefContext arg : ctx.vardefs) {
            print(arg.varType.getText() + " l_" + arg.varName.getText() + ", ");
        }
        if (ctx.vardefs.size() > 0) {
            removeLastChars(2);
        }
        print(")");
        return "";
    }

    @Override
    public String visitShaderBlock(ShaderBlockContext ctx) {
        super.visitShaderBlock(ctx);
//        if (ctx.inBlock() != null) {
//            visit(ctx.inBlock());
//        }
//        newline();
//        if (ctx.outBlock() != null) {
//            visit(ctx.outBlock());
//        }
//        newline();
//        if (ctx.transBlock() != null) {
//            visit(ctx.transBlock());
//        }
//        newline();
//        if (ctx.uniformsBlock() != null) {
//            visit(ctx.uniformsBlock());
//        }
//        newline();
//        if (ctx.functions != null) {
//            for (FunctionBlockContext f : ctx.functions) {
//                visit(f);
//            }
//        }
//        if (ctx.mainFunc != null) {
//            print("void main() {");
//            increaseIndent();
//            newline();
//            visit(ctx.mainFunc);
//            decreaseIndent();
//            newline();
//            print("}");
//            newline();
//        }

        return "";
    }

    public ArrayList<String> outVars() {
        return outVars;
    }
}

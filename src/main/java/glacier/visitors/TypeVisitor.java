package glacier.visitors;

import antlr4.GlacierBaseVisitor;
import antlr4.GlacierParser.ExprContext;
import antlr4.GlacierParser.ExprPrimaryContext;
import glacier.compiler.VarManager;

public class TypeVisitor extends GlacierBaseVisitor<String> {
	private VarManager varManager;

	public TypeVisitor(VarManager varManager, boolean vert) {
		this.varManager = varManager;
	}


	@Override
	public String visitExpr(ExprContext ctx) {
		if(ctx.op != null && ctx.left != null && ctx.right != null) {
			// Expression has an operator and 2 variables
			if(ctx.op.getText().equals("*")) {
				// Is a multiplication
				// Fix "mul(mat4, vec3)"
				if(visit(ctx.left).equals("mat4") && visit(ctx.right).equals("vec3")) {
					System.out.println("Fix mul(mat4, vec3)");
				}
			}
		}
		if(ctx.ieD != null && ctx.varName != null) {
//			System.out.println(ctx.varName.getText());
//			Definition global = varManager.getGlobal(GlobalType.valueOf(ctx.ieD.getText().toUpperCase()), vert, ctx.varName.getText());
//			if(global != null) {
//				return global.getType();
//			} else {
//				return "unknown";
//			}
			
		}
		return "";
	}

	@Override
	public String visitExprPrimary(ExprPrimaryContext ctx) {
		if (ctx.varname != null) {
			// Local variable ?
			System.out.println("local var");
		}
		return "primaryUnknown";
	}

}

package glacier.builder.cdefinitions;

import antlr4.GlacierParser.StatementsBlockContext;
import org.antlr.v4.runtime.tree.*;

import java.util.Arrays;

public class Function {
	private String name;
	private String[] args;
	private String returnType;
	private String body;
	private static Function mainFunc;
	
	public Function(String name, String[] args, String returnType, StatementsBlockContext body2, boolean main) {
		this.name = name;
		this.args = args;
		this.returnType = returnType;
		this.body = parseBody(body2);
		if(main) {
			mainFunc = this;
		}
		
	}
	
	private String parseBody(StatementsBlockContext body2) {
		StringBuilder sb = new StringBuilder();
		body2.accept(new ParseTreeVisitor<StatementsBlockContext>() {

			@Override
			public StatementsBlockContext visit(ParseTree arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public StatementsBlockContext visitChildren(RuleNode arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public StatementsBlockContext visitErrorNode(ErrorNode arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public StatementsBlockContext visitTerminal(TerminalNode arg0) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		
		return sb.toString();
	}



	public String print() {
		String s = Arrays.toString(args);
		return returnType + " " + name + "(" + s.substring(1, s.length()-1) + ") {\n" + body + "\n}";
	}
}

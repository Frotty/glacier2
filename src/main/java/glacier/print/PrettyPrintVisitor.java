package glacier.print;

import antlr4.GlacierParser;
import glacier.visitors.VisitorUtil;

public class PrettyPrintVisitor extends PrintVisitor {

    private String keyword(String keyword) {
        return ColorConfig.KEYWORD_COLOR + keyword + ColorConfig.WHITE_COLOR;
    }


    @Override
    public String visitNameDeclaration(GlacierParser.NameDeclarationContext ctx) {
        if (VisitorUtil.hasSize(ctx)) {
            print(keyword("shader ") + ctx.shaderName.getText());
            newline();
        }
        return "";
    }


//    @Override
//    public String visitDrawDirective(GlacierParser.DrawDirectiveContext ctx) {
//        if (VisitorUtil.hasSize(ctx)) {
//            print(keyword("draw ") + ctx.directiveKey.getText());
//            newline();
//        }
//        return "";
//    }
//
//
//    @Override
//    public String visitContextOptions(GlacierParser.ContextOptionsContext ctx) {
//        if (VisitorUtil.hasSize(ctx)) {
//            print(keyword("context") + " [");
//            ctx.options.forEach(this::visit);
//            print("]");
//            newline();
//        }
//        return "";
//    }

    @Override
    public String visitContextOpt(GlacierParser.ContextOptContext ctx) {
        print(ctx.optionName.getText() + "=" + ctx.optionValue + ", ");
        return "";
    }
}

package glacier.visitors;

import antlr4.GlacierBaseVisitor;
import antlr4.GlacierParser.ContextOptContext;
import antlr4.GlacierParser.GlacierHeaderContext;
import glacier.error.GlacierErrorType;
import glacier.parser.CompilationResult;

public class HeaderVisitor extends GlacierBaseVisitor<String> {
    private final EvalResult evalResult;
    private final CompilationResult compilationResult;

    public HeaderVisitor(EvalResult evalResult, CompilationResult compilationResult) {
        this.evalResult = evalResult;
        this.compilationResult = compilationResult;
    }

    @Override
    public String visitGlacierHeader(GlacierHeaderContext ctx) {
        compilationResult.lastProcessedToken = ctx.start;
        if (VisitorUtil.hasSize(ctx)) {
            if (VisitorUtil.hasSize(ctx.directiveKey)) {
                String drawDirectiveS = ctx.directiveKey.getText();
                try {
                    evalResult.setDrawDirective(DrawDirective.valueOf(drawDirectiveS.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    compilationResult.error(compilationResult.lastProcessedToken, GlacierErrorType.INVALID_DRAW_DIRECT);
                }
            } else {
                compilationResult.error(ctx, GlacierErrorType.MISSING_DRAW_DIRECT);
            }
            if (VisitorUtil.hasSize(ctx.options)) {
                for (ContextOptContext co : ctx.options) {
                    try {
                        ContextOption contextOption = ContextOption.valueOf(co.optionName.getText());
                        evalResult.addContextOption(contextOption);
                    } catch (IllegalArgumentException e) {
                        compilationResult.error(co.optionName, GlacierErrorType.INVALID_CONTEXT_OPT);
                    }
                }
            } else {
                compilationResult.error(compilationResult.lastProcessedToken, GlacierErrorType.MISSING_CONTEXT_OPT);
            }


        }
        return "";
    }
}

package glacier.visitors;

import antlr4.GlacierBaseVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

import static antlr4.GlacierParser.*;

public class ExtendedVisitor extends GlacierBaseVisitor<String> {
    private ShaderProgContext root;

    @Override
    public String visitShaderProg(ShaderProgContext ctx) {
        root = ctx;
        return "";
    }

    protected ParserRuleContext getOutScope(ParserRuleContext ruleContext) {
        ParserRuleContext shaderPart = getShaderPart(ruleContext);
        if (shaderPart instanceof VertexShaderContext) {
            VertexShaderContext vertex = (VertexShaderContext) shaderPart;
            return vertex.shaderBlock().outBlock();
        } else if (shaderPart instanceof FragmentShaderContext) {
            FragmentShaderContext fragment = (FragmentShaderContext) shaderPart;
            return fragment.shaderBlock().outBlock();
        }
        return null;
    }

//    protected ParserRuleContext getParentScope(ParserRuleContext child) {
//        while (!isScopeContext(child.getParent())) {
//
//        }
//    }

    protected ParserRuleContext getShaderPart(ParserRuleContext child) {
        ParserRuleContext context = child;
        while (!(context instanceof VertexShaderContext || context instanceof FragmentShaderContext)) {
            context = context.getParent();
        }
        return context;
    }

//    private boolean isScopeContext(ParserRuleContext parent) {
//        return parent instanceof
//    }
}

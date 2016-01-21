package glacier.visitors;

import antlr4.GlacierBaseVisitor;
import antlr4.GlacierParser.ContextOptContext;
import antlr4.GlacierParser.GlacierHeaderContext;
import glacier.error.GlacierErrorType;
import glacier.parser.CompilationResult;

public class HeaderVisitor extends GlacierBaseVisitor<String> {
    public enum DrawDirective {
        FULLSCREEN, GEOMETRY;

    }

    enum DepthTest {
        LESS() {
            @Override
            public String toString() {
                return "GL20.GL_LESS";
            }
        };

    }

    enum CullFace {
        BACK() {
            @Override
            public String toString() {
                return "GL20.GL_BACK";
            }
        },
        FRONT() {
            @Override
            public String toString() {
                return "GL20.GL_FRONT";
            }
        },
        NONE() {
            @Override
            public String toString() {
                return "GL20.GL_NONE";
            }
        };


    }

    public DrawDirective drawDirective;
    private boolean depthMask;
    private DepthTest depthTest;
    private CullFace cullFace;
    private final CompilationResult compilationResult;

    public HeaderVisitor(CompilationResult compilationResult) {
        this.compilationResult = compilationResult;
    }

    @Override
    public String visitGlacierHeader(GlacierHeaderContext ctx) {
        compilationResult.lastProcessedToken = ctx.start;
        if (ctx.drawDirective() == null || ctx.drawDirective().isEmpty()) {
            compilationResult.error(ctx.drawDirective(), GlacierErrorType.MISSING_DRAW_DIRECT);
        } else {
            if (ctx.drawDirective().directiveKey == null) {
                compilationResult.error(compilationResult.lastProcessedToken, GlacierErrorType.INVALID_DRAW_DIRECT);
            } else {
                String drawDirectiveS = ctx.drawDirective().directiveKey.getText();
                this.drawDirective = DrawDirective.valueOf(drawDirectiveS.toUpperCase());
            }
        }
        if (ctx.contextOptions() == null || ctx.contextOptions().isEmpty()) {
            compilationResult.error(compilationResult.lastProcessedToken, GlacierErrorType.MISSING_CONTEXT_OPT);
        } else {
            for (ContextOptContext co : ctx.contextOptions().options) {
                String varname = co.optionName.getText();
                switch (varname) {
                    case "depthmask":
                        depthMask = true;
                        break;
                    case "depthtest":
                        depthTest = DepthTest.valueOf(co.optionValue.getText().toUpperCase());
                        break;
                    case "cullFace":
                        cullFace = CullFace.valueOf(co.optionValue.getText().toUpperCase());
                        break;
                    default:
                        compilationResult.error(co.optionName.getLine(), co.start.getCharPositionInLine(), co.stop.getCharPositionInLine(),
                                GlacierErrorType.INVALID_CONTEXT_OPT);
                        break;
                }
            }
        }
        return "";
    }

    public String getContextOptionsString() {
        String s = "";
        if (depthMask) {
            s += "\tcontext.setDepthMask(true);\n";
        }
        if (depthTest != null) {
            s += "\tcontext.setDepthTest(" + depthTest.toString() + ");\n";
        }
        if (cullFace != null) {
            s += "\tcontext.setCullFace(" + cullFace.toString() + ");\n";
        }
        return s;
    }

}

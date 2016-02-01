package glacier.parser;

import antlr4.GlacierParser;
import glacier.print.GLSLPrintVisitor;
import glacier.util.ExtendedLexer;
import glacier.visitors.EvalVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class GlacierCompiler {

    public CompilationResult compileShader(CompilationConfig compilationConfig) {
        System.out.println("Compiling Shader:\n---\n" + compilationConfig.getGlacierShader()+"\n---\n");
        // Create a lexer and glacier.parser for the input.
        ExtendedLexer lexer = new ExtendedLexer(new ANTLRInputStream(compilationConfig.getGlacierShader()));
        GlacierParser parser = new GlacierParser(new CommonTokenStream(lexer));

        GlacierParser.ShaderProgContext tree = parser.shaderProg();
        // The result
        CompilationResult result = new CompilationResult();
        // Start compilation
        VarManager variableManager = new VarManager();

        result.log("Evaluation Visitor..");
        EvalVisitor evalVisitor = new EvalVisitor(variableManager, result);
        evalVisitor.visit(tree);
        result.log("Evaluation Visitor..Finished\n====================");
        if (result.hasErrors()) {
            result.log("found <" + result.counter + "> errors. Aborting");
            return result;
        }

        result.log("Printing GLSL: " + compilationConfig.isGenerateGLSL());
        if (compilationConfig.isGenerateGLSL()) {
            GLSLPrintVisitor glslPrintVisitor = new GLSLPrintVisitor(variableManager, result);
            glslPrintVisitor.visit(tree.vertexShader());
            result.log("--Vert--");
            result.vertexShader = glslPrintVisitor.out();
            result.log(result.vertexShader);
            result.log("--Frag--");
            glslPrintVisitor.visit(tree.fragmentShader());
            result.fragmentShader = glslPrintVisitor.out();
            result.log(result.fragmentShader);
            result.log("-----");
        }

        result.log("Generating LibGdx: " + compilationConfig.isGenerateLibgdx());
        if (compilationConfig.isGenerateLibgdx()) {
//    TODO        LibgdxBuilder libgdxBuilder = new LibgdxBuilder();
//            try {
//                if (evalVisitor.headerV.drawDirective == HeaderVisitor.DrawDirective.FULLSCREEN) {
//                    result.javaShaderSrc = libgdxBuilder.build(result.name, variableManager, evalVisitor.headerV, compilationConfig.getFullscreenTemplate());
//                } else {
//                    result.javaShaderSrc = libgdxBuilder.build(result.name, variableManager, evalVisitor.headerV, compilationConfig.getGeometryTemplate());
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }


// TODO        result.renderTargets = variableManager.getRenderTargetCount();
        return result;
    }

}

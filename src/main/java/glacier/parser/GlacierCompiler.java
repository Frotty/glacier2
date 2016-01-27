package glacier.parser;

import antlr4.GlacierBaseListener;
import antlr4.GlacierListener;
import antlr4.GlacierParser;
import glacier.print.GLSLPrintVisitor;
import glacier.print.PrettyPrintVisitor;
import glacier.util.ExtendedLexer;
import glacier.visitors.EvalVisitor;
import glacier.visitors.TypeVisitor;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;

public class GlacierCompiler {

    public CompilationResult compileShader(CompilationConfig compilationConfig) {
        System.out.println("Compiling Shader:\n" + compilationConfig.getGlacierShader());
        // Create a lexer and glacier.parser for the input.
        ExtendedLexer lexer = new ExtendedLexer(new ANTLRInputStream(compilationConfig.getGlacierShader()));
        GlacierParser parser = new GlacierParser(new CommonTokenStream(lexer));

        GlacierParser.ShaderProgContext tree = parser.shaderProg();
        ParseTreeWalker walker = new ParseTreeWalker();
        GlacierListener listener = new GlacierBaseListener();
        // The result
        CompilationResult result = new CompilationResult();
        // Start compilation
        VarManager variableManager = new VarManager();

        result.log("Evaluation Visitor..");
        EvalVisitor evalVisitor = new EvalVisitor(variableManager, result);
        evalVisitor.visit(tree);
        if(result.hasErrors()) {
            return result;
        }

        result.log("Type Visitor..");
        TypeVisitor tV = new TypeVisitor(variableManager, false);
        result.log("Printing..");
        GLSLPrintVisitor visitor = new GLSLPrintVisitor(null, tV);
        walker.walk(listener, tree);

        // Fixing
        result.log("Fixing..");
        TypeVisitor fixv = new TypeVisitor(variableManager, true);
        fixv.visit(tree);

        if (result.hasErrors()) {
            result.log("found <" + result.counter + "> errors. Aborting");
            return result;
        }

        // Print Shader Class
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
        } else {
            result.log("Not generating libgdx");
        }
        // Print GLSL
        System.out.println("Printing");
        visitor.visit(tree.vertexShader());
        result.vertexShader = visitor.out();
        System.out.println("---------------\n");
        ArrayList<String> al = new ArrayList<>();
        al.addAll(visitor.outVars());

        visitor = new GLSLPrintVisitor(al, tV);
        visitor.visit(tree.fragmentShader());
        result.fragmentShader = visitor.out();
// TODO        result.renderTargets = variableManager.getRenderTargetCount();

        PrettyPrintVisitor prettyPrintVisitor = new PrettyPrintVisitor();
        prettyPrintVisitor.visit(tree);
        System.out.println("Pretty:\n" + prettyPrintVisitor.out());
        return result;
    }

}

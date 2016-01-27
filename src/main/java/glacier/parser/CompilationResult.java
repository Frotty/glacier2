package glacier.parser;

import glacier.error.GlacierError;
import glacier.error.GlacierErrorType;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.List;

public class CompilationResult {
    private long startTime = System.currentTimeMillis();
    public String name;
    public String javaShaderSrc;
    public String vertexShader;
    public String fragmentShader;
    public boolean fsShader;
    public int renderTargets;
    public StringBuilder logger = new StringBuilder();
    public List<GlacierError> errors = new ArrayList<>();
    public int counter = 0;
    public Token lastProcessedToken;

    public void printLog() {
        System.out.println(logger.toString());
    }

    public void printErrors() {
        for (GlacierError error : errors) {
            System.out.println(error.print());
        }
    }

    public void log(String msg) {
        System.out.println("<" + (System.currentTimeMillis() - startTime) + "ms> " + msg + "\n");
        logger.append("<" + (System.currentTimeMillis() - startTime) + "ms> " + msg + "\n");
    }

    public void error(ParserRuleContext ruleContext, GlacierErrorType errorType, String... inlineData) {
        System.err.println("<" + (System.currentTimeMillis() - startTime) + "ms> " + errorType.print(inlineData) + "\n");
        errors.add(new GlacierError(ruleContext.start.getLine(), ruleContext.start.getCharPositionInLine(), ruleContext.stop.getCharPositionInLine(),
                errorType, inlineData));
    }

    public void error(Token token, GlacierErrorType errorType, String... inlineData) {
        errors.add(new GlacierError(token.getLine(), token.getCharPositionInLine(), token.getCharPositionInLine(), errorType, inlineData));
    }

    public void error(int line, int startPos, int endPos, GlacierErrorType errorType, String... inlineData) {
        errors.add(new GlacierError(line, startPos, endPos, errorType, inlineData));
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public int getErrorCount() {
        return errors.size();
    }
}
package test;

import glacier.error.GlacierErrorType;
import glacier.parser.CompilationResult;
import org.junit.Test;

public class VertTests extends  GlacierBaseTest {

    private CompilationResult compileVertPart(String vert) {
        String shader=
                "shader test\n" +
                "draw geometry\n" +
                "context []\n\n" +
                "vert\n\t" +
                vert +
                "\nfrag\n" +
                "\tout\n" +
                "\t\tbuff\n" +
                "\tmain()\n";
        return compileShader(shader);
    }

    @Test
    public void testVertEmpty() {
        String shader = "\n";
        CompilationResult compilationResult = compileVertPart(shader);
        assertErrors(compilationResult.errors, GlacierErrorType.MISSING_VERTEX);
    }

    @Test
    public void testVertEmptyDecls() {
        String shader = "out\n\tuni\n\tmain()\n";
        CompilationResult compilationResult = compileVertPart(shader);
        assertErrors(compilationResult.errors);
    }
}

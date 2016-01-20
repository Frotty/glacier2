package test;

import glacier.parser.CompilationResult;
import org.junit.Test;

import static glacier.error.GlacierErrorType.MISSING_FRAGMENT;
import static glacier.error.GlacierErrorType.MISSING_HEADER;
import static glacier.error.GlacierErrorType.MISSING_VERTEX;

public class VertTests extends  GlacierBaseTest {

    private CompilationResult compileVertPart(String vert) {
        String shader=
                "shader test\n" +
                "draw geometry\n\n" +
                "vert\n\t" +
                vert +
                "\nfrag\n" +
                "\tout\n" +
                "\t\tbuff\n" +
                "\tmain()\n";
        return compileShader(shader);
    }

    @Test
    public void testEmpty() {
        String shader = "\n";
        CompilationResult compilationResult = compileVertPart(shader);
        assertErrors(compilationResult.errors);
    }

    @Test
    public void testEmptyDecls() {
        String shader = "out\n\tuni\n\tmain()\n";
        CompilationResult compilationResult = compileVertPart(shader);
        assertErrors(compilationResult.errors);
    }
}

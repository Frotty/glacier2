package test;

import glacier.error.GlacierErrorType;
import glacier.compiler.CompilationResult;
import org.junit.Test;

public class VertTests extends  GlacierBaseTest {


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

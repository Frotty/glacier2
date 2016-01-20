package test;

import static glacier.error.GlacierErrorType.*;

import glacier.parser.CompilationResult;
import org.junit.Assert;
import org.junit.Test;

public class BasicTests extends GlacierBaseTest {

    @Test
    public void testEmpty() {
        String shader = "";
        CompilationResult compilationResult = compileShader(shader);
        assertErrors(compilationResult.errors, MISSING_NAME, MISSING_HEADER, MISSING_VERTEX, MISSING_FRAGMENT);
    }

    @Test
    public void testNameOnly() {
        String shader = "shader test\n";
        CompilationResult compilationResult = compileShader(shader);
        assertErrors(compilationResult.errors, MISSING_HEADER, MISSING_VERTEX, MISSING_FRAGMENT);
    }

    @Test
    public void testDrawOnly() {
        String shader = "shader test\n" +
                "draw fullscreen\n";
        CompilationResult compilationResult = compileShader(shader);
        assertErrors(compilationResult.errors, MISSING_CONTEXT_OPT, MISSING_VERTEX, MISSING_FRAGMENT);
    }

    @Test
    public void testContextOnly() {
        String shader = "shader test\n" +
                "context []\n";
        CompilationResult compilationResult = compileShader(shader);
        assertErrors(compilationResult.errors, MISSING_HEADER, MISSING_VERTEX, MISSING_FRAGMENT);
    }

    @Test
    public void testHeaderOnly() {
        String shader = "shader test\n" +
                "draw fullscreen\n" +
                "context []\n";
        CompilationResult compilationResult = compileShader(shader);
        assertErrors(compilationResult.errors, MISSING_VERTEX, MISSING_FRAGMENT);
    }

    @Test
    public void testVertOnly() {
        String shader = "shader test\n" +
                "draw fullscreen\n" +
                "context []\n" +
                "vert\n";
        CompilationResult compilationResult = compileShader(shader);
        assertErrors(compilationResult.errors, MISSING_FRAGMENT);
    }

    @Test
    public void testFragOnly() {
        String shader = "shader test\n" +
                "draw fullscreen\n" +
                "context []\n" +
                "frag\n";
        CompilationResult compilationResult = compileShader(shader);
        Assert.assertTrue(compilationResult.errors.isEmpty());
    }


}

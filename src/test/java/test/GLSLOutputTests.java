package test;

import glacier.parser.CompilationResult;
import org.junit.Assert;
import org.junit.Test;

public class GLSLOutputTests extends GlacierBaseTest {

    @Test
    public void testPosUse() {
        CompilationResult compilationResult = compileVertPart("main()\n" +
                "\t\tvec4 temp = vec4(in.pos,1.0)", true);
        assertErrors(compilationResult.errors);
        assertGlslExists(compilationResult.vertexShader, "vec4 l_temp = vec4(a_position, 1.0)");
        assertGlslExists(compilationResult.vertexShader, "in vec3 a_position");
    }

    private void assertGlslExists(String vertexShader, String s) {
        Assert.assertTrue(vertexShader.contains(s));
    }


}

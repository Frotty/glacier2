package test;

import glacier.parser.CompilationResult;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class GLSLOutputTests extends GlacierBaseTest {
    private void assertGlslExists(String vertexShader, String s) {
        Assert.assertTrue(vertexShader.contains(s));
    }

    @Test
    public void testAttributePosUse() {
        CompilationResult compilationResult = compileVertPart("main()\n" +
                "\t\tvec4 temp = vec4(in.pos,1.0)", true);
        assertErrors(compilationResult.errors);
        assertGlslExists(compilationResult.vertexShader, "in vec3 a_position");
        assertGlslExists(compilationResult.vertexShader, "vec4 l_temp = vec4(a_position, 1.0)");
    }

    @Test
    public void testAttributeNormalUse() {
        CompilationResult compilationResult = compileVertPart("main()\n" +
                "\t\tvec4 temp = vec4(in.normal,1.0)", true);
        assertErrors(compilationResult.errors);
        assertGlslExists(compilationResult.vertexShader, "in vec3 a_normal");
        assertGlslExists(compilationResult.vertexShader, "vec4 l_temp = vec4(a_normal, 1.0)");
    }

    @Test
    public void testAttributeColorUse() {
        CompilationResult compilationResult = compileVertPart("main()\n" +
                "\t\tvec4 temp = in.color", true);
        assertErrors(compilationResult.errors);
        assertGlslExists(compilationResult.vertexShader, "in vec4 a_color");
        assertGlslExists(compilationResult.vertexShader, "vec4 l_temp = a_color");
    }

    @Test
    public void testUniformUse() {
        CompilationResult compilationResult = compileVertPart(
                "uni\n" +
                "\t\tvec4 someUniform\n" +
                "\tmain()\n" +
                "\t\tvec4 temp = uni.someUniform", true);
        assertErrors(compilationResult.errors);
        assertGlslExists(compilationResult.vertexShader, "uniform vec4 u_someUniform");
        assertGlslExists(compilationResult.vertexShader, "vec4 l_temp = u_someUniform");
    }

    @Ignore
    @Test
    public void testVertOutUse() {
        CompilationResult compilationResult = compileVertPart(
                "out\n" +
                "\t\tvec4 boat\n" +
                "\tmain()\n" +
                "\t\tout.boat = in.color", true);
        assertErrors(compilationResult.errors);
        assertGlslExists(compilationResult.vertexShader, "uniform vec4 v_boat");
        assertGlslExists(compilationResult.vertexShader, "v_boat = a_color");
    }




}

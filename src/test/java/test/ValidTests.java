package test;

import glacier.parser.CompilationResult;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class ValidTests extends GlacierBaseTest {

    @Test
    public void test1() throws IOException, URISyntaxException {
        String shader = loadTestFile("/testShaders/test1.gl");
        CompilationResult compilationResult = compileShader(shader);
        assertErrors(compilationResult.errors);
    }

}

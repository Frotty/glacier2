package test;

import glacier.error.GlacierError;
import glacier.error.GlacierErrorType;
import glacier.compiler.CompilationConfig;
import glacier.compiler.CompilationResult;
import glacier.compiler.GlacierCompiler;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlacierBaseTest {

    protected GlacierCompiler glacierCompiler;

    protected String loadTestFile(String path) throws URISyntaxException, IOException {
        URL resource = getClass().getResource(path);
        Assert.assertNotNull("Test file " + path +  " missing", resource);
        Path resourcePath = Paths.get(resource.toURI());
        return new String(Files.readAllBytes(resourcePath), Charset.defaultCharset());
    }


    protected void assertErrors(List<GlacierError> actual, GlacierErrorType... expected) {
        System.out.println("Found: " + actual.size() + " errors");
        ArrayList expectList = new ArrayList(Arrays.<GlacierErrorType>asList(expected));
        if (expectList.isEmpty() && !actual.isEmpty()) {
            Assert.fail();
        }
        for (GlacierError actualError : actual) {
            expectList.remove(actualError.getErrorType());
        }
        Assert.assertTrue(expectList.isEmpty());
    }

    protected CompilationResult compileShader(String shader) {
        return compileShader(shader, false);
    }

    protected CompilationResult compileShader(String shader, boolean glsl) {
        CompilationConfig config = new CompilationConfig(shader);
        config.setGenerateGLSL(glsl);
        CompilationResult result = glacierCompiler.compileShader(config);
        result.printErrors();
        System.out.println(result.vertexShader);
        return result;
    }

    protected CompilationResult compileVertPart(String vert) {
        return compileVertPart(vert, false);
    }

    protected CompilationResult compileVertPart(String vert, boolean glsl) {
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
        return compileShader(shader, glsl);
    }

    @Before
    public void prepare() {
        glacierCompiler = new GlacierCompiler();
    }

}

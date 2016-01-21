package test;

import glacier.error.GlacierError;
import glacier.error.GlacierErrorType;
import glacier.parser.CompilationConfig;
import glacier.parser.CompilationResult;
import glacier.parser.GlacierCompiler;
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
        CompilationConfig config = new CompilationConfig(shader);
        CompilationResult result = glacierCompiler.compileShader(config);
        result.printLog();
        result.printErrors();
        System.out.println(result.vertexShader);
        return result;
    }

    @Before
    public void prepare() {
        glacierCompiler = new GlacierCompiler();
    }

}

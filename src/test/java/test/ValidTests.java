package test;

import glacier.parser.CompilationResult;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static glacier.error.GlacierErrorType.*;

public class ValidTests extends GlacierBaseTest {

    @Test
    public void test1() throws IOException {
        String shader = new String(Files.readAllBytes(Paths.get("./src/test/resources/testShaders/test1.gl")), Charset.defaultCharset());
        CompilationResult compilationResult = compileShader(shader);
        assertErrors(compilationResult.errors);
    }
    @Test
    public void test2() throws IOException {
        String shader = new String(Files.readAllBytes(Paths.get("./src/test/resources/testShaders/test2.gl")), Charset.defaultCharset());
        CompilationResult compilationResult = compileShader(shader);
        assertErrors(compilationResult.errors);
    }@Test
    public void test3() throws IOException {
        String shader = new String(Files.readAllBytes(Paths.get("./src/test/resources/testShaders/test3.gl")), Charset.defaultCharset());
        CompilationResult compilationResult = compileShader(shader);
        assertErrors(compilationResult.errors);
    }@Test
    public void test4() throws IOException {
        String shader = new String(Files.readAllBytes(Paths.get("./src/test/resources/testShaders/test4.gl")), Charset.defaultCharset());
        CompilationResult compilationResult = compileShader(shader);
        assertErrors(compilationResult.errors);
    }




}

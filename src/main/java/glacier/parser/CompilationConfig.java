package glacier.parser;


public class CompilationConfig {
    private final String glacierShader;
    private boolean generateLibgdx = false;
    public boolean generateGLSL = true;
    public String outputLocation = "/";
    private String geometryTemplate = "";
    private String fullscreenTemplate = "";

    public CompilationConfig(String glacierShader) {
        this.glacierShader = glacierShader;
    }

    public void enableLibgdxGeneration(String geometryTemplate, String fullscreenTemplate) {
        generateLibgdx = true;
        this.geometryTemplate = geometryTemplate;
        this.fullscreenTemplate = fullscreenTemplate;
    }

    public String getGlacierShader() {
        return glacierShader;
    }

    public String getGeometryTemplate() {
        return geometryTemplate;
    }

    public String getFullscreenTemplate() {
        return fullscreenTemplate;
    }

    public boolean isGenerateLibgdx() {
        return generateLibgdx;
    }
}

package glacier.error;

public enum GlacierErrorType {
    MISSING_NAME("Missing name declaration", "Please put the name declaration at the top of the shader.\nSyntax: shader {}"),
    MISSING_HEADER("Missing header definition", "Please put the header after the name declaration."),
    MISSING_DRAW_DIRECT("Missing draw directive", "Please add the draw directive after the name."),
    MISSING_CONTEXT_OPT("Missing context options", "Please put the context options after the draw directive."),
    MISSING_VERTEX("Missing vertex shader", "Please add the vertex shader after the header."),
    MISSING_FRAGMENT("Missing fragment shader", "Please add the fragment shader after the vertex shader."),
    INVALID_NAME("Invalid name", "Please only use alphanumerals and underscores."),
    INVALID_DRAW_DIRECT("Invalid draw directive", "Please use either fullscreen or geometry."),
    INVALID_CONTEXT_OPT("Invalid context options", "Please use any of []."),
    INVALID_VERTEX_IN("Invalid vertex in-variable <{}>", "Please use only {}"),
    INVALID_IEDIRECTIVE("Invalid IEDirective", "Please use only 'in' or 'out'");

    private String errorName;
    private String errorDetail;

    GlacierErrorType(String errorName, String errorDetail) {
        this.errorName = errorName;
        this.errorDetail = errorDetail;
    }

    public String print(String... inlineData) {
        String error = "Error(id:" + ordinal() + ") " + errorName + ": " + errorDetail;
        for (String dat : inlineData) {
            error.replaceFirst("[{}]", dat);
        }
        return error;
    }
    }

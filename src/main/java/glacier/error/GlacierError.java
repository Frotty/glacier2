package glacier.error;

public class GlacierError {
    private GlacierErrorType errorType;
    private int line;
    private int startPos;
    private int endPos;
    private String[] inlineData;

    public GlacierError(int line, int startPos, int endPos, GlacierErrorType errorType, String... inlineData) {
        this.errorType = errorType;
        this.line = line;
        this.startPos = startPos;
        this.endPos = endPos;
        this.inlineData = inlineData;
    }

    public GlacierErrorType getErrorType() {
        return errorType;
    }

    public String print() {
        return "(" + line + ":" + startPos + ")" + errorType.print(inlineData);
    }

    public int getEndPos() {
        return endPos;
    }
}

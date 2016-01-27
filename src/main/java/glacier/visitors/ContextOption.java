package glacier.visitors;

public enum ContextOption {
    DEPTH_TEST("depthtest", "less", "GL20.GL_LESS"),
    CULL_FACE("back", "GL_BACK", "front", "GL_FRONT", "none", "GL_NONE");

    private final String name;
    private final String[] valuePairs;


    ContextOption(String optionName, String... keyValuePairs) {
        this.name = optionName;
        this.valuePairs = keyValuePairs;
    }

    public String getGLConstant(String name) {
        for (int i = 0; i < valuePairs.length; i+=2) {
            if(name.equals(valuePairs[i])) {
                return valuePairs[i+1];
            }
        }
        return null;
    }


}

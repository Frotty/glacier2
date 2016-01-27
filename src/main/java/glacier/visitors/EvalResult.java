package glacier.visitors;

import java.util.ArrayList;

public class EvalResult {
    private DrawDirective drawDirective;
    private ArrayList<ContextOption> contextOptions = new ArrayList<>();

    public DrawDirective getDrawDirective() {
        return drawDirective;
    }

    public void setDrawDirective(DrawDirective drawDirective) {
        this.drawDirective = drawDirective;
    }

    public ArrayList<ContextOption> getContextOptions() {
        return contextOptions;
    }

    public void addContextOption(ContextOption contextOption) {
        contextOptions.add(contextOption);
    }

//    public String getContextOptionsString() {
//        String s = "";
//        if (depthMask) {
//            s += "\tcontext.setDepthMask(true);\n";
//        }
//        if (depthTest != null) {
//            s += "\tcontext.setDepthTest(" + depthTest.toString() + ");\n";
//        }
//        if (cullFace != null) {
//            s += "\tcontext.setCullFace(" + cullFace.toString() + ");\n";
//        }
//        return s;
//    }
}

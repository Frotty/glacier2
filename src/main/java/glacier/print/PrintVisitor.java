package glacier.print;

import antlr4.GlacierBaseVisitor;

public abstract class PrintVisitor extends GlacierBaseVisitor<String> {
    protected StringBuilder stringBuilder = new StringBuilder();
    protected String indentLvl = "";
    protected int indentLevel = 0;

    protected void print(String text) {
        stringBuilder.append(text);
    }

    protected void increaseIndent() {
        indentLevel++;
        indentLvl = "";
        for (int i = 0; i < indentLevel; i++) {
            indentLvl += "\t";
        }
    }

    protected void decreaseIndent() {
        indentLevel--;
        indentLvl = "";
        for (int i = 0; i < indentLevel; i++) {
            indentLvl += "\t";
        }
    }

    protected void newline() {
        stringBuilder.append("\n");
        stringBuilder.append(indentLvl);
    }

    protected void removeLastChars(int chars) {
        stringBuilder.setLength(stringBuilder.length() - chars);
    }

    public String out() {
        return stringBuilder.toString();
    }
}

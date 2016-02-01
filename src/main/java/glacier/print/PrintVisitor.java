package glacier.print;

import glacier.visitors.ExtendedVisitor;

public abstract class PrintVisitor<T extends String>  extends ExtendedVisitor<T> {
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
        String shader = stringBuilder.toString();
        clear();
        return shader;
    }

    public void clear() {
        stringBuilder = new StringBuilder();
    }
}

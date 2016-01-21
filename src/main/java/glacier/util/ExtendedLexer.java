package glacier.util;

import antlr4.GlacierLexer;
import antlr4.GlacierParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.misc.Pair;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class ExtendedLexer implements TokenSource {
    private final GlacierLexer orig;
    private Queue<Token> nextTokens = new LinkedList<>();
    private State state = State.INIT;
    private Stack<Integer> indentationLevels = new Stack<>();
    private Token eof = null;
    private Token firstNewline;
    private int numberOfTabs;
    private LineOffsets lineOffsets = new LineOffsets();
    public StringBuilder debugSb = new StringBuilder();
    private final boolean debug = false;
    private Pair<TokenSource, CharStream> sourcePair;
    private boolean isWurst = true;
    private Token lastToken = null;

    enum State {
        INIT, NEWLINES, BEGIN_LINE
    }


    public ExtendedLexer(CharStream input) {
        orig = new GlacierLexer(input);
        sourcePair = new Pair<TokenSource, CharStream>(orig, input);
        indentationLevels.push(0);
    }


    @Override
    public int getCharPositionInLine() {
        return orig.getCharPositionInLine();
    }

    @Override
    public CharStream getInputStream() {
        return orig.getInputStream();
    }

    @Override
    public int getLine() {
        return orig.getLine();
    }

    @Override
    public String getSourceName() {
        return orig.getSourceName();
    }

    @Override
    public TokenFactory<?> getTokenFactory() {
        return orig.getTokenFactory();
    }

    @Override
    public Token nextToken() {
        Token t = nextTokenIntern();
        lastToken = t;

        debugSb.append(t.getText() + " ");
        if (debug) System.out.println("		new token: " + t);
        return t;
    }


    private Token nextTokenIntern() {

        if (!nextTokens.isEmpty()) {
            return nextTokens.poll();
        }

        Token l_eof = eof;
        if (l_eof != null) {
            return makeToken(GlacierParser.EOF, "$EOF", l_eof.getStartIndex(), l_eof.getStopIndex());
        }

        boolean lastCharWasWrap = false;
        for (; ; ) {
            Token token = orig.nextToken();

            if (debug) System.out.println("orig token = " + token);

            if (token == null) {
                continue;
            }

            if (token.getType() == GlacierParser.NL) {
                lineOffsets.set(token.getLine(), token.getStartIndex());
            }

            if (token.getType() == GlacierParser.EOF) {
                // at EOF close all blocks and return an extra newline
                handleIndent(0, token.getStartIndex(), token.getStopIndex());
                eof = token;
                // if inside wurst, add a closing 'endpackage' and a newline
                nextTokens.add(makeToken(GlacierParser.NL, "$NL", token.getStartIndex(), token.getStopIndex()));
                // add a single newline
                return makeToken(GlacierParser.NL, "$NL", token.getStartIndex(), token.getStopIndex());
            }


            switch (state) {
                case INIT:
                    if (token.getType() == GlacierParser.NL) {
                        firstNewline = token;
                        state(State.NEWLINES);
                        continue;
                    } else if (token.getType() == GlacierParser.TAB) {
                        continue;
                    }
                    lastCharWasWrap = isWrapCharEndLine(token.getType());
                    return token;
                case NEWLINES:
                    if (isWrapCharBeginLine(token.getType())) {
                        // ignore all the newlines when a wrap char comes after newlines
                        lastCharWasWrap = true;
                        state(State.INIT);
                        return token;
                    } else if (token.getType() == GlacierParser.NL) {
                        continue;
                    } else if (token.getType() == GlacierParser.TAB) {
                        state(State.BEGIN_LINE);
                        numberOfTabs = 1;
                        continue;
                    } else {
                        // no tabs after newline
                        handleIndent(0, token.getStartIndex(), token.getStopIndex());
                        nextTokens.add(token);
                        state(State.INIT);
                        return firstNewline;
                    }
                case BEGIN_LINE:
                    if (token.getType() == GlacierParser.TAB) {
                        numberOfTabs++;
                        continue;
                    } else if (token.getType() == GlacierParser.NL) {
                        state(State.NEWLINES);
                        continue;
                    } else if (isWrapCharBeginLine(token.getType())) {
                        // ignore all the newlines when a wrap char comes after newlines
                        lastCharWasWrap = true;
                        state(State.INIT);
                        return token;
                    } else {
                        if (lastCharWasWrap && numberOfTabs > indentationLevels.peek()) {
                            // ignore the newline, only return the token
                            state(State.INIT);
                            return token;
                        } else {
                            handleIndent(numberOfTabs, token.getStartIndex(), token.getStopIndex());
                            state(State.INIT);
                            nextTokens.add(token);
                            return firstNewline;
                        }
                    }
            }
        }
    }

    private void state(State s) {
        if (debug) System.out.println("state " + state + " -> " + s);
        state = s;
    }


    private void handleIndent(int n, int start, int stop) {
        if (!isWurst) {
            return;
        }
        Token t = lastToken;
        if (t != null) {
            start = t.getStopIndex();
            stop = t.getStopIndex();
        }
        if (debug) System.out.println("handleIndent " + n + "	 " + indentationLevels);
        if (n > indentationLevels.peek()) {
            indentationLevels.push(n);
            nextTokens.add(makeToken(GlacierParser.STARTBLOCK, "$begin", start, stop));
        } else {
            while (n < indentationLevels.peek()) {
                indentationLevels.pop();
                nextTokens.add(makeToken(GlacierParser.ENDBLOCK, "$end", start, stop));
            }
            if (n != indentationLevels.peek()) {
                for (ANTLRErrorListener el : orig.getErrorListeners()) {
                    int line = lineOffsets.getLine(start);
                    el.syntaxError(orig, "", line, start - lineOffsets.get(line), "Invalid indentation level.", null);
                }
            }
        }
    }


    private boolean isWrapChar(int type) {
        switch (type) {
//		case GlacierParser.PAREN_LEFT: 
//		case GlacierParser.BRACKET_LEFT:
            case GlacierParser.COMMA:
//		case GlacierParser.PLUS:
//		case GlacierParser.MULT:
//		case GlacierParser.MINUS:
//		case GlacierParser.DIV:
//		case GlacierParser.DIV_REAL:
//		case GlacierParser.MOD:
//		case GlacierParser.MOD_REAL:
//		case GlacierParser.AND:
//		case GlacierParser.OR:
//		case GlacierParser.ARROW:
                return true;
        }
        return false;
    }


    private boolean isWrapCharEndLine(int type) {
        switch (type) {
//		case GlacierParser.PAREN_LEFT: 
//		case GlacierParser.BRACKET_LEFT:
//			return true;
            default:
                return isWrapChar(type);
        }
    }


    private boolean isWrapCharBeginLine(int type) {
        switch (type) {
//		case GlacierParser.PAREN_RIGHT: 
//		case GlacierParser.BRACKET_RIGHT:
//		case GlacierParser.DOT:
//		case GlacierParser.DOTDOT:
//			return true;
            default:
                return isWrapChar(type);
        }
    }


    private
    @NotNull
    Token makeToken(int type, String text, int start, int stop) {
        Pair<TokenSource, CharStream> source = sourcePair;
        int channel = 0;
        CommonToken t = new CommonToken(source, type, channel, start, stop);
        t.setText(text);
        return t;
    }

    @Override
    public void setTokenFactory(TokenFactory<?> factory) {
        orig.setTokenFactory(factory);
    }


    public LineOffsets getLineOffsets() {
        return lineOffsets;
    }


    public void addErrorListener(ANTLRErrorListener listener) {
        orig.addErrorListener(listener);
    }
}
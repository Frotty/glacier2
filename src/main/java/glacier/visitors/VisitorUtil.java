package glacier.visitors;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class VisitorUtil {
    public static boolean hasSize(ParserRuleContext context) {
        if (context != null) {
            Token start = context.start;
            Token stop = context.stop;
            return !(context.isEmpty()
                    || (start == null)
                    || (stop == null)
                    || (((stop.getLine() - start.getLine()) <= 0) && (stop.getCharPositionInLine() - start.getCharPositionInLine()) <= 0));
        }
        return false;
    }

    public static boolean hasSize(List<?> vardefs) {
        return vardefs == null || vardefs.isEmpty();
    }
}

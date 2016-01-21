package glacier.util;

public class LineOffsets {
	private int[] offsets = new int[128];
	private int maxLine = 0;
	
	public final static LineOffsets dummy = new LineOffsets();
	
	
	public void set(int line, int offset) {
		maxLine = Math.max(line, maxLine);
		if (line >= offsets.length) {
			int newLen = offsets.length;
			while (line >= newLen) {
				newLen *= 2;
			}
			int[] offsets2 = new int[newLen];
			System.arraycopy(offsets, 0, offsets2, 0, offsets.length);
			offsets = offsets2;
		}
		offsets[line] = offset;
	}

	public int get(int line) {
		int lineNumber = line;
		if (lineNumber >= offsets.length) {
            lineNumber = offsets.length-1;
		}
		while (lineNumber >= 0 && offsets[lineNumber] == 0) {
            lineNumber--;
		}
		if (lineNumber >= 0) {
			return offsets[lineNumber];
		} else {
			return 0;
		}
	}
	
	public int getLine(int offset) {
		int min = 0;
		int max = maxLine;
		while (min < max) {
			int test = (min + max) / 2;
			int v = get(test);
			if (v < offset) {
				if (min == test) {
					min++;
				} else {
					min = test;
				}
			} else {
				max = test;
			}
		}
		return min;
	}
}
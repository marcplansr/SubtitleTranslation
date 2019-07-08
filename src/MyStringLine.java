import java.io.UnsupportedEncodingException;

public class MyStringLine {
	
	private boolean translate;
	private String stringLine;
	private int stringByteLength;
	
	public MyStringLine(boolean translate, String stringLine) throws UnsupportedEncodingException {
		this.translate = translate;
		this.stringLine = stringLine;
		if (translate) {
			stringByteLength = stringLine.getBytes("UTF-8").length;
		} else {
			stringByteLength = 0;
		}
	}
	
	public MyStringLine(String stringLine) {
		this.translate = false;
		this.stringLine = stringLine;
		stringByteLength = 0;
	}

	public final boolean isTranslate() {
		return translate;
	}

	public final void setTranslate(boolean translate) {
		this.translate = translate;
	}

	public final String getStringLine() {
		return stringLine;
	}

	public final void setStringLine(String stringLine) {
		this.stringLine = stringLine;
	}
		
	
	public final int getStringByteLength() {
		return stringByteLength;
	}

	public final void setStringByteLength(int stringByteLength) {
		this.stringByteLength = stringByteLength;
	}

	public String toString() {
		return "(" + translate + ", '" + stringLine + "', " + stringByteLength + ")";
	}
}

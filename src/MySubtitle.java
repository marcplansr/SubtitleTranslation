import java.util.ArrayList;

public class MySubtitle {
	
	private String time;
	private ArrayList<String> subsArray;
	
	public MySubtitle(String time, ArrayList<String> subsArray) {
		this.time = time;
		this.subsArray = subsArray;
	}

	public final String getTime() {
		return time;
	}

	public final void setTime(String time) {
		this.time = time;
	}

	public final ArrayList<String> getsubsArray() {
		return subsArray;
	}

	public final void setSubtitle(ArrayList<String> subsArray) {
		this.subsArray = subsArray;
	}
	
	public final String getSubsString() {
		String fullString = "";
		for (int i = 0; i < subsArray.size(); ++i) {
			fullString += subsArray.get(i) + "\n";			
		}
		return fullString;		
	}
	
	public String toString() {
		String subtitles = "";
		for (int i = 0; i < subsArray.size(); ++i) {
			subtitles += subsArray.get(i) + "\n";
		}
		subtitles = subtitles.substring(0, subtitles.length() - 1);
		return time + "\n" + subtitles;
	}

}

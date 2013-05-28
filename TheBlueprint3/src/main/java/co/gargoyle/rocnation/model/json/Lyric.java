package co.gargoyle.rocnation.model.json;

public class Lyric {

	public String text;
	public String annotation;
	public long timestamp;

	public boolean hasAnnotation() {
		if (annotation == null || annotation.equals("")) {
			return false;
		}
		return true;
	}

}

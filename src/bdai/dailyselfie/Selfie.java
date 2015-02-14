package bdai.dailyselfie;

// Define a selfie
public class Selfie {

	private String name;
	private String path;
	private String thumbPath;
	
	public Selfie(String selfieName, String selfiePath, String selfieThumbPath){
		name = selfieName;
		path = selfiePath;
		thumbPath = selfieThumbPath;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getThumbPath() {
		return thumbPath;
	}

	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}
}

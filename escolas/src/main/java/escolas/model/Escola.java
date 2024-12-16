package escolas.model;

public class Escola {
	
	private String nomeString;
	private double longitude;
	private double latitude;
	
	public Escola(String nomeString, double longitude, double latitude) {
		this.nomeString = nomeString;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public String getNomeString() {
		return nomeString;
	}

	public void setNomeString(String nomeString) {
		this.nomeString = nomeString;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	

}

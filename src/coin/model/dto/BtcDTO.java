package coin.model.dto;

public class BtcDTO {
	String day;
	double last_price;
	double volume;
	int eid;
	
	public BtcDTO() {
		super();
	}

	public BtcDTO(String day, double last_price, double volume, int eid) {
		super();
		this.day = day;
		this.last_price = last_price;
		this.volume = volume;
		this.eid = eid;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public double getLast_price() {
		return last_price;
	}

	public void setLast_price(double last_price) {
		this.last_price = last_price;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public int getEid() {
		return eid;
	}

	public void setEid(int eid) {
		this.eid = eid;
	}

	public String toString() {
		return "BtcDTO [day=" + day + ", last_price=" + last_price + ", volume=" + volume + ", eid=" + eid + "]";
	}
}

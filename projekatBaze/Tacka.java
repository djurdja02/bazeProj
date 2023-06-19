package projekatBaze;

import java.text.DecimalFormat;

public class Tacka {

	private double x;
	private double y;
	public Tacka(double x,double y) {
		this.x=x;
		this.y=y;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	@Override
    public String toString() {
		DecimalFormat format=new DecimalFormat("#.##");
		String x1=format.format(x);
		String y1=format.format(y);
        return "(" + x1 + ", " + y1 + ")";
    }
}

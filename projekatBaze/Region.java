package projekatBaze;


public class Region{
	private Tacka gl;
	private Tacka dd;
	private Tacka gd;
	private Tacka dl;
	
	@Override
    public String toString() {
        return new StringBuilder("MBR: (" + gl + "), (" + dd + ")"+ "P"+povrsina()).toString();
    }
	
	public Region(Tacka g,Tacka d){
		gl=g;
		dd=d;
		gd=new Tacka(dd.getX(),gl.getY());
		dl=new Tacka(gl.getX(),dd.getY());
	}	
	public boolean presek(Region r2) {
		if(gl.getX() > r2.dd.getX())return false;
		if(r2.gl.getX()> dd.getX())return false;
		if (gl.getY() < r2.dd.getY())return false;
		if(dd.getY()>r2.gl.getY()) return false;
		return true;
	}
	//algoritam za insert korak dva:In case of ties, select the node whose MBR has
	//the minimum area
	public double povrsina() {
		return Math.abs((gl.getY()-dl.getY())*(dd.getX()-dl.getX()));
	}
	//korak 1:select the node, L, whose MBR will require the minimum area enlargement
	//to cover E.mbr
	public double povecanje(Region novi) {
		double ukupno=
				(Math.max(dd.getX(), novi.dd.getX())-Math.min(dl.getX(), novi.dl.getX()))*
				(Math.max(gl.getY(), novi.gl.getY())-Math.min(dl.getY(), novi.dl.getY()));
	return ukupno-povrsina();
	}
	//za spajanje MBR
	public Region unija(Region r2) {
		Tacka gl=new Tacka(0,0);
		Tacka dd=new Tacka(0,0);
		gl.setX(this.gl.getX()<r2.gl.getX()?this.gl.getX(): r2.gl.getX());
		gl.setY(this.gl.getY()>r2.gl.getY()?this.gl.getY(): r2.gl.getY());
		dd.setX(this.dd.getX()>r2.dd.getX()?this.dd.getX():r2.dd.getX());
		dd.setY(this.dd.getY()<r2.dd.getY()?this.dd.getY():r2.dd.getY());
		return new Region(gl,dd);
	}
	
	public double rastojanje(Region r2) {
		if(presek(r2))return 0;
		double x=Math.max(gl.getX()-r2.dd.getX(),r2.gl.getX()-dd.getX());
		double y=Math.max(r2.gl.getY()-dd.getY(), gl.getY()-r2.dd.getY());
		return Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
	}
	public Tacka getGl() {
		return gl;
	}
	public void setGl(Tacka gl) {
		this.gl = gl;
	}
	public Tacka getDd() {
		return dd;
	}
	public void setDd(Tacka dd) {
		this.dd = dd;
	}
	public Tacka getGd() {
		return gd;
	}
	public void setGd(Tacka gd) {
		this.gd = gd;
	}
	public Tacka getDl() {
		return dl;
	}
	public void setDl(Tacka dl) {
		this.dl = dl;
	}
}
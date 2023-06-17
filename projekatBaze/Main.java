package projekatBaze;

public class Main {

	public static void main(String[] args) {
		Rtree stablo=new Rtree(4, 2);
		for(int i=0;i<25;i++) {
			double x=i;
			double y=i+1;
			Tacka t=new Tacka(x,y);
			stablo.dodaj(t);
			}
		stablo.ispis();
	}

}

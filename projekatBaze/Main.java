package projekatBaze;

public class Main {

	public static void main(String[] args) {
		
		Rtree stablo=new Rtree(4, 2);
		//Tacke za koje znamo redosled
		/*for(int i=0;i<100;i++) {
			double x=i;
			double y=i+1;
			Tacka t=new Tacka(x,y);
			stablo.dodaj(t);
			}
			*/
		//Slucajne tacke
		/*for(int i=0;i<100;i++) {
			double x=Math.random()*10*i;
			double y=Math.random()*10*i;
			Tacka t=new Tacka(x,y);
			stablo.dodaj(t);
			}

			*/
		stablo.ucitajDatoteku("src/projekatBaze/"+args[0]);
		stablo.ispis();
		System.out.println("-------------------------------");
		stablo.ispisiDubine();
		System.out.println("-------------------------------");
		stablo.pretrazivanje(new Region(new Tacka(0.1, 8.9),new Tacka(9.0,1.0)));
		
	}

}

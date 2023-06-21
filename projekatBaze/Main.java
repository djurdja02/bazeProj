package projekatBaze;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Unesite M i m u obliku M,m:");
		String linija = scanner.nextLine();
		int M=Integer.parseInt(linija.split(",")[0]);
		int m=Integer.parseInt(linija.split(",")[1]);
		Rtree stablo=new Rtree(M, m);
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
		while(true) {
			
	        System.out.println("Unesite koordinate tacke u obliku X,Y:");
	        linija = scanner.nextLine();
	        if(linija.equals(""))break;
	        double x=Double.parseDouble(linija.split(",")[0]);
	        double y=Double.parseDouble(linija.split(",")[1]);
	        stablo.dodaj(new Tacka(x,y));
	        stablo.ispis();
	        
		}
		scanner.close();
		System.out.println("-------------------------------");
		//stablo.ispisiDubine();
		System.out.println("-------------------------------");
		//stablo.pretrazivanje(new Region(new Tacka(6.1, 8.9),new Tacka(9.0,1.0)));
		
	}

}

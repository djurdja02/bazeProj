package projekatBaze;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Rtree{
	private Cvor koren=null;
	private int max;
	private int min;
	private int dubina=0;
	
	public Rtree(int M,int m) {
		this.max=M;
		this.min=m;
		if(m>(M/2))this.min=M/2;
		koren=new Cvor();
	}
	
	private class Ulaz{
		Region mbr;
		Tacka t;
		Cvor dete=null;
		public Ulaz(Region mbr,Tacka t){
			this.mbr=mbr;
			this.t=t;	
		}
	}
	
	private class Cvor{
		private boolean list=true;
		List<Ulaz> ulazi;
		private Cvor prev=null;
		private Region mbr;
		
		public Cvor() {
			ulazi=new ArrayList<Ulaz>();
		}

		public Region izracunajMBR() {
			//ovo se desava samo ako je koren prazan
			if(ulazi.size()==0)return null;
			mbr=ulazi.get(0).mbr;
			for(int i=1;i<ulazi.size();i++) {
				mbr=mbr.unija(ulazi.get(i).mbr);
			}
			if(prev!=null) {
				for(Ulaz u:prev.ulazi) {
					if(u.dete.equals(this)) {					
						u.mbr=mbr;
						break;
					}
				}
			}
			return mbr;
		}

		@Override
		public String toString() {
			if(list) {
				 StringBuilder sb = new StringBuilder();
			        for (Ulaz ulaz : ulazi) {
			            sb.append(ulaz.t).append(", ");
			        }
			        sb.delete(sb.length() - 2, sb.length());  // Ukloni poslednji zarez
			        return "[" + sb.toString() + "]";
			}
			else {
		        return new StringBuilder(mbr+"").toString();
		    }
		}	
	}
	
	public void linearSplit(List<Ulaz> ulazi, Cvor novi1, Cvor novi2) {
		double rastojanje=0;
		Ulaz ulazp=null,ulazd=null; 
		for(int i=0;i<ulazi.size()-1;i++) {
			for(int j=i+1;j<ulazi.size();j++) {
				if(ulazi.get(i).mbr.rastojanje(ulazi.get(j).mbr)>rastojanje) {
					ulazp=ulazi.get(i);ulazd=ulazi.get(j);
					rastojanje=ulazi.get(i).mbr.rastojanje(ulazi.get(j).mbr);
				}
			}
		}
		novi1.ulazi.add(ulazp);
		novi2.ulazi.add(ulazd);
		for(int i=0;i<ulazi.size();i++) {
			//ako je neki od ova dva nasa ulaza
			if(ulazi.get(i).equals(ulazp) || ulazi.get(i).equals(ulazd))continue;
			
			//15.korak-ako ima jos k ulaza da se dodeli a jedan cvor ima m-k mesta onda se njemu dodeljuju svi ulazi
			if((ulazi.size()-i-1)==(min-novi2.ulazi.size())) {
				novi2.ulazi.add(ulazi.get(i));
				continue;
			}
			if((ulazi.size()-i-1)==(min-novi1.ulazi.size())) {
				novi1.ulazi.add(ulazi.get(i));
				
				continue;
			}
			//ako je blizi prvom cvoru
			if(ulazp.mbr.povecanje(ulazi.get(i).mbr)<ulazd.mbr.povecanje(ulazi.get(i).mbr)) {
				novi1.ulazi.add(ulazi.get(i));
				
			}
			//ako je blizi drugom cvoru
			else if(ulazp.mbr.povecanje(ulazi.get(i).mbr)>ulazd.mbr.povecanje(ulazi.get(i).mbr)) {
				novi2.ulazi.add(ulazi.get(i));
				
			}
			//ako su jednaka rastojanja korak 9
			else {
				if(novi1.ulazi.size()>novi2.ulazi.size()) {
					novi1.ulazi.add(ulazi.get(i));
					
				}
				else {
					novi2.ulazi.add(ulazi.get(i));
					
				}
			}
		}
		for(Ulaz u:novi2.ulazi) {
			if(u.dete!=null)u.dete.prev=novi2;
		}
		for(Ulaz u:novi1.ulazi) {
			if(u.dete!=null)u.dete.prev=novi1;
		}
		novi1.izracunajMBR();
		novi2.izracunajMBR();
	}
	//koristimo linear split
	public void splitInsert(Ulaz u,Cvor cvor) {	
		//7.korak 
		List<Ulaz> ulazi=new ArrayList<>();
		for(Ulaz ulaz:cvor.ulazi) {
			ulazi.add(ulaz);
		}
		ulazi.add(u);
		Cvor novi1=new Cvor();
		Cvor novi2=new Cvor();
		linearSplit(ulazi, novi1, novi2);
		if(cvor.list==false) {
			novi1.list=false;
			novi2.list=false;
		}
		if(cvor.equals(koren)) {
			Cvor noviKoren = new Cvor();
			Ulaz u1=new Ulaz(novi1.izracunajMBR(),novi1.mbr.getGl());
			u1.dete=novi1;
			Ulaz u2=new Ulaz(novi2.izracunajMBR(),novi2.mbr.getGl());
			u2.dete=novi2;
			novi1.prev=noviKoren;
			novi2.prev=noviKoren;
			noviKoren.ulazi.add(u1);
			noviKoren.ulazi.add(u2);
			noviKoren.list=false;
			noviKoren.izracunajMBR();
	        koren = noviKoren;
	        dubina++;
		}
		else {
			Cvor prethodni = cvor.prev;
	        for(int i=0;i<prethodni.ulazi.size();i++) {
	        	if(prethodni.ulazi.get(i).dete==cvor) {
	        		prethodni.ulazi.remove(i);
	        		break;
	        	}
	        }
	        Ulaz u1=new Ulaz(novi1.izracunajMBR(),novi1.mbr.getGl());
			u1.dete=novi1;
			Ulaz u2=new Ulaz(novi2.izracunajMBR(),novi2.mbr.getGl());
			u2.dete=novi2;
			novi1.prev=prethodni;
			prethodni.list=false;
			ubaciListNadjen(u1, prethodni);
			ubaciListNadjen(u2,prethodni);
		}
		
	}
	public void izracunajMBR(Cvor trenutni) {
		trenutni.izracunajMBR();
		Cvor pre=trenutni.prev;
		while(pre!=null) {
			pre.izracunajMBR();
			pre=pre.prev;
		}
	}
	public void ubaciListNadjen(Ulaz ulaz,Cvor trenutni) {
		//korak 3: ako moze da stane
		if(trenutni.ulazi.size()<max) {
			//korak 4: ubacujem novi ulaz
			trenutni.ulazi.add(ulaz);
			if(ulaz.dete!=null) {ulaz.dete.prev=trenutni;trenutni.list=false;}		
			//korak 5: uvecavam mbr svim prethodnicima
			izracunajMBR(trenutni);
		}
		//korak 6: pun je list
		else {
			splitInsert(ulaz,trenutni);
		}
	}

	public void dodaj(Tacka t) {
		//nalazimo list
		Cvor trenutni=koren;
		Region novi=new Region(t, t);
		while(!trenutni.list) {
			double min=-1;
			List<Cvor> kandidati=new ArrayList<>();
			//trazimo najmanje povecanje -korak 1
			for(Ulaz ulaz:trenutni.ulazi) {
				double povecanje=ulaz.mbr.povecanje(novi);
				if(min==-1 || povecanje<min) {
					kandidati.clear();
					min=povecanje;
					}
				if(min==povecanje) {
					kandidati.add(ulaz.dete);
				}
			}
			//ako ima vise sa istim povecanjem trazimo najmanju povrsinu- korak 2
			min=-1;
			Cvor sledeci=null;
			for(Cvor kandidat: kandidati) {
				if(min==-1 || kandidat.mbr.povrsina()<min) {
					min=kandidat.mbr.povrsina();
					sledeci=kandidat;
				}
			}
			trenutni=sledeci;
		}
		Ulaz ulaz=new Ulaz(novi,t);
		ubaciListNadjen(ulaz, trenutni);
		
	}
	//za ispis po dubini
	public void ispisiCvorDubina(Cvor cvor,int dubina,int i) {
		if(dubina==i) {
			System.out.println(cvor.toString()+ "  ");
			return;
		}else {
			if(cvor.list)return;
			else {
				for(Ulaz ulaz: cvor.ulazi)
				ispisiCvorDubina(ulaz.dete, dubina, i+1);
			}
		}
	}
	public void ispisPoDubini(int dubina) {
		int i=0;
		System.out.println("Dubina "+ dubina);
		ispisiCvorDubina(koren,dubina,i);
	}
	//za ispis celog stabla stabla po dubinama
	public void ispisiDubine() {
		for(int i=0;i<=dubina;i++) {
			ispisPoDubini(i);
			System.out.println("\n");
		}
	}
	//ispis celog stabla
	public void ispis() {
		String str="";
		Cvor trenutni=koren;
		ispisCvora(trenutni, str,true);	
	}
	public void ispisCvora(Cvor trenutni,String str,boolean isLast) {
		System.out.print(str);
	    System.out.print(isLast ? "|__ " : "|--");
	    System.out.println(trenutni.toString());
		
		if(!trenutni.list) {
			int index=trenutni.ulazi.size()-1;
			for (int i = 0; i < index; i++) {
				 ispisCvora(trenutni.ulazi.get(i).dete, str+(isLast ? "    " : "|   "), false);
			 }
			 if(index>=0) {
				ispisCvora(trenutni.ulazi.get(index).dete,str+ (isLast? "    " : "|   "), true);
			 }
		 }
		
	}
	//ucitavanje iz datoteke
	public void ucitajDatoteku(String ime) {
		try(BufferedReader br=new BufferedReader(new FileReader(ime))){
			String line=null;
			while((line=br.readLine())!=null) {
				String[] tacka=line.split(",");
				double x = Double.parseDouble(tacka[0].trim());
                double y = Double.parseDouble(tacka[1].trim());
                Tacka t = new Tacka(x, y);
                dodaj(t);
			}
		} catch (Exception e) {
			System.out.println("Nije moguce citanje iz datoteke!");
		}
	}
	
}

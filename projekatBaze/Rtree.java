package projekatBaze;

import java.util.ArrayList;
import java.util.List;


public class Rtree{
	private Cvor koren=null;
	private int max;
	private int min;
	
	public Rtree(int M,int m) {
		this.max=M;
		this.min=m;
		if(m>M/2)this.min=M/2;
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
		private List<Ulaz> ulazi;
		private int brojUlaza=0;
		private Cvor prev=null;
		private Region mbr;
		
		public Cvor() {
			ulazi=new ArrayList<Ulaz>();
		}
		
		public void uvecaj() {
			brojUlaza++;
			
		}
		
		public Region izracunajMBR() {
			if(brojUlaza==0)return null;
			mbr=ulazi.get(0).mbr;
			for(int i=1;i<brojUlaza;i++) {
				mbr=mbr.unija(ulazi.get(i).mbr);
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
	//koristimo linear split
	public void splitInsert(Ulaz u,Cvor cvor) {		
			List<Ulaz> ulazi=cvor.ulazi;
			ulazi.add(u);
			//7.korak najvece rastojanje
			Ulaz ulazp=null,ulazd=null;
			double distanca=0;
			for(Ulaz ulaz1:ulazi) {
				for(int i=ulazi.indexOf(ulaz1)+1;i<ulazi.size();i++) {
					if(ulaz1.mbr.rastojanje(ulazi.get(i).mbr)>distanca) {
						ulazp=ulaz1;ulazd=ulazi.get(i);
						distanca=ulaz1.mbr.rastojanje(ulazi.get(i).mbr);
					}
				}
			}
			Cvor novi1=new Cvor();
			novi1.ulazi.add(ulazp);
			Cvor novi2=new Cvor();
			if(!cvor.list) {
				novi1.list=false;novi2.list=false;
			}
			novi1.uvecaj();
			novi2.ulazi.add(ulazd);
			novi2.uvecaj();
			//8.korak dodajes ostale ulaze u neki od ova dva cvora
			for(int i=0;i<ulazi.size();i++) {
				//ako je neki od ova dva nasa ulaza
				if(ulazi.get(i).equals(ulazp) || ulazi.get(i).equals(ulazd))continue;
				
				//15.korak-ako ima jos k ulaza da se dodeli a jedan cvor ima m-k mesta onda se njemu dodeljuju svi ulazi
				if((ulazi.size()-i-1)==(min-novi2.ulazi.size())) {
					novi2.ulazi.add(ulazi.get(i));
					novi2.uvecaj();
					continue;
				}
				if((ulazi.size()-i-1)==(min-novi1.ulazi.size())) {
					novi1.ulazi.add(ulazi.get(i));
					novi1.uvecaj();
					continue;
				}
				//ako je blizi prvom cvoru
				if(ulazp.mbr.povecanje(ulazi.get(i).mbr)<ulazd.mbr.povecanje(ulazi.get(i).mbr)) {
					novi1.ulazi.add(ulazi.get(i));
					novi1.uvecaj();
				}
				//ako je blizi drugom cvoru
				else if(ulazp.mbr.povecanje(ulazi.get(i).mbr)>ulazd.mbr.povecanje(ulazi.get(i).mbr)) {
					novi2.ulazi.add(ulazi.get(i));
					novi2.uvecaj();
				}
				//ako su jednaka rastojanja korak 9
				else {
					if(novi1.brojUlaza>novi2.brojUlaza) {
						novi1.ulazi.add(ulazi.get(i));
						novi1.uvecaj();
					}
					else {
						novi2.ulazi.add(ulazi.get(i));
						novi2.uvecaj();
					}
				}
			}
			Cvor prev=cvor.prev;
			//nove cvorove uvezujemo u stablo
			//stari brisemo
			cvor.prev=null;
			cvor.ulazi=null;
			Ulaz ulaz1=new Ulaz(novi1.izracunajMBR(), novi1.mbr.getGl());
			ulaz1.dete=novi1;
			Ulaz ulaz2=new Ulaz(novi2.izracunajMBR(), novi2.mbr.getGl());
			ulaz2.dete=novi2;
			//ako je koren
			if(prev==null) {
				prev=new Cvor();
				prev.list=false;
				prev.ulazi.add(ulaz1);
				prev.ulazi.add(ulaz2);
				prev.brojUlaza+=2;
				prev.izracunajMBR();
				koren=prev;
				ulaz1.dete.prev=prev;
				ulaz2.dete.prev=prev;
			}
			//else
			else {
				for(int i=0;i<prev.brojUlaza;i++) {
					if(prev.ulazi.get(i).dete.equals(cvor)) {
						prev.ulazi.set(i, ulaz1);
						ulaz1.dete.prev=prev;
						prev.list=false;
						break;
					};
				}
				ubaciListNadjen(ulaz2,prev);
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
		if(trenutni.brojUlaza<max) {
			//korak 4: ubacujem novi ulaz
			trenutni.ulazi.add(trenutni.brojUlaza, ulaz);
			if(ulaz.dete!=null) {ulaz.dete.prev=trenutni;trenutni.list=false;}
			trenutni.uvecaj();
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
	//TODO izmeni ispis
	//ispis celog stabla
	public void ispis() {
		String str="";
		Cvor trenutni=koren;
		ispisCvora(trenutni, str);	
	}
	public void ispisCvora(Cvor trenutni,String str) {
		System.out.println(str + "└── " + trenutni.toString());
		 if(!trenutni.list) {
			 str+="    ";
			 for(Ulaz u: trenutni.ulazi) {
				 ispisCvora(u.dete, str);
			 }
		 }
	}
}

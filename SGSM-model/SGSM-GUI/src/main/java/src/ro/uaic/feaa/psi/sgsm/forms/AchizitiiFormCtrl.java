package src.ro.uaic.feaa.psi.sgsm.forms;

import ro.uaic.feaa.psi.sgsm.model.entities.DocInsotitor;
import ro.uaic.feaa.psi.sgsm.model.entities.LinieDocument;
import ro.uaic.feaa.psi.sgsm.model.entities.Receptie;

/**
 * Aceasta clasa are rol de Controller. Asta inseamna ca furnizeaza metodele
 * necesare pentru implementarea comportamentului formularului, comportament ce
 * se va manifesta ca raspuns la actiunile utilizatorului (apasarea butoanelor,
 * selectia unui element din liste etc). Pentru a accesa datele introduse de
 * utilizator pe forma grafica, clasa utilizeaza metoda compunerii prin
 * intermediul atributului formData.
 * 
 * 
 * @author cretuli
 * 
 */
public class AchizitiiFormCtrl {
	// cateva constante pentru tipuri de documente posibile (cum nu avem o lista
	// dinamica, acestea vor fi considerate constante
	public static final String NECUNOSCUT = "Necunoscut";
	public static final String STORNARE = "Stornare";
	public static final String AVIZ = "Aviz";
	public static final String FACTURA = "Factura";

	// date formularului sunt pastrate intr-un obiect AchizitiiFormBean
	private AchizitiiFormData formData = new AchizitiiFormData(); // acest
																	// atribut
																	// nu
																	// trebuie
																	// sa fie
																	// null

	

	public AchizitiiFormData getFormData() {
		return formData;
	}

	public void setFormData(AchizitiiFormData formData) {
		this.formData = formData;
	}

	/**
	 * Implementeaza comportamentul pentru butonul Adaugare. ATENTIE! aceasta
	 * operatie nu presupune salvarea obiectului in baza de date ci doar
	 * pregatirea unui obiect nou de tip DocumentInsotitor care va prelua
	 * ulterior datele introduse de utilizator in formular.
	 * 
	 */
	public void documentNou() {

		// se creeaza noul document si se procedeaza la setarea lui ca document
		// curent al formularului
		DocInsotitor doc = new DocInsotitor();
		this.formData.setDocumentCurent(doc);
		// ar fi perfect suficient atat, daca nu am avea si date predefinite

		// se adauga eventuale date predefinite - din nou, folosim constante!!!
		// daca nu avem o sursa de date configurabila de catre utilizator
		// (fisiere text sau tabele in BD)
		if (this.formData.getOperatieSelectata().equals(
				AchizitiiFormData.ACHIZITIE_CU_FACTURA))
			doc.setTipDocument(FACTURA);
		else if (this.formData.getOperatieSelectata().equals(
				AchizitiiFormData.ACHIZITIE_CU_AVIZ))
			doc.setTipDocument(AVIZ);
		else if (this.formData.getOperatieSelectata().equals(
				AchizitiiFormData.STORNARE))
			doc.setTipDocument(STORNARE);
		else
			doc.setTipDocument(NECUNOSCUT);// intotdeauna trebuie sa existe si
		// un default absolut

		doc.setDataOperare(new java.util.Date());
		// Este bine sa stabilim si o valoare default pentru toate relatiile
		// ManyToOne
		// Stabilim primul furnizor din lista ca furnizor implicit.
		doc.setFurnizor(this.formData.getFurnizorSelectat());
	}

	/**
	 * Implementeaza comportamentul pentru butonul Adaugare din tab-ul Receptii.
	 * Genereaza un nou document receptie pentru a fi prezentat in tabelul 10
	 */
	public void adaugaReceptie() {
		// verificam existenta unul document curent si generam o eroare daca nu
		// exista
		if (this.formData.getDocumentCurent() == null)
			throw new RuntimeException("Selectati mai intai un document!!!!");
		Receptie r = new Receptie();
		r.setGestiune(this.formData.getListaGestiuni().get(0));// implicit,
																// prima
																// gestiune din
																// lista
		this.formData.setReceptieSelectata(r);
		this.formData.getDocumentCurent().addReceptie(r);
		// utilizatorul va selecta gestiunea ulterior prin intermediul
		// obiectelor grafice
	}

	public void adaugaLinieReceptie() {
		// verifica existenta unei receptii selectate
		if (this.formData.getReceptieSelectata() == null)
			throw new RuntimeException("Selectati mai intai o receptie!!!!");
		LinieDocument linie = new LinieDocument();
		linie.setMaterial(this.formData.getListaMateriale().get(0));// implicit,
																	// primul
																	// din lista
		// adaugam noua linie in lista receptiei selectate
		this.formData.getReceptieSelectata().addLinieDocument(linie);
		// utilizatorul va folosi apoi obiectele grafice pentru a adauga date pe
		// noua linie
	}

	/**
	 * comite modificarile din formular in baza de date
	 */
	public void salveazaModificariDocument() {
		this.getFormData().getDocRepo().beginTransaction();
		this.getFormData().getDocRepo().saveDocument(this.getFormData().getDocumentCurent());
		this.getFormData().getDocRepo().commitTransaction();

	}

}

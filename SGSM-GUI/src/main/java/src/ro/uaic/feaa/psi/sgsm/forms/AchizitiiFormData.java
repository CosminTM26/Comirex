package src.ro.uaic.feaa.psi.sgsm.forms;

import java.util.LinkedList;
import java.util.List;

import ro.uaic.feaa.psi.sgsm.model.entities.BunMaterial;
import ro.uaic.feaa.psi.sgsm.model.entities.DocInsotitor;
import ro.uaic.feaa.psi.sgsm.model.entities.Furnizor;
import ro.uaic.feaa.psi.sgsm.model.entities.Gestiune;
import ro.uaic.feaa.psi.sgsm.model.entities.LinieDocument;
import ro.uaic.feaa.psi.sgsm.model.entities.Receptie;

import ro.uaic.feaa.psi.sgsm.model.repository.DocumentRepository;

import ro.uaic.feaa.psi.sgsm.model.repository.MasterRepository;

/**
 * Aceasta clasa constituie modelul pentru formularul de achizitii. Este un
 * adaptor al modelului SGSM: are rolul de a pregati (filtra/adapta/transforma)
 * modelul SGSM in forma necesara formularului.
 * 
 * Structura clasei este organziata pe sectiuni pentru a fi mai clara
 * corespondenta cu descrierea narativa a scenariilor si cu imaginea "mock-up" a
 * formularului (figura 4).
 * 
 * @author cretuli
 * 
 */
public class AchizitiiFormData {

	// o suita de constante necesare mai tarziu
	public static final String STORNARE = "Stornare";
	public static final String FACTURA_INTARZIATA = "Factura intarziata";
	public static final String ACHIZITIE_CU_AVIZ = "Achizitie cu Aviz";
	public static final String ACHIZITIE_CU_FACTURA = "Achizitie cu factura";

	// --------------ZONA 0 - COMUNA TUTUROR FORMULARELOR -------------------//

	// obiectul-tinta (sursa de date) al formularului - cel aflat curent in
	// editare
	// (in cazul nostru poate fi factura sau aviz)

	private DocInsotitor documentCurent;

	// colectia de obiecte-tinta obtinute in urma unei operatii de cautare
	private List<DocInsotitor> listaDocumente;

	// obiecte de tip Repository necesare pentru interogarea modelului
	private MasterRepository masterRepo = new MasterRepository();
	private DocumentRepository docRepo = new DocumentRepository();

	// si metodele de acces (getteri/setteri) - mutate la finalul clasei pentru
	// claritate

	// ---------------ZONA 1 - FURNIZORI --------------//
	/**
	 * Pentru incarcarea elementelor din lista (2) cboTipOperatie. Obiectul de
	 * sincronizare selectie corespunzator este documentCurent.furnizor
	 */
	private List<Furnizor> listaFurnizorilor;

	/**
	 * implementare standard pentru o colectie-sursa de date a unui obiect de
	 * tip lista
	 * 
	 * @return
	 */
	public List<Furnizor> getListaFurnizorilor() {
		if (this.listaFurnizorilor == null) // e primul apel (userul tocmai a
		// deschis formul)
		{
			// this.masterRepo.findFurnizoriAll();//varianta neoptimizata va
			// dura mai mult si va returna si obiecte Localitate, atasate prin
			// relatia Furnizor-ManyToOne-Localitate. In cazul nostru nu avem
			// nevoie de obiecte Localitate atasate obiectelor Furnizor
			this.listaFurnizorilor = this.masterRepo.findFurnizoriAllLight();
		}
		return listaFurnizorilor;
	}

	public void setListaFurnizorilor(List<Furnizor> listaFurnizorilor) {
		this.listaFurnizorilor = listaFurnizorilor;
	}

	// Getteri si Setteri pentru manevrarea slectiei curente.
	// lucreaza cu obiectul atasat documentului aflat in editare.
	public Furnizor getFurnizorSelectat() {
		return this.documentCurent.getFurnizor();
	}

	/**
	 * ATENTIE - in majoritatea mediilor de dezvoltare, obiectele grafice de tip
	 * lista returneaza un id (o valoare atomica / un singul atribut) al
	 * obiectului selectat din lista. Ca urmare, trebuie incarcat obiectul
	 * corespunzator pentru relatia ManyToOne.
	 * 
	 * @param furnizorSelectat
	 */
	public void setFurnizorSelectat(Furnizor furnizorSelectat) {
		Furnizor furnizorComplet = this.masterRepo
				.findFurnizorById(furnizorSelectat.getId());
		this.documentCurent.setFurnizor(furnizorComplet);
	}

	// ------------- ZONA 2 -- tip operatiune --------------

	// adaugam si lista de tipuri de operatiuni:
	private List<String> operatiuni;

	public List<String> getOperatiuni() {
		if (operatiuni == null)// primul acces - userul deschide formul
		{
			operatiuni = new LinkedList<String>();
			// Folosim constante declarate specific pentru aceasta situatie, din
			// moment ce nu avem o alta sursa
			// de date.
			// Evident, asta inseamna ca lista va fi "inchisa", orice modificare
			// aici necesitand recompilarea si redistribuirea aplicatiei.
			// De regula, se folosesc fisiere de configurare sau tabele-utilitar
			// in BD.
			// Aici insa, folosim aceasta eroare de design cu titlu de exemplu
			operatiuni.add(ACHIZITIE_CU_FACTURA);
			operatiuni.add(ACHIZITIE_CU_AVIZ);
			operatiuni.add(FACTURA_INTARZIATA);
			operatiuni.add(STORNARE);
		}
		return operatiuni;
	}

	// operatia selectata - o asociem cu tipDocument
	private String operatieSelectata;

	public String getOperatieSelectata() {
		return this.operatieSelectata;
	}

	public void setOperatieSelectata(String operatieSelectata) {
		this.operatieSelectata = operatieSelectata;
	}

	// ------------- ZONA 3 -- date despre documentul curent afisat pe ecran
	// --------------
	// date privind documentul curent = obiectul tinta al formularului si este
	// deja reprezentat (vezi zona 0, atributul documentCurent)

	// ------------- ZONA 4 -- linii document - read only
	// ---------------------------------------------
	// --tabelul afisa totalul receptionat pentru docummentul curent
	// --pentru ca pot fi mai multe receptii pentru acelasi document, este
	// necesara cumularea liniilor fiecarei receptii intr-o singura colectie

	public List<LinieDocument> getArticoleReceptionate() {
		return this.documentCurent.getArticoleReceptionate();
	}

	// ------------- ZONA 5 - date despre mijlocul de transaport -
	// -------------------------
	// --nimic de adaugat - se lucreaza cu atributele obiectului documentCurent

	// ------------- ZONA 6 - totaluri
	// ----------------------------------------------------
	// -- nimic de adaugat - se lucreaza cu atributele specifice ale obiectului
	// documentCurent

	// ----------Zona 10 - tabelul receptii ------------------
	// Formularul va prezenta lista de obiecte Receptie reprezentate de colectia
	// OneToMany corespunzatoare a documentCurent
	// trebuie adaugat un atribut ce va tine referinta catre obiectul Receptie
	// curent selectat

	private Receptie receptieSelectata;

	public Receptie getReceptieSelectata() {
		return receptieSelectata;
	}

	public void setReceptieSelectata(Receptie receptieSelectata) {
		this.receptieSelectata = receptieSelectata;
	}

	// trebuie adaugat si un atribut pentru sursa de date a listei de gestiuni

	private List<Gestiune> listaGestiuni;

	public List<Gestiune> getListaGestiuni() {
		if (listaGestiuni == null)
			listaGestiuni = masterRepo.findGestiuniAll();

		return listaGestiuni;
	}

	// ----------Zona 11 - tabelul de materiale receptionate pe receptia curenta
	// ------------------
	// -- nimic de adaugat aici pentru sursa de date a tabelului- formularul va
	// prezenta colectia liniiDocument a receptiei selectate
	// --de adaugat doar suportul pentru lista de materiale

	private List<BunMaterial> listaMateriale;

	public List<BunMaterial> getListaMateriale() {
		if (listaMateriale == null)
			listaMateriale = masterRepo.findBunuriMaterialeAll();

		return listaMateriale;
	}

	// GETTERI SI SETTERI pentru proprietatile zonei 0

	// metode de acces pentru zona 0
	public DocInsotitor getDocumentCurent() {
		return documentCurent;
	}

	public void setDocumentCurent(DocInsotitor documentCurent) {
		this.documentCurent = documentCurent;
	}

	public List<DocInsotitor> getListaDocumente() {
		return listaDocumente;
	}

	public void setListaDocumente(List<DocInsotitor> listaDocumente) {
		this.listaDocumente = listaDocumente;
	}

	public MasterRepository getMasterRepo() {
		return masterRepo;
	}

	public void setMasterRepo(MasterRepository masterRepo) {
		this.masterRepo = masterRepo;
	}

	public DocumentRepository getDocRepo() {
		return docRepo;
	}

	public void setDocRepo(DocumentRepository docRepo) {
		this.docRepo = docRepo;
	}

}

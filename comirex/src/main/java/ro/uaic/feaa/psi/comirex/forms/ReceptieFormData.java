package ro.uaic.feaa.psi.comirex.forms;

import java.util.LinkedList;
import java.util.List;

import ro.uaic.feaa.psi.comirex.model.entities.DocInsotitor;
import ro.uaic.feaa.psi.comirex.model.entities.Furnizor;
import ro.uaic.feaa.psi.comirex.model.entities.Gestiune;
import ro.uaic.feaa.psi.comirex.model.entities.LinieIntrare;
import ro.uaic.feaa.psi.comirex.model.entities.Produs;
import ro.uaic.feaa.psi.comirex.model.entities.Receptie;
import ro.uaic.feaa.psi.comirex.model.repository.DocumentRepository;
import ro.uaic.feaa.psi.comirex.model.repository.MasterRepository;

/**
 * Modelul (in sensul MVC) pentru formularul "Receptie - Document insotitor".
 *
 * Aceasta clasa este un adaptor al modelului de domeniu Comirex: are rolul de
 * a pregati (filtra/adapta/transforma) entitatile in forma necesara
 * formularului. Listele afisate in liste/combo-uri (furnizori, gestiuni,
 * produse, tipuri de operatii) sunt incarcate "lazy" - doar la primul acces -
 * pentru a evita interogari inutile asupra bazei de date (mecanism de tip
 * cache).
 */
public class ReceptieFormData {

	// ---------- Constante pentru tipurile de operatii ----------//
	public static final String RECEPTIE_CU_FACTURA = "Receptie cu Factura";
	public static final String RECEPTIE_CU_AVIZ = "Receptie cu Aviz";
	public static final String FACTURA_INTARZIATA = "Factura intarziata";
	public static final String STORNARE = "Stornare";

	// ---------- ZONA 0 - DATE COMUNE ----------//

	// Documentul curent in editare (obiectul-tinta al formularului)
	private DocInsotitor documentCurent;

	// Lista de documente obtinute in urma unei cautari
	private List<DocInsotitor> listaDocumente;

	// Repository-uri pentru interogarea/salvarea modelului
	private MasterRepository masterRepo = new MasterRepository();
	private DocumentRepository docRepo = new DocumentRepository();

	// ---------- ZONA 1 - FURNIZORI ----------//

	private List<Furnizor> listaFurnizori;

	public List<Furnizor> getListaFurnizori() {
		if (this.listaFurnizori == null) {
			this.listaFurnizori = this.masterRepo.findFurnizoriAll();
		}
		return this.listaFurnizori;
	}

	public void setListaFurnizori(List<Furnizor> listaFurnizori) {
		this.listaFurnizori = listaFurnizori;
	}

	public Furnizor getFurnizorSelectat() {
		if (this.documentCurent == null) {
			return null;
		}
		return this.documentCurent.getFurnizor();
	}

	public void setFurnizorSelectat(Furnizor furnizorSelectat) {
		// Pentru sincronizare cu obiectul atasat documentului curent, se
		// reincarca obiectul complet din BD (relatia ManyToOne).
		Furnizor furnizorComplet = this.masterRepo
				.findFurnizorById(furnizorSelectat.getId());
		this.documentCurent.setFurnizor(furnizorComplet);
	}

	// ---------- ZONA 2 - TIP OPERATIE ----------//

	private List<String> operatiuni;
	private String operatieSelectata;

	public List<String> getOperatiuni() {
		if (this.operatiuni == null) {
			this.operatiuni = new LinkedList<String>();
			this.operatiuni.add(RECEPTIE_CU_FACTURA);
			this.operatiuni.add(RECEPTIE_CU_AVIZ);
			this.operatiuni.add(FACTURA_INTARZIATA);
			this.operatiuni.add(STORNARE);
		}
		return this.operatiuni;
	}

	public String getOperatieSelectata() {
		return this.operatieSelectata;
	}

	public void setOperatieSelectata(String operatieSelectata) {
		this.operatieSelectata = operatieSelectata;
	}

	// ---------- ZONA 3 - DATE DESPRE DOCUMENT ----------//
	// Datele documentului curent sunt accesate direct prin documentCurent

	// ---------- ZONA 4 - LINII DOCUMENT (read-only, totalul receptiilor) ----------//
	// Pentru ca pot fi mai multe receptii pe acelasi document insotitor,
	// returnam toate liniile cumulate.
	public List<LinieIntrare> getArticoleReceptionate() {
		List<LinieIntrare> articole = new LinkedList<LinieIntrare>();
		if (this.documentCurent == null) {
			return articole;
		}
		for (Receptie r : this.documentCurent.getReceptii()) {
			articole.addAll(r.getLiniiIntrare());
		}
		return articole;
	}

	// ---------- ZONA 10 - TABELUL DE RECEPTII ----------//

	private Receptie receptieSelectata;

	public Receptie getReceptieSelectata() {
		return this.receptieSelectata;
	}

	public void setReceptieSelectata(Receptie receptieSelectata) {
		this.receptieSelectata = receptieSelectata;
	}

	// Lista de gestiuni pentru selectie (combo-box pe receptie)
	private List<Gestiune> listaGestiuni;

	public List<Gestiune> getListaGestiuni() {
		if (this.listaGestiuni == null) {
			this.listaGestiuni = this.masterRepo.findGestiuniAll();
		}
		return this.listaGestiuni;
	}

	public void setListaGestiuni(List<Gestiune> listaGestiuni) {
		this.listaGestiuni = listaGestiuni;
	}

	// ---------- ZONA 11 - LINII RECEPTIE CURENTA + LISTA DE PRODUSE ----------//

	private List<Produs> listaProduse;

	public List<Produs> getListaProduse() {
		if (this.listaProduse == null) {
			this.listaProduse = this.masterRepo.findProduseAll();
		}
		return this.listaProduse;
	}

	public void setListaProduse(List<Produs> listaProduse) {
		this.listaProduse = listaProduse;
	}

	// ---------- GETTERI / SETTERI - ZONA 0 ----------//

	public DocInsotitor getDocumentCurent() {
		return this.documentCurent;
	}

	public void setDocumentCurent(DocInsotitor documentCurent) {
		this.documentCurent = documentCurent;
	}

	public List<DocInsotitor> getListaDocumente() {
		return this.listaDocumente;
	}

	public void setListaDocumente(List<DocInsotitor> listaDocumente) {
		this.listaDocumente = listaDocumente;
	}

	public MasterRepository getMasterRepo() {
		return this.masterRepo;
	}

	public void setMasterRepo(MasterRepository masterRepo) {
		this.masterRepo = masterRepo;
	}

	public DocumentRepository getDocRepo() {
		return this.docRepo;
	}

	public void setDocRepo(DocumentRepository docRepo) {
		this.docRepo = docRepo;
	}
}

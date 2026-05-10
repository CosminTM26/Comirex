package ro.uaic.feaa.psi.comirex.forms;

import java.util.Date;
import java.util.List;

import ro.uaic.feaa.psi.comirex.model.entities.DocInsotitor;
import ro.uaic.feaa.psi.comirex.model.entities.Furnizor;
import ro.uaic.feaa.psi.comirex.model.entities.Gestiune;
import ro.uaic.feaa.psi.comirex.model.entities.LinieIntrare;
import ro.uaic.feaa.psi.comirex.model.entities.Produs;
import ro.uaic.feaa.psi.comirex.model.entities.Receptie;
import ro.uaic.feaa.psi.comirex.model.entities.Stoc;
import ro.uaic.feaa.psi.comirex.model.repository.MasterRepository;

/**
 * Controllerul (in sensul MVC) pentru formularul "Receptie - Document
 * insotitor".
 *
 * Furnizeaza metodele necesare pentru implementarea comportamentului
 * formularului ca raspuns la actiunile utilizatorului (apasarea butoanelor,
 * selectia unui element din liste etc). Acceseaza datele introduse de
 * utilizator pe forma grafica prin compunere, prin intermediul atributului
 * formData.
 */
public class ReceptieFormCtrl {

	// Constante pentru tipurile de documente posibile (cum nu avem o lista
	// dinamica, le tratam drept constante)
	public static final String NECUNOSCUT = "Necunoscut";
	public static final String STORNARE = "Stornare";
	public static final String AVIZ = "Aviz";
	public static final String FACTURA = "Factura";

	// Datele formularului sunt pastrate intr-un obiect ReceptieFormData
	// Atributul nu trebuie sa fie null!
	private ReceptieFormData formData = new ReceptieFormData();

	public ReceptieFormData getFormData() {
		return this.formData;
	}

	public void setFormData(ReceptieFormData formData) {
		this.formData = formData;
	}

	/**
	 * Implementeaza comportamentul pentru butonul "Document nou".
	 *
	 * ATENTIE: aceasta operatie nu presupune salvarea obiectului in baza de
	 * date, ci doar pregatirea unui obiect nou de tip DocInsotitor care va
	 * prelua ulterior datele introduse de utilizator in formular.
	 */
	public void documentNou() {
		DocInsotitor doc = new DocInsotitor();
		this.formData.setDocumentCurent(doc);

		// Setare tip document pe baza operatiei selectate
		String operatie = this.formData.getOperatieSelectata();
		if (RECEPTIE_CU_FACTURA_OP.equals(operatie)) {
			doc.setTipDocument(FACTURA);
		} else if (RECEPTIE_CU_AVIZ_OP.equals(operatie)) {
			doc.setTipDocument(AVIZ);
		} else if (STORNARE_OP.equals(operatie)) {
			doc.setTipDocument(STORNARE);
		} else {
			// Default - intotdeauna trebuie sa existe si un default absolut
			doc.setTipDocument(NECUNOSCUT);
		}

		doc.setDataDocument(new Date());
		doc.setDataOperare(new Date());

		// Daca avem deja furnizori incarcati, atasam primul ca default
		if (!this.formData.getListaFurnizori().isEmpty()) {
			doc.setFurnizor(this.formData.getListaFurnizori().get(0));
		}

		// Resetam si receptia selectata - documentul nou nu are inca receptii
		this.formData.setReceptieSelectata(null);
	}

	/**
	 * Implementeaza comportamentul pentru selectia unui furnizor din combo-box.
	 *
	 * @param furnizor furnizorul selectat de utilizator
	 */
	public void selectieFurnizor(Furnizor furnizor) {
		if (this.formData.getDocumentCurent() == null) {
			throw new RuntimeException("Selectati sau creati mai intai un document!");
		}
		this.formData.setFurnizorSelectat(furnizor);
	}

	/**
	 * Implementeaza comportamentul pentru butonul "Adauga receptie" din tab-ul
	 * Receptii. Genereaza o noua receptie pentru documentul curent.
	 */
	public void adaugaReceptie() {
		if (this.formData.getDocumentCurent() == null) {
			throw new RuntimeException("Selectati sau creati mai intai un document!");
		}
		Receptie r = new Receptie();
		r.setDataReceptie(new Date());

		// Implicit, prima gestiune din lista (utilizatorul poate schimba
		// ulterior prin obiectele grafice)
		if (!this.formData.getListaGestiuni().isEmpty()) {
			r.setGestiune(this.formData.getListaGestiuni().get(0));
		}

		this.formData.getDocumentCurent().addReceptie(r);
		this.formData.setReceptieSelectata(r);
	}

	/**
	 * Implementeaza comportamentul pentru butonul "Adauga linie" din grid-ul de
	 * linii intrare. Genereaza o noua linie pentru receptia curent selectata.
	 *
	 * Cantitatile recepționate sunt editabile, iar diferentele fata de
	 * cantitatea comandata se vor calcula prin obiectele grafice (binding).
	 */
	public void adaugaLinieIntrare() {
		if (this.formData.getReceptieSelectata() == null) {
			throw new RuntimeException("Selectati mai intai o receptie!");
		}
		LinieIntrare linie = new LinieIntrare();
		linie.setCantitate(0.0);
		linie.setPretAchizitie(0.0);
		linie.setPretVanzare(0.0);
		linie.setAdaos(0.0);

		// Implicit, primul produs din lista (utilizatorul va modifica)
		if (!this.formData.getListaProduse().isEmpty()) {
			linie.setProdus(this.formData.getListaProduse().get(0));
		}

		this.formData.getReceptieSelectata().addLinieIntrare(linie);
	}

	/**
	 * Comite modificarile din formular in baza de date si actualizeaza stocul.
	 *
	 * Fluxuri suportate:
	 *   - Receptie cu Factura / Aviz : cantitate pozitiva → creste stocCurent
	 *   - Stornare                   : cantitate negativa → scade stocCurent
	 *   - Stergere linie din memorie : linia nu exista la salvare → stocul neafectat
	 *
	 * Actualizarea stocului se face DOAR pentru documente noi (id == null inainte
	 * de salvare). Documentele deja salvate (incarcate prin navigare) nu
	 * retrigeraza actualizarea stocului, evitandu-se dubla contabilizare.
	 */
	public void salveazaModificariDocument() {
		if (this.formData.getDocumentCurent() == null) {
			throw new RuntimeException("Nu exista nici un document de salvat!");
		}

		// Retinem daca documentul este nou INAINTE de salvare
		boolean esteDocumentNou = (this.formData.getDocumentCurent().getId() == null);

		this.formData.getDocRepo().beginTransaction();
		DocInsotitor doc = this.formData.getDocumentCurent();
		DocInsotitor salvat = this.formData.getDocRepo().saveDocInsotitor(doc);
		this.formData.setDocumentCurent(salvat);

		// Actualizam stocul numai la crearea unui document nou
		if (esteDocumentNou) {
			actualizeazaStoc(salvat);
		}

		this.formData.getDocRepo().commitTransaction();
	}

	/**
	 * Actualizeaza stocul curent (tabela stoc) pentru fiecare linie de intrare
	 * din documentul salvat.
	 *
	 * Logica per linie:
	 *   - Cauta inregistrarea Stoc pentru perechea (produs, gestiune).
	 *   - Daca exista: stocCurent += cantitate (pozitiv sau negativ).
	 *   - Daca nu exista: creeaza o inregistrare noua cu stocCurent = cantitate.
	 *
	 * Liniile cu produs null, gestiune null sau cantitate 0 sunt sarite silentios.
	 */
	private void actualizeazaStoc(DocInsotitor doc) {
		MasterRepository masterRepo = this.formData.getMasterRepo();

		for (Receptie receptie : doc.getReceptii()) {
			Gestiune gestiune = receptie.getGestiune();
			if (gestiune == null || gestiune.getId() == null) {
				continue; // receptie fara gestiune alocata - sarim
			}

			for (LinieIntrare linie : receptie.getLiniiIntrare()) {
				Produs produs = linie.getProdus();
				if (produs == null || produs.getId() == null) {
					continue; // linie fara produs - sarim
				}

				Double cantitate = linie.getCantitate();
				if (cantitate == null || cantitate == 0.0) {
					continue; // cantitate zero nu modifica stocul
				}

				List<Stoc> stocuri = masterRepo.findStocByProdusGestiune(
						produs.getId(), gestiune.getId());

				if (stocuri.isEmpty()) {
					// Prima intrare pentru acest produs in aceasta gestiune
					Stoc stocNou = new Stoc();
					stocNou.setProdus(produs);
					stocNou.setGestiune(gestiune);
					stocNou.setStocCurent(cantitate);
					stocNou.setStocInitialLuna(0.0);
					stocNou.setStocInitialAn(0.0);
					masterRepo.addStoc(stocNou);
				} else {
					// Stoc existent → adunam cantitatea (negativa pentru Stornare)
					Stoc stoc = stocuri.get(0);
					stoc.setStocCurent(stoc.getStocCurent() + cantitate);
					masterRepo.updateStoc(stoc);
				}
			}
		}
	}

	/**
	 * Initializeaza documentul curent ca stornare a unui document original.
	 * Se apeleaza dupa documentNou() — documentul curent trebuie sa existe.
	 *
	 * Copiaza automat toate liniile din documentul original cu cantitatile negate
	 * (pozitiv → negativ), preia furnizorul si gestiunea, si seteaza referinta
	 * catre documentul original (docInsotitorReferinta).
	 *
	 * Utilizatorul nu trebuie sa introduca manual cantitati negative.
	 *
	 * @param docOriginal documentul (Factura / Aviz) care se storneaza
	 */
	public void initiazaStornareDocument(DocInsotitor docOriginal) {
		DocInsotitor stornat = this.formData.getDocumentCurent();
		stornat.setTipDocument(STORNARE);
		stornat.setFurnizor(docOriginal.getFurnizor());
		stornat.setDocInsotitorReferinta(docOriginal);

		// Creeaza receptia de stornare
		adaugaReceptie();

		// Preia gestiunea si liniile din receptiile documentului original
		for (Receptie receptieOriginala : docOriginal.getReceptii()) {
			if (receptieOriginala.getGestiune() != null) {
				this.formData.getReceptieSelectata().setGestiune(receptieOriginala.getGestiune());
			}
			for (LinieIntrare original : receptieOriginala.getLiniiIntrare()) {
				LinieIntrare linieStor = new LinieIntrare();
				linieStor.setProdus(original.getProdus());
				// Cantitatea devine intotdeauna negativa — stornare cantitativa
				double cantOrig = original.getCantitate() != null ? original.getCantitate() : 0.0;
				linieStor.setCantitate(-Math.abs(cantOrig));
				linieStor.setPretAchizitie(original.getPretAchizitie());
				linieStor.setPretVanzare(original.getPretVanzare());
				linieStor.setAdaos(original.getAdaos());
				this.formData.getReceptieSelectata().addLinieIntrare(linieStor);
			}
		}
	}

	// ---------- Constante interne (legate de operatiile din FormData) ----------//
	private static final String RECEPTIE_CU_FACTURA_OP = ReceptieFormData.RECEPTIE_CU_FACTURA;
	private static final String RECEPTIE_CU_AVIZ_OP = ReceptieFormData.RECEPTIE_CU_AVIZ;
	private static final String STORNARE_OP = ReceptieFormData.STORNARE;
}

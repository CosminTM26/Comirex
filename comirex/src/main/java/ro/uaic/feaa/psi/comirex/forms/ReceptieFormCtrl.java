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

/** Controller MVC pentru formularul "Receptie - Document insotitor". */
public class ReceptieFormCtrl {

	public static final String NECUNOSCUT = "Necunoscut";
	public static final String AVIZ       = "Aviz";
	public static final String FACTURA    = "Factura";
	public static final String STORNARE   = ReceptieFormData.STORNARE;

	private ReceptieFormData formData = new ReceptieFormData();

	public ReceptieFormData getFormData() {
		return this.formData;
	}

	/**
	 * Pregateste un DocInsotitor nou in memorie (fara salvare in BD).
	 * Tipul documentului se deduce din operatia selectata.
	 */
	public void documentNou() {
		DocInsotitor doc = new DocInsotitor();
		this.formData.setDocumentCurent(doc);

		String operatie = this.formData.getOperatieSelectata();
		if (RECEPTIE_CU_FACTURA_OP.equals(operatie)) {
			doc.setTipDocument(FACTURA);
		} else if (RECEPTIE_CU_AVIZ_OP.equals(operatie)) {
			doc.setTipDocument(AVIZ);
		} else if (STORNARE_OP.equals(operatie)) {
			doc.setTipDocument(STORNARE);
		} else {
			doc.setTipDocument(NECUNOSCUT);
		}

		doc.setDataDocument(new Date());
		doc.setDataOperare(new Date());

		if (!this.formData.getListaFurnizori().isEmpty()) {
			doc.setFurnizor(this.formData.getListaFurnizori().get(0));
		}

		this.formData.setReceptieSelectata(null);
	}

	/**
	 * Atribuie furnizorul selectat documentului curent.
	 *
	 * @param furnizor furnizorul selectat de utilizator
	 */
	public void selectieFurnizor(Furnizor furnizor) {
		if (this.formData.getDocumentCurent() == null) {
			throw new RuntimeException("Selectati sau creati mai intai un document!");
		}
		this.formData.setFurnizorSelectat(furnizor);
	}

	/** Adauga o receptie noua documentului curent si o seteaza ca selectata. */
	public void adaugaReceptie() {
		if (this.formData.getDocumentCurent() == null) {
			throw new RuntimeException("Selectati sau creati mai intai un document!");
		}
		Receptie r = new Receptie();
		r.setDataReceptie(new Date());

		if (!this.formData.getListaGestiuni().isEmpty()) {
			r.setGestiune(this.formData.getListaGestiuni().get(0));
		}

		this.formData.getDocumentCurent().addReceptie(r);
		this.formData.setReceptieSelectata(r);
	}

	/** Adauga o linie de intrare goala receptiei curente. */
	public void adaugaLinieIntrare() {
		if (this.formData.getReceptieSelectata() == null) {
			throw new RuntimeException("Selectati mai intai o receptie!");
		}
		LinieIntrare linie = new LinieIntrare();
		linie.setCantitate(0.0);
		linie.setPretAchizitie(0.0);
		linie.setPretVanzare(0.0);
		linie.setAdaos(0.0);

		if (!this.formData.getListaProduse().isEmpty()) {
			linie.setProdus(this.formData.getListaProduse().get(0));
		}

		this.formData.getReceptieSelectata().addLinieIntrare(linie);
	}

	/**
	 * Salveaza documentul curent in BD si actualizeaza stocul.
	 * Stocul se actualizeaza doar la prima salvare (document nou), nu la
	 * re-deschiderea unui document existent.
	 */
	public void salveazaModificariDocument() {
		if (this.formData.getDocumentCurent() == null) {
			throw new RuntimeException("Nu exista nici un document de salvat!");
		}

		boolean esteDocumentNou = (this.formData.getDocumentCurent().getId() == null);

		// Tranzactia 1: salvare document cu cascade (docRepo — EntityManager propriu)
		this.formData.getDocRepo().beginTransaction();
		DocInsotitor doc = this.formData.getDocumentCurent();
		DocInsotitor salvat = this.formData.getDocRepo().saveDocInsotitor(doc);
		this.formData.setDocumentCurent(salvat);
		this.formData.getDocRepo().commitTransaction();

		// Tranzactia 2: actualizare stoc (masterRepo — EntityManager separat)
		if (esteDocumentNou) {
			this.formData.getMasterRepo().beginTransaction();
			actualizeazaStoc(this.formData.getDocumentCurent());
			this.formData.getMasterRepo().commitTransaction();
		}
	}

	/**
	 * Actualizeaza tabela Stoc pentru fiecare linie din document.
	 * Daca stocul nu exista il creeaza; altfel aduna cantitatea (negativa pentru Stornare).
	 * Liniile fara produs, gestiune sau cu cantitate 0 sunt sarite.
	 */
	private void actualizeazaStoc(DocInsotitor doc) {
		MasterRepository masterRepo = this.formData.getMasterRepo();

		for (Receptie receptie : doc.getReceptii()) {
			Gestiune gestiune = receptie.getGestiune();
			if (gestiune == null || gestiune.getId() == null) continue;

			for (LinieIntrare linie : receptie.getLiniiIntrare()) {
				Produs produs = linie.getProdus();
				if (produs == null || produs.getId() == null) continue;

				Double cantitate = linie.getCantitate();
				if (cantitate == null || cantitate == 0.0) continue;

				List<Stoc> stocuri = masterRepo.findStocByProdusGestiune(
						produs.getId(), gestiune.getId());

				if (stocuri.isEmpty()) {
					Stoc stocNou = new Stoc();
					stocNou.setProdus(produs);
					stocNou.setGestiune(gestiune);
					stocNou.setStocCurent(cantitate);
					stocNou.setStocInitialLuna(0.0);
					stocNou.setStocInitialAn(0.0);
					masterRepo.addStoc(stocNou);
				} else {
					Stoc stoc = stocuri.get(0);
					stoc.setStocCurent(stoc.getStocCurent() + cantitate);
					masterRepo.updateStoc(stoc);
				}
			}
		}
	}

	/**
	 * Initializeaza documentul curent ca stornare a unui document original.
	 * Copiaza automat liniile cu cantitati negate; utilizatorul nu scrie valori negative.
	 *
	 * @param docOriginal documentul (Factura / Aviz) care se storneaza
	 */
	public void initiazaStornareDocument(DocInsotitor docOriginal) {
		DocInsotitor stornat = this.formData.getDocumentCurent();
		stornat.setTipDocument(STORNARE);
		stornat.setFurnizor(docOriginal.getFurnizor());
		stornat.setDocInsotitorReferinta(docOriginal);

		adaugaReceptie();

		boolean gestiuneSetata = false;
		for (Receptie receptieOriginala : docOriginal.getReceptii()) {
			if (!gestiuneSetata && receptieOriginala.getGestiune() != null) {
				this.formData.getReceptieSelectata().setGestiune(receptieOriginala.getGestiune());
				gestiuneSetata = true;
			}
			for (LinieIntrare original : receptieOriginala.getLiniiIntrare()) {
				LinieIntrare linieStor = new LinieIntrare();
				linieStor.setProdus(original.getProdus());
				double cantOrig = original.getCantitate() != null ? original.getCantitate() : 0.0;
				linieStor.setCantitate(-Math.abs(cantOrig));
				linieStor.setPretAchizitie(original.getPretAchizitie());
				linieStor.setPretVanzare(original.getPretVanzare());
				linieStor.setAdaos(original.getAdaos());
				this.formData.getReceptieSelectata().addLinieIntrare(linieStor);
			}
		}
	}

	private static final String RECEPTIE_CU_FACTURA_OP = ReceptieFormData.RECEPTIE_CU_FACTURA;
	private static final String RECEPTIE_CU_AVIZ_OP    = ReceptieFormData.RECEPTIE_CU_AVIZ;
	private static final String STORNARE_OP            = ReceptieFormData.STORNARE;
}

package ro.uaic.feaa.psi.comirex.forms;

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
 * Modelul MVC pentru formularul "Receptie - Document insotitor".
 * Adaptor intre domeniu si formular; listele pentru combo-uri se incarca lazy.
 */
public class ReceptieFormData {

	public static final String RECEPTIE_CU_FACTURA = "Receptie cu Factura";
	public static final String RECEPTIE_CU_AVIZ    = "Receptie cu Aviz";
	public static final String FACTURA_INTARZIATA  = "Factura intarziata";
	public static final String STORNARE            = "Stornare";

	private DocInsotitor       documentCurent;
	private List<DocInsotitor> listaDocumente;

	private MasterRepository  masterRepo = new MasterRepository();
	private DocumentRepository docRepo   = new DocumentRepository();

	private List<Furnizor> listaFurnizori;

	public List<Furnizor> getListaFurnizori() {
		if (this.listaFurnizori == null) {
			this.listaFurnizori = this.masterRepo.findFurnizoriAll();
		}
		return this.listaFurnizori;
	}

	/** Reincarca furnizorul complet din BD si il ataseaza documentului curent. */
	public void setFurnizorSelectat(Furnizor furnizorSelectat) {
		if (furnizorSelectat == null || furnizorSelectat.getId() == null) {
			return;
		}
		if (this.documentCurent == null) {
			throw new RuntimeException("Nu exista document curent pentru atribuirea furnizorului!");
		}
		Furnizor furnizorComplet = this.masterRepo.findFurnizorById(furnizorSelectat.getId());
		this.documentCurent.setFurnizor(furnizorComplet);
	}

	private String operatieSelectata;

	public String getOperatieSelectata() { return this.operatieSelectata; }
	public void setOperatieSelectata(String operatieSelectata) { this.operatieSelectata = operatieSelectata; }

	private Receptie receptieSelectata;

	public Receptie getReceptieSelectata() { return this.receptieSelectata; }
	public void setReceptieSelectata(Receptie receptieSelectata) { this.receptieSelectata = receptieSelectata; }

	private List<Gestiune> listaGestiuni;

	public List<Gestiune> getListaGestiuni() {
		if (this.listaGestiuni == null) {
			this.listaGestiuni = this.masterRepo.findGestiuniAll();
		}
		return this.listaGestiuni;
	}

	private List<Produs> listaProduse;

	public List<Produs> getListaProduse() {
		if (this.listaProduse == null) {
			this.listaProduse = this.masterRepo.findProduseAll();
		}
		return this.listaProduse;
	}

	public DocInsotitor getDocumentCurent() { return this.documentCurent; }
	public void setDocumentCurent(DocInsotitor documentCurent) { this.documentCurent = documentCurent; }

	public List<DocInsotitor> getListaDocumente() { return this.listaDocumente; }

	public MasterRepository getMasterRepo() { return this.masterRepo; }
	public DocumentRepository getDocRepo() { return this.docRepo; }
}

package ro.uaic.feaa.psi.comirex.model.repository;

import java.util.Date;
import java.util.List;

import ro.uaic.feaa.psi.metamodel.AbstractRepository;
import ro.uaic.feaa.psi.comirex.model.entities.DocInsotitor;
import ro.uaic.feaa.psi.comirex.model.entities.Document;
import ro.uaic.feaa.psi.comirex.model.entities.Receptie;

public class DocumentRepository extends AbstractRepository {

	// ---------- Metode generice pentru entitatile care extind Document ----------//
	public Document saveDocument(Document document) {
		if (document.getId() == null) // obiect nou
			document = (Document) this.create(document);
		else // obiect existent in BD
			document = (Document) this.update(document);

		return document;
	}

	public void deleteDocument(Document document) {
		this.delete(document);
	}

	// ---------- DocInsotitor ----------//

	public DocInsotitor saveDocInsotitor(DocInsotitor d) {
		// DocInsotitor inca mosteneste Document, deci putem refolosi metoda saveDocument
		return (DocInsotitor) this.saveDocument(d);
	}

	@SuppressWarnings("unchecked")
	public List<DocInsotitor> findDocInsotitoriAll() {
		return this.getEm().createQuery("Select d from DocInsotitor d").getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DocInsotitor> getAvizeFaraFacturaByFurnizorId(Long id) {
		return this.getEm()
				.createQuery("Select d from DocInsotitor d where d.furnizor.id=:id and d.tipDocument='Aviz' and size(d.receptii)=0")
				.setParameter("id", id)
				.getResultList();
	}

	// ---------- Receptie ----------//

	public Receptie saveReceptie(Receptie receptie) {
		// Receptie nu mai este un Document, asa ca ii scriem logica de salvare direct
		if (receptie.getId() == null)
			receptie = (Receptie) this.create(receptie);
		else
			receptie = (Receptie) this.update(receptie);

		return receptie;
	}

	@SuppressWarnings("unchecked")
	public List<Receptie> findReceptiiAll() {
		return this.getEm().createQuery("Select r from Receptie r").getResultList();
	}

	public Receptie findReceptieById(Long id) {
		return (Receptie) this.getEm()
				.createQuery("Select r from Receptie r where id=:id")
				.setParameter("id", id)
				.getSingleResult();
	}

	public Receptie getReceptieByNrData(String nr, Date data) {
		// Modificat aici: r.numarDocument -> r.nrNIR si r.dataDocument -> r.dataReceptie
		return (Receptie) this.getEm()
				.createQuery("Select r from Receptie r where r.nrNIR=:nr and r.dataReceptie=:data")
				.setParameter("nr", nr)
				.setParameter("data", data)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Receptie> getReceptiiByGestiuneId(Long id) {
		return this.getEm()
				.createQuery("Select r from Receptie r where r.gestiune.id=:id")
				.setParameter("id", id)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Receptie> getReceptiiByFurnizorId(Long id) {
		return this.getEm()
				.createQuery("Select r from Receptie r where r.docInsotitor.furnizor.id=:id")
				.setParameter("id", id)
				.getResultList();
	}
}
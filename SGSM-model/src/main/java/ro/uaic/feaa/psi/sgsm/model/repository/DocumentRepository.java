package ro.uaic.feaa.psi.sgsm.model.repository;

import java.util.Date;
import java.util.List;

import ro.uaic.feaa.psi.metamodel.AbstractRepository;
import ro.uaic.feaa.psi.sgsm.model.entities.DocInsotitor;
import ro.uaic.feaa.psi.sgsm.model.entities.Document;
import ro.uaic.feaa.psi.sgsm.model.entities.Receptie;

public class DocumentRepository extends AbstractRepository {


	public Document saveDocument(Document document) {
		if (document.getId()==null)//obiect nou
			document=(Document)this.create(document);
		else //obiect existent in BD
			document=(Document)this.update(document);

		return document;
	}

	public List<DocInsotitor> getAvizeFaraFacturaByFurnId(Long id){
		return null;
	}

	public DocInsotitor getDocInsotitorByFurnId(){
		return null;
	}

	public Document getDocInsotitorByTipNrData(String tip, String nr, Date data) {
		return null;
	}

	public Receptie getReceptieByNrData(String nr, Date data) {
		return null;
	}

	public List<Receptie> getReceptiiByGestiuneId(Long id){
		return null;
	}



}

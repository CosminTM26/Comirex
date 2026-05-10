package ro.uaic.feaa.psi.sgsm.model.entities;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ro.uaic.feaa.psi.metamodel.AbstractEntity;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Document extends AbstractEntity {

	private String tipDocument;
	private String numarDocument;
	@Temporal(value = TemporalType.DATE)
	private Date dataDocument;
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date dataOperare;
	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<LinieDocument> liniiDocument = new HashSet<LinieDocument>();

	// ---------- metode pentru managementul colectiei liniiDocument
	// -----------//

	public void addLinieDocument(LinieDocument linie) {
		this.liniiDocument.add(linie);
		linie.setDocument(this);// set the inverse relation. MANDATORY!
	}

	public void addLinieDocument(BunMaterial material, Double cantitate,
			Double pret) {
		LinieDocument linie = new LinieDocument(cantitate, pret, material);
		this.liniiDocument.add(linie);
		linie.setDocument(this);// set the inverse relation. MANDATORY!
	}

	public void removeLinieDocument(LinieDocument linie) {
		this.liniiDocument.remove(linie);
		linie.setDocument(null);// detach the inverse relation. MANDATORY!
	}



	/**
	 * Lista returnata este read-only. Utilizati metodele publicate pentru
	 * managementul colectiei.
	 *
	 * @return - lista read-only
	 */
	public List<LinieDocument> getLiniiDocument() {
		return Collections.unmodifiableList(new LinkedList(liniiDocument));
	}

	// ----------the usual getters and setters for Java Beans------//
	public String getTipDocument() {
		return tipDocument;
	}

	public void setTipDocument(String tipDocument) {
		this.tipDocument = tipDocument;
	}

	public String getNumarDocument() {
		return numarDocument;
	}

	public void setNumarDocument(String numarDocument) {
		this.numarDocument = numarDocument;
	}

	public Date getDataDocument() {
		return dataDocument;
	}

	public void setDataDocument(Date dataDocument) {
		this.dataDocument = dataDocument;
	}

	public Date getDataOperare() {
		return dataOperare;
	}

	public void setDataOperare(Date dataOperare) {
		this.dataOperare = dataOperare;
	}

}

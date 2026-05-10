package ro.uaic.feaa.psi.comirex.model.entities;

import ro.uaic.feaa.psi.metamodel.AbstractEntity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Document extends AbstractEntity {

	private String tipDocument;
	private String numarDocument;

	@Temporal(value = TemporalType.DATE)
	private Date dataDocument;

	@Temporal(value = TemporalType.DATE)
	private Date dataOperare; // Corectat din dataInregistrare

	// S-a sters campul 'stare', care nu aparea in diagrama

	public Document() {
		super();
	}

	// --- Getteri si Setteri ---
	public String getTipDocument() { return tipDocument; }
	public void setTipDocument(String tipDocument) { this.tipDocument = tipDocument; }

	public String getNumarDocument() { return numarDocument; }
	public void setNumarDocument(String numarDocument) { this.numarDocument = numarDocument; }

	public Date getDataDocument() { return dataDocument; }
	public void setDataDocument(Date dataDocument) { this.dataDocument = dataDocument; }

	public Date getDataOperare() { return dataOperare; }
	public void setDataOperare(Date dataOperare) { this.dataOperare = dataOperare; }
}
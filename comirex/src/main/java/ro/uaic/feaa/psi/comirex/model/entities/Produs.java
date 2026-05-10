package ro.uaic.feaa.psi.comirex.model.entities;

import ro.uaic.feaa.psi.metamodel.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Produs extends AbstractEntity {

	@Column(unique = true)
	private String codProdus;
	private String denumire;
	private String um;
	private String categorie;
	private Double costTVA;
	private Double pretVanzare;
	private Double stocMinim;
	private Double costMediuPonderat;

	public Produs() { super(); }

	public String getCodProdus() { return codProdus; }
	public void setCodProdus(String codProdus) { this.codProdus = codProdus; }

	public String getDenumire() { return denumire; }
	public void setDenumire(String denumire) { this.denumire = denumire; }

	public String getUm() { return um; }
	public void setUm(String um) { this.um = um; }

	public String getCategorie() { return categorie; }
	public void setCategorie(String categorie) { this.categorie = categorie; }

	public Double getCostTVA() { return costTVA; }
	public void setCostTVA(Double costTVA) { this.costTVA = costTVA; }

	public Double getPretVanzare() { return pretVanzare; }
	public void setPretVanzare(Double pretVanzare) { this.pretVanzare = pretVanzare; }

	public Double getStocMinim() { return stocMinim; }
	public void setStocMinim(Double stocMinim) { this.stocMinim = stocMinim; }

	public Double getCostMediuPonderat() { return costMediuPonderat; }
	public void setCostMediuPonderat(Double costMediuPonderat) { this.costMediuPonderat = costMediuPonderat; }
}
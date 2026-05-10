package ro.uaic.feaa.psi.comirex.model.entities;

import ro.uaic.feaa.psi.metamodel.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class LinieIntrare extends AbstractEntity {

	private Double cantitate = 0.0;
	private Double pretAchizitie = 0.0;
	private Double pretVanzare = 0.0;
	private Double adaos = 0.0;

	@ManyToOne
	private Receptie receptie;

	@ManyToOne
	private Produs produs;

	public LinieIntrare() { super(); }

	public LinieIntrare(Double cantitate, Double pretAchizitie, Double pretVanzare, Produs produs) {
		this();
		this.cantitate = cantitate;
		this.pretAchizitie = pretAchizitie;
		this.pretVanzare = pretVanzare;
		this.adaos = pretVanzare - pretAchizitie;
		this.produs = produs;
	}

	public Double getCantitate() { return cantitate; }
	public void setCantitate(Double cantitate) { this.cantitate = cantitate; }

	public Double getPretAchizitie() { return pretAchizitie; }
	public void setPretAchizitie(Double pretAchizitie) { this.pretAchizitie = pretAchizitie; }

	public Double getPretVanzare() { return pretVanzare; }
	public void setPretVanzare(Double pretVanzare) { this.pretVanzare = pretVanzare; }

	public Double getAdaos() { return adaos; }
	public void setAdaos(Double adaos) { this.adaos = adaos; }

	public Receptie getReceptie() { return receptie; }
	public void setReceptie(Receptie receptie) { this.receptie = receptie; }

	public Produs getProdus() { return produs; }
	public void setProdus(Produs produs) { this.produs = produs; }
}
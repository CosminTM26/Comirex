package ro.uaic.feaa.psi.comirex.model.entities;

import ro.uaic.feaa.psi.metamodel.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Stoc extends AbstractEntity {

	private Double stocInitialLuna = 0.0;
	private Double stocCurent = 0.0;
	private Double stocInitialAn = 0.0;

	@ManyToOne
	private Produs produs;

	@ManyToOne
	private Gestiune gestiune;

	public Stoc() { super(); }

	public Double getStocInitialLuna() { return stocInitialLuna; }
	public void setStocInitialLuna(Double stocInitialLuna) { this.stocInitialLuna = stocInitialLuna; }

	public Double getStocCurent() { return stocCurent; }
	public void setStocCurent(Double stocCurent) { this.stocCurent = stocCurent; }

	public Double getStocInitialAn() { return stocInitialAn; }
	public void setStocInitialAn(Double stocInitialAn) { this.stocInitialAn = stocInitialAn; }

	public Produs getProdus() { return produs; }
	public void setProdus(Produs produs) { this.produs = produs; }

	public Gestiune getGestiune() { return gestiune; }
	public void setGestiune(Gestiune gestiune) { this.gestiune = gestiune; }
}
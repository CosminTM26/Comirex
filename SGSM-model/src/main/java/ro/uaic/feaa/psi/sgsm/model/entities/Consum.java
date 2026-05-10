package ro.uaic.feaa.psi.sgsm.model.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Consum extends Document {

	String numarComanda;
	String locConsum;

	@ManyToOne
	Gestiune gestiune;

	public String getNumarComanda() {
		return numarComanda;
	}

	public void setNumarComanda(String numarComanda) {
		this.numarComanda = numarComanda;
	}

	public String getLocConsum() {
		return locConsum;
	}

	public void setLocConsum(String locConsum) {
		this.locConsum = locConsum;
	}

	public Gestiune getGestiune() {
		return gestiune;
	}

	public void setGestiune(Gestiune gestiune) {
		this.gestiune = gestiune;
	}
}

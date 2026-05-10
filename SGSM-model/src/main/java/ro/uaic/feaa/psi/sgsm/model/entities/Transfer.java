package ro.uaic.feaa.psi.sgsm.model.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import ro.uaic.feaa.psi.metamodel.AbstractEntity;

@Entity
public class Transfer extends AbstractEntity {

	@ManyToOne
	Gestiune gestiuneSursa;

	@ManyToOne
	Gestiune gestiuneDestinatie;

	public Transfer() {
		super();
	}

	public Transfer(Gestiune gestiuneSursa, Gestiune gestiuneDestinatie) {
		this();
		this.gestiuneSursa = gestiuneSursa;
		this.gestiuneDestinatie = gestiuneDestinatie;
	}

	public Gestiune getGestiuneSursa() {
		return gestiuneSursa;
	}

	public void setGestiuneSursa(Gestiune gestiuneSursa) {
		this.gestiuneSursa = gestiuneSursa;
	}

	public Gestiune getGestiuneDestinatie() {
		return gestiuneDestinatie;
	}

	public void setGestiuneDestinatie(Gestiune gestiuneDestinatie) {
		this.gestiuneDestinatie = gestiuneDestinatie;
	}
}

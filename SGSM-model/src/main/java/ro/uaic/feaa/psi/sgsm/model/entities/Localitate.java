package ro.uaic.feaa.psi.sgsm.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

import ro.uaic.feaa.psi.metamodel.AbstractEntity;

@Entity
public class Localitate extends AbstractEntity {

	@Column(unique = true)
	Integer cod;
	String denumire;

	public Localitate() {
		super();
	}

	public String getDenumire() {
		return denumire;
	}

	public void setDenumire(String denumire) {
		this.denumire = denumire;
	}

	public Integer getCod() {
		return cod;
	}

	public void setCod(Integer cod) {
		this.cod = cod;
	}
}

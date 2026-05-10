package ro.uaic.feaa.psi.sgsm.model.entities;

import javax.persistence.Entity;

import ro.uaic.feaa.psi.metamodel.AbstractEntity;

@Entity
public class BunMaterial extends AbstractEntity{

	String codMaterial;
	String denumireMaterial;
	String um;
	String cont;
	Double costMediuPonderat;

	public String getCodMaterial() {
		return codMaterial;
	}
	public void setCodMaterial(String codMaterial) {
		this.codMaterial = codMaterial;
	}
	public String getDenumireMaterial() {
		return denumireMaterial;
	}
	public void setDenumireMaterial(String denumireMaterial) {
		this.denumireMaterial = denumireMaterial;
	}
	public String getUm() {
		return um;
	}
	public void setUm(String um) {
		this.um = um;
	}
	public String getCont() {
		return cont;
	}
	public void setCont(String cont) {
		this.cont = cont;
	}
	public Double getCostMediuPonderat() {
		return costMediuPonderat;
	}
	public void setCostMediuPonderat(Double costMediuPonderat) {
		this.costMediuPonderat = costMediuPonderat;
	}


}

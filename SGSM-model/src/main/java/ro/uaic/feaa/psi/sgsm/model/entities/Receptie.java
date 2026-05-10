package ro.uaic.feaa.psi.sgsm.model.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Receptie extends Document{
	@ManyToOne
	DocInsotitor docInsotitor;
	@ManyToOne
	Gestiune gestiune;

	Boolean facturaPrimita;

	public DocInsotitor getDocInsotitor() {
		return docInsotitor;
	}
	public void setDocInsotitor(DocInsotitor docInsotitor) {
		this.docInsotitor = docInsotitor;
	}
	public Gestiune getGestiune() {
		return gestiune;
	}
	public void setGestiune(Gestiune gestiune) {
		this.gestiune = gestiune;
	}
	public Boolean isFacturaPrimita() {
		return facturaPrimita;
	}
	public void setFacturaPrimita(Boolean facturaPrimita) {
		this.facturaPrimita = facturaPrimita;
	}


}

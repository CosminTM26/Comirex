package ro.uaic.feaa.psi.comirex.model.entities;

import ro.uaic.feaa.psi.metamodel.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Gestiune extends AbstractEntity {

	@Column(unique = true)
	private String codGestiune;
	private String denumireGestiune;
	private String numeGestionar;
	private String adresa;

	public Gestiune() { super(); }

	public String getCodGestiune() { return codGestiune; }
	public void setCodGestiune(String codGestiune) { this.codGestiune = codGestiune; }

	public String getDenumireGestiune() { return denumireGestiune; }
	public void setDenumireGestiune(String denumireGestiune) { this.denumireGestiune = denumireGestiune; }

	public String getNumeGestionar() { return numeGestionar; }
	public void setNumeGestionar(String numeGestionar) { this.numeGestionar = numeGestionar; }

	public String getAdresa() { return adresa; }
	public void setAdresa(String adresa) { this.adresa = adresa; }
}
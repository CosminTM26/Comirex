package ro.uaic.feaa.psi.comirex.model.entities;

import ro.uaic.feaa.psi.metamodel.AbstractEntity;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Receptie extends AbstractEntity {

	@Column(unique = true)
	private String codNIR;
	private String nrNIR;

	@Temporal(value = TemporalType.DATE)
	private Date dataReceptie;

	private Integer facturaPrimita;

	@ManyToOne
	private DocInsotitor docInsotitor;

	@ManyToOne
	private Gestiune gestiune;

	@OneToMany(mappedBy = "receptie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<LinieIntrare> liniiIntrare = new HashSet<LinieIntrare>();

	public Receptie() { super(); }

	public void addLinieIntrare(LinieIntrare linie) {
		this.liniiIntrare.add(linie);
		linie.setReceptie(this);
	}
	public void removeLinieIntrare(LinieIntrare linie) {
		this.liniiIntrare.remove(linie);
		linie.setReceptie(null);
	}
	public List<LinieIntrare> getLiniiIntrare() {
		return Collections.unmodifiableList(new LinkedList<LinieIntrare>(this.liniiIntrare));
	}

	public String getCodNIR() { return codNIR; }
	public void setCodNIR(String codNIR) { this.codNIR = codNIR; }

	public String getNrNIR() { return nrNIR; }
	public void setNrNIR(String nrNIR) { this.nrNIR = nrNIR; }

	public Date getDataReceptie() { return dataReceptie; }
	public void setDataReceptie(Date dataReceptie) { this.dataReceptie = dataReceptie; }

	public Integer getFacturaPrimita() { return facturaPrimita; }
	public void setFacturaPrimita(Integer facturaPrimita) { this.facturaPrimita = facturaPrimita; }

	public DocInsotitor getDocInsotitor() { return docInsotitor; }
	public void setDocInsotitor(DocInsotitor docInsotitor) { this.docInsotitor = docInsotitor; }

	public Gestiune getGestiune() { return gestiune; }
	public void setGestiune(Gestiune gestiune) { this.gestiune = gestiune; }
}
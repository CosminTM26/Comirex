package ro.uaic.feaa.psi.comirex.model.entities;

import java.util.Collections;
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

@Entity
public class DocInsotitor extends Document {

	@Column(unique = true)
	private String codDocInsot;
	private String mijlocTransport;

	@ManyToOne
	private Furnizor furnizor;

	@ManyToOne
	private DocInsotitor docInsotitorReferinta;

	@OneToMany(mappedBy = "docInsotitor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Receptie> receptii = new HashSet<Receptie>();

	public DocInsotitor() { super(); }

	public void addReceptie(Receptie receptie) {
		this.receptii.add(receptie);
		receptie.setDocInsotitor(this);
	}
	public void removeReceptie(Receptie receptie) {
		this.receptii.remove(receptie);
		receptie.setDocInsotitor(null);
	}
	public List<Receptie> getReceptii() {
		return Collections.unmodifiableList(new LinkedList<Receptie>(this.receptii));
	}

	public String getCodDocInsot() { return codDocInsot; }
	public void setCodDocInsot(String codDocInsot) { this.codDocInsot = codDocInsot; }

	public String getMijlocTransport() { return mijlocTransport; }
	public void setMijlocTransport(String mijlocTransport) { this.mijlocTransport = mijlocTransport; }

	public Furnizor getFurnizor() { return furnizor; }
	public void setFurnizor(Furnizor furnizor) { this.furnizor = furnizor; }

	public DocInsotitor getDocInsotitorReferinta() { return docInsotitorReferinta; }
	public void setDocInsotitorReferinta(DocInsotitor docInsotitorReferinta) { this.docInsotitorReferinta = docInsotitorReferinta; }
}
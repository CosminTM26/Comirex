package ro.uaic.feaa.psi.sgsm.model.entities;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class DocInsotitor extends Document {
	private String mijlTransport;

	@ManyToOne
	private DocInsotitor docInsotitorStornat;

	@ManyToOne
	private DocInsotitor docInsotitorDeStornare;

	@ManyToOne
	private Furnizor furnizor;

	@OneToMany(mappedBy = "docInsotitor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Receptie> receptii = new HashSet<Receptie>();

	// ---------------metode pentru managementul colectiei receptii-----//

	public void addReceptie(Receptie receptie) {
		this.receptii.add(receptie);
		receptie.setDocInsotitor(this);
	}

	public void addReceptie(Gestiune gestiune) {
		Receptie r = new Receptie();
		r.setGestiune(gestiune);
		this.addReceptie(r);
	}

	public void removeReceptie(Receptie receptie) {

		this.receptii.remove(receptie);
		receptie.setDocInsotitor(null);
	}

	public List<Receptie> getReceptii() {
		return Collections.unmodifiableList(new LinkedList(this.receptii));
	}

	/**
	 * Furnizeaza o lista cumulativa cu toate materialele receptionate
	 *
	 * @return - lista materialelor receptionate - READ ONLY
	 */
	public List<LinieDocument> getArticoleReceptionate() {
		List<LinieDocument> artReceptionate = new LinkedList<LinieDocument>();
		for (Receptie receptie : receptii)
			for (LinieDocument row : receptie.getLiniiDocument())
				artReceptionate.add(row);
		// lista trebuie sa fie read-only --> nu poate fi utilizata pentru a
		// adauga linii noi
		return Collections.unmodifiableList(artReceptionate);
	}

	public String getMijlTransport() {
		return mijlTransport;
	}

	public void setMijlTransport(String mijlTransport) {
		this.mijlTransport = mijlTransport;
	}

	public DocInsotitor getDocInsotitorStornat() {
		return docInsotitorStornat;
	}

	public void setDocInsotitorStornat(DocInsotitor docInsotitorStornat) {
		this.docInsotitorStornat = docInsotitorStornat;
	}

	public DocInsotitor getDocInsotitorDeStornare() {
		return docInsotitorDeStornare;
	}

	public void setDocInsotitorDeStornare(DocInsotitor docInsotitorDeStornare) {
		this.docInsotitorDeStornare = docInsotitorDeStornare;
	}

	public Furnizor getFurnizor() {
		return furnizor;
	}

	public void setFurnizor(Furnizor furnizor) {
		this.furnizor = furnizor;
	}

}

package ro.uaic.feaa.psi.sgsm.model.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import ro.uaic.feaa.psi.metamodel.AbstractEntity;

@Entity
public class LinieDocument extends AbstractEntity {


	Double cantitate=0.0; //valoarea null trebuie evitata cu orice pret aici
	Double pret=0.0; //valoarea null trebuie evitata cu orice pret aici
	@ManyToOne
	BunMaterial material;
	@ManyToOne
	Document document;

	/**
	 * Default constructor mandatory for JPA
	 */
	public LinieDocument(){super();}

	/**
	 * convenient constructor
	 * @param cantitate
	 * @param pret
	 * @param material
	 */
	public LinieDocument(Double cantitate, Double pret,
			BunMaterial material) {
		this();
		this.cantitate = cantitate;
		this.pret = pret;
		this.material = material;
	}

	public Double getCantitate() {
		return cantitate;
	}
	public void setCantitate(Double cantitate) {
		this.cantitate = cantitate;
	}
	public Double getPret() {
		return pret;
	}
	public void setPret(Double pret) {
		this.pret = pret;
	}

	public BunMaterial getMaterial() {
		return material;
	}

	public void setMaterial(BunMaterial material) {
		this.material = material;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}


}

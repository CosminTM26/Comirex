package ro.uaic.feaa.psi.comirex.model.entities;

import ro.uaic.feaa.psi.metamodel.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Furnizor extends AbstractEntity {

	@Column(unique = true)
	private String codFurnizor;
	private String numeFurnizor;
	private String adresa;
	private String telefon;
	private String email;
	private String cui;
	private String iban;
	private Double sold;

	// Transformat din tabel/entitate intr-un simplu String!
	private String localitate;

	public Furnizor() { super(); }

	// --- Getteri si Setteri ---
	public String getCodFurnizor() { return codFurnizor; }
	public void setCodFurnizor(String codFurnizor) { this.codFurnizor = codFurnizor; }

	public String getNumeFurnizor() { return numeFurnizor; }
	public void setNumeFurnizor(String numeFurnizor) { this.numeFurnizor = numeFurnizor; }

	public String getAdresa() { return adresa; }
	public void setAdresa(String adresa) { this.adresa = adresa; }

	public String getTelefon() { return telefon; }
	public void setTelefon(String telefon) { this.telefon = telefon; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getCui() { return cui; }
	public void setCui(String cui) { this.cui = cui; }

	public String getIban() { return iban; }
	public void setIban(String iban) { this.iban = iban; }

	public Double getSold() { return sold; }
	public void setSold(Double sold) { this.sold = sold; }

	public String getLocalitate() { return localitate; }
	public void setLocalitate(String localitate) { this.localitate = localitate; }
}
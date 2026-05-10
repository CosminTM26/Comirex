package ro.uaic.feaa.psi.sgsm.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import ro.uaic.feaa.psi.metamodel.AbstractEntity;

@Entity
public class Furnizor extends AbstractEntity {

	@Column(unique = true)
	private String cod;
	private String nume;
	private String adresa;

	private String CUI;
	private String banca;
	private String contBancar;
	private Double sold;
	@ManyToOne
	private Localitate localitate;

	public Furnizor() {
	}

	/**
	 * Constructor necesar pentru a incarca doar numite campuri din baza de
	 * date. Scop: optimizare acces la date si timp de executie interogari. A se
	 * OBSERVA: Lipseste atributul localitate, care este o relatie ManyToOne si
	 * va genera un join. In lipsa acestuia, hibernate va executa interogarea
	 * numai pe tabela Furnizor. Verificati prin rularea trestului
	 * {@link TestFurnizori} si urmarind SQL generat la consola.
	 *
	 * @param id
	 *            - id-ul trebuie OBLIGATORIU incarcat din BD - in caz contrar, JPA nu va
	 *            sti cum sa gaseasca cheile straine necesare relatiilor
	 *            ManyToOne in care apare entitatea Furnizor
	 * @param cod
	 * @param nume
	 * @param adresa
	 * @param cui
	 * @param banca
	 * @param contBancar
	 */
	public Furnizor(Long id, String cod, String nume, String adresa,
			String cui, String banca, String contBancar) {
		super();
		this.id=id; //din superclasa
		this.cod = cod;
		this.nume = nume;
		this.adresa = adresa;
		CUI = cui;
		this.banca = banca;
		this.contBancar = contBancar;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getBanca() {
		return banca;
	}

	public void setBanca(String banca) {
		this.banca = banca;
	}

	public Double getSold() {
		return sold;
	}

	public void setSold(Double sold) {
		this.sold = sold;
	}

	public Localitate getLocalitate() {
		return localitate;
	}

	public void setLocalitate(Localitate localitate) {
		this.localitate = localitate;
	}

	public String getContBancar() {
		return contBancar;
	}

	public void setContBancar(String contBancar) {
		this.contBancar = contBancar;
	}

	public String getCUI() {
		return CUI;
	}

	public void setCUI(String cui) {
		CUI = cui;
	}

}

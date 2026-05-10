package test;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;

import ro.uaic.feaa.psi.sgsm.model.entities.Furnizor;
import ro.uaic.feaa.psi.sgsm.model.entities.Localitate;

import ro.uaic.feaa.psi.sgsm.model.repository.MasterRepository;

/**
 * Un test prin care verificam cele doua metode de incarecare a datelor despre
 * furnizori:
 *
 * # {@link MasterRepository#findFurnizoriAll()} - incarca complet graful de
 * obiecte, inclusiv obiecte Localitate corespunzatoare relatiilor ManyToMany
 *
 * # {@link MasterRepository#findFurnizoriAllLight()} - incarca doar datele
 * specificate prin constructorul Furnizor care ignora atributul
 * <code> localitate </code>
 *
 *
 * @author cretuli
 *
 */
public class TestFurnizori {

	static MasterRepository repo = new MasterRepository();

	public static void main(String[] args) {
		List<Furnizor> x = repo.findFurnizoriAll();
		if (x.size() == 0)// daca nu sunt date in tabela, adaugam date de test
		{
			adaugaFurnizori();
			x = repo.findFurnizoriAll();
		}
		System.out.println("##################### Vezi SQL generat deasupra acestei linii si compara cu cel generat dedesubt. " +
										"Atentie la numarul de interogari pe tabela Localitate############");
		// acum ar trebui sa avem date de test
		assert x.size() > 0;

		List<Furnizor> y = repo.findFurnizoriAllLight();
		// verificam daca cele doua metode au returnat acelasi numar de obiecte
		Assert.assertTrue( y.size() == x.size());

		// verificam incarcarea relatiei manyToOne Localitate
		// obiectele din lista x trebuie sa contina aceasta relatie
		Assert.assertTrue( x.get(0).getLocalitate() != null);

		// obiectele din lista y NU trebuie sa contina aceasta relatie
		Assert.assertTrue("metoda findFurnizoriAllLight NU trebuie sa incarce obiecte Localitate pentru fiecare furnizor", y.get(0).getLocalitate() == null);

		// urmariti pe display-ul Console frazele SQL generate pentru cele doua
		// liste

	}

	public static void adaugaFurnizori() {
		// need Localitati pentru noii furnizori
		List<Localitate> localitati = repo.findLocalitatiAll();
		//nu putem continua fara localitati
		Assert.assertTrue("Rulati mai intai testul TestLocalitati", localitati.size()>0);

		Furnizor f = null;
		// pentru test, cate localitati, atatia furnizori
		repo.beginTransaction();

		int i = 0;
		for (Localitate loc : localitati) {
			i++;
			//primul argument, ID-ul entitatii, va fi generat la insertul in BD
			f = new Furnizor(null, "100" + i, "furnizor 100" + i, "Adresa 100" + i,
					"CUI 100" + i, "Banca f 100" + i, "cont f 100" + i);
			f.setLocalitate(loc);
			repo.addFurnizor(f);
		}
		repo.commitTransaction();
	}
}

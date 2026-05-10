package test.ro.uaic.feaa.psi.sgsm.test.forms;

import java.util.List;

import org.junit.Assert;

import ro.uaic.feaa.psi.sgsm.model.entities.LinieDocument;
import src.ro.uaic.feaa.psi.sgsm.forms.AchizitiiFormCtrl;

public class TestAchizitiiForm {

	public static void main(String[] args) {

		AchizitiiFormCtrl form = new AchizitiiFormCtrl();

		// cateva verificari initiale: listele trebuie sa aiba o sursa de date
		// valida

		verificariInitiale(form);

		// 1 Adauga document
		// 1.1 simuleaza selectie tip operatiune
		form.getFormData().setOperatieSelectata(
				form.getFormData().ACHIZITIE_CU_FACTURA);

		// 1.2- simuleaza apasare buton Adaugare din tab Facturi/Avize
		form.documentNou();

		// 1.3 verifica tipul de document creat - trebuie sa fie Factura
		Assert.assertTrue("Tipul de document nou creat nu este corect.", form
				.getFormData().getDocumentCurent().getTipDocument().equals(
						form.FACTURA));

		// 1.4 simuleaza furnizor selectat
		form.getFormData().setFurnizorSelectat(
				form.getFormData().getListaFurnizorilor().get(0));
		// verifica furnizor
		Assert.assertTrue("Furnizor atasat documentului incorect", form
				.getFormData().getDocumentCurent().getFurnizor().equals(
						form.getFormData().getFurnizorSelectat()));

		// 2 Adauga Receptie pe document

		form.adaugaReceptie();
		// 2.1 verifca lista de receptii

		Assert
				.assertTrue(
						"La acest moment documentul trebuie sa aiba exact o receptie.",
						form.getFormData().getDocumentCurent().getReceptii()
								.size() == 1);

		// 2 adauga o linie
		// 2.1 verifica eroare daca nu este nimic selectat
		boolean eroare = false;
		try {
			form.adaugaLinieReceptie();
		} catch (Exception e) {
			System.out.println("Eroare asteptata :" + e);
			eroare = true;
		}
	/*	Assert
				.assertTrue(
						"pentru ca nu am selectat receptia, ar fi trebuit sa avem o eroare aici",
						eroare);
*/
		// 2.2 simuleaza selectie prima receptie (si singura) si adauga o linie
		form.getFormData().setReceptieSelectata(
				form.getFormData().getDocumentCurent().getReceptii().get(0));

		// 2.3 simuleaza selecttie gestiune
		form.getFormData().getReceptieSelectata().setGestiune(
				form.getFormData().getListaGestiuni().get(0));

		// 2.3 adauga o linie
		//form.adaugaLinieReceptie();

		// 2.4 simuleaza selectie linie si introducere date
		LinieDocument linieReceptie = form.getFormData().getReceptieSelectata()
				.getLiniiDocument().get(0);// prima si singura linie
		linieReceptie
				.setMaterial(form.getFormData().getListaMateriale().get(0));// primul
																			// din
																			// lista
		linieReceptie.setCantitate(20.00);
		linieReceptie.setPret(30.00);

		// 3 verifica lista de articole receptionate (zona 4) de pe tabul
		// pricipal - trebuie sa fie exect 1
		Assert.assertTrue(
				"Lista de articole receptionate trebuie sa contina 1 linie",
				form.getFormData().getArticoleReceptionate().size() == 1);

		// 4 salveaza datele
		form.salveazaModificariDocument();
		
		
		// verifica datele salvate in baza de date... 
		//TODO - tema + comentarii asupra acestei verificari: 
		//1) cum identificam in mod unic documentul pe care tocmai l-am introdus (ce verificari/validari ar mai trebui efectuare inainte de a-l salva; unde putem introduce aceste validari)?
		//2) ce modificari trebuie efectuate  in aplicatie pentru a putea extrage acest document din baza de date daca nu stim id-ul?

	}

	/**
	 * Verificari privind initializarea corecta a formului
	 * 
	 * @param form
	 */
	private static void verificariInitiale(AchizitiiFormCtrl form) {
		Assert
				.assertTrue(
						"BUG sau Nu exista Furnizori pentru test. Rulati mai intai testul TestFurnizori din proiectul SGSM-Model",
						form.getFormData().getListaFurnizorilor().size() > 0);

		Assert
				.assertTrue(
						"BUG sau Nu exista Gestiuni pentru test. Rulati mai intai testul TestGestiuni din proiectul SGSM-Model",
						form.getFormData().getListaGestiuni().size() > 0);

		Assert
				.assertTrue(
						"BUG sau Nu exista Materiale pentru test. Rulati mai intai testul TestBunuriMateriale din proiectul SGSM-Model",
						form.getFormData().getListaMateriale().size() > 0);
	}

}

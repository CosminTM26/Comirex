package test;

import java.util.List;

import ro.uaic.feaa.psi.sgsm.model.entities.Localitate;
import ro.uaic.feaa.psi.sgsm.model.repository.MasterRepository;

/**
 * Test pentru Localitati. Teste efectuate:
 * # extragere lista de localitati
 * # adaugare Localitate
 *
 * @author cretuli
 *
 */
public class TestLocalitati {

	static MasterRepository repo = new MasterRepository();

	public static void main(String[] args) {
		List<Localitate> x = repo.findLocalitatiAll();
		if (x.size() == 0) {
			addLocalitati();
			x = repo.findLocalitatiAll();
		}

		assert x.size() > 0;
	}

	private static void addLocalitati() {
		Localitate l = null;
		repo.beginTransaction();

		for (int i = 0; i < 3; i++) {
			l = new Localitate();
			l.setCod(1000 + i);
			l.setDenumire("Localitate " + l.getCod());
			repo.addLocalitate(l);
		}
		repo.commitTransaction();
	}
}

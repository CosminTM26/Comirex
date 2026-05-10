package test;

import java.util.List;

import ro.uaic.feaa.psi.sgsm.model.entities.Gestiune;

import ro.uaic.feaa.psi.sgsm.model.repository.MasterRepository;

/**
 * Test pentru Gestiuni. Teste efectuate:
 * # extragere lista de gestiuni
 * # adaugare Gestiune
 *
 * @author cretuli
 *
 */
public class TestGestiuni {

	static MasterRepository repo = new MasterRepository();

	public static void main(String[] args) {
		List<Gestiune> x = repo.findGestiuniAll();
		if (x.size() == 0)// daca nu sunt date in tabela, adaugam date de test
		{
			adaugaGestiuni();
			x = repo.findGestiuniAll();
		}

		// acum ar trebui sa avem date de test
		assert x.size() > 0;


	}

	public static void adaugaGestiuni() {
		Gestiune g = null;
		repo.beginTransaction();

		for (int i=0; i<3; i++) {

			g=new Gestiune();
			g.setCodGestiune(1000+i);
			g.setDenumireGestiune("Gestiune "+g.getCodGestiune());
			repo.addGestiune(g);
		}
		repo.commitTransaction();
	}
}

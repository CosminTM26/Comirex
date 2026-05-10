package ro.uaic.feaa.psi.comirex.model.repository;

import java.util.List;

import ro.uaic.feaa.psi.metamodel.AbstractRepository;
import ro.uaic.feaa.psi.comirex.model.entities.Furnizor;
import ro.uaic.feaa.psi.comirex.model.entities.Gestiune;
import ro.uaic.feaa.psi.comirex.model.entities.Produs;
import ro.uaic.feaa.psi.comirex.model.entities.Stoc;

public class MasterRepository extends AbstractRepository {

	// ---------- Furnizor ----------//

	public Furnizor addFurnizor(Furnizor furnizor) {
		return (Furnizor) this.create(furnizor);
	}

	public void deleteFurnizor(Furnizor furnizor) {
		this.delete(furnizor);
	}

	public Furnizor updateFurnizor(Furnizor furnizor) {
		return (Furnizor) this.update(furnizor);
	}

	public Furnizor findFurnizorById(Long id) {
		return (Furnizor) this.getEm()
				.createQuery("Select f from Furnizor f where id=:id")
				.setParameter("id", id)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Furnizor> findFurnizoriAll() {
		return this.getEm().createQuery("Select f from Furnizor f").getResultList();
	}

	// ---------- Gestiune ----------//

	public Gestiune addGestiune(Gestiune gestiune) {
		return (Gestiune) this.create(gestiune);
	}

	public void deleteGestiune(Gestiune gestiune) {
		this.delete(gestiune);
	}

	public Gestiune updateGestiune(Gestiune gestiune) {
		return (Gestiune) this.update(gestiune);
	}

	public Gestiune findGestiuneById(Long id) {
		return (Gestiune) this.getEm()
				.createQuery("Select g from Gestiune g where id=:id")
				.setParameter("id", id)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Gestiune> findGestiuniAll() {
		return this.getEm().createQuery("Select g from Gestiune g").getResultList();
	}

	// ---------- Produs ----------//

	public Produs addProdus(Produs produs) {
		return (Produs) this.create(produs);
	}

	public void deleteProdus(Produs produs) {
		this.delete(produs);
	}

	public Produs updateProdus(Produs produs) {
		return (Produs) this.update(produs);
	}

	public Produs findProdusById(Long id) {
		return (Produs) this.getEm()
				.createQuery("Select p from Produs p where id=:id")
				.setParameter("id", id)
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Produs> findProduseAll() {
		return this.getEm().createQuery("Select p from Produs p").getResultList();
	}

	// ---------- Stoc ----------//

	public Stoc addStoc(Stoc stoc) {
		return (Stoc) this.create(stoc);
	}

	public Stoc updateStoc(Stoc stoc) {
		return (Stoc) this.update(stoc);
	}

	public void deleteStoc(Stoc stoc) {
		this.delete(stoc);
	}

	@SuppressWarnings("unchecked")
	public List<Stoc> findStocByProdusGestiune(Long produsId, Long gestiuneId) {
		return this.getEm()
				.createQuery("Select s from Stoc s where s.produs.id=:pid and s.gestiune.id=:gid")
				.setParameter("pid", produsId)
				.setParameter("gid", gestiuneId)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Stoc> findStocuriByGestiune(Long gestiuneId) {
		return this.getEm()
				.createQuery("Select s from Stoc s where s.gestiune.id=:gid")
				.setParameter("gid", gestiuneId)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Stoc> findStocuriAll() {
		return this.getEm().createQuery("Select s from Stoc s").getResultList();
	}
}

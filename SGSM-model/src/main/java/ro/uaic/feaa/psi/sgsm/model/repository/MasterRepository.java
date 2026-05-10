package ro.uaic.feaa.psi.sgsm.model.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import ro.uaic.feaa.psi.metamodel.AbstractRepository;
import ro.uaic.feaa.psi.sgsm.model.entities.BunMaterial;
import ro.uaic.feaa.psi.sgsm.model.entities.Furnizor;
import ro.uaic.feaa.psi.sgsm.model.entities.Gestiune;
import ro.uaic.feaa.psi.sgsm.model.entities.Localitate;

public class MasterRepository extends AbstractRepository {


	public BunMaterial addBunMaterial(BunMaterial bunMaterial) {
		return (BunMaterial)this.create(bunMaterial);

	}


	public Furnizor addFurnizor(Furnizor furnizor) {
		return (Furnizor)this.create(furnizor);

	}


	public Gestiune addGestiune(Gestiune gestiune) {
		return (Gestiune)this.create(gestiune);

	}


	public Localitate addLocalitate(Localitate localitate) {

		return (Localitate)this.create(localitate);
	}


	public void deleteBunMaterial(BunMaterial bunMaterial) {
		this.delete(bunMaterial);

	}


	public void deleteFurnizor(Furnizor furnizor) {
		this.delete(furnizor);

	}


	public void deleteGestiune(Gestiune gestiune) {
		this.delete(gestiune);

	}


	public void deleteLocalitate(Localitate localitate) {
		this.delete(localitate);

	}


	public BunMaterial findBunuriMaterialById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<BunMaterial> findBunuriMaterialeAll() {
		return this.getEm().createQuery("Select b from BunMaterial b")
				.getResultList();
	}


	public Furnizor findFurnizorById(Long id) {

		return (Furnizor) this.getEm().createQuery(
				"Select f from Furnizor f where id=:id").setParameter("id", id)
				.getSingleResult();
	}


	public List<Furnizor> findFurnizoriAll() {

		return this.getEm().createQuery("Select f from Furnizor f")
				.getResultList();
	}


	public List<Furnizor> findFurnizoriAllLight() {
		// maxima atentie la ordinea parametrilor
		return this
				.getEm()
				.createQuery(
						"Select new Furnizor( f.id, f.cod, f.nume, f.adresa, f.CUI, f.banca, f.contBancar) from Furnizor f")
				.getResultList();
	}


	public Gestiune findGestiuneById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<Gestiune> findGestiuniAll() {
		return this.getEm().createQuery("Select g from Gestiune g")
				.getResultList();
	}


	public Localitate findLocalitateById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<Localitate> findLocalitatiAll() {

		return this.getEm().createQuery("Select l from Localitate l")
				.getResultList();
	}


	public BunMaterial updateBunMaterial(BunMaterial bunMaterial) {
		// TODO Auto-generated method stub
		return (BunMaterial) this.update(bunMaterial);
	}


	public Furnizor updateFurnizor(Furnizor furnizor) {
		// TODO Auto-generated method stub
		return null;
	}


	public Gestiune updateGestiune(Gestiune gestiune) {
		// TODO Auto-generated method stub
		return null;
	}


	public Localitate updateLocalitate(Localitate localitate) {
		// TODO Auto-generated method stub
		return null;
	}



}

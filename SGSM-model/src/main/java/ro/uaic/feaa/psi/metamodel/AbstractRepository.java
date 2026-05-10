package ro.uaic.feaa.psi.metamodel;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import ro.uaic.feaa.psi.sgsm.model.repository.MasterRepository;

/**
 * Clasa radacina pentru Repositories. Pentru convenienta, ofera un
 * EntityManager gata configurat care poate fi insa suprascris de subclase, in
 * functie de necesitati. Desi nu defineste nici o metoda abstracta, clasa este
 * declarata abstracta pentru a nu se putea lucra direct cu metodele acesteia,
 * spre exemplu din interfata grafica.
 * 
 * Clientul va fi obligat sa instantieze si sa lucreze cu un Repository specific
 * (e.g. {@link MasterRepository}
 * 
 * @author cretuli
 * 
 */
public abstract class AbstractRepository {
	/**
	 * Atribut static--> la run-time, toate obiectele de tip AbstractRepository
	 * (adica toate subtipurile) vor lucra cu o singura instante EntityManager.
	 * Acest aspect este foarte important pentru managementul corect al tranzactiilor
	 * intr-o aplicatie POJO (Plain Old Java Objects) care nu utilizeaza nici un
	 * framework pentru managementul tranzactiilor si nu ruleaza pe un server de
	 * aplicatii.
	 * 
	 */
	private static EntityManager em = Persistence.createEntityManagerFactory(
			"SGSMPersistenceUnit").createEntityManager();

	public void beginTransaction() {
		this.getEm().getTransaction().begin();
	}

	public void commitTransaction() {
		this.getEm().getTransaction().commit();
	}

	public Object create(Object o) {
		this.getEm().persist(o);
		return o;
	}

	public Object update(Object o) {
		Object managedEntity = this.em.merge(o);
		return managedEntity;
	}

	public void delete(Object o) {
		this.getEm().remove(o);

	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
}

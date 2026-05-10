package ro.uaic.feaa.psi.sgsm.model.entities;

import javax.persistence.Entity;

import ro.uaic.feaa.psi.metamodel.AbstractEntity;

@Entity
public class Gestiune extends AbstractEntity {

		Integer codGestiune;
		String denumireGestiune;
		String numeGestionar;

		public Integer getCodGestiune() {
			return codGestiune;
		}
		public void setCodGestiune(Integer codGestiune) {
			this.codGestiune = codGestiune;
		}
		public String getDenumireGestiune() {
			return denumireGestiune;
		}
		public void setDenumireGestiune(String denumireGestiune) {
			this.denumireGestiune = denumireGestiune;
		}
		public String getNumeGestionar() {
			return numeGestionar;
		}
		public void setNumeGestionar(String numeGestionar) {
			this.numeGestionar = numeGestionar;
		}

}

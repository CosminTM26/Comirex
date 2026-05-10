package ro.uaic.feaa.psi.metamodel;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	protected Long id; // Obligatoriu tehnic pentru JPA

	public AbstractEntity() {
		super();
	}

	public AbstractEntity(Long id) {
		this();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// Metodele equals() și hashCode() rămân aceleași (necesare tehnic)
	@Override
	public boolean equals(Object obj) {
		if (id == null) return super.equals(obj);
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final AbstractEntity other = (AbstractEntity) obj;
		if (id == null) {
			if (other.id != null) return false;
		} else if (!id.equals(other.id)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		if (id == null) return super.hashCode();
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}
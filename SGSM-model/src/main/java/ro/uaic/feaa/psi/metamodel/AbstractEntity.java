package ro.uaic.feaa.psi.metamodel;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = -4803471783122679780L;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	protected Long id;

	/**
	 * the @Version is used for Optimistic Locking mechanism
	 */
	@Version
	@Column(name = "version")
	private Integer version;
	private String createdByUser;
	private String updatedByUser;

	/**
	 * most used for inheritance strategies
	 */
	private String entity_type = this.getClass().getSimpleName();

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "dateCreated", nullable = false)
	private Date dateCreated=Calendar.getInstance().getTime();

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "dateUpdated", nullable = false)
	private Date dateUpdated=Calendar.getInstance().getTime();

	/**
	 * The default constructor is always needed for JPA.
	 */
	public AbstractEntity() {
		super();
	}

	public AbstractEntity(Long id) {
		this();
		this.id = id;

	}

	/**
	 * Provide the right equals implementation. Really mandatory. This
	 * implementation is based on the object's ID. However, if the object has
	 * not been yet serialized to the DB, hence, no ID, then provide the usual
	 * implementation.
	 */
	@Override
	public boolean equals(Object obj) {
		if (id == null)
			return super.equals(obj);
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AbstractEntity other = (AbstractEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * Provide the right hashcode implementation. Really mandatory. This
	 * implementation is based on the object's ID. However, if the object has
	 * not been yet serialized to the DB, hence, no ID, then provide the usual
	 * implementation.
	 */
	@Override
	public int hashCode() {
		if (id == null)
			return super.hashCode();

		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	
	//---------------public GETTERS and SETTERS - the usual Java Beans approach-----------------------//
	public String getCreatedByUser() {
		return createdByUser;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public String getEntity_type() {
		return entity_type;
	}

	public Long getId() {
		return id;
	}

	

	public String getUpdatedByUser() {
		return updatedByUser;
	}

	public Integer getVersion() {
		return version;
	}

	

	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	
	public void setEntity_type(String entity_type) {
		this.entity_type = entity_type;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public void setUpdatedByUser(String updatedByUser) {
		this.updatedByUser = updatedByUser;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}

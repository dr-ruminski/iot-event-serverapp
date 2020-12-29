package com.dr.iot.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class provides unique Universal Unique Identifier (UUID) and default
 * implementation of equals() and hashCode() methods. The motivation behind
 * using UUID is to differentiate between object identity in a virtual machine
 * (VM) and object identity within a database.
 * 
 * @author dr
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SuppressWarnings("serial")
public abstract class BaseEntity implements Serializable { 
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	protected Long id;

	@NaturalId
	@JsonIgnore
	private final String uuid = UUID.randomUUID().toString();

	/**
	 * Used for caching a hashCode (we don't want to calculate each time the
	 * hashCode of this object.
	 */
	@Transient
	@JsonIgnore
	private int hashcode;

	/**
	 * Gets DB id of the object.
	 * 
	 * @return database id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets DB id of the object.
	 * 
	 * @param database id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * <p>
	 * Gets an instance of string representation of {@link UUID} - immutable
	 * universally unique identifier.
	 * </p>
	 * 
	 * @return uuid
	 */
	public String getUUID() {
		return uuid;
	}

	@Override
	public boolean equals(Object baseEntity) {
		if (this == baseEntity)
			return true;
		if (!(baseEntity instanceof BaseEntity))
			return false;
		BaseEntity be = (BaseEntity) baseEntity;
		return Objects.equals(uuid, be.getUUID());
	}

	@Override
	public int hashCode() {
		if (hashcode == 0)
			hashcode = Objects.hash(uuid);
		return hashcode;
	}

}

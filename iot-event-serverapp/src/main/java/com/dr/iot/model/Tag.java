package com.dr.iot.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class Tag { // this is the only one class that doesn't need to subclass BaseEntity due to its
					// tag name uniqueness

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tag_id")
	private Long tagId;

	@Column(name = "name", length = 20, nullable = false, unique = true)
	private String name;

	@ManyToMany(mappedBy = "tags")
	private Set<Event> events;

	/*
	 * Sets a new name (when we're going to update tag's name we need to recalculate hashCode
	 */
	public void setName(String name) {
		this.name = name;
		this.hash = 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Tag))
			return false;
		Tag tag = (Tag) o;
		return Objects.equals(name, tag.getName());
	}

	@Transient
	@JsonIgnore
	private int hash; // used for caching hashCode

	@Override
	public int hashCode() {
		if (hash == 0 || name != null)
			hash = Objects.hash(name);
		return hash;
	}

}

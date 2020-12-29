package com.dr.iot.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Domain model representing an event created by an instance of IoT device. It is bound by 
 * ESP8266Device (ManyToOne) and Tag (ManyToMany) relationships. 
 * 
 * @author dr
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")				// id is taken from superclass
public class Event extends BaseEntity {
	
	@Column(name = "subject", nullable = false)
	private String subject;
	
	@Column(name = "content", nullable = false)
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
			name = "device_id_fk", 
			referencedColumnName = "id", 
			nullable = false
	)
	private ESP8266Device device;
	
	@Column(name = "created_at", nullable = true)
	private LocalDateTime createdAt;

	@ManyToMany( 
			cascade = { 
					CascadeType.PERSIST, 
					CascadeType.MERGE
			}
	)
	@JoinTable(
			name = "EVENT_TAG", 
			joinColumns = 			@JoinColumn(name = "event_id_fk", referencedColumnName = "id", nullable = false), 
			inverseJoinColumns = 	@JoinColumn(name = "tag_id_fk", referencedColumnName = "tag_id", nullable = false),
			uniqueConstraints = {	@UniqueConstraint(columnNames={"event_id_fk", "tag_id_fk"})})
	private Set<Tag> tags = new HashSet<>();
	
}
package com.dr.iot.model.subscription;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.dr.iot.model.ESP8266Device;
import com.dr.iot.model.BaseEntity;
import com.dr.iot.model.Subscriber;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Subscription class represents component that ties a {@link Subscriber} with an  
 * {@link ESP8266Device}. Each subscription consists of unique database id. 
 * @author dr
 *
 */
@Data
@Entity
@Table(uniqueConstraints={
	    @UniqueConstraint(columnNames = {"subscriber_id_fk", "device_id_fk"}) // unique key 
}) 
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
public class Subscription extends BaseEntity {

	@ManyToOne(

			fetch = FetchType.LAZY, 
			optional = false, 
			cascade = { 
				CascadeType.PERSIST, 
				CascadeType.MERGE
			}
	)
	@JoinColumn(
			name = "subscriber_id_fk", 
			referencedColumnName = "id", 
			nullable = false
	)
	@JsonIgnore
	private Subscriber subscriber;
	
	@ManyToOne(
			fetch = FetchType.LAZY, 
			optional = false, 
			cascade = { 
				CascadeType.PERSIST, 
				CascadeType.MERGE
			}
	)
	@JoinColumn(
			name = "device_id_fk", 
			referencedColumnName = "id", 
			nullable = false
			)
	@JsonIgnore
	private ESP8266Device subscribedDevice;
	
	@Override
	@JsonProperty("subscriptionId")
	public Long getId() {
		return super.getId();
	}
}

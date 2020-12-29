package com.dr.iot.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.dr.iot.model.subscription.Subscription;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A specific user that contains a {@link List} of subscriptions.
 * @author dr
 *
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Subscriber extends BaseEntity {
	
	// others properties of a subscriber (aka reader) ...
	
	@OneToMany(
			fetch = FetchType.LAZY,
			mappedBy = "subscriber"
	)
	private List<Subscription> subscriptions = new ArrayList<>();
	
}

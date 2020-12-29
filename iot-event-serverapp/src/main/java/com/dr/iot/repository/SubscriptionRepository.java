package com.dr.iot.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.dr.iot.model.subscription.Subscription;

@RepositoryRestResource(collectionResourceRel = "subscriptions", path = "subscribe")
public interface SubscriptionRepository extends PagingAndSortingRepository<Subscription, Long> {
}

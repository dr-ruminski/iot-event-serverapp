package com.dr.iot.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.dr.iot.model.Subscriber;

@RepositoryRestResource(collectionResourceRel = "subscribers", path = "subscribers")
public interface SubscriberRepository extends PagingAndSortingRepository<Subscriber, Long> {
}

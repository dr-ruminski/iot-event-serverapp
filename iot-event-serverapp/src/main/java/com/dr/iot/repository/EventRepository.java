package com.dr.iot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dr.iot.model.Event;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
}

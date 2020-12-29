package com.dr.iot.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.dr.iot.model.ESP8266Device;

@RepositoryRestResource(collectionResourceRel = "devices", path = "devices")
public interface DeviceRepository extends PagingAndSortingRepository<ESP8266Device, Long> {
	
	Optional<ESP8266Device> findDeviceByName(String name);
}

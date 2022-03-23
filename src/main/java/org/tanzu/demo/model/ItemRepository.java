package org.tanzu.demo.model;

import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Sensor, Integer> {
}

package com.sprint.Repository;

import com.sprint.Entities.Inventory;
import com.sprint.Projections.InventoryProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "inventories", path = "inventories", excerptProjection = InventoryProjection.class)
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}

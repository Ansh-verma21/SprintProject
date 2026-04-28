package com.sprint.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.sprint.Entities.Store;
import com.sprint.Projections.StoreProjection;

@RepositoryRestResource(collectionResourceRel = "stores", path = "stores", excerptProjection = StoreProjection.class)
public interface StoreRepository extends JpaRepository<Store, Long> {
}

package com.sprint.Repository;

import com.sprint.Entities.Staff;
import com.sprint.Projections.StaffProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "staff", path = "staff", excerptProjection = StaffProjection.class)
public interface StaffRepository extends JpaRepository<Staff, Long> {
}

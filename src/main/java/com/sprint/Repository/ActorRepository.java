package com.sprint.Repository;

import com.sprint.Entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "actors", path = "actors", exported = true)
public interface ActorRepository extends JpaRepository<Actor, Long> {
}

package com.sprint.Repository;

import com.sprint.Entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "actors", path = "actors", exported = true)
public interface ActorRepository extends JpaRepository<Actor, Long> {
    @Query("SELECT a FROM Actor a WHERE LOWER(a.firstName) = LOWER(:firstName)")
    List<Actor> findByFirstName(String firstName);
}

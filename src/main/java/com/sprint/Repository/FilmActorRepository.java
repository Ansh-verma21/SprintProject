package com.sprint.Repository;

import com.sprint.Entities.FilmActor;
import com.sprint.Entities.FilmActorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "filmActors", path = "filmActors")
public interface FilmActorRepository extends JpaRepository<FilmActor, FilmActorId> {
}

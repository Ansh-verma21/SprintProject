package com.sprint.Repository;

import com.sprint.Entities.FilmActor;
import com.sprint.Entities.FilmActorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.query.Param;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "filmActors", path = "filmActors")
public interface FilmActorRepository extends JpaRepository<FilmActor, FilmActorId>  {

    // Page 2 — actor name
    List<FilmActor> findByActorActorId(@Param("actorId") Long actorId);

    // Page 3 — films by actor
    List<FilmActor> findByActorActorIdOrderByFilmFilmId(@Param("actorId") Long actorId);
    
    
}
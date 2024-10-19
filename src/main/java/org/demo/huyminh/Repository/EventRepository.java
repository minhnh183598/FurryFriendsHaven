package org.demo.huyminh.Repository;

import org.demo.huyminh.Entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    List<Event> findByCategory(String category);

    @Query("SELECT e FROM Event e WHERE "
            + "(?1 IS NULL OR e.name LIKE %?1%) AND "
            + "(?2 IS NULL OR e.postedBy LIKE %?2%) AND "
            + "(?3 IS NULL OR e.location LIKE %?3%) AND "
            + "(?4 IS NULL OR e.date = ?4) AND "
            + "(?5 IS NULL OR ?5 MEMBER OF e.tags)")
    List<Event> findEventsByCriteria(String name, String postedBy, String location, LocalDate date, List<String> tags);
}

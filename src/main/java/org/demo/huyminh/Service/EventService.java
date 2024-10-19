package org.demo.huyminh.Service;

import org.demo.huyminh.Entity.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventService {

    Event saveEvent(Event event, String username);

    List<Event> getEventsByCategory(String category);

    List<Event> getAllEvent();

    Event getEventById(Long eventId);

    void likeEvent(Long eventId, String username);

    List<Event> searchEvent(String name, String postedBy, String location, LocalDate date, List<String> tags);

    Event updateEvent(Long eventId, Event event, String username);

    void deleteEvent(Long eventId);
}

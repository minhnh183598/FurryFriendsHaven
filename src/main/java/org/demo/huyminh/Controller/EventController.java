package org.demo.huyminh.Controller;

import jakarta.persistence.EntityNotFoundException;
import org.demo.huyminh.Entity.Event;
import org.demo.huyminh.Entity.Post;
import org.demo.huyminh.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();  // Lấy tên người dùng hiện tại

            event.setPostedBy(username);
            Event createdEvent = eventService.saveEvent(event, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Trả về lỗi 400 nếu dữ liệu không hợp lệ
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getEventByCategory(@PathVariable String category) {
        try {
            // Lấy tên người dùng hiện tại
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            List<Event> events = eventService.getEventsByCategory(category);
            if (events.isEmpty()) {
                // Nếu không có bài viết nào, trả về 200 OK với thông điệp
                return ResponseEntity.status(HttpStatus.OK).body("No events found for the given category.");
            }
            return ResponseEntity.status(HttpStatus.OK).body(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvent() {
        try {
            // Lấy tên người dùng hiện tại
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // Nếu cần sử dụng tên người dùng

            return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEvent());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEventById(@PathVariable Long eventId) {
        try {
            // Lấy tên người dùng hiện tại
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // Nếu cần sử dụng tên người dùng

            Event event = eventService.getEventById(eventId);
            return ResponseEntity.ok(event);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{eventId}/like")
    public ResponseEntity<?> likeEvent(@PathVariable Long eventId) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();  // Lấy tên người dùng hiện tại


            eventService.likeEvent(eventId, username);  // Truyền tên người dùng vào method likePost
            return ResponseEntity.ok(new String[]{"Event liked successfully."});
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvent(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String postedBy,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) List<String> tags) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();  // Lấy tên người dùng hiện tại

            List<Event> events = eventService.searchEvent(name, postedBy, location, date, tags);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long eventId) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();  // Lấy tên người dùng hiện tại

            Event event = eventService.getEventById(eventId);

            // Kiểm tra nếu người dùng hiện tại không phải là người tạo bài viết
            if (!event.getPostedBy().equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this post.");
            }
            eventService.deleteEvent(eventId);
            return ResponseEntity.status(HttpStatus.OK).body("Event deleted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable Long eventId, @RequestBody Event event) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();  // Lấy tên người dùng hiện tại

            Event existingEvent = eventService.getEventById(eventId);

            // Kiểm tra nếu người dùng hiện tại không phải là người tạo bài viết
            if (!existingEvent.getPostedBy().equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to update this event.");
            }

            Event updatedEvent = eventService.updateEvent(eventId, event, username);
            return ResponseEntity.ok(updatedEvent);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

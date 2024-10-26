package org.demo.huyminh.Service.Impl;

import org.demo.huyminh.Entity.Event;
import org.demo.huyminh.Repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.demo.huyminh.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    private static final Set<String> VALID_TAGS = new HashSet<>(Arrays.asList(
            "Adoption",
            "Fundraising",
            "Volunteering",
            "Awareness",
            "Community",
            "Education",
            "Rescue"
    ));
    private static final List<String> VALID_CATEGORIES = Arrays.asList(
            "ANIMAL_RESCUE",
            "ADOPTION",
            "FUNDRAISING",
            "VOLUNTEER"
    );

    @Override
    public Event saveEvent(Event event, String username) {
        // Gán tên người dùng vào bài viết
        event.setPostedBy(username);

        // Kiểm tra category hợp lệ
        if (!VALID_CATEGORIES.contains(event.getCategory())) {
            throw new IllegalArgumentException("Category '" + event.getCategory() + "' is not valid.");
        }

        if (event.getTags() != null) {
            for (String tagName : event.getTags()) {
                if (!VALID_TAGS.contains(tagName)) {
                    throw new IllegalArgumentException("Tag '" + tagName + "' is not valid."); // Ném lỗi nếu tag không hợp lệ
                }
            }
        }
        // Khởi tạo các giá trị mặc định
        event.setLikeCount(0);
        event.setViewCount(0);
        event.setDate(LocalDate.now());

        // Lưu bài viết vào cơ sở dữ liệu
        return eventRepository.save(event);
    }

    public List<Event> getEventsByCategory(String category) {
        return eventRepository.findByCategory(category);
    }

    public List<Event> getAllEvent() {
        Iterable<Event> iterable = eventRepository.findAll();
        List<Event> events = new ArrayList<>();
        iterable.forEach(events::add);
        return events;
    }

    public Event getEventById(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setViewCount(event.getViewCount() + 1);
            return eventRepository.save(event);
        } else {
            throw new EntityNotFoundException("Event not found");
        }
    }

    public void likeEvent(Long eventId, String username) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();

            // Kiểm tra xem người dùng đã thích sự kiện chưa (nếu bạn đang lưu danh sách người thích)
            if (!event.getLikedByUsers().contains(username)) {
                event.setLikeCount(event.getLikeCount() + 1); // Tăng số lượng likes
                event.getLikedByUsers().add(username); // Thêm người dùng vào danh sách đã thích
                eventRepository.save(event); // Lưu sự kiện
            } else {
                // Có thể xử lý nếu người dùng đã thích bài viết trước đó
                throw new IllegalArgumentException("User has already liked this post.");
            }
        } else {
            throw new EntityNotFoundException("Event not found with id: " + eventId);
        }
    }

    public List<Event> searchEvent(String name, String postedBy, String location, LocalDate date, List<String> tags) {
        // Logic tìm kiếm để lọc các bài viết theo tiêu chí
        // Giả định bạn có một repository tương ứng để truy vấn cơ sở dữ liệu
        // Sử dụng phương thức tìm kiếm dựa trên tiêu chí đã cho
        return eventRepository.findEventsByCriteria(name, postedBy, location, date, tags);
    }

    public List<Event> searchByLikeCount(int minLikes, int maxLikes) {
        return eventRepository.findByLikeCountBetween(minLikes, maxLikes);
    }

    public Event updateEvent(Long eventId, Event event, String username) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event existingEvent = optionalEvent.get();
            // Cập nhật các trường cần thiết
            existingEvent.setName(event.getName());
            existingEvent.setContent(event.getContent());
            existingEvent.setLocation(event.getLocation());
            existingEvent.setPostedBy(username); // Cập nhật người đăng nếu cần
            // Cập nhật các trường khác nếu cần
            return eventRepository.save(existingEvent);
        } else {
            throw new EntityNotFoundException("Event not found with id: " + eventId);
        }
    }

    public void deleteEvent(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            eventRepository.deleteById(eventId);
        } else {
            throw new EntityNotFoundException("Event not found with id: " + eventId);
        }
    }
}

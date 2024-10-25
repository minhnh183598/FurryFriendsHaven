package org.demo.huyminh.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "event") // Tên bảng trong SQL Server
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 500000)
    private String content;

    private String postedBy;

    private String category;

    private String location; // Địa điểm sự kiện

    @ElementCollection
    private List<String> imgs;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private int likeCount;

    @ElementCollection // Để lưu danh sách người dùng
    private List<String> likedByUsers = new ArrayList<>();

    private int viewCount;

    @ElementCollection
    private List<String> tags;


}
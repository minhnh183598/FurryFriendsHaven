package org.demo.huyminh.Entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "post") // Tên bảng trong SQL Server
public class Post {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 5000000)
    private String content;

    private String postedBy;

    private String category;

    private List<String> imgs;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private int likeCount;

    @ElementCollection // Để lưu danh sách người dùng
    private List<String> likedByUsers = new ArrayList<>();

    private int viewCount;

    private List<String> tags;
}

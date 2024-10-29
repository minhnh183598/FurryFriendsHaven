package org.demo.huyminh.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.demo.huyminh.Entity.Image;
import org.demo.huyminh.Entity.Tag;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PostCreationRequest {

    String title;
    String content;
    String description;
    String nickname;
    String category;
    List<String> images;
    List<Tag> tags;
}

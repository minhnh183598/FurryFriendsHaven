package org.demo.huyminh.DTO.Reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Minh
 * Date: 10/26/2024
 * Time: 11:54 PM
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BriefPostResponse {

    int id;
    String title;
    String description;
    String username;
    String nickname;
    String category;
    List<String> images;
    LocalDateTime createAt;
    LocalDateTime editedAt;
    int likeCount;
    List<String> likedByUsers;
    int viewCount;
    List<String> tags;
}

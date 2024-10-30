package org.demo.huyminh.Mapper;

import org.demo.huyminh.DTO.Reponse.BriefPostResponse;
import org.demo.huyminh.DTO.Reponse.PostResponse;
import org.demo.huyminh.DTO.Request.PostCreationRequest;
import org.demo.huyminh.Entity.Feedback;
import org.demo.huyminh.Entity.Image;
import org.demo.huyminh.Entity.Post;
import org.demo.huyminh.Entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

/**
 * @author Minh
 * Date: 10/10/2024
 * Time: 1:49 PM
 */

@Mapper(componentModel = "Spring")
public interface PostMapper {

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "images", ignore = true)
    Post toPost(PostCreationRequest request);

    @Mapping(target = "images", expression = "java(mapImagesToStrings(post.getImages()))")
    @Mapping(target = "tags", expression = "java(mapTagsToStrings(post.getTags()))")
    PostResponse toPostResponse(Post post);

    @Mapping(target = "images", expression = "java(mapImagesToStrings(post.getImages()))")
    @Mapping(target = "tags", expression = "java(mapTagsToStrings(post.getTags()))")
    BriefPostResponse toBriefPostResponse(Post post);

    default List<String> mapTagsToStrings(List<Tag> tags) {
        if (tags == null) {
            return null;
        }
        return tags.stream()
                .map(Tag::getName)
                .toList();
    }

    default List<String> mapImagesToStrings(List<Image> images) {
        if (images == null) {
            return null;
        }

        return images.stream()
                .map(Image::getImageUrl)
                .toList();
    }

    default Image stringToImage(String imageUrl, Post post) {
        if (imageUrl == null || post == null) {
            return null;
        }
        Image image = new Image();
        image.setImageUrl(imageUrl);
        image.setPost(post);
        return image;
    }


    default List<Image> mapStringsToImages(List<String> imageUrls, Post post) {
        if (imageUrls == null) {
            return null;
        }
        return imageUrls.stream()
                .map(url -> stringToImage(url, post))
                .toList();
    }
}

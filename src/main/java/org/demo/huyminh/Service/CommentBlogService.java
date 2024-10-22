package org.demo.huyminh.Service;

import org.demo.huyminh.Entity.CommentBlog;

import java.util.List;

public interface CommentBlogService {

    CommentBlog createComment(Long postId, String postedBy, String content);

    List<CommentBlog> getCommentsByPostId(Long postId);
}

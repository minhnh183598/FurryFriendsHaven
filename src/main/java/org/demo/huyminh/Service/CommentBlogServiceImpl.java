package org.demo.huyminh.Service;

import jakarta.persistence.EntityNotFoundException;
import org.demo.huyminh.Entity.CommentBlog;
import org.demo.huyminh.Entity.Post;
import org.demo.huyminh.Repository.CommentBlogRepository;
import org.demo.huyminh.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentBlogServiceImpl implements CommentBlogService {

    @Autowired
    private CommentBlogRepository commentBlogRepository;

    @Autowired
    private PostRepository postRepository;

    public CommentBlog createComment(Long postId, String postedBy, String content) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            CommentBlog commentBlog = new CommentBlog();
            commentBlog.setPost(optionalPost.get());
            commentBlog.setContent(content);
            commentBlog.setPostedBy(postedBy);
            commentBlog.setCreateAt(new Date());

            return commentBlogRepository.save(commentBlog);
        }
        throw new EntityNotFoundException("Post not found");
    }

    public List<CommentBlog> getCommentsByPostId(Long postId) {
        return commentBlogRepository.findByPostId(postId);
    }
}

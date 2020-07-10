package com.zhuangfei.serivce;

import com.zhuangfei.entity.Comment;
import com.zhuangfei.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository repository;

    public void saveComment(Comment comment){
        repository.save(comment);
    }

    public Comment getCommentById(Integer id){
        return repository.findById(id).get();
    }
}

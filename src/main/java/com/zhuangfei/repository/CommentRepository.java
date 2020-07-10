package com.zhuangfei.repository;

import com.zhuangfei.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment,Integer> {
}

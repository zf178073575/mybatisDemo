package com.zhuangfei.demo;

import com.alibaba.nacos.client.config.utils.IOUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.zhuangfei.component.Sender;
import com.zhuangfei.entity.Comment;
import com.zhuangfei.serivce.CommentService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DemoApplicationTests {

    @Autowired
    CommentService commentService;

    @Autowired
    Sender sender;

    @Autowired
    GridFsTemplate gridFsTemplate;


    @Test
    void contextLoads() {
    }

    @Test
    public void saveComment(){
        Comment comment = new Comment();
        comment.setId(2);
        comment.setName("zay");
        comment.setAge(2);
        comment.setAddress("nanjing");
        commentService.saveComment(comment);
    }

    @Test
    public void testComment(){
        System.out.println(commentService.getCommentById(1));
    }

    @Test
    public void GridFsTest() throws FileNotFoundException {
        //选择要存储的文件
        File file = new File("C:/aaa.docx");
        InputStream inputStream = new FileInputStream(file);
        //存储文件并起名称
        ObjectId objectId = gridFsTemplate.store(inputStream, "world");
        String id = objectId.toString();
        //获取到文件的id，可以从数据库中查找
        System.out.println(id);
    }

    @Test
    public void queryFile() throws IOException {
        String id = "5f05be4576759f4f5347da9d";
        //根据id查找文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
        //打开下载流对象
        GridFSDownloadStream gridFS = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建gridFsSource，用于获取流对象
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFS);
        //获取流中的数据
        String string = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
        System.out.println(string);
    }

}

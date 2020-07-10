package com.zhuangfei.controller;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    // 上传文件控制器
    @RequestMapping(value = "/uploadfile",method = RequestMethod.POST)
    String uploadfile(@RequestParam("file") MultipartFile multiportFile) {
        String result = "error";
        try {
            // 获得提交的文件名
            String fileName = multiportFile.getOriginalFilename();
            // 获得文件输入流
            InputStream ins = multiportFile.getInputStream();
            // 获得文件类型
            String contentType = multiportFile.getContentType();
            // 将文件存储到mongodb中,mongodb 将会返回这个文件的具体信息
            ObjectId gfs = gridFsTemplate.store(ins, fileName, contentType);
            result = gfs.toString();

        } catch (IOException e) {
        }
        return result;
    }

    // 下载文件控制器
    @RequestMapping(value = "/downloadfile",method = {RequestMethod.GET,RequestMethod.POST})
    public void downloadfile(@RequestParam(value="id") String id, HttpServletRequest request, HttpServletResponse response){

        /**
         * 关于Query的具体用发下面的链接给的很清楚了，这里就不多说了。
         *
         * @link{http://www.baeldung.com/queries-in-spring-data-mongodb}
         */
        Query query = Query.query(Criteria.where("_id").is(id));
        // 查询单个文件
        GridFSFile gfsfile = gridFsTemplate.findOne(query);

        if (gfsfile == null) {
            return;
        }
        try {
            String fileName = gfsfile.getFilename().replace(",", "");
            //处理中文文件名乱码
            if (request.getHeader("User-Agent").toUpperCase().contains("MSIE") ||
                    request.getHeader("User-Agent").toUpperCase().contains("TRIDENT")
                    || request.getHeader("User-Agent").toUpperCase().contains("EDGE")) {
                fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            } else {
                //非IE浏览器的处理：
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
            byte bs[] = new byte[1024];
            GridFsResource resource = gridFsTemplate.getResource(gfsfile);
            InputStream inputStream = resource.getInputStream();
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            if(inputStream.read(bs)>0) {
                outputStream.write(bs);
            }
            inputStream.close();
            outputStream.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    public Object deleteFile(@RequestParam(name = "id") String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        // 查询单个文件
        GridFSFile gfsfile = gridFsTemplate.findOne(query);
        if (gfsfile == null) {
            return "error";
        }
        gridFsTemplate.delete(query);
        return "success";
    }

}

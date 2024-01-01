package com.wzy.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzy.common.Result;
import com.wzy.entity.ArticleFile;
import com.wzy.service.ArticleFileService;
import com.wzy.utils.BaiduCloudApi;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;

@Transactional
@RestController
@RequestMapping("/file")
public class ArticleFileController {

    private Integer articleFileId;


    @Autowired
    private ArticleFileService articleFileService;

    @Value("${lostfound.path}")
    private String basePath;

    @GetMapping
    public Result list(){
        return Result.success(articleFileService.list());
    }

    @Transactional
    @PostMapping("/upload")
    public String upload(@RequestParam(name = "file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);

        //定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        File uploadFile = new File(basePath + fileUUID);


        //判断配置的父级文件目录是否存在，若不存在则创建一个新的目录
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        //当文件存在时再获取文件的md5
        String url;
        String md5;
        //上传文件到磁盘
        file.transferTo(uploadFile);
        //获取文件的md5，通过对比md5防止上传重复的文件
        md5 = SecureUtil.md5(uploadFile);
        //从数据库查询是否存在相同的记录
        ArticleFile dbFiles = getFileMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
            //由于文件已经存在，所以删除刚才上传的重复文件
            uploadFile.delete();
        } else {
            //数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://139.155.157.156:9091/file/" + fileUUID;
//            url = "http://localhost:9091/file/" + fileUUID;
        }

        ArticleFile articleFile = new ArticleFile();
        articleFile.setMd5(md5);
        articleFile.setName(originalFilename);
        articleFile.setUrl(url);


        String imageInfo = BaiduCloudApi.getImageInfo(basePath + fileUUID);

        System.out.println(imageInfo);

        // 解析JSON字符串
        JSONObject jsonObject = new JSONObject(imageInfo);

        String description = jsonObject.getJSONArray("result").getJSONObject(0).getString("keyword");

        articleFile.setDescription(description);

        articleFileService.saveOrUpdate(articleFile);



        setArticleFileId(articleFile.getId());

        System.out.println(articleFile.getId());


        System.out.println(description);;

        return url;
    }

    /**
     * 文件下载接口  http://localhost:9091/file/{fileUUID}
     *
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
//        根据文件的唯一标识码获取文件
        File uploadFile = new File(basePath + fileUUID);
//        设置输出流格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");//写出流格式

        //读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    private ArticleFile getFileMd5(String md5) {
//查询文件的md5是否存在
        QueryWrapper<ArticleFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<ArticleFile> filesList = articleFileService.list(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    public void setArticleFileId(Integer articleFileId){
        this.articleFileId = articleFileId;
    }

    public Integer getArticleFileId() {
        System.out.println(articleFileId);
        return articleFileId;
    }

}

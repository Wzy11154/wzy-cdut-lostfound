package com.wzy.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzy.common.BaseContext;
import com.wzy.common.Result;
import com.wzy.entity.Article;
import com.wzy.entity.ArticleFile;
import com.wzy.entity.Users;
import com.wzy.service.ArticleFileService;
import com.wzy.service.ArticleService;
import com.wzy.utils.TokenUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@CrossOrigin
@RestController
@RequestMapping("/article")
public class ArticleController {


    @Autowired
    private ArticleFileController articleFileController;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleFileService articleFileService;


    @GetMapping
    public Result findAll(){
       return Result.success(articleService.list());
    }

    @GetMapping("/pages")
    public Result pages(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(required = false) String description){

        Page<Article> pages = articleService.findPages(new Page<>(pageNum,pageSize), description);
        return Result.success(pages);
    }

    @GetMapping("/page")
    public Result page(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(required = false) String name){

        Page<Article> page = articleService.findPage(new Page<>(pageNum,pageSize), name);
        return Result.success(page);
    }

    @GetMapping("/pages/{nickname}")
    public Result myArticle(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @PathVariable String nickname){
        Page<Article> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",0);
        queryWrapper.eq("user", nickname);
        return Result.success(articleService.page(page,queryWrapper));
    }

    @GetMapping("{id}")
    public Result select(@PathVariable Integer id) {
        return Result.success(articleService.getById(id));
    }

    //新增或更新

    /**
     * 新增或更新，并且在文件上传成功之后获取文件的id，将文件id与文章id相关联
     * @param article
     * @return
     */
    @PostMapping//插入操作
    public Result save(@RequestBody Article article) {
        if(article.getId() == null){
            article.setCreateTime(LocalDateTime.now());
            article.setUpdateTime(LocalDateTime.now());
            article.setUser(TokenUtils.getCurrentUser().getNickname());
//            Object nickname = request.getSession().getAttribute("nickname");
//            System.out.println(Thread.currentThread().getId());
//            String nickname = BaseContext.getCurrentNickname();
//            article.setUser((String) nickname);
            ArticleFile articleFile = articleFileService.getById(articleFileController.getArticleFileId());


            articleService.saveOrUpdate(article);

            articleFile.setArticleId(article.getId());

            articleFileService.updateById(articleFile);
        }

        article.setUpdateTime(LocalDateTime.now());

        articleService.saveOrUpdate(article);


//        System.out.println(articleFileController.getArticleFileId());


//        System.out.println(article.getId());
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody Article article) {
        article.setIsDeleted("1");
        return Result.success(articleService.updateById(article));
    }



}

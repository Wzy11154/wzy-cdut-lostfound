package com.wzy.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzy.common.Result;
import com.wzy.entity.Comment;
import com.wzy.service.CommentService;
import com.wzy.service.CommentService;
import com.wzy.service.UserService;
import com.wzy.utils.TokenUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    //新增或更新
    @PostMapping//插入操作
    public Result save(@RequestBody Comment comment) {
        if(comment.getId() == null){
            comment.setUserId(TokenUtils.getCurrentUser().getId());
            comment.setTime(DateUtil.now());

            if(comment.getPid() != null) { //判断如果是回复的话才进行处理
                Integer pid = comment.getPid();
                Comment pComment = commentService.getById(pid);
                if (pComment.getOriginId() != null) {//如果当前的父级有祖宗，那么设置相同的祖宗
                    comment.setOriginId(pComment.getOriginId());
                } else{//否则设置父级为当前回复的祖宗
                    comment.setOriginId(comment.getPid());
                }
            }
        }
        commentService.saveOrUpdate(comment);
        return Result.success();
    }


    @GetMapping
    public Result findAll() {
        return Result.success(commentService.list());
    }

    @GetMapping("/tree/{articleId}")
    public Result findCommentDetail(@PathVariable Integer articleId) {
        List<Comment> articleComments = commentService.findCommentDetail(articleId);//查询所有评论和回复数据
        //查询评论数据（不包含回复）
        List<Comment> originList = articleComments.stream().filter(comment -> comment.getOriginId() == null).collect(Collectors.toList());

        //设置评论数据的子节点，也就是回复内容
        for (Comment origin: originList){
            List<Comment> comments = articleComments.stream().filter(comment -> origin.getId().equals(comment.getOriginId())).collect(Collectors.toList());//表示回复的对象集合
            comments.forEach(comment -> {
                articleComments.stream().filter(c1 -> c1.getId().equals(comment.getPid())).findFirst().ifPresent(v -> {//找到父级评论的用户id和用户昵称，并设置给当前对象
                    comment.setPUserid(v.getUserId());
                    comment.setPNickname(v.getNickname());
                });
            });

            origin.setChildren(comments);
        }
        return Result.success(originList);
    }

    @DeleteMapping("{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(commentService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(commentService.removeByIds(ids));
    }

    @PutMapping
    public Result update(@RequestBody Comment comment) {
        return Result.success(commentService.updateById(comment));
    }

    @GetMapping("{id}")
    public Result select(@PathVariable Integer id) {
        return Result.success(commentService.getById(id));
    }




    //分页查询，使用mybatis-plus方式
    @GetMapping("/pages")
    public Result selectPages(@RequestParam(required = false) String name,
                              @RequestParam Integer pageNum,
                              @RequestParam Integer pageSize) {
        IPage<Comment> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper();
        queryWrapper.like((!Strings.isEmpty(name)),"name",name);
        queryWrapper.orderByDesc("id");
        return Result.success(commentService.page(page,queryWrapper));
    }



}

package com.wzy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ArticleFile {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer articleId;

    private String md5;

    private String name;

    private String url;

    private String description;
}

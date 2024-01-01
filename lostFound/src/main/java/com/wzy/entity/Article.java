package com.wzy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String content;

    private String user;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String isDeleted;
}

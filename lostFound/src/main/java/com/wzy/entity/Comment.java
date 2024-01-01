package com.wzy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String content;

    private Integer userId;

    private String time;

    private Integer pid;

    @TableField(exist = false)
    private String pNickname;//父节点用户的用户昵称

    @TableField(exist = false)
    private Integer pUserid;//父节点用户的用户id

    private Integer originId;

    private Integer articleId;

    @TableField(exist = false)
    private String nickname;

    @TableField(exist = false)
    private List<Comment> children;
}

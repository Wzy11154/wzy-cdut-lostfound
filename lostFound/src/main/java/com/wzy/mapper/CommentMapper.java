package com.wzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wzy.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("select c.*,u.nickname from comment c left join users u on c.user_id = u.id " +
            " where c.article_id = #{articleId} order by id desc")
    List<Comment> findCommentDetail(Integer articleId);
}

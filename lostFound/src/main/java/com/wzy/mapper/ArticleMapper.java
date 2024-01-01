package com.wzy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzy.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    Page<Article> findPage(Page<Article> page, @Param("name") String name);
    Page<Article> findPages(Page<Article> page, @Param("description") String description);
}

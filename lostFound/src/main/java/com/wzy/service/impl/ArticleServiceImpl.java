package com.wzy.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzy.entity.Article;
import com.wzy.mapper.ArticleMapper;
import com.wzy.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public Page<Article> findPage(Page<Article> page, String name) {
        return articleMapper.findPage(page , name);
    }
    @Override
    public Page<Article> findPages(Page<Article> page, String description) {
        return articleMapper.findPages(page , description);
    }
}

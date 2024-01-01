package com.wzy.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wzy.entity.Article;


public interface ArticleService extends IService<Article> {
    Page<Article> findPage(Page<Article> page, String name);
    Page<Article> findPages(Page<Article> page, String description);
}

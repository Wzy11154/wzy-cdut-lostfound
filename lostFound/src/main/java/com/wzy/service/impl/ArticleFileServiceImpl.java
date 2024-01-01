package com.wzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzy.entity.ArticleFile;
import com.wzy.mapper.ArticleFileMapper;
import com.wzy.service.ArticleFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleFileServiceImpl extends ServiceImpl<ArticleFileMapper, ArticleFile> implements ArticleFileService {

}

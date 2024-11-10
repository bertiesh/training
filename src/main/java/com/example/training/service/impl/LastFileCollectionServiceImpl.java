package com.example.training.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.training.dao.LastFileCollectionDao;
import com.example.training.entity.LastFileCollection;
import com.example.training.service.LastFileCollectionService;
import org.springframework.stereotype.Service;

/**
 * @author Xuxinyuan
 */
@Service
public class LastFileCollectionServiceImpl extends ServiceImpl<LastFileCollectionDao, LastFileCollection> implements LastFileCollectionService {
}

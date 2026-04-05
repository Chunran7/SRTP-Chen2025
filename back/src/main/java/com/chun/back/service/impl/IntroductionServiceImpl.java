package com.chun.back.service.impl;

import com.chun.back.mapper.IntroductionMapper;
import com.chun.back.pojo.Introduction;
import com.chun.back.service.IntroductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntroductionServiceImpl implements IntroductionService {

    @Autowired
    private IntroductionMapper introductionMapper;

    @Override
    public Introduction getIntroduction() {
        return introductionMapper.getIntroduction();
    }

    @Override
    public int updateIntroduction(Introduction introduction) {
        // 先检查是否存在，不存在则创建
        Introduction existing = introductionMapper.getIntroduction();
        if (existing == null) {
            return introductionMapper.createIntroduction(introduction);
        } else {
            return introductionMapper.updateIntroduction(introduction);
        }
    }
}

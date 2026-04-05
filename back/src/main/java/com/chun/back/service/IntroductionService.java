package com.chun.back.service;

import com.chun.back.pojo.Introduction;

public interface IntroductionService {
    
    /**
     * 获取介绍信息
     * @return 介绍信息对象
     */
    Introduction getIntroduction();
    
    /**
     * 更新介绍信息
     * @param introduction 介绍信息对象
     * @return 更新的行数
     */
    int updateIntroduction(Introduction introduction);
}

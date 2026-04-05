package com.chun.back.mapper;

import com.chun.back.pojo.Introduction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Insert;

@Mapper
public interface IntroductionMapper {

    @Select("SELECT * FROM introduction WHERE id = 1")
    Introduction getIntroduction();

    @Update("UPDATE introduction SET title = #{title}, content = #{content}, image_url = #{imageUrl} WHERE id = 1")
    int updateIntroduction(Introduction introduction);

    @Insert("INSERT INTO introduction (title, content, image_url) VALUES (#{title}, #{content}, #{imageUrl})")
    int createIntroduction(Introduction introduction);
}

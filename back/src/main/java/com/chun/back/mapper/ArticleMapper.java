package com.chun.back.mapper;

import com.chun.back.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Select("SELECT * FROM article WHERE id = #{id}")
    Article getArticleById(Long id);

    @Select("SELECT * FROM article")
    List<Article> getArticleList();

    @Select("SELECT * FROM article ORDER BY create_time DESC LIMIT #{count}")
    List<Article> getLatestArticleList(Integer count);

    // 插入文章，返回受影响行数
    @Insert("INSERT INTO article(title, first_picture, description, content, author, create_time, update_time, views, likes) VALUES(#{title}, #{firstPicture}, #{description}, #{content}, #{author}, #{createTime}, #{updateTime}, #{views}, #{likes})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertArticle(Article article);
    
    // 删除文章
    @Delete("DELETE FROM article WHERE id = #{id}")
    int deleteArticle(Long id);

    // 软删除文章（设置 is_deleted 标记）
    @Update("UPDATE article SET is_deleted = 1, update_time = NOW() WHERE id = #{id}")
    int softDeleteArticle(Long id);

    // 恢复已删除的文章
    @Update("UPDATE article SET is_deleted = 0, update_time = NOW() WHERE id = #{id}")
    int restoreArticle(Long id);

    // 彻底删除文章（物理删除）
    @Delete("DELETE FROM article WHERE id = #{id}")
    int hardDeleteArticle(Long id);

    // 获取文章列表（支持分页和搜索）
    @Select("<script>" +
            "SELECT * FROM article " +
            "<where>" +
            "<if test='keyword != null and keyword != \"\">"+
            "AND (title LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))" +
            "</if>" +
            "<if test='includeDeleted == 0'> " +
            "AND is_deleted = 0 " +
            "</if>" +
            "</where>" +
            "ORDER BY create_time DESC " +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Article> getArticleListByPage(@Param("page") Integer page, 
                                        @Param("pageSize") Integer pageSize, 
                                        @Param("keyword") String keyword, 
                                        @Param("includeDeleted") Integer includeDeleted,
                                        @Param("offset") Integer offset);

    // 统计文章总数
    @Select("<script>" +
            "SELECT COUNT(*) FROM article " +
            "<where>" +
            "<if test='keyword != null and keyword != \"\">"+
            "AND (title LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%'))" +
            "</if>" +
            "<if test='includeDeleted == 0'> " +
            "AND is_deleted = 0 " +
            "</if>" +
            "</where>" +
            "</script>")
    Long countArticles(@Param("keyword") String keyword, @Param("includeDeleted") Integer includeDeleted);
}
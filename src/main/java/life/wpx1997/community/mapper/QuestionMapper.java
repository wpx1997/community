package life.wpx1997.community.mapper;

import life.wpx1997.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(account_id,title,description,gmt_create,gmt_modified,creator,tag) values(#{account_id},#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    public void create(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{userId} limit #{offset},#{size}")
    List<Question> listByUserId(@Param("userId") Integer userId,@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    @Select("select count(1) from question where creator = #{userId}")
    Integer countByUserId(@Param("userId") Integer userId);

    @Select("select * from question where id = #{id}")
    Question getById(@Param("id") Integer id);

    @Select("select * from question where tag like '%${tag}%'")
    List<Question> listByTag(@Param("tag") String tag);

    @Update("update question set title = #{title},description = #{description},tag = #{tag},gmt_modified = #{gmtModified} where id = #{id}")
    void update(Question question);

    @Select("select count(1) from question where tag = #{tag}")
    Integer countByQuestionTag(@Param("tag") String tag);

    @Select("select * from question where tag like '%${tag}%' limit #{offset},#{size}")
    List<Question> listByQuestionTag(@Param("tag")String tag, @Param(value = "offset")Integer offset,@Param(value = "size") Integer size);

    @Select("select * from question where tag = #{tag}")
    Question getByTag(@Param("tag")String tag);

    @Select("select * from question where creator = #{creator}")
    List<Question> listByCreator(@Param("creator") Integer creator);
}

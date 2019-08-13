package life.wpx1997.community.mapper;

import life.wpx1997.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(account_id,title,description,gmt_create,gmt_modified,creator,tag) values(#{account_id},#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    public void create(Question question);

    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where account_id = #{userAccountId} limit #{offset},#{size}")
    List<Question> listByUserAccountId(@Param("userAccountId") String userAccountId,@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    @Select("select count(1) from question where account_id = #{userAccountId}")
    Integer countByUserAccountId(@Param("userAccountId") String userAccountId);
}

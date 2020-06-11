package life.wpx1997.community.mapper;

import life.wpx1997.community.dto.QuestionQueryDTO;
import life.wpx1997.community.model.Question;
import java.util.List;

public interface QuestionExpandMapper {
    Integer cumulativeView(Question record);
    Integer cumulativeCommentCount(Question record);
    List<Question> selectByTag(Question question);

    Integer countByTag(QuestionQueryDTO questionQueryDTO);
    List<Question> selectByTagWithPage(QuestionQueryDTO questionQueryDTO);
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);
    List<Question> selectBySearchWithPage(QuestionQueryDTO questionQueryDTO);
}
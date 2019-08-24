package life.wpx1997.community.mapper;

import life.wpx1997.community.model.Comment;
import life.wpx1997.community.model.CommentExample;
import life.wpx1997.community.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExpandMapper {
    int cumulativeCommentCount(Comment record);
}
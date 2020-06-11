package life.wpx1997.community.mapper;

import life.wpx1997.community.model.Comment;

public interface CommentExpandMapper {
    int cumulativeCommentCount(Comment record);
}
package life.wpx1997.community.mapper;

import life.wpx1997.community.dto.LikeQueryDTO;
import life.wpx1997.community.model.CommunityLike;

import java.util.List;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/27 22:07
 * @Description CommunityLikeExpandMapper is
 */
public interface CommunityLikeExpandMapper {

    /**
     *
     * selectLikeByParentIdWithUserId by 根据基本信息查询点赞记录
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/27 22:12
     * @param communityLike
     * @return: CommunityLike
     */
    CommunityLike selectLikeByParentIdWithUserId(CommunityLike communityLike);

    /**
     *
     * selectCommentLikeListByParentIdList by
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/28 0:52
     * @param likeQueryDTO
     * @return: List<Long>
     */
    List<CommunityLike> selectCommentLikeListByParentIdList(LikeQueryDTO likeQueryDTO);
}

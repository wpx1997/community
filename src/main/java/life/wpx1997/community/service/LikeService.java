package life.wpx1997.community.service;

import life.wpx1997.community.cache.CumulativeCache;
import life.wpx1997.community.dto.LikeDTO;
import life.wpx1997.community.dto.LikeQueryDTO;
import life.wpx1997.community.enums.CommentTypeEnum;
import life.wpx1997.community.mapper.CommunityLikeExpandMapper;
import life.wpx1997.community.mapper.CommunityLikeMapper;
import life.wpx1997.community.model.CommunityLike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/27 21:56
 * @Description LikeService is
 */
@Service
public class LikeService {

    @Autowired
    private CommunityLikeExpandMapper communityLikeExpandMapper;

    @Autowired
    private CommunityLikeMapper communityLikeMapper;

    @Autowired
    private CumulativeCache cumulativeCache;

    /**
     *
     * selectLikeByParentIdWithUserId by 根据条件查询点赞记录
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/28 0:03
     * @param like
     * @return: CommunityLike
     */
    public CommunityLike selectLikeByParentIdWithUserId(CommunityLike like){

        CommunityLike communityLike = communityLikeExpandMapper.selectLikeByParentIdWithUserId(like);

        return communityLike;
    }

    /**
     *
     * createOrUpdateLikeByParentIdWithUserId by 根据是否有点赞记录进行新增或更新
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/27 22:29
     * @param likeDTO
     * @param userId
     * @return: void
     */
    public void createOrUpdateLikeByParentIdWithUserId(LikeDTO likeDTO, Long userId) {

        CommunityLike communityLike = new CommunityLike();
        communityLike.setParentId(likeDTO.getParentId());
        communityLike.setUserId(userId);
        communityLike.setType(likeDTO.getType());

        // 查询是否有点赞记录
        CommunityLike dbCommunityLike = selectLikeByParentIdWithUserId(communityLike);

        Long likeId;
        if (dbCommunityLike == null){
            communityLike.setIsDelete((byte) 0);
            communityLike.setGmtCreate(System.currentTimeMillis());
            communityLike.setGmtModified(System.currentTimeMillis());
            communityLikeMapper.insert(communityLike);
            likeId = communityLike.getId();
        }else {
            dbCommunityLike.setIsDelete((byte) 0);
            dbCommunityLike.setGmtModified(System.currentTimeMillis());
            communityLikeMapper.updateByPrimaryKeySelective(dbCommunityLike);
            likeId = dbCommunityLike.getId();
        }

        cumulativeLikeCount(likeId,likeDTO.getType(),1L);

    }

    /**
     *
     * deleteLikeByParentIdWithUserId by 删除点赞记录
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/28 0:00
     * @param likeDTO
     * @param userId
     * @return: Boolean
     */
    public Boolean deleteLikeByParentIdWithUserId(LikeDTO likeDTO, Long userId) {

        CommunityLike communityLike = new CommunityLike();
        communityLike.setParentId(likeDTO.getParentId());
        communityLike.setUserId(userId);
        communityLike.setType(likeDTO.getType());

        // 查询是否存在点赞记录
        CommunityLike dbCommunityLike = communityLikeExpandMapper.selectLikeByParentIdWithUserId(communityLike);
        if (dbCommunityLike == null){
            return false;
        }else {
            dbCommunityLike.setIsDelete((byte) 1);
            dbCommunityLike.setGmtModified(System.currentTimeMillis());
            communityLikeMapper.updateByPrimaryKeySelective(dbCommunityLike);
            cumulativeLikeCount(dbCommunityLike.getId(),likeDTO.getType(),-1L);
            return true;
        }

    }

    /**
     *
     * cumulativeLikeCount by 更新问题点赞数的缓存
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/27 23:48
     * @param id
     * @param type
     * @param likeCount
     * @return: void
     */
    public void cumulativeLikeCount(Long id,Byte type,Long likeCount){

        if (CommentTypeEnum.QUESTION.getType().equals(type)){
            cumulativeCache.cumulativeQuestionLikeCount(id,likeCount);
        }
        if (CommentTypeEnum.COMMENT.getType().equals(type)){
            cumulativeCache.cumulativeCommentLikeCount(id,likeCount);
        }

    }

    /**
     *
     * selectCommentLikeListByParentIdList by
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/28 0:50
     * @param likeQueryDTO
     * @return: List<CommunityLike>
     */
    public List<Long> selectCommentLikeListByParentIdList(LikeQueryDTO likeQueryDTO) {

        List<CommunityLike> communityLikeList = communityLikeExpandMapper.selectCommentLikeListByParentIdList(likeQueryDTO);
        List<Long> parentIdList = communityLikeList.stream().map(CommunityLike::getParentId).collect(Collectors.toList());

        return parentIdList;
    }
}

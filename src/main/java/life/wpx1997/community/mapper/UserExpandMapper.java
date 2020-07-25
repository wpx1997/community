package life.wpx1997.community.mapper;

import life.wpx1997.community.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/4 21:23
 * @Description UserExpandMapper is
 */
public interface UserExpandMapper {

    /**
     *
     * selectUserMessageDaoListById 根据用户id查询用户主要信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/4 21:24
     * @param userIdSet
     * @return: String
     */
    List<User> selectUserMessageModelListById(@Param("userIdSet")Set<Long> userIdSet);

    /**
     *
     * selectUserMessageByUserId 根据用户id查询用户主要信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/17 0:25
     * @param userId
     * @return: UserMessageDao
     */
    User selectUserMessageByUserId(Long userId);

}

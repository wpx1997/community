package life.wpx1997.community.service;

import life.wpx1997.community.mapper.UserExpandMapper;
import life.wpx1997.community.mapper.UserMapper;
import life.wpx1997.community.model.User;
import life.wpx1997.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author 不会飞的小鹏
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserExpandMapper userExpandMapper;

    /**
     *
     * createOrUpdate by 新增或更新用户
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 1:21
     * @param user
     * @return: void
     */
    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0){
//            新增用户
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
//            更新用户
            User dbUser = users.get(0);

            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }

    /**
     *
     * selectUserMessageListByCreatorSet by 根据set查询用户的基本信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 1:21
     * @param creatorSet
     * @return: List<User>
     */
    public List<User> selectUserMessageListByCreatorSet(Set<Long> creatorSet) {

        List<User> userList = userExpandMapper.selectUserMessageModelListById(creatorSet);
        return userList;
    }

    /**
     *
     * selectUseById by 查询用户信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 1:23
     * @param id
     * @return: User
     */
    public User selectUseById(Long id) {

        User user = userMapper.selectByPrimaryKey(id);

        return user;
    }

    /**
     *
     * selectUserMessageByUserId by 根据id查询用户基本信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/26 1:24
     * @param userId
     * @return: User
     */
    public User selectUserMessageByUserId(Long userId) {

        User user = userExpandMapper.selectUserMessageByUserId(userId);

        return user;
    }
}

package life.wpx1997.community.service;

import life.wpx1997.community.mapper.UserExpandMapper;
import life.wpx1997.community.mapper.UserMapper;
import life.wpx1997.community.model.User;
import life.wpx1997.community.model.UserExample;
import life.wpx1997.community.model.UserMessageModel;
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

    public List<UserMessageModel> selectUserMessageModelListByCreatorSet(Set<Long> creatorSet) {

        List<UserMessageModel> userMessageModelList = userExpandMapper.selectUserMessageModelListById(creatorSet);
        return userMessageModelList;
    }

    public User selectUseById(Long id) {

        User user = userMapper.selectByPrimaryKey(id);

        return user;
    }

    public UserMessageModel selectUserMessageDaoByUserId(Long userId) {

        UserMessageModel userMessageModel = userExpandMapper.selectUserMessageByUserId(userId);

        return userMessageModel;
    }
}

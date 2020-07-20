package life.wpx1997.community.cache;

import life.wpx1997.community.dto.MessageTagDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 小case
 * created on 2019/9/7 23:37
 */
public class MessageTagCache {
    public List<MessageTagDTO> cacheTags(Map<String,Long> tags){

        List<MessageTagDTO> userTags = new ArrayList<>();
        tags.forEach((name,priority) -> {
            MessageTagDTO messageTagDTO = new MessageTagDTO();
            messageTagDTO.setName(name);
            messageTagDTO.setPriority(priority);
            userTags.add(messageTagDTO);
        });

        return userTags;
    }
}

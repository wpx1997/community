package life.wpx1997.community.cache;

import life.wpx1997.community.dto.HotTagDTO;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

/**
 * created on 2019/9/1 23:08
 * @author 不会飞的小鹏
 */
@Component
public class HotTagCache {

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/8/7 13:21
     * @description: HotTagCache's 热门标签列表
     */
    private List<HotTagDTO> hotTagDTOList;

    /**
     *
     * updateTagList by 更新热门标签
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/7 13:21
     * @param tagMap
     * @return: void
     */
    public List<HotTagDTO> updateTagList(Map<String,Long> tagMap){

        int max = 7;

        List<HotTagDTO> hotTagDTOList = new ArrayList<>();
        tagMap.forEach((name,priority) -> {
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setName(name);
            hotTagDTO.setPriority(priority);
            hotTagDTOList.add(hotTagDTO);
        });

        List<HotTagDTO> priorities = hotTagDTOList.stream().sorted(Comparator.comparingLong(HotTagDTO::getPriority).reversed()).limit(max).collect(Collectors.toList());

        this.hotTagDTOList = priorities;

        return this.hotTagDTOList;
    }

    /**
     *
     * getHotTagDTOList by 获取热门标签
     *
     * @author: 不会飞的小鹏
     * @date: 2020/8/7 13:22
     * @param
     * @return: List<HotTagDTO>
     */
    public List<HotTagDTO> getHotTagDTOList(){
        return this.hotTagDTOList;
    }

}

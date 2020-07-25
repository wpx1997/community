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

    private List<HotTagDTO> hotTagDTOList;

    public void updateTagList(Map<String,Long> tagMap){

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
    }

    public List<HotTagDTO> getHotTagDTOList(){
        return this.hotTagDTOList;
    }

}

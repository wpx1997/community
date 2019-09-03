package life.wpx1997.community.cache;

import life.wpx1997.community.dto.HotTagDTO;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * created by 小case on 2019/9/1 23:08
 */
@Component
@Data
public class HotTagCache {
    private List<HotTagDTO> hots = new ArrayList<>();

    public void updateTags(Map<String,Integer> tags){
        int max = 7;
        PriorityQueue<HotTagDTO> priorityQueue =new PriorityQueue<>(max);
        tags.forEach((name,priority) -> {
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setName(name);
            hotTagDTO.setPriority(priority);
            if (priorityQueue.size() < max){
                priorityQueue.add(hotTagDTO);
            }else {
                HotTagDTO minHot = priorityQueue.peek();
                if (hotTagDTO.compareTo(minHot) > 0){
                    priorityQueue.poll();
                    priorityQueue.add(hotTagDTO);
                }
            }
        });

        List<HotTagDTO> sortedTags = new ArrayList<>();

        HotTagDTO poll = priorityQueue.poll();
        while (poll != null){
            sortedTags.add(0,poll);
            poll = priorityQueue.poll();
        }
        hots = sortedTags;
    }
}

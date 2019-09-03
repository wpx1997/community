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
    Map<String,Integer> tags  = new HashMap<>();
    private List<String> hots = new ArrayList<>();

    public void updateTags(Map<String,Integer> tags){
        int max = 5;
        PriorityQueue<HotTagDTO> priorityQueue =new PriorityQueue<>(max);
        tags.forEach((name,priority) -> {
            HotTagDTO hotTagDTO = new HotTagDTO();
            hotTagDTO.setName(name);
            hotTagDTO.setPriority(priority);
            if (priorityQueue.size() < max){
                priorityQueue.add(hotTagDTO);
            }else {
                HotTagDTO minHot = priorityQueue.peek();
                if (minHot.compareTo(minHot) > 0){
                    priorityQueue.poll();
                    priorityQueue.add(hotTagDTO);
                }
            }
        });

        List<String> sortedTags = new ArrayList<>();

        HotTagDTO poll = priorityQueue.poll();
        while (poll != null){
            sortedTags.add(0,poll.getName());
            poll = priorityQueue.poll();
        }
        hots = sortedTags;
    }
}
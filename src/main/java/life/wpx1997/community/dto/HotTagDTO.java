package life.wpx1997.community.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * created by 小case on 2019/9/2 12:32
 */
@Data
public class HotTagDTO implements Comparable{
   private String name;
   private Integer priority;

    @Override
    public int compareTo(Object o) {
        return this.getPriority() - ((HotTagDTO) o).getPriority();
    }
}

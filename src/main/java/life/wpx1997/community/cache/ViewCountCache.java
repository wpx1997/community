package life.wpx1997.community.cache;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/25 23:46
 * @Description ViewCountCache is
 */
@Component
public class ViewCountCache {

    private Map<Long,Long> viewCountMap = new HashMap<>();

    public void cumulativeViewCount(Long id){

        Long questionViewCount = this.viewCountMap.get(id);
        if (questionViewCount == null){
            this.viewCountMap.put(id,1L);
        }else {
            this.viewCountMap.put(id,questionViewCount + 1L);
        }

    }

    public Long getViewCountCacheById(Long id){
        Long viewCount = this.viewCountMap.get(id);
        return viewCount;
    }

    public Map<Long,Long> getViewCountMap(){
        return this.viewCountMap;
    }

    public void clearViewCountMap(){
        this.viewCountMap.clear();
    }

}

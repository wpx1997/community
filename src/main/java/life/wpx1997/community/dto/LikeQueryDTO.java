package life.wpx1997.community.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/28 0:40
 * @Description LikeQueryDTO is
 */
@Data
public class LikeQueryDTO {
    private Long userId;
    private List<Long> parentIdList;
}

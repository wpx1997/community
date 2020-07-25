package life.wpx1997.community.dto;

import lombok.Data;

import java.util.List;

/**
 * created on 2019/8/25 21:58
 * @author 不会飞的小鹏
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}

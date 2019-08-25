package life.wpx1997.community.dto;

import lombok.Data;

import java.util.List;

/**
 * created by 小case on 2019/8/25 21:58
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}

package life.wpx1997.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 不会飞的小鹏
 */
@Data
public class PaginationDTO<T> {

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/16 19:49
     * @description: PaginationWebDTO's 分页的条数
     */
    private static final Integer SIZE = 10;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/16 19:49
     * @description: PaginationWebDTO's 分页的内容
     */
    private List<T> data;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/16 19:50
     * @description: PaginationWebDTO's 当前页码
     */
    private Integer page;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/16 19:50
     * @description: PaginationWebDTO's 总页码
     */
    private Integer totalPage;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/20 22:36
     * @description: PaginationDTO's 是否展示跳转第一页
     */
    private Boolean showFirstPage;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/20 22:35
     * @description: PaginationDTO's 是否展示跳转上一页
     */
    private Boolean showPrevious;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/20 22:37
     * @description: PaginationDTO's 是否展示跳转下一页
     */
    private Boolean showNext;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/20 22:37
     * @description: PaginationDTO's 是否展示跳转最后一页
     */
    private Boolean showEndPage;

    /**
     * @author: 不会飞的小鹏
     * @date: 2020/7/20 22:38
     * @description: PaginationDTO's 页码列表
     */
    List<Integer> pageList = new ArrayList<>();

    /**
     * setPagination by 对页码总数和当前页码进行处理
     *
     * @param totalCount
     * @param page
     * @author: 不会飞的小鹏
     * @date: 2020/7/16 19:48
     * @return: void
     */
    public Integer setPagination(Integer totalCount, Integer page) {

        Integer showPage = 3;

        if (totalCount % SIZE == 0) {
            this.totalPage = totalCount / SIZE;
        } else {
            this.totalPage = totalCount / SIZE + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        this.page = page;

        pageList.add(page);
        for (int i = 1; i <= showPage; i++) {
            if (page - i > 0) {
                pageList.add(0, page - i);
            }
            if (page + i <= totalPage) {
                pageList.add(page + i);
            }
        }

        // 是否展示上一页
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        // 是否展示下一页
        if (page.equals(totalPage)) {
            showNext = false;
        } else {
            showNext = true;
        }

        // 是否展示第一页
        if (pageList.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

//        是否展示最后一页
        if (pageList.contains(totalPage)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }

        Integer offset = SIZE * (page - 1);

        return offset;
    }

}

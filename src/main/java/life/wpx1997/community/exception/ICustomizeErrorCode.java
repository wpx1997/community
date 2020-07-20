package life.wpx1997.community.exception;

/**
 * @author 不会飞的小鹏
 */
public interface ICustomizeErrorCode {

    /**
     *
     * getCode by 获取错误码
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/5 17:31
     * @param
     * @return: Integer
     */
    Integer getCode();

    /**
     *
     * getMessage by 获取错误信息
     *
     * @author: 不会飞的小鹏
     * @date: 2020/7/5 17:32
     * @param
     * @return: String
     */
    String getMessage();
}

package life.wpx1997.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 不会飞的小鹏
 * create on 2020/8/1 19:10
 * @Description TestController is
 */
@Controller
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "test";
    }

}

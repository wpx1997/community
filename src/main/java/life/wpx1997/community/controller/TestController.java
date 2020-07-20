package life.wpx1997.community.controller;

import life.wpx1997.community.dto.ResultDTO;
import life.wpx1997.community.model.User;
import life.wpx1997.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 不会飞的小鹏
 * create on 2020/7/8 14:24
 * @Description TestController is
 */
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/web")
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUser/{id}")
    public Object getUserById(@PathVariable(name = "id") Long id){

        User user = userService.selectUseById(id);

        return ResultDTO.okOf(user);
    }

}

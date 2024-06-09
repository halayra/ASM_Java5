package org.phamxuantruong.asm2.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class testController {
    @RequestMapping("cart")
    public String test() {
        return "home/detail";
    }
}

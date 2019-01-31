package cn.jiiiiiin.manager.controller;

import cn.jiiiiiin.security.core.properties.SecurityProperties;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author jiiiiiin
 */
@Controller
public class CommonController {

    @Autowired
    private SecurityProperties securityProperties;

    @GetMapping({"/", "/index"})
    public String index(Model model){
        model.addAttribute("fontUrl", securityProperties.getBrowser().getFontUrl());
        return "index";
    }
}

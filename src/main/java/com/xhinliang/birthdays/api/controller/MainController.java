package com.xhinliang.birthdays.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller // Don't use RestController as this method is mapping to a static file not a JSON
public class MainController {

    @RequestMapping(value = {"/"})
    public String index() {
        return "index.html";
    }
}

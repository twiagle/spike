package com.twigle.spike.controller;

import com.twigle.spike.model.SpikeUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @RequestMapping("/to_list")
    public String list(Model model, SpikeUser user) {
        model.addAttribute("user", user);

        return "goods_list";
    }

}

package com.mastermind.masterminddemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UIController {

    @GetMapping("/")
    public String toString() {
        return "UIController []";
    }

}


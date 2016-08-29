package se.jaitco.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.jaitco.model.TestResponse;

@RestController
public class TestController {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public TestResponse test() {
        return TestResponse.builder()
                .info("testar")
                .build();
    }
}
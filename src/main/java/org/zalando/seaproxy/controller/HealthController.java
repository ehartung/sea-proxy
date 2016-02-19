package org.zalando.seaproxy.controller;

import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.zalando.seaproxy.domain.Health;

@RestController
@RequestMapping("/status.info")
@EnableOAuth2Resource
public class HealthController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Health health() {
        final Health health = new Health();
        health.setStatus("OK");

        return health;
    }

}

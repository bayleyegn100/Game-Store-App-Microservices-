package com.yedebkid.gamestoreinvoicing.util.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.Console;
import java.util.List;

@FeignClient(name= "console")
public interface ConsoleMagicClient {
    @RequestMapping(value = "/console", method = RequestMethod.GET)
    public List<Console> getAllConsoles();
}

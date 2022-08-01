package com.yedebkid.gamestoreinvoicing.util.feign;

import com.yedebkid.gamestoreinvoicing.model.Console;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import java.util.List;
import java.util.Optional;

@FeignClient(name= "gamestore-catalog")
public interface ConsoleFeignClient {
    @RequestMapping(value = "/console", method = RequestMethod.GET)
    public List<Console> getAllConsoles();

    @RequestMapping(value = "/console/{id}", method = RequestMethod.PUT)
    public void updateConsoleById(@PathVariable long id, @RequestBody Console console);

    @RequestMapping(value = "/console/{id}", method = RequestMethod.GET)
    public Optional<Console> getConsoleById(@PathVariable long id);

    @RequestMapping(value = "/console}", method = RequestMethod.POST)
    public Console addConsole(@RequestBody Console console);
}

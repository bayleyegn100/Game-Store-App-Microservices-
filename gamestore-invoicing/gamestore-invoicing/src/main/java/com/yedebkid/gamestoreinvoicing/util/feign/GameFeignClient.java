package com.yedebkid.gamestoreinvoicing.util.feign;

import com.yedebkid.gamestoreinvoicing.model.Game;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

@FeignClient(name="gamestore-catalog")
public interface GameFeignClient {
    @RequestMapping(value="/game", method = RequestMethod.GET)
    public List<Game> getAllGames();
    @RequestMapping(value = "/game/{id}", method = RequestMethod.PUT)
    public void updateGameById(@PathVariable long id, @RequestBody Game game);

    @RequestMapping(value = "/game/{id}", method = RequestMethod.GET)
    public Optional<Game> getGameById(@PathVariable long id);

    @RequestMapping(value = "/game}", method = RequestMethod.POST)
    public Game addGame(@RequestBody Game game);
}


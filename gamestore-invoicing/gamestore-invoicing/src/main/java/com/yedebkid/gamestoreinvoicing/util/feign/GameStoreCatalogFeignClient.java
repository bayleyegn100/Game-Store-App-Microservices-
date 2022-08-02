package com.yedebkid.gamestoreinvoicing.util.feign;

import com.yedebkid.gamestoreinvoicing.model.ConsoleResponseModel;
import com.yedebkid.gamestoreinvoicing.model.GameResponseModel;
import com.yedebkid.gamestoreinvoicing.model.TShirtResponseModal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name="gamestore-catalog")
public interface GameStoreCatalogFeignClient {
//Get by id
    @RequestMapping(value = "/tshirt/{id}", method = RequestMethod.GET)
    public TShirtResponseModal getTShirtById(@PathVariable("id") long id);

    @RequestMapping(value = "/game/{id}", method = RequestMethod.GET)
    public GameResponseModel getGameById(@PathVariable("id") long id);

    @RequestMapping(value = "/console/{id}", method = RequestMethod.GET)
    public ConsoleResponseModel getConsoleById(@PathVariable("id") long id);
    //    Get all
    @RequestMapping(value="/tshirt", method = RequestMethod.GET)
    public List<TShirtResponseModal> getAllTShirt();

    @RequestMapping(value="/game", method = RequestMethod.GET)
    public List<GameResponseModel> getAllGames();

    @RequestMapping(value = "/console", method = RequestMethod.GET)
    public List<ConsoleResponseModel> getAllConsoles();
//update
    @RequestMapping(value = "/tshirt/{id}", method = RequestMethod.PUT)
    public void updateTShirtById(@RequestBody TShirtResponseModal tshirt);

    @RequestMapping(value = "/console/{id}", method = RequestMethod.PUT)
    public void updateConsoleById(@RequestBody ConsoleResponseModel console);

    @RequestMapping(value = "/game/{id}", method = RequestMethod.PUT)
    public void updateGameById(@RequestBody GameResponseModel game);
//    Add
    @RequestMapping(value = "/tshirt}", method = RequestMethod.POST)
    public TShirtResponseModal addTShirt(@RequestBody TShirtResponseModal tshirt);

    @RequestMapping(value = "/console}", method = RequestMethod.POST)
    public ConsoleResponseModel addConsole(@RequestBody ConsoleResponseModel console);

    @RequestMapping(value = "/game}", method = RequestMethod.POST)
    public GameResponseModel addGame(@RequestBody GameResponseModel game);

}

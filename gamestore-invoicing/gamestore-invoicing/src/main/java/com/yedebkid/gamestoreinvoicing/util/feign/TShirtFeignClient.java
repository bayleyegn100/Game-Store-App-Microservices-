package com.yedebkid.gamestoreinvoicing.util.feign;

import com.yedebkid.gamestoreinvoicing.model.TShirt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

@FeignClient(name="gamestore-catalog")
public interface TShirtFeignClient {
    @RequestMapping(value="/tshirt", method = RequestMethod.GET)
    public List<TShirt> getAllTShirt();
    @RequestMapping(value = "/tshirt/{id}", method = RequestMethod.PUT)
    public void updateTShirtById(@PathVariable long id, @RequestBody TShirt tshirt);

    @RequestMapping(value = "/tshirt/{id}", method = RequestMethod.GET)
    public Optional<TShirt> getTShirtById(@PathVariable long id);

    @RequestMapping(value = "/tshirt}", method = RequestMethod.POST)
    public TShirt addTShirt(@RequestBody TShirt tshirt);
}

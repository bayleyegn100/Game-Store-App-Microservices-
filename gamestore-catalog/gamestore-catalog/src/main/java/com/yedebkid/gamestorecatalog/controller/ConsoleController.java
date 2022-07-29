package com.yedebkid.gamestorecatalog.controller;

import com.yedebkid.gamestorecatalog.model.Console;
import com.yedebkid.gamestorecatalog.rpository.ConsoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/console")
//@CrossOrigin(origins = {"http://localhost:3000"})
@RefreshScope
public class ConsoleController {

    @Autowired
    ConsoleRepository consoleRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Console createConsole(@RequestBody Console console) {
        console = consoleRepository.save(console);
        return console;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Console> getConsole(@PathVariable("id") long consoleId) {
        Optional<Console> console = consoleRepository.findById(consoleId);
        if (console == null) {
            throw new IllegalArgumentException("Console could not be retrieved for id " + consoleId);
        } else {
            return console;
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateConsole(@RequestBody Console console) {

        if (console==null || console.getId()< 1) {
            throw new IllegalArgumentException("Id in path must match id in view model");
        } else if (console.getId() > 0) {
            consoleRepository.findById(console.getId());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConsole(@PathVariable("id") long consoleId) {
        consoleRepository.deleteById(consoleId);
    }

    @GetMapping("/manufacturer/{manufacturer}")
    @ResponseStatus(HttpStatus.OK)
    public List<Console> getConsoleByManufacturer(@PathVariable("manufacturer") String manu) {
        List<Console> cvmByManufacturer = consoleRepository.findAllByManufacturer(manu);
        if (cvmByManufacturer == null || cvmByManufacturer.isEmpty()) {
            throw new IllegalArgumentException("No consoles, manufactured by " + manu + ", were found");
        } else
            return cvmByManufacturer;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Console> getAllConsoles() {
        List<Console> cvmByManufacturer = consoleRepository.findAll();
        if (cvmByManufacturer == null || cvmByManufacturer.isEmpty()) {
            throw new IllegalArgumentException("No consoles were found");
        } else
            return cvmByManufacturer;
    }
}

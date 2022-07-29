package com.yedebkid.gamestoreinvoicing.controller;

import com.yedebkid.gamestoreinvoicing.util.feign.ConsoleMagicClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Console;
import java.util.List;

@RestController
@RefreshScope
public class InvoiceController {
    @Autowired
    private ConsoleMagicClient consoleMagicClient;

    public InvoiceController(ConsoleMagicClient consoleMagicClient) {
        this.consoleMagicClient = consoleMagicClient;
    }

    @GetMapping(value="/invoice")
    public String getAllInvoice(){
        return "To Do list";
    }

    @GetMapping(value= "/console")
    public List<Console> getAllConsole(){
        return consoleMagicClient.getAllConsoles();
    }
}

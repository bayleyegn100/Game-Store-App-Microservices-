package com.yedebkid.gamestoreinvoicing.controller;

import com.yedebkid.gamestoreinvoicing.model.Invoice;
import com.yedebkid.gamestoreinvoicing.serviceLayer.InvoiceServiceLayer;
import com.yedebkid.gamestoreinvoicing.util.feign.GameStoreCatalogFeignClient;
import com.yedebkid.gamestoreinvoicing.viewModel.InvoiceViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/invoice")
@CrossOrigin(origins = {"http://localhost:3000"})
@RefreshScope
public class InvoiceController {
    @Autowired
    private InvoiceServiceLayer invoiceServiceLayer;

    @GetMapping("name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> getInvoicesByCustomerName(@PathVariable String name){
        return invoiceServiceLayer.getInvoicesByCustomerName(name);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> getAllInvoices() {
        return invoiceServiceLayer.getAllInvoices();
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Invoice getInvoice(@PathVariable  long id){
        return invoiceServiceLayer.getInvoice(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Invoice createInvoice(@RequestBody Invoice invoice){
        return invoiceServiceLayer.createInvoice(invoice);
    }

//    @PostMapping("/purchaseInvoices")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Invoice createInvoice(@RequestBody  Invoice invoice) {
//        return invoiceServiceLayer.createInvoice(invoice);
//    }

}

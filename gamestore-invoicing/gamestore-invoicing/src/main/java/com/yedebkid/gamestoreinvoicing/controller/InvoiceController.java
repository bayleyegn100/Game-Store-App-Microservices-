package com.yedebkid.gamestoreinvoicing.controller;

import com.yedebkid.gamestoreinvoicing.model.Invoice;
import com.yedebkid.gamestoreinvoicing.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/invoice")
//@CrossOrigin(origins = {"http://localhost:3000"})
@RefreshScope
public class InvoiceController {

    @Autowired
    InvoiceRepository invoiceRepository;

    // Assumption: All orders are final and data privacy is not top priority. Therefore, the Update & Delete EndPoints
    // are left out by design due to its potential danger. The getAllInvoices is a questionable one since it could
    // overwhelm the system and infringes on data privacy; however, it does not damage data as with the Update and Delete

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Invoice purchaseItem(@RequestBody @Valid Invoice invoice) {
        invoice = invoiceRepository.save(invoice);
        return invoice;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Invoice> findInvoice(@PathVariable("id") long invoiceId) {
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice could not be retrieved for id " + invoiceId);
        } else {
            return invoice;
        }
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> findAllInvoices() {
        List<Invoice> invoiceList = invoiceRepository.findAll();

        if (invoiceList == null || invoiceList.isEmpty()) {
            throw new IllegalArgumentException("No invoices were found.");
        } else {
            return invoiceList;
        }
    }

    @GetMapping("/cname/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> findInvoicesByCustomerName(@PathVariable String name) {
        List<Invoice> invoiceList = invoiceRepository.findByName(name);

        if (invoiceList == null || invoiceList.isEmpty()) {
            throw new IllegalArgumentException("No invoices were found for: "+name);
        } else {
            return invoiceList;
        }
    }
//    @Autowired
//    private ConsoleMagicClient consoleMagicClient;
//
//    public InvoiceController(ConsoleMagicClient consoleMagicClient) {
//        this.consoleMagicClient = consoleMagicClient;
//    }
//
//    @GetMapping(value="/invoice")
//    public String getAllInvoice(){
//        return "To Do list";
//    }
//
//    @GetMapping(value= "/console")
//    public List<Console> getAllConsole(){
//        return consoleMagicClient.getAllConsoles();
//    }
}

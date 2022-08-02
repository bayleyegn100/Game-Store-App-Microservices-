package com.yedebkid.gamestoreinvoicing.controller;

import com.yedebkid.gamestoreinvoicing.model.Invoice;
import com.yedebkid.gamestoreinvoicing.serviceLayer.InvoiceServiceLayer;
import com.yedebkid.gamestoreinvoicing.util.feign.GameStoreCatalogFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@RestController
@RequestMapping(value = "/invoice")
@CrossOrigin(origins = {"http://localhost:3000"})
@RefreshScope
public class InvoiceController {
    private InvoiceServiceLayer invoiceServiceLayer;
    private GameStoreCatalogFeignClient gameStoreCatalogFeignClient;

    @Autowired
    public InvoiceController(InvoiceServiceLayer invoiceServiceLayer, GameStoreCatalogFeignClient gameStoreCatalogFeignClient) {
        this.invoiceServiceLayer = invoiceServiceLayer;
        this.gameStoreCatalogFeignClient = gameStoreCatalogFeignClient;
    }
    @GetMapping("/invoice")
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoiceList = invoiceServiceLayer.getInvoices();

        if (invoiceList == null || invoiceList.isEmpty()) {
            throw new IllegalArgumentException("No invoices were found.");
        } else {
            return invoiceList;
        }

    }
    @GetMapping("/invoice/{id}")
    public Invoice findInvoice(@PathVariable("id") long invoiceId) {
        Invoice invoice = invoiceServiceLayer.getInvoicesById(invoiceId);
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice could not be retrieved for id " + invoiceId);
        } else {
            return invoice;
        }
    }
    @GetMapping("/invoice/cname/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> findInvoicesByCustomerName(@PathVariable("name") String name) {
        List<Invoice> invoiceList = invoiceServiceLayer.getInvoicesByCustomerName(name);

        if (invoiceList == null || invoiceList.isEmpty()) {
            throw new IllegalArgumentException("No invoices were found for: " + name);
        } else {
            return invoiceList;
        }
    }
    @PostMapping("/invoice")
    @ResponseStatus(HttpStatus.CREATED)
    public Invoice purchaseItem(@RequestBody @Valid Invoice invoice) {

        if (invoiceServiceLayer.createInvoice(invoice) == null) {
            throw new RuntimeException("Invoice creation failed");
        } else return invoice;
    }
    @DeleteMapping("/invoice/{id}")
    public void deleteInvoice(@RequestBody Invoice invoice) {
        invoiceServiceLayer.deleteInvoice(invoice);
    }
}

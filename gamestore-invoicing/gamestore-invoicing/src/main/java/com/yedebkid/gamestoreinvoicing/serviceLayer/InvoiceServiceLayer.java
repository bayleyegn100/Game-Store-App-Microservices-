package com.yedebkid.gamestoreinvoicing.serviceLayer;

import com.yedebkid.gamestoreinvoicing.model.*;
import com.yedebkid.gamestoreinvoicing.repository.InvoiceRepository;
import com.yedebkid.gamestoreinvoicing.repository.ProcessingFeeRepository;
import com.yedebkid.gamestoreinvoicing.repository.TaxRepository;
import com.yedebkid.gamestoreinvoicing.util.feign.GameStoreCatalogFeignClient;
import com.yedebkid.gamestoreinvoicing.viewModel.InvoiceViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InvoiceServiceLayer {

    private final BigDecimal PROCESSING_FEE = new BigDecimal("15.49");
    private final BigDecimal MAX_INVOICE_TOTAL = new BigDecimal("999.99");
    private final String GAME_ITEM_TYPE = "Game";
    private final String CONSOLE_ITEM_TYPE = "Console";
    private final String TSHIRT_ITEM_TYPE = "T-Shirt";

    private GameStoreCatalogFeignClient gameStoreCatalogFeignClient;
    private InvoiceRepository invoiceRepository;
    private TaxRepository taxRepository;
    private ProcessingFeeRepository processingFeeRepository;

    @Autowired
    public InvoiceServiceLayer(GameStoreCatalogFeignClient gameStoreCatalogFeignClient, InvoiceRepository invoiceRepository, TaxRepository taxRepository, ProcessingFeeRepository processingFeeRepository) {
        this.gameStoreCatalogFeignClient = gameStoreCatalogFeignClient;
        this.invoiceRepository = invoiceRepository;
        this.taxRepository = taxRepository;
        this.processingFeeRepository = processingFeeRepository;
    }
    public List<Invoice> getInvoices() {

        List<Invoice> invoiceList = invoiceRepository.findAll();

        if (invoiceList == null || invoiceList.isEmpty()) {
            throw new IllegalArgumentException("No invoices were found.");
        } else {
            return invoiceList;
        }
    }

    public Invoice getInvoicesById(Long id) {
        return invoiceRepository.findById(id).orElse(null); }

    public List<Invoice> getInvoicesByCustomerName(String customerName) {

        List<Invoice> invoiceList = invoiceRepository.findByName(customerName);

        if (invoiceList == null || invoiceList.isEmpty()) {
            throw new IllegalArgumentException("No invoices were found.");
        } else {
            return invoiceList;
        } }
    public void deleteInvoice(Invoice invoice) {
        invoiceRepository.deleteById(invoice.getId());   }

    public Invoice createInvoice(Invoice invoice) {


        //validation...
        if (invoice==null)
            throw new NullPointerException("Create invoice failed. no invoice data.");

        if(invoice.getItemType()==null)
            throw new IllegalArgumentException("Unrecognized Item type. Valid ones: Console or Game or T-Shirt");

        //Check Quantity is > 0...
        if(invoice.getQuantity()<=0){
            throw new IllegalArgumentException(invoice.getQuantity() +
                    ": Unrecognized Quantity. Must be > 0.");
        }

        //Checks the item type and get the correct unit price
        //Check if we have enough quantity
        if (invoice.getItemType().equals(CONSOLE_ITEM_TYPE)) {

            ConsoleResponseModel tempCon = null;
//            wrapped using ava.util.Optional
            Optional<ConsoleResponseModel> returnVal = Optional.ofNullable(gameStoreCatalogFeignClient.getConsoleById(invoice.getItemId()));

            if (returnVal.isPresent()) {
                tempCon = returnVal.get();
            } else {
                throw new IllegalArgumentException("Requested console is not there");
            }

            if (invoice.getQuantity() > tempCon.getQuantity()) {
                throw new IllegalArgumentException("Requested console quantity is more than in stock.");
            }

            invoice.setUnitPrice(tempCon.getPrice());


        } else if (invoice.getItemType().equals(GAME_ITEM_TYPE)) {

            GameResponseModel tempGame = null;
            //            wrapped using ava.util.Optional
            Optional<GameResponseModel> returnVal = Optional.ofNullable(gameStoreCatalogFeignClient.getGameById(invoice.getItemId()));

            if (returnVal.isPresent()) {
                tempGame = returnVal.get();
            } else {
                throw new IllegalArgumentException("Requested game is unavailable.");
            }

            if (invoice.getQuantity() > tempGame.getQuantity()) {
                throw new IllegalArgumentException("Requested game quantity is unavailable.");
            }
            invoice.setUnitPrice(tempGame.getPrice());

        } else if (invoice.getItemType().equals(TSHIRT_ITEM_TYPE)) {
            TShirtResponseModal tempTShirt = null;
            //            wrapped using ava.util.Optional
            Optional<TShirtResponseModal> returnVal = Optional.ofNullable(gameStoreCatalogFeignClient.getTShirtById(invoice.getItemId()));

            if (returnVal.isPresent()) {
                tempTShirt = returnVal.get();
            } else {
                throw new IllegalArgumentException("Requested shirt is unavailable.");
            }

            if (invoice.getQuantity() > tempTShirt.getQuantity()) {
                throw new IllegalArgumentException("Requested shirt quantity is unavailable.");
            }
            invoice.setUnitPrice(tempTShirt.getPrice());

        }

        else  throw new IllegalArgumentException(invoice.getItemType() +
                    ": Unrecognized Item type. Valid ones: T-Shirt, Console, or Game");


        invoice.setQuantity(invoice.getQuantity());

        invoice.setSubtotal(
                invoice.getUnitPrice().multiply(
                        new BigDecimal(invoice.getQuantity())).setScale(2, RoundingMode.HALF_UP));

        if ((invoice.getSubtotal().compareTo(new BigDecimal(999.99)) > 0)) {
            throw new IllegalArgumentException("Subtotal exceeds maximum purchase price of $999.99");
        }

        //Validate State and Calc tax...
        BigDecimal tempTaxRate;
        Optional<Tax> returnVal = taxRepository.findById(invoice.getState());

        if (returnVal.isPresent()) {
            tempTaxRate = returnVal.get().getRate();
        } else {
            throw new IllegalArgumentException(invoice.getState() + ": Invalid State code.");
        }

        if (!tempTaxRate.equals(BigDecimal.ZERO))
            invoice.setTax(tempTaxRate.multiply(invoice.getSubtotal()));
        else
            throw new IllegalArgumentException( invoice.getState() + ": Invalid State code.");

        BigDecimal processingFee;
        Optional<ProcessingFee> returnVal2 = processingFeeRepository.findById(invoice.getItemType());

        if (returnVal2.isPresent()) {
            processingFee = returnVal2.get().getFee();
        } else {
            throw new IllegalArgumentException("Processing Fee not found for this item type.");
        }

        invoice.setProcessingFee(processingFee);

        if (invoice.getQuantity() > 10) {
            invoice.setProcessingFee(invoice.getProcessingFee().add(PROCESSING_FEE));
        }

        invoice.setTotal(invoice.getSubtotal().add(invoice.getProcessingFee()).add(invoice.getTax()));

        if ((invoice.getTotal().compareTo(MAX_INVOICE_TOTAL) > 0)) {
            throw new IllegalArgumentException("Subtotal exceeds maximum purchase price of $999.99");
        }

        return  invoiceRepository.save(invoice);

    }
//    public void seedFees() {
//        processingFeeRepository.save(new ProcessingFee("Games", 1.49));
//        processingFeeRepository.save(new ProcessingFee("Consoles", 14.99));
//        processingFeeRepository.save(new ProcessingFee("TShirts", 1.99));
//    }
//
//    public void seedTaxes() {
//        taxRepository.save(new Tax("AL", 0.05));
//        taxRepository.save(new Tax("AK", 0.06));
//        taxRepository.save(new Tax("AZ", 0.04));
//        taxRepository.save(new Tax("AR", 0.06));
//        taxRepository.save(new Tax("CA", 0.06));
//        taxRepository.save(new Tax("CO", 0.04));
//        taxRepository.save(new Tax("CT", 0.03));
//        taxRepository.save(new Tax("DE", 0.05));
//        taxRepository.save(new Tax("FL", 0.06));
//        taxRepository.save(new Tax("GA", 0.07));
//        taxRepository.save(new Tax("HI", 0.05));
//        taxRepository.save(new Tax("ID", 0.03));
//        taxRepository.save(new Tax("IL", 0.05));
//        taxRepository.save(new Tax("IN", 0.05));
//        taxRepository.save(new Tax("IA", 0.04));
//        taxRepository.save(new Tax("KS", 0.06));
//        taxRepository.save(new Tax("KY", 0.04));
//        taxRepository.save(new Tax("LA", 0.05));
//        taxRepository.save(new Tax("ME", 0.03));
//        taxRepository.save(new Tax("MD", 0.07));
//        taxRepository.save(new Tax("MA", 0.05));
//        taxRepository.save(new Tax("MI", 0.06));
//        taxRepository.save(new Tax("MN", 0.06));
//        taxRepository.save(new Tax("MS", 0.05));
//        taxRepository.save(new Tax("MO", 0.05));
//        taxRepository.save(new Tax("MT", 0.03));
//        taxRepository.save(new Tax("NE", 0.04));
//        taxRepository.save(new Tax("NV", 0.04));
//        taxRepository.save(new Tax("NH", 0.06));
//        taxRepository.save(new Tax("NJ", 0.05));
//        taxRepository.save(new Tax("NM", 0.05));
//        taxRepository.save(new Tax("NY", 0.06));
//        taxRepository.save(new Tax("NC", 0.05));
//        taxRepository.save(new Tax("ND", 0.05));
//        taxRepository.save(new Tax("OH", 0.04));
//        taxRepository.save(new Tax("OK", 0.04));
//        taxRepository.save(new Tax("OR", 0.07));
//        taxRepository.save(new Tax("PA", 0.06));
//        taxRepository.save(new Tax("RI", 0.06));
//        taxRepository.save(new Tax("SC", 0.06));
//        taxRepository.save(new Tax("SD", 0.06));
//        taxRepository.save(new Tax("TN", 0.05));
//        taxRepository.save(new Tax("TX", 0.03));
//        taxRepository.save(new Tax("UT", 0.04));
//        taxRepository.save(new Tax("VT", 0.07));
//        taxRepository.save(new Tax("VA", 0.06));
//        taxRepository.save(new Tax("WA", 0.05));
//        taxRepository.save(new Tax("WV", 0.05));
//        taxRepository.save(new Tax("WI", 0.03));
//        taxRepository.save(new Tax("WY", 0.04));
//    }



}

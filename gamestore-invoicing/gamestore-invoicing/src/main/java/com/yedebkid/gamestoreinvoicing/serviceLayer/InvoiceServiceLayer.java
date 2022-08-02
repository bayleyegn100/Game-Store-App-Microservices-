package com.yedebkid.gamestoreinvoicing.serviceLayer;

import com.yedebkid.gamestoreinvoicing.model.*;
import com.yedebkid.gamestoreinvoicing.repository.InvoiceRepository;
import com.yedebkid.gamestoreinvoicing.repository.ProcessingFeeRepository;
import com.yedebkid.gamestoreinvoicing.repository.TaxRepository;
import com.yedebkid.gamestoreinvoicing.util.feign.GameStoreCatalogFeignClient;
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

    public Invoice createInvoice(Invoice input) {

        //validation...
        if (input == null)
            throw new NullPointerException("Create input failed. no invoice data.");

        if (input.getItemType() == null)
            throw new IllegalArgumentException("Unrecognized Item type. Valid ones: Console or Game");

        //Check Quantity is > 0...
        if (input.getQuantity() <= 0) {
            throw new IllegalArgumentException(input.getQuantity() +
                    ": Unrecognized Quantity. Must be > 0.");
        }
        //start building invoice...
        Invoice invoice = new Invoice();
        invoice.setName(input.getName());
        invoice.setStreet(input.getStreet());
        invoice.setCity(input.getCity());
        invoice.setState(input.getState());
        invoice.setZipcode(input.getZipcode());
        invoice.setItemType(input.getItemType());
        invoice.setItemId(input.getItemId());
        invoice.setUnitPrice(input.getUnitPrice());
        invoice.setQuantity(input.getQuantity());
        invoice.setSubtotal(input.getSubtotal());
        invoice.setProcessingFee(input.getProcessingFee());
        invoice.setTax(input.getTax());
        invoice.setProcessingFee(input.getProcessingFee());
        invoice.setTotal(input.getTotal());


        //Checks the item type and get the correct unit price
        //Check if we have enough quantity
        if (invoice.getItemType().equals(CONSOLE_ITEM_TYPE)) {
            ConsoleResponseModel tempCon = null;
            Optional<ConsoleResponseModel> returnVal = Optional.ofNullable(gameStoreCatalogFeignClient.getConsoleById(invoice.getItemId()));

            if (returnVal.isPresent()) {
                tempCon = returnVal.get();
            } else {
                throw new IllegalArgumentException("Requested item is unavailable.");
            }


            if (invoice.getQuantity() > tempCon.getQuantity()) {
                throw new IllegalArgumentException("Requested quantity is unavailable.");
            }

            invoice.setUnitPrice(tempCon.getPrice());
            tempCon.setQuantity(tempCon.getQuantity() - invoice.getQuantity());
            gameStoreCatalogFeignClient.updateConsoleById(tempCon);


        } else if (invoice.getItemType().equals(GAME_ITEM_TYPE)) {
            GameResponseModel tempGame = null;
            Optional<GameResponseModel> returnVal = Optional.ofNullable(gameStoreCatalogFeignClient.getGameById(invoice.getItemId()));

            if (returnVal.isPresent()) {
                tempGame = returnVal.get();
            } else {
                throw new IllegalArgumentException("Requested item is unavailable.");
            }

            if (invoice.getQuantity() > tempGame.getQuantity()) {
                throw new IllegalArgumentException("Requested quantity is unavailable.");
            }
            invoice.setUnitPrice(tempGame.getPrice());
            tempGame.setQuantity(tempGame.getQuantity() - invoice.getQuantity());
            gameStoreCatalogFeignClient.updateGameById(tempGame);


        } else if (invoice.getItemType().equals(TSHIRT_ITEM_TYPE)) {
            TShirtResponseModal tempTShirt = null;
            Optional<TShirtResponseModal> returnVal = Optional.ofNullable(gameStoreCatalogFeignClient.getTShirtById(invoice.getItemId()));

            if (returnVal.isPresent()) {
                tempTShirt = returnVal.get();
            } else {
                throw new IllegalArgumentException("Requested item is unavailable.");
            }

            if (invoice.getQuantity() > tempTShirt.getQuantity()) {
                throw new IllegalArgumentException("Requested quantity is unavailable.");
            }
            invoice.setUnitPrice(tempTShirt.getPrice());
            tempTShirt.setQuantity(tempTShirt.getQuantity() - invoice.getQuantity());
            gameStoreCatalogFeignClient.updateTShirtById(tempTShirt);

        } else {
            throw new IllegalArgumentException(invoice.getItemType() +
                    ": Unrecognized Item type. Valid ones: T-Shirt, Console, or Game");
        }

        invoice.setQuantity(input.getQuantity());

        invoice.setSubtotal(
                invoice.getUnitPrice().multiply(
                        new BigDecimal(input.getQuantity())).setScale(2, RoundingMode.HALF_UP));

        //Throw Exception if subtotal is greater than 999.99
        if ((invoice.getSubtotal().compareTo(new BigDecimal(999.99)) > 0)) {
            throw new IllegalArgumentException("Subtotal exceeds maximum purchase price of $999.99");
        }

        //Validate State and Calc tax...
        BigDecimal tempTaxRate;
        Optional<Tax> taxReturnVal = taxRepository.findById(invoice.getState());

        if (taxReturnVal.isPresent()) {
            tempTaxRate = taxReturnVal.get().getRate();
        } else {
            throw new IllegalArgumentException(invoice.getState() + ": Invalid State code.");
        }

        if (!tempTaxRate.equals(BigDecimal.ZERO))
            invoice.setTax(tempTaxRate.multiply(invoice.getSubtotal()));
        else
            throw new IllegalArgumentException(invoice.getState() + ": Invalid State code.");

        BigDecimal processingFee;
        Optional<ProcessingFee> returnVal2 = processingFeeRepository.findById(invoice.getItemType());

        if (returnVal2.isPresent()) {
            processingFee = returnVal2.get().getFee();
        } else {
            throw new IllegalArgumentException("Requested item is unavailable.");
        }

        invoice.setProcessingFee(processingFee);

        //Checks if quantity of items if greater than 10 and adds additional processing fee
        if (invoice.getQuantity() > 10) {
            invoice.setProcessingFee(invoice.getProcessingFee().add(PROCESSING_FEE));
        }

        invoice.setTotal(invoice.getSubtotal().add(invoice.getProcessingFee()).add(invoice.getTax()));

        //checks total for validation
        if ((invoice.getTotal().compareTo(MAX_INVOICE_TOTAL) > 0)) {
            throw new IllegalArgumentException("Subtotal exceeds maximum purchase price of $999.99");
        }

        invoice = invoiceRepository.save(invoice);


        return buildInvoice(invoice);
    }

    public Invoice saveInvoice(Invoice invoice) {
        if (invoice == null) {
            return null;
        }
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getInvoicesByCustomerName(String name) {
        List<Invoice> invoiceList = invoiceRepository.findByName(name);
        List<Invoice> ivmList = new ArrayList<>();
        List<Invoice> exceptionList = null;

        if (invoiceList == null) {
            return exceptionList;
        } else {
            invoiceList.stream().forEach(i -> ivmList.add(buildInvoice(i)));
        }
        return ivmList;
    }


    public List<Invoice> getAllInvoices() {
        List<Invoice> invoiceList = invoiceRepository.findAll();
        List<Invoice> ivmList = new ArrayList<>();
        List<Invoice> exceptionList = null;

        if (invoiceList == null) {
            return exceptionList;
        } else {
            invoiceList.stream().forEach(i -> {
                ivmList.add(buildInvoice(i));
            });
        }
        return ivmList;
    }

    public Invoice getInvoice(long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice == null)
            return null;
        else
            return buildInvoice(invoice.get());
    }

    public Invoice buildInvoice(Invoice input) {
        Invoice invoice = new Invoice();
        invoice.setId(input.getId());
        invoice.setName(input.getName());
        invoice.setStreet(input.getStreet());
        invoice.setCity(input.getCity());
        invoice.setState(input.getState());
        invoice.setZipcode(input.getZipcode());
        invoice.setItemType(input.getItemType());
        invoice.setItemId(input.getItemId());
        invoice.setUnitPrice(input.getUnitPrice());
        invoice.setQuantity(input.getQuantity());
        invoice.setSubtotal(input.getSubtotal());
        invoice.setProcessingFee(input.getProcessingFee());
        invoice.setTax(input.getTax());
        invoice.setProcessingFee(input.getProcessingFee());
        invoice.setTotal(input.getTotal());

        return invoice;
    }
}

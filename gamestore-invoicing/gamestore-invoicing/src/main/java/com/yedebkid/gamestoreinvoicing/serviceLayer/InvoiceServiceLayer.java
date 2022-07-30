package com.yedebkid.gamestoreinvoicing.serviceLayer;

import com.netflix.discovery.converters.Auto;
import com.yedebkid.gamestoreinvoicing.repository.InvoiceRepository;
import com.yedebkid.gamestoreinvoicing.repository.ProcessingFeeRepository;
import com.yedebkid.gamestoreinvoicing.repository.TaxRepository;
import com.yedebkid.gamestoreinvoicing.util.feign.ConsoleFeignClient;
import com.yedebkid.gamestoreinvoicing.util.feign.GameFeignClient;
import com.yedebkid.gamestoreinvoicing.util.feign.TShirtFeignClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class InvoiceServiceLayer {
    private final BigDecimal PROCESSING_FEE = new BigDecimal("15.49");
    private final BigDecimal MAX_INVOICE_TOTAL = new BigDecimal("999.99");
    private final String GAME_ITEM_TYPE = "Game";
    private final String CONSOLE_ITEM_TYPE = "Console";
    private final String TSHIRT_ITEM_TYPE = "T-Shirt";

    ConsoleFeignClient consoleFeignClient;
    GameFeignClient gameFeignClient;
    TShirtFeignClient tShirtFeignClient;
    InvoiceRepository invoiceRepository;
    TaxRepository taxRepository;
    ProcessingFeeRepository processingFeeRepository;

    @Autowired

    public InvoiceServiceLayer(ConsoleFeignClient consoleFeignClient, GameFeignClient gameFeignClient, TShirtFeignClient tShirtFeignClient, InvoiceRepository invoiceRepository, TaxRepository taxRepository, ProcessingFeeRepository processingFeeRepository) {
        this.consoleFeignClient = consoleFeignClient;
        this.gameFeignClient = gameFeignClient;
        this.tShirtFeignClient = tShirtFeignClient;
        this.invoiceRepository = invoiceRepository;
        this.taxRepository = taxRepository;
        this.processingFeeRepository = processingFeeRepository;
    }

    public InvoiceServiceLayer() {
    }
}

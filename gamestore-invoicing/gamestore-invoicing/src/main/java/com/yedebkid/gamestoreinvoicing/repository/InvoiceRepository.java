package com.yedebkid.gamestoreinvoicing.repository;

import com.yedebkid.gamestoreinvoicing.model.Invoice;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RefreshScope
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByName(String name);
}


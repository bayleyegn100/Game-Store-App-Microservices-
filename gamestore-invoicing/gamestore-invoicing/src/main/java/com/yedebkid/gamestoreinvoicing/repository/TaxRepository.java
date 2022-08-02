package com.yedebkid.gamestoreinvoicing.repository;

import com.yedebkid.gamestoreinvoicing.model.Tax;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RefreshScope
public interface TaxRepository extends JpaRepository<Tax, String> {
    Tax findByState(String state);
}

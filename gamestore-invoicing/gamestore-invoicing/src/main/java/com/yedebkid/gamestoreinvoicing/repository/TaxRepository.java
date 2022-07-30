package com.yedebkid.gamestoreinvoicing.repository;

import com.yedebkid.gamestoreinvoicing.model.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxRepository extends JpaRepository<Tax, String> {
}

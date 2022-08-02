package com.yedebkid.gamestoreinvoicing.repository;

import com.yedebkid.gamestoreinvoicing.model.ProcessingFee;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RefreshScope
public interface ProcessingFeeRepository extends JpaRepository<ProcessingFee, String> {
    ProcessingFee findByProductType(String productType);
}

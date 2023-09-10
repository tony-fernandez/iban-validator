package com.pyur.ibanvalidator.repository;

import com.pyur.ibanvalidator.model.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, Long> {

  List<BankEntity> findByBankCodeAndCountry(@Param("bankCode") String bankCode, @Param("country") String country);

}

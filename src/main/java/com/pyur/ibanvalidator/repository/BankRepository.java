package com.pyur.ibanvalidator.repository;

import com.pyur.ibanvalidator.model.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<BankEntity, Long> {

  Optional<BankEntity> findByBankCodeAndCountryCodeAndBicEndsWith(@Param("bankCode") String bankCode, @Param("countryCode") String countryCode, @Param("bic") String bic);

  List<BankEntity> findByBankCodeAndCountryCode(@Param("bankCode") String bankCode, @Param("countryCode") String countryCode);

}

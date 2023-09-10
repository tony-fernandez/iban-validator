package com.pyur.ibanvalidator.repository;

import com.pyur.ibanvalidator.model.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {

  Optional<CountryEntity> findByCode(@Param("code") String code);
}

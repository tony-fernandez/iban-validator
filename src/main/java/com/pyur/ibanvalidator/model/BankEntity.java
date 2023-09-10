package com.pyur.ibanvalidator.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "bank")
@Table(indexes = {
    @Index(columnList = "bank_code", name = "bank_code_index"),
    @Index(columnList = "bic", name = "bic_index")})
public class BankEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "bank_code")
  private String bankCode;

  @Column(name = "bic")
  private String bic;

  @Column(name = "city")
  private String city;

  @Column(name = "country")
  private String country;

  @Column(name = "name")
  private String name;

  @Column(name = "postal_code")
  private String postalCode;

}

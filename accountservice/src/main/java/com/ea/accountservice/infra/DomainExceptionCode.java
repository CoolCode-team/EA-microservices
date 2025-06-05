package com.ea.accountservice.infra;

import lombok.Getter;

@Getter
public enum DomainExceptionCode {
  DOMAIN_ERROR("DOMAIN_ERROR"),
  DUPLICATE_FOUND("DUPLICATE_FOUND");
  private final String code;

  DomainExceptionCode(String code) {
    this.code = code;
  }

}

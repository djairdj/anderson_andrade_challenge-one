package br.dev.andersonandrade.moedaOne.records;

import java.math.BigDecimal;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 11/10/2024
 */
public record MoedaRecord(
    String result,
    String documentation,
    String termsOfUse,
    Long timeLastUpdateUnix,
    String timeLastUpdateUtc,
    Long timeNextUpdateUnix,
    String timeNextUpdateUtc,
    String baseCode,
    String targetCode,
    BigDecimal conversionRate,
    BigDecimal conversionResult
) {}

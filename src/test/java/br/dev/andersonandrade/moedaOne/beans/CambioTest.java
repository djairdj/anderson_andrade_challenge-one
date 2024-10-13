package br.dev.andersonandrade.moedaOne.beans;

import br.dev.andersonandrade.moedaOne.enuns.Moeda;
import br.dev.andersonandrade.moedaOne.records.MoedaRecord;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 12/10/2024
 */
public class CambioTest {
  private final String dataHoje = "Sat, 12 Oct 2024 00:00:01 +0000";
  private final String dataAmanha = "Sun, 13 Oct 2024 00:00:01 +0000";

  @Test
  void testCambioConstrutorValido() {

    // Configuração de um MoedaRecord simulado
    MoedaRecord record = new MoedaRecord(
        "result",
        "documentation",
        "termsOfUse",
        System.currentTimeMillis() / 1000L, // timeLastUpdateUnix
        dataHoje, // timeLastUpdateUtc
        System.currentTimeMillis() / 1000L + 3600, // timeNextUpdateUnix
        dataAmanha, // timeNextUpdateUtc
        "USD", // baseCode
        "BRL", // targetCode
        BigDecimal.valueOf(5.00), // conversionRate
        BigDecimal.valueOf(500.00) // conversionResult
    );

    // Testando com valores válidos
    Cambio cambio = new Cambio(BigDecimal.valueOf(100), record);

    assertNotNull(cambio.getDataTransacao());
    assertEquals(Moeda.BRL, cambio.getDestino());
    assertEquals(Moeda.USD, cambio.getOrigem());
    assertEquals(BigDecimal.valueOf(100), cambio.getQuantidadeMoedasOrigem());
    assertEquals(BigDecimal.valueOf(5.00), cambio.getQuantidadeMoedasDestino());
  }

  @Test
  void testQuantidadeNegativaLancaExcecao() {
    // Configuração de um MoedaRecord simulado
    MoedaRecord record = new MoedaRecord(
        "result",
        "documentation",
        "termsOfUse",
        System.currentTimeMillis() / 1000L,
        dataHoje,
        System.currentTimeMillis() / 1000L + 3600,
        dataAmanha,
        "USD",
        "BRL",
        BigDecimal.valueOf(5.00),
        BigDecimal.valueOf(500.00)
    );

    // Testando com quantidade negativa
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Cambio(BigDecimal.valueOf(-100), record);
    });

    assertEquals("A quantidade de moedas de origem não pode ser nula ou negativa.", exception.getMessage());
  }

  @Test
  void testMoedaRecordNuloLancaExcecao() {
    // Testando com MoedaRecord nulo
    Exception exception = assertThrows(NullPointerException.class, () -> {
      new Cambio(BigDecimal.valueOf(100), null);
    });

    assertEquals("MoedaRecord não pode ser nula!", exception.getMessage());
  }

  @Test
  void testQuantidadeNulaLancaExcecao() {
    // Configuração de um MoedaRecord simulado
    MoedaRecord record = new MoedaRecord(
        "result",
        "documentation",
        "termsOfUse",
        System.currentTimeMillis() / 1000L,
        LocalDateTime.now().toString(),
        System.currentTimeMillis() / 1000L + 3600,
        LocalDateTime.now().plusHours(1).toString(),
        "USD",
        "BRL",
        BigDecimal.valueOf(5.00),
        BigDecimal.valueOf(500.00)
    );

    // Testando com quantidade nula
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      new Cambio(null, record);
    });

    assertEquals("A quantidade de moedas de origem não pode ser nula ou negativa.", exception.getMessage());
  }

  @Test
  void testEqualsEHashCode() {
    // Configuração de MoedaRecord simulados
    MoedaRecord record1 = new MoedaRecord(
        "result",
        "documentation",
        "termsOfUse",
        System.currentTimeMillis() / 1000L,
        dataHoje,
        System.currentTimeMillis() / 1000L + 3600,
        dataAmanha,
        "USD",
        "BRL",
        BigDecimal.valueOf(5.00),
        BigDecimal.valueOf(500.00)
    );
    MoedaRecord record2 = new MoedaRecord(
        "result",
        "documentation",
        "termsOfUse",
        System.currentTimeMillis() / 1000L,
        dataHoje,
        System.currentTimeMillis() / 1000L + 3600,
        dataAmanha,
        "USD",
        "BRL",
        BigDecimal.valueOf(5.00),
        BigDecimal.valueOf(500.00)
    );

    // Criando dois objetos iguais
    Cambio cambio1 = new Cambio(BigDecimal.valueOf(100), record1);
    Cambio cambio2 = new Cambio(BigDecimal.valueOf(100), record2);

    assertEquals(cambio1, cambio2);
    assertEquals(cambio1.hashCode(), cambio2.hashCode());
  }

  @Test
  void testToString() {
    // Configuração de um MoedaRecord simulado
    MoedaRecord record = new MoedaRecord(
        "result",
        "documentation",
        "termsOfUse",
        System.currentTimeMillis() / 1000L,
        dataHoje,
        System.currentTimeMillis() / 1000L + 3600,
        dataAmanha,
        "USD",
        "BRL",
        BigDecimal.valueOf(5.00),
        BigDecimal.valueOf(500.00)
    );

    Cambio cambio = new Cambio(BigDecimal.valueOf(100), record);

    String expected = "Cambio{" +
        "dataTransacao=" + cambio.getDataTransacao() +
        ", origem=" + cambio.getOrigem() +
        ", destino=" + cambio.getDestino() +
        ", quantidadeMoedasOrigem=" + cambio.getQuantidadeMoedasOrigem() +
        ", quantidadeMoedasDestino=" + cambio.getQuantidadeMoedasDestino() +
        '}';
    assertEquals(expected, cambio.toString());
  }
}

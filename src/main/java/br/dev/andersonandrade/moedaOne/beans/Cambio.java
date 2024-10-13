package br.dev.andersonandrade.moedaOne.beans;

import br.dev.andersonandrade.moedaOne.enuns.Moeda;
import br.dev.andersonandrade.moedaOne.records.MoedaRecord;
import br.dev.andersonandrade.moedaOne.uteis.ConversorDatas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Classe que representa uma operação de câmbio entre duas moedas.
 * A instância desta classe é imutável e contém informações sobre a transação,
 * incluindo as moedas de origem e destino, as quantidades convertidas e a data da transação.
 *
 * @author Anderson Andrade Dev
 * @Data de Criação 12/10/2024
 */
public class Cambio {

  private final LocalDateTime dataTransacao;
  private final Moeda destino;
  private final Moeda origem;
  private final BigDecimal quantidadeMoedasOrigem;
  private final BigDecimal quantidadeMoedasDestino;

  /**
   * Construtor da classe que inicializa a operação de câmbio com a quantidade de moeda
   * de origem e os dados da conversão fornecidos por um MoedaRecord.
   *
   * @param quantidade Quantidade de moedas de origem.
   * @param moedaRecord Registro contendo as informações sobre a taxa de conversão.
   * @throws IllegalArgumentException se a quantidade for negativa ou nula.
   * @throws NullPointerException se moedaRecord for nulo.
   */
  public Cambio(BigDecimal quantidade, MoedaRecord moedaRecord) {
    validarQuantidadePositiva(quantidade);
    Objects.requireNonNull(moedaRecord, "MoedaRecord não pode ser nula!");

    this.dataTransacao = ConversorDatas.converterDataAPI(moedaRecord.timeLastUpdateUtc());
    this.destino = Moeda.buscarPorCodigo(moedaRecord.targetCode());
    this.origem = Moeda.buscarPorCodigo(moedaRecord.baseCode());
    this.quantidadeMoedasOrigem = quantidade;
    this.quantidadeMoedasDestino = quantidade.multiply(moedaRecord.conversionRate());
  }

  /**
   * Obtém a data e hora em que a transação de câmbio foi realizada.
   *
   * @return A data e hora da transação.
   */
  public LocalDateTime getDataTransacao() {
    return dataTransacao;
  }

  /**
   * Obtém a moeda de destino da transação de câmbio.
   *
   * @return A moeda de destino.
   */
  public Moeda getDestino() {
    return destino;
  }

  /**
   * Obtém a moeda de origem da transação de câmbio.
   *
   * @return A moeda de origem.
   */
  public Moeda getOrigem() {
    return origem;
  }

  /**
   * Obtém a quantidade de moedas de origem fornecidas na transação.
   *
   * @return A quantidade de moedas de origem.
   */
  public BigDecimal getQuantidadeMoedasOrigem() {
    return quantidadeMoedasOrigem.setScale(2, RoundingMode.HALF_DOWN);
  }

  /**
   * Obtém a quantidade de moedas de destino recebidas após a conversão.
   *
   * @return A quantidade de moedas de destino.
   */
  public BigDecimal getQuantidadeMoedasDestino() {
    return quantidadeMoedasDestino.setScale(2, RoundingMode.HALF_DOWN);
  }

  /**
   * Valida se a quantidade de moedas de origem é positiva.
   *
   * @param quantidade A quantidade de moedas de origem a ser validada.
   * @throws IllegalArgumentException se a quantidade for negativa ou nula.
   */
  private void validarQuantidadePositiva(BigDecimal quantidade) {
    if(quantidade == null || quantidade.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("A quantidade de moedas de origem não pode ser nula ou negativa.");
    }
  }

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;
    Cambio cambio = (Cambio) o;
    return Objects.equals(dataTransacao, cambio.dataTransacao) && destino == cambio.destino && origem == cambio.origem && Objects.equals(quantidadeMoedasOrigem, cambio.quantidadeMoedasOrigem) && Objects.equals(quantidadeMoedasDestino, cambio.quantidadeMoedasDestino);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataTransacao, destino, origem, quantidadeMoedasOrigem, quantidadeMoedasDestino);
  }

  @Override
  public String toString() {
    return "Cambio{" +
        "dataTransacao=" + dataTransacao +
        ", origem=" + origem +
        ", destino=" + destino +
        ", quantidadeMoedasOrigem=" + quantidadeMoedasOrigem +
        ", quantidadeMoedasDestino=" + quantidadeMoedasDestino +
        '}';
  }

}

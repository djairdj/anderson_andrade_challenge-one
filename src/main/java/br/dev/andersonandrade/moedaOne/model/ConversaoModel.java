package br.dev.andersonandrade.moedaOne.model;

import br.dev.andersonandrade.moedaOne.beans.Cambio;
import br.dev.andersonandrade.moedaOne.enuns.Moeda;
import br.dev.andersonandrade.moedaOne.records.MoedaRecord;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 12/10/2024
 */
public class ConversaoModel {

  public Optional<Cambio> converter(BigDecimal quantidade, Moeda origem, Moeda destino) {

    Optional<MoedaRecord> moedaAConverter = ConexaoModel.buscaValoreMoedas(origem, destino);

    return moedaAConverter.map(moedaRecord -> new Cambio(quantidade, moedaRecord));
  }

}

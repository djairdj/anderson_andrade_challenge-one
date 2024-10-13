package br.dev.andersonandrade.moedaOne;

import br.dev.andersonandrade.moedaOne.beans.Cambio;
import br.dev.andersonandrade.moedaOne.enuns.Moeda;
import br.dev.andersonandrade.moedaOne.model.ConversaoModel;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 11/10/2024
 */

public class Main {
  public static void main(String[] args) {
    ConversaoModel conversao = new ConversaoModel();
    Optional<Cambio> cambio = conversao.converter(BigDecimal.valueOf(130), Moeda.USD, Moeda.BRL);

    if(cambio.isPresent()) {
      System.out.println(cambio.get().getQuantidadeMoedasDestino());
    } else {
      System.out.println("Erro!");
    }
  }

}

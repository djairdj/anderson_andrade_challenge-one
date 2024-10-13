package br.dev.andersonandrade.moedaOne.controller;

import br.dev.andersonandrade.moedaOne.enuns.Moeda;
import br.dev.andersonandrade.moedaOne.model.ConexaoModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 11/10/2024
 */
public class HomeControllerTest {

  public HomeControllerTest() {
  }

  @Test
  public void testaConexaoComAPI() {

    var sucesso = ConexaoModel.buscaValoreMoedas(Moeda.ARS, Moeda.BRL);
    assertTrue(sucesso.isPresent());
  }
}

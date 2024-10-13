package br.dev.andersonandrade.moedaOne.uteis;

import java.io.File;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A classe {@code ConversorDatas} fornece utilitários para conversão de datas
 * em formatos específicos e gerenciamento de logs para rastreamento de erros.
 * <p>
 * Esta classe é final e não pode ser estendida. Todos os métodos são estáticos,
 * e a classe é projetada para ser utilizada sem a necessidade de instanciar objetos.
 *
 * @author Anderson Andrade Dev
 * @data De Criação 12/10/2024
 */
public final class ConversorDatas {

  private static final String LOG_PATH = "logs/dataconversao.log";
  private static Logger logger;

  static {
    init();
  }

  private ConversorDatas() {}

  /**
   * Converte uma string de data no formato da API para um {@code LocalDateTime}.
   *
   * @param data A string de data no formato "EEE, dd MMM yyyy HH:mm:ss Z".
   * @return O objeto {@code LocalDateTime} correspondente à string de data.
   * @throws IllegalArgumentException Se a data for nula ou vazia, ou se o formato estiver incorreto.
   */
  public static LocalDateTime converterDataAPI(String data) {
    if(Objects.isNull(data) || data.trim().isEmpty()) {
      logger.log(Level.SEVERE, "A data fornecida é nula ou está vazia.");
      throw new IllegalArgumentException("A string de data não pode ser nula ou vazia.");
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    try {
      return OffsetDateTime.parse(data, formatter).toLocalDateTime();
    } catch(DateTimeParseException e) {
      logger.log(Level.SEVERE, "Erro ao converter a string de data: " + data + " - " + e.getMessage());
      throw new IllegalArgumentException("Formato de data inválido. A data fornecida deve estar no formato correto.");
    }
  }

  /**
   * Inicializa o logger para registrar eventos e erros relacionados à conversão de datas.
   * Este método cria o diretório de logs se ele não existir e configura o logger para
   * gravar em um arquivo específico.
   */
  private static void init() {
    logger = Logger.getLogger(ConversorDatas.class.getName());
    try {
      new File("logs").mkdirs();

      FileHandler fileHandler = new FileHandler(LOG_PATH, true);
      fileHandler.setFormatter(new SimpleFormatter());
      logger.addHandler(fileHandler);
      logger.setLevel(Level.ALL);

    } catch(Exception e) {
      logger.log(Level.SEVERE, "Erro ao configurar Log de Datas: " + e.getMessage(), e);
    }
  }

}

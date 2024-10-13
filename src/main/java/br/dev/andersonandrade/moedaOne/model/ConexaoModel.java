package br.dev.andersonandrade.moedaOne.model;

import br.dev.andersonandrade.moedaOne.enuns.Moeda;
import br.dev.andersonandrade.moedaOne.records.MoedaRecord;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * A classe ConexaoModel é responsável por gerenciar a conexão com uma API de taxa de câmbio de moedas,
 * além de armazenar e recuperar dados JSON relacionados às conversões de moeda.
 * Utiliza HttpClient para se comunicar com a API e Gson para manipulação de JSON.
 * Esta classe contém métodos para buscar, gravar e processar dados de conversão de moeda.
 *
 * @author Anderson Andrade Dev
 * @version 1.0
 * @since 2024-10-11
 */
public final class ConexaoModel {

  private static final String LOG_PATH = "logs/conexao.log";
  private static final String JSON_PATH = "jsons/moedas.json";
  private static final Logger logger = Logger.getLogger(ConexaoModel.class.getClass().getName());
  private static final Gson gson = new GsonBuilder()
      .setPrettyPrinting()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create();
  private static final Set<MoedaRecord> moedaRecordSet = new HashSet<>();
  private static final Properties prop = new Properties();
  private static final HttpClient client = HttpClient.newHttpClient();
  private static String url;
  private static String apiKey;
  private static HttpRequest request;

  /**
   * Construtor privado para evitar a instanciação da classe.
   * Essa classe utiliza apenas métodos estáticos.
   */
  private ConexaoModel() {}

  /**
   * Busca os valores de conversão de moedas, primeiro verificando se os dados estão armazenados
   * localmente em um arquivo JSON. Se os dados não forem encontrados ou estiverem desatualizados,
   * uma requisição à API é feita.
   *
   * @param origem  A moeda de origem.
   * @param destino A moeda de destino.
   * @return Um Optional contendo um MoedaRecord, se os dados forem encontrados ou recuperados com sucesso.
   */
  public static Optional<MoedaRecord> buscaValoreMoedas(Moeda origem, Moeda destino) {

    init();

    Set<MoedaRecord> listaMoedas = listaMoedaJson();

    if(Objects.isNull(origem) || Objects.isNull(destino)) {
      logger.log(Level.SEVERE, "Moeda não pode ser nula!");
      throw new IllegalArgumentException("Verifique os parâmetros, eles não podem ser nulos!");
    }

    if(!listaMoedas.isEmpty()) {
      var moedaJson = listaMoedas.stream()
          .filter(moeda -> moeda.baseCode().contains(origem.toString()))
          .filter(moeda -> moeda.targetCode().contains(destino.toString()))
          .filter(moeda -> converterDataAPI(moeda.timeNextUpdateUtc()).isAfter(LocalDateTime.now()))
          .findFirst();

      if(moedaJson.isPresent()) {
        logger.log(Level.INFO, "Moeda recuperada do JSON!");
        return moedaJson;
      }
    }

    Optional<MoedaRecord> moedaRecord = requisacaoAPI(origem, destino);

    moedaRecord.ifPresent(record -> {
      gravarJson(record);
      logger.log(Level.INFO, "Moeda gravada com sucesso no JSON");
    });

    return moedaRecord;
  }

  /**
   * Inicializa o sistema, configurando o logger e carregando o arquivo de configuração.
   * Cria diretórios para logs e JSON se não existirem.
   */
  private static void init() {
    try {
      new File("logs").mkdirs();
      new File("jsons").mkdirs();

      FileHandler fileHandler = new FileHandler(LOG_PATH, true);
      fileHandler.setFormatter(new SimpleFormatter());
      logger.addHandler(fileHandler);
      logger.setLevel(Level.ALL);
    } catch(Exception e) {
      System.err.println("Erro ao configurar Log " + e);
    }

    try(InputStream file = ConexaoModel.class.getClassLoader().getResourceAsStream("configuracao.properties")) {
      if(file == null) {
        throw new FileNotFoundException("Arquivo de configuração não encontrado!");
      }

      prop.load(file);

      apiKey = prop.getProperty("apikey");
      url = prop.getProperty("url");

    } catch(Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Grava um MoedaRecord no arquivo JSON. O método lê o JSON atual, adiciona o novo registro
   * e grava o arquivo atualizado.
   *
   * @param moedaRecord O registro de moeda a ser adicionado.
   */
  private static void gravarJson(MoedaRecord moedaRecord) {
    Objects.requireNonNull(moedaRecord, "Verifique o parâmetro, ele não pode ser nulo!");

    moedaRecordSet.addAll(listaMoedaJson());
    moedaRecordSet.add(moedaRecord);

    try(FileWriter fileWriter = new FileWriter(JSON_PATH)) {
      fileWriter.write(gson.toJson(moedaRecordSet));
    } catch(IOException e) {
      logger.log(Level.SEVERE, "Erro ao gravar JSON: " + e.getMessage());
      System.err.println("Erro de I/O ao gravar JSON!" + e.getMessage());
    }
  }

  /**
   * Lê o arquivo JSON e retorna um conjunto de registros de moedas (MoedaRecord).
   *
   * @return Um conjunto de MoedaRecord recuperados do JSON.
   */
  private static Set<MoedaRecord> listaMoedaJson() {
    try(FileReader reader = new FileReader(JSON_PATH)) {
      Type listType = new TypeToken<HashSet<MoedaRecord>>() {}.getType();
      Set<MoedaRecord> moedaRecords = gson.fromJson(reader, listType);

      return Objects.isNull(moedaRecords) ? Set.of() : moedaRecords;
    } catch(IOException e) {
      logger.log(Level.WARNING, "Erro ao ler o arquivo JSON: " + e.getMessage());
    }
    return Set.of();
  }

  /**
   * Faz uma requisição à API para obter a taxa de conversão entre duas moedas.
   *
   * @param origem  A moeda de origem.
   * @param destino A moeda de destino.
   * @return Um Optional contendo um MoedaRecord com os dados de conversão da API.
   */
  private static Optional<MoedaRecord> requisacaoAPI(Moeda origem, Moeda destino) {
    var urlApiKey = url + apiKey + "/pair/" + origem + "/" + destino;

    request = HttpRequest.newBuilder()
        .GET()
        .timeout(Duration.ofSeconds(10))
        .uri(URI.create(urlApiKey))
        .build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      if(response.statusCode() == 200) {
        logger.log(Level.INFO, "Conexão bem-sucedida!");
        logger.log(Level.INFO, "Resposta: " + response.body());
        return Optional.of(gson.fromJson(response.body(), MoedaRecord.class));
      } else {
        logger.log(Level.SEVERE, "Erro ao conectar. Código Status: " + response.statusCode());
      }
    } catch(InterruptedException e) {
      logger.log(Level.SEVERE, "Requisição interrompida: " + e.getMessage());
      Thread.currentThread().interrupt();
    } catch(IOException e) {
      logger.log(Level.SEVERE, "Erro I/O ao tentar conectar: " + e.getMessage());
    }
    return Optional.empty();
  }

  /**
   * Converte uma string de data no formato da API para um LocalDateTime.
   *
   * @param data A string de data no formato "EEE, dd MMM yyyy HH:mm:ss Z".
   * @return O objeto LocalDateTime correspondente à string de data.
   * @throws IllegalArgumentException Se a data for nula ou vazia, ou se o formato estiver incorreto.
   */
  private static LocalDateTime converterDataAPI(String data) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    if(Objects.isNull(data) || data.trim().isEmpty()) {
      logger.log(Level.SEVERE, "A data fornecida é nula ou está vazia.");
      throw new IllegalArgumentException("A string de data não pode ser nula ou vazia.");
    }

    try {
      return OffsetDateTime.parse(data, formatter).toLocalDateTime();
    } catch(DateTimeParseException e) {
      logger.log(Level.SEVERE, "Erro ao converter a string de data: " + data + " - " + e.getMessage());
      throw new IllegalArgumentException("Formato de data inválido. A data fornecida deve estar no formato correto.");
    }
  }

}

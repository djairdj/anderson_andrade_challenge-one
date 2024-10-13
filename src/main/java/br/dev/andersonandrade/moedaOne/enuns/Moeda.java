package br.dev.andersonandrade.moedaOne.enuns;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 11/10/2024
 */
public enum Moeda {
  //region Moedas
  AED("Dirham dos Emirados Árabes Unidos", "Emirados Árabes Unidos", "د.إ"),
  AFN("Afegani Afegão", "Afeganistão", "؋"),
  ALL("Lek Albanês", "Albânia", "L"),
  AMD("Dram Armênio", "Armênia", "֏"),
  ANG("Florim das Antilhas Neerlandesas", "Antilhas Neerlandesas", "ƒ"),
  AOA("Kwanza Angolano", "Angola", "Kz"),
  ARS("Peso Argentino", "Argentina", "$"),
  AUD("Dólar Australiano", "Austrália", "$"),
  AWG("Florim Arubano", "Aruba", "ƒ"),
  AZN("Manat Azeri", "Azerbaijão", "₼"),
  BAM("Marco Conversível da Bósnia e Herzegovina", "Bósnia e Herzegovina", "KM"),
  BBD("Dólar de Barbados", "Barbados", "$"),
  BDT("Taka de Bangladesh", "Bangladesh", "৳"),
  BGN("Lev Búlgaro", "Bulgária", "лв"),
  BHD("Dinar do Bahrein", "Bahrein", ".د.ب"),
  BIF("Franco Burundês", "Burundi", "FBu"),
  BMD("Dólar das Bermudas", "Bermudas", "$"),
  BND("Dólar do Brunei", "Brunei", "$"),
  BOB("Boliviano", "Bolívia", "Bs."),
  BRL("Real Brasileiro", "Brasil", "R$"),
  BSD("Dólar Bahamense", "Bahamas", "$"),
  BTN("Ngultrum Butanês", "Butão", "Nu."),
  BWP("Pula Botsuanense", "Botsuana", "P"),
  BYN("Rublo Bielorrusso", "Belarus", "Br"),
  BZD("Dólar de Belize", "Belize", "$"),
  CAD("Dólar Canadense", "Canadá", "$"),
  CDF("Franco Congolês", "República Democrática do Congo", "FC"),
  CHF("Franco Suíço", "Suíça", "CHF"),
  CLP("Peso Chileno", "Chile", "$"),
  CNY("Renminbi Chinês", "China", "¥"),
  COP("Peso Colombiano", "Colômbia", "$"),
  CRC("Colón Costarriquenho", "Costa Rica", "₡"),
  CUP("Peso Cubano", "Cuba", "$"),
  CVE("Escudo Cabo-Verdiano", "Cabo Verde", "$"),
  CZK("Coroa Tcheca", "República Tcheca", "Kč"),
  DJF("Franco Djibutiano", "Djibuti", "Fdj"),
  DKK("Coroa Dinamarquesa", "Dinamarca", "kr"),
  DOP("Peso Dominicano", "República Dominicana", "$"),
  DZD("Dinar Argelino", "Argélia", "دج"),
  EGP("Libra Egípcia", "Egito", "£"),
  ERN("Nakfa Eritreia", "Eritreia", "Nfk"),
  ETB("Birr Etíope", "Etiópia", "Br"),
  EUR("Euro", "União Europeia", "€"),
  FJD("Dólar Fijiano", "Fiji", "$"),
  FKP("Libra das Ilhas Falkland", "Ilhas Falkland", "£"),
  FOK("Coroa das Ilhas Faroé", "Ilhas Faroé", "kr"),
  GBP("Libra Esterlina", "Reino Unido", "£"),
  GEL("Lari Georgiano", "Geórgia", "₾"),
  GGP("Libra de Guernsey", "Guernsey", "£"),
  GHS("Cedi Ganês", "Gana", "₵"),
  GIP("Libra de Gibraltar", "Gibraltar", "£"),
  GMD("Dalasi Gambiano", "Gâmbia", "D"),
  GNF("Franco Guineense", "Guiné", "FG"),
  GTQ("Quetzal Guatemalteco", "Guatemala", "Q"),
  GYD("Dólar de Guiana", "Guiana", "$"),
  HKD("Dólar de Hong Kong", "Hong Kong", "$"),
  HNL("Lempira Hondurenha", "Honduras", "L"),
  HRK("Kuna Croata", "Croácia", "kn"),
  HTG("Gourde Haitiano", "Haiti", "G"),
  HUF("Florim Húngaro", "Hungria", "Ft"),
  IDR("Rupia Indonésia", "Indonésia", "Rp"),
  ILS("Novo Shekel Israelense", "Israel", "₪"),
  IMP("Libra da Ilha de Man", "Ilha de Man", "£"),
  INR("Rupia Indiana", "Índia", "₹"),
  IQD("Dinar Iraquiano", "Iraque", "ع.د"),
  IRR("Rial Iraniano", "Irã", "﷼"),
  ISK("Coroa Islandesa", "Islândia", "kr"),
  JEP("Libra de Jersey", "Jersey", "£"),
  JMD("Dólar Jamaicano", "Jamaica", "$"),
  JOD("Dinar Jordaniano", "Jordânia", "د.ا"),
  JPY("Iene Japonês", "Japão", "¥"),
  // Outros símbolos seguem o mesmo padrão
  USD("Dólar Americano", "Estados Unidos", "$"),
  ZWL("Dólar Zimbabuano", "Zimbábue", "$");
  //endregion

  private final String nome;
  private final String pais;
  private final String simbolo;

  Moeda(String nome, String pais, String simbolo) {
    this.nome = nome;
    this.pais = pais;
    this.simbolo = simbolo;
  }

  public static Moeda buscarPorCodigo(String codigo) {
    for(Moeda moeda : Moeda.values()) {
      if(moeda.name().equalsIgnoreCase(codigo)) {
        return moeda;
      }
    }
    throw new IllegalArgumentException("Moeda não encontrada: " + codigo);
  }

  public String getSimbolo() {
    return simbolo;
  }
  public String getNome() {
    return nome;
  }

  public String getPais() {
    return pais;
  }

}

package br.dev.andersonandrade.moedaOne.view;

import br.dev.andersonandrade.moedaOne.beans.Cambio;
import br.dev.andersonandrade.moedaOne.enuns.Moeda;
import br.dev.andersonandrade.moedaOne.model.ConversaoModel;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 12/10/2024
 */
public class Principal extends JFrame {

  private final ConversaoModel conversaoModel;
  private JPanel jPanel;
  private JButton jBConverter;
  private JLabel jLQtdMoeda;
  private JComboBox<String> comboBox1;
  private JComboBox<String> comboBox2;
  private JLabel jLMoedaOrigem;
  private JLabel jLMoedaDestino;
  private JLabel jLValorOrigem;
  private JLabel jLValorConvertido;
  private JLabel jLDataTransacao;
  private JLabel jLNomeMoedaOrigem;
  private JLabel jLNomeMoedaDestino;
  private JFormattedTextField fTquantidadeMoeda;

  public Principal() {
    conversaoModel = new ConversaoModel();
    init();
    comboBox1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        jLNomeMoedaDestino.setText(Moeda.buscarPorCodigo(comboBox1.getSelectedItem().toString()).getNome());
      }
    });
    comboBox2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        jLNomeMoedaOrigem.setText(Moeda.buscarPorCodigo(comboBox2.getSelectedItem().toString()).getNome());
      }
    });
    jBConverter.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(fTquantidadeMoeda.getText().isEmpty()) {
          JOptionPane.showMessageDialog(Principal.this,
              "Insira uma quantidade de moedas para conversão!");
          fTquantidadeMoeda.requestFocusInWindow();
        } else {
          BigDecimal quantidadeMoeda = new BigDecimal(fTquantidadeMoeda.getText().replace(",", ""));
          Moeda moedaOrigem = Moeda.buscarPorCodigo(comboBox2.getSelectedItem().toString());
          Moeda moedadDestino = Moeda.buscarPorCodigo(comboBox1.getSelectedItem().toString());
          Optional<Cambio> cambio = conversaoModel.converter(quantidadeMoeda, moedaOrigem, moedadDestino);
          cambio.ifPresent(c -> {
            jLValorConvertido.setText(
                moedadDestino.getPais() + " : " + moedadDestino.getSimbolo() + " "+
                c.getQuantidadeMoedasDestinoToString());
            jLDataTransacao.setText("Data Transação: " + c.getDataTransacao().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            jLValorOrigem.setText(
                moedaOrigem.getPais() + " : "+ moedaOrigem.getSimbolo() + " " +
                c.getQuantidadeMoedasOrigemToString());
          });
        }
      }
    });
  }

  public static void main(String[] args) {
    new Principal();
  }

  private static String[] convertMoedaEmString() {
    String[] moedas = new String[Moeda.values().length];
    int index = 0;
    for(Moeda m : Moeda.values()) {
      moedas[index++] = m.name();
    }
    return moedas;
  }

  private void init() {
    comboBox1.setModel(new DefaultComboBoxModel<>(convertMoedaEmString()));
    comboBox2.setModel(new DefaultComboBoxModel<>(convertMoedaEmString()));
    jLNomeMoedaDestino.setText(Moeda.buscarPorCodigo(comboBox1.getSelectedItem().toString()).getNome());
    jLNomeMoedaOrigem.setText(Moeda.buscarPorCodigo(comboBox2.getSelectedItem().toString()).getNome());
    configurarInputMoeda();
    setContentPane(jPanel);
    setTitle("Conversor de Moeda Oracle One");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(800, 600);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void configurarInputMoeda() {
    // Configura o formato numérico para aceitar até 2 casas decimais
    NumberFormat numberFormat = DecimalFormat.getNumberInstance();
    numberFormat.setMaximumFractionDigits(2); // Limita a duas casas decimais
    numberFormat.setMinimumFractionDigits(2); // Garante que tenha sempre duas casas decimais

    NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
    numberFormatter.setAllowsInvalid(false); // Impede valores temporariamente inválidos
    numberFormatter.setMinimum(0.0); // Apenas valores não negativos

    // Configura a fábrica do formatador para o campo de texto formatado
    fTquantidadeMoeda.setFormatterFactory(new DefaultFormatterFactory(numberFormatter));

    // Configura o comportamento quando o campo perde o foco: aplica o valor formatado
    fTquantidadeMoeda.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);

    // Listener para permitir apenas números e ponto (.) como separador decimal
    fTquantidadeMoeda.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        // Permite apenas números e ponto (.) como separador decimal
        if(!Character.isDigit(c) && c != '.' && c != '\b') {
          evt.consume(); // Ignora a entrada inválida
        }
      }
    });
  }

}

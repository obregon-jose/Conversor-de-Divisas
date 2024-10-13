import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CurrencyConverterApp extends JFrame {
    private JComboBox<String> baseCurrencyComboBox;
    private JComboBox<String> targetCurrencyComboBox;
    private JTextField amountField;
    private JLabel resultLabel;
    private CurrencyService currencyService;

    public CurrencyConverterApp() {
        currencyService = new CurrencyService();
        setTitle("Conversor de Monedas");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Moneda base:"));
        baseCurrencyComboBox = new JComboBox<>();
        panel.add(baseCurrencyComboBox);

        panel.add(new JLabel("Moneda objetivo:"));
        targetCurrencyComboBox = new JComboBox<>();
        panel.add(targetCurrencyComboBox);

        panel.add(new JLabel("Cantidad a convertir:"));
        amountField = new JTextField();
        panel.add(amountField);

        JButton convertButton = new JButton("Convertir");
        panel.add(convertButton);

        resultLabel = new JLabel("Resultado: ");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(resultLabel);

        add(panel, BorderLayout.CENTER);

        // Cargar las monedas disponibles
        loadCurrencies();

        // Acción del botón de conversión
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });
    }

    private void loadCurrencies() {
        try {
            String[] currencies = currencyService.loadCurrencies();
            for (String currency : currencies) {
                baseCurrencyComboBox.addItem(currency);
                targetCurrencyComboBox.addItem(currency);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener las monedas disponibles.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void convertCurrency() {
        String baseCurrency = (String) baseCurrencyComboBox.getSelectedItem();
        String targetCurrency = (String) targetCurrencyComboBox.getSelectedItem();
        double amount;

        try {
            amount = Double.parseDouble(amountField.getText());
            double convertedAmount = currencyService.convertCurrency(baseCurrency, targetCurrency, amount);
            resultLabel.setText(String.format("Resultado: %.1f %s", convertedAmount, targetCurrency));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            resultLabel.setText("No se pudo obtener la tasa de cambio.");
        }
    }
}

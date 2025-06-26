import javax.swing.*;
import java.awt.*;

public class Calculator extends JFrame {
    private final JTextField display;
    private final StringBuilder currentInput = new StringBuilder();
    private double result = 0;
    private String lastOperator = "";
    private boolean startNewNumber = true;

    public Calculator() {
        setTitle("Calculator");
        setSize(360, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(30, 30, 30));

        // Display screen
        display = new JTextField("0");
        display.setFont(new Font("Segoe UI", Font.BOLD, 38));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setEditable(false);
        display.setBackground(new Color(20, 20, 20));
        display.setForeground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(display, BorderLayout.NORTH);

        // Calculator buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(5, 4, 10, 10));
        buttonsPanel.setBackground(new Color(30, 30, 30));

        String[] buttons = {
            "C", "±", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "−",
            "1", "2", "3", "+",
            "0", ".", "←", "="
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.BOLD, 24));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);

            // Set button color based on type
            if ("0123456789.".contains(text)) {
                button.setBackground(new Color(50, 50, 50)); // Number buttons
            } else if ("C±%←".contains(text)) {
                button.setBackground(new Color(220, 80, 80)); // Function buttons (red)
            } else {
                button.setBackground(new Color(70, 130, 180)); // Operator buttons (blue)
            }

            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.addActionListener(e -> buttonPressed(e.getActionCommand()));
            buttonsPanel.add(button);
        }

        add(buttonsPanel, BorderLayout.CENTER);
    }

    private void buttonPressed(String command) {
        switch (command) {
            case "C" -> clear();
            case "±" -> toggleSign();
            case "%" -> percent();
            case "÷", "×", "−", "+" -> processOperator(command);
            case "=" -> calculateResult();
            case "←" -> backspace();
            default -> appendNumber(command);
        }
    }

    private void clear() {
        currentInput.setLength(0);
        result = 0;
        lastOperator = "";
        startNewNumber = true;
        display.setText("0");
    }

    private void toggleSign() {
        try {
            if (currentInput.length() > 0) {
                if (currentInput.charAt(0) == '-') {
                    currentInput.deleteCharAt(0);
                } else {
                    currentInput.insert(0, '-');
                }
                display.setText(currentInput.toString());
            } else if (!startNewNumber) {
                result = -result;
                display.setText(formatNumber(result));
            }
        } catch (Exception e) {
            showError("Error toggling sign");
        }
    }

    private void percent() {
        try {
            double val = Double.parseDouble(display.getText()) / 100;
            currentInput.setLength(0);
            currentInput.append(val);
            display.setText(formatNumber(val));
        } catch (NumberFormatException e) {
            showError("Invalid number format");
        }
    }

    private void processOperator(String op) {
        try {
            double inputValue = currentInput.length() > 0 ? Double.parseDouble(currentInput.toString()) : result;
            if (lastOperator.isEmpty()) {
                result = inputValue;
            } else {
                calculate(inputValue);
            }
            lastOperator = op;
            startNewNumber = true;
            currentInput.setLength(0);
            display.setText(formatNumber(result));
        } catch (NumberFormatException e) {
            showError("Invalid number format");
            clear();
        }
    }

    private void calculateResult() {
        try {
            double inputValue = currentInput.length() > 0 ? Double.parseDouble(currentInput.toString()) : result;
            if (!lastOperator.isEmpty()) {
                calculate(inputValue);
                lastOperator = "";
                display.setText(formatNumber(result));
                currentInput.setLength(0);
                currentInput.append(result);
                startNewNumber = true;
            }
        } catch (NumberFormatException e) {
            showError("Invalid number format");
            clear();
        }
    }

    private void calculate(double inputValue) {
        try {
            switch (lastOperator) {
                case "+" -> result += inputValue;
                case "−" -> result -= inputValue;
                case "×" -> result *= inputValue;
                case "÷" -> {
                    if (inputValue != 0) {
                        result /= inputValue;
                    } else {
                        showError("Cannot divide by zero");
                    }
                }
            }
        } catch (Exception e) {
            showError("Calculation error");
            clear();
        }
    }

    private void appendNumber(String num) {
        try {
            if (startNewNumber) {
                currentInput.setLength(0);
                startNewNumber = false;
            }
            if (num.equals(".") && currentInput.toString().contains(".")) {
                return; // Prevent multiple decimals
            }
            currentInput.append(num);
            display.setText(currentInput.toString());
        } catch (Exception e) {
            showError("Error inputting number");
        }
    }

    private void backspace() {
        try {
            if (currentInput.length() > 0) {
                currentInput.deleteCharAt(currentInput.length() - 1);
                if (currentInput.length() == 0) {
                    display.setText("0");
                    startNewNumber = true;
                } else {
                    display.setText(currentInput.toString());
                }
            }
        } catch (Exception e) {
            showError("Error deleting character");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private String formatNumber(double number) {
        if (number == (long) number)
            return String.format("%d", (long) number);
        else
            return String.format("%s", number);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calc = new Calculator();
            calc.setVisible(true);
        });
    }
}
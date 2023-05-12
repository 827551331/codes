package com.wdssdream.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class CalculatorGUI extends JFrame {
    private JTextField textField;

    public CalculatorGUI() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 20));
        textField.setEditable(false);
        add(textField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 4));
        add(buttonPanel, BorderLayout.CENTER);

        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.addActionListener(new ButtonClickListener());
            buttonPanel.add(button);
        }

        pack();
        setLocationRelativeTo(null);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();

            if (buttonText.equals("=")) {
                calculateResult();
            } else {
                textField.setText(textField.getText() + buttonText);
            }
        }

        private void calculateResult() {
            String expression = textField.getText();
            Stack<Double> numberStack = new Stack<>();
            Stack<Character> operatorStack = new Stack<>();

            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i);

                if (Character.isDigit(c) || c == '.') {
                    StringBuilder number = new StringBuilder();

                    while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        number.append(expression.charAt(i));
                        i++;
                    }

                    numberStack.push(Double.parseDouble(number.toString()));
                    i--;
                } else if (isOperator(c)) {
                    while (!operatorStack.isEmpty() && hasPrecedence(c, operatorStack.peek())) {
                        calculate(numberStack, operatorStack);
                    }

                    operatorStack.push(c);
                }
            }

            while (!operatorStack.isEmpty()) {
                calculate(numberStack, operatorStack);
            }

            if (!numberStack.isEmpty()) {
                double result = numberStack.pop();
                textField.setText(String.valueOf(result));
            }
        }

        private boolean isOperator(char c) {
            return c == '+' || c == '-' || c == '*' || c == '/';
        }

        private boolean hasPrecedence(char op1, char op2) {
            return (op2 == '*' || op2 == '/') && (op1 == '+' || op1 == '-');
        }

        private void calculate(Stack<Double> numberStack, Stack<Character> operatorStack) {
            char operator = operatorStack.pop();
            double operand2 = numberStack.pop();
            double operand1 = numberStack.pop();
            double result = 0.0;

            switch (operator) {
                case '+':
                    result = operand1 + operand2;
                    break;
                case '-':
                    result = operand1 - operand2;
                    break;
                case '*':
                    result = operand1 * operand2;
                    break;
                case '/':
                    result = operand1 / operand2;
                    break;
            }

            numberStack.push(result);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorGUI calculator = new CalculatorGUI();
            calculator.setVisible(true);
        });
    }
}



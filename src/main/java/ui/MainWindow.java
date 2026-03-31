package ui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private JTextArea outputArea;
    private JButton orderButton;
    private JButton historyButton;

    public MainWindow() {
        setTitle("NordStew Order");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        // Главная панель
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Текстовое поле
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Панель кнопок
        JPanel buttonPanel = new JPanel();

        orderButton = new JButton("Оформить заказ");
        historyButton = new JButton("Показать историю");

        buttonPanel.add(orderButton);
        buttonPanel.add(historyButton);

        // Добавляем всё на экран
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }
}
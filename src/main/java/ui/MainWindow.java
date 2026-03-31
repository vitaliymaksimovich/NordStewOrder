package ui;

import model.Dish;
import model.NordStew;
import model.Order;
import model.additions.*;

import service.OrderService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainWindow extends JFrame {

    private JTextArea outputArea;
    private JButton orderButton;
    private JButton historyButton;

    private JCheckBox fireSauceCheckBox;
    private JCheckBox meatCheckBox;
    private JCheckBox berriesCheckBox;
    private JCheckBox flatbreadCheckBox;

    private OrderService orderService;

    public MainWindow() {
        orderService = new OrderService();

        setTitle("NordStew Order");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        // ТЕКСТОВОЕ ПОЛЕ
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // ЧЕКБОКСЫ
        JPanel checkBoxPanel = new JPanel(new GridLayout(2, 2));

        fireSauceCheckBox = new JCheckBox("Огненный соус (+10)");
        meatCheckBox = new JCheckBox("Двойная порция оленины (+20)");
        berriesCheckBox = new JCheckBox("Нежные ягоды (+8)");
        flatbreadCheckBox = new JCheckBox("Нордская лепёшка (+7)");

        checkBoxPanel.add(fireSauceCheckBox);
        checkBoxPanel.add(meatCheckBox);
        checkBoxPanel.add(berriesCheckBox);
        checkBoxPanel.add(flatbreadCheckBox);

        panel.add(checkBoxPanel, BorderLayout.NORTH);

        // КНОПКИ
        JPanel buttonPanel = new JPanel();

        orderButton = new JButton("Оформить заказ");
        historyButton = new JButton("Показать историю");

        buttonPanel.add(orderButton);
        buttonPanel.add(historyButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        addCheckBoxListeners();
        addButtonListeners();
    }

    // --- ЛОГИКА ЧЕКБОКСОВ (без if) ---
    private void addCheckBoxListeners() {
        List<JCheckBox> boxes = List.of(
                fireSauceCheckBox,
                meatCheckBox,
                berriesCheckBox,
                flatbreadCheckBox
        );

        boxes.forEach(box ->
                box.addItemListener(e -> updateCheckBoxes())
        );
    }

    private void updateCheckBoxes() {
        List<JCheckBox> boxes = List.of(
                fireSauceCheckBox,
                meatCheckBox,
                berriesCheckBox,
                flatbreadCheckBox
        );

        long selectedCount = boxes.stream()
                .filter(JCheckBox::isSelected)
                .count();

        boolean limitReached = selectedCount >= 3;

        boxes.forEach(box ->
                box.setEnabled(box.isSelected() || !limitReached)
        );
    }

    private void addButtonListeners() {

        orderButton.addActionListener(e -> {
            Dish dish = buildDish();

            String message = "Ваш заказ:\n"
                    + dish.getDescription()
                    + "\nСтоимость: " + dish.getCost() + " септимов\n\nОформляем?";

            int result = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    "Подтверждение",
                    JOptionPane.YES_NO_OPTION
            );

            if (result == JOptionPane.YES_OPTION) {
                Order order = new Order(dish.getDescription(), dish.getCost());

                outputArea.append(order.format() + "\n");

                orderService.saveOrder(order);

                JOptionPane.showMessageDialog(this, "Заказ оформлен!");

                resetCheckBoxes();
            }
        });

        // История заказов
        historyButton.addActionListener(e -> {
            String history = orderService.getHistory();
            outputArea.setText(history);
        });
    }

    // Декораторы
    private Dish buildDish() {
        Dish dish = new NordStew();

        List<JCheckBox> boxes = List.of(
                fireSauceCheckBox,
                meatCheckBox,
                berriesCheckBox,
                flatbreadCheckBox
        );

        List<java.util.function.Function<Dish, Dish>> decorators = List.of(
                FireSauceDecorator::new,
                DoubleMeatDecorator::new,
                BerriesDecorator::new,
                FlatbreadDecorator::new
        );

        for (int i = 0; i < boxes.size(); i++) {
            if (boxes.get(i).isSelected()) {
                dish = decorators.get(i).apply(dish);
            }
        }

        return dish;
    }

    private void resetCheckBoxes() {
        List<JCheckBox> boxes = List.of(
                fireSauceCheckBox,
                meatCheckBox,
                berriesCheckBox,
                flatbreadCheckBox
        );

        boxes.forEach(box -> {
            box.setSelected(false);
            box.setEnabled(true);
        });
    }
}
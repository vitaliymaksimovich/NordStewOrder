package ui;

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

    public MainWindow() {
        setTitle("NordStew Order");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout());

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

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

        JPanel buttonPanel = new JPanel();

        orderButton = new JButton("Оформить заказ");
        historyButton = new JButton("Показать историю");

        buttonPanel.add(orderButton);
        buttonPanel.add(historyButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        addCheckBoxListeners();
    }

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
}
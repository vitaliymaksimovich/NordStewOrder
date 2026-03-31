package ui;

import model.Dish;
import model.NordStew;
import model.Order;
import model.additions.BerriesDecorator;
import model.additions.DoubleMeatDecorator;
import model.additions.FireSauceDecorator;
import model.additions.FlatbreadDecorator;
import service.OrderService;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.function.Function;

public class MainWindow extends JFrame {

    private JTextArea outputArea;
    private JButton orderButton;
    private JButton historyButton;

    private JCheckBox fireSauceCheckBox;
    private JCheckBox meatCheckBox;
    private JCheckBox berriesCheckBox;
    private JCheckBox flatbreadCheckBox;

    private JLabel titleLabel;
    private JLabel totalLabel;
    private JLabel bannerLabel;

    private OrderService orderService;

    public MainWindow() {
        orderService = new OrderService();

        setTitle("NordStew Order");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        updateTotalLabel();
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        bannerLabel = createBannerLabel();
        bannerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel = new JLabel("Выберите добавки к рагу");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel checkBoxPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        fireSauceCheckBox = new JCheckBox("Огненный соус (+10)");
        meatCheckBox = new JCheckBox("Двойная порция оленины (+20)");
        berriesCheckBox = new JCheckBox("Нежные ягоды (+8)");
        flatbreadCheckBox = new JCheckBox("Нордская лепёшка (+7)");

        checkBoxPanel.add(fireSauceCheckBox);
        checkBoxPanel.add(meatCheckBox);
        checkBoxPanel.add(berriesCheckBox);
        checkBoxPanel.add(flatbreadCheckBox);

        totalLabel = new JLabel();
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(Box.createVerticalStrut(5));
        topPanel.add(bannerLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(checkBoxPanel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(totalLabel);
        topPanel.add(Box.createVerticalStrut(10));

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel buttonPanel = new JPanel();

        orderButton = new JButton("Оформить заказ");
        historyButton = new JButton("История заказов");

        buttonPanel.add(orderButton);
        buttonPanel.add(historyButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        addCheckBoxListeners();
        addButtonListeners();
    }

    private JLabel createBannerLabel() {
        URL imageUrl = getClass().getResource("/images/banner.png");

        ImageIcon originalIcon = new ImageIcon(imageUrl);
        Image scaledImage = originalIcon.getImage().getScaledInstance(600, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        return new JLabel(scaledIcon);
    }

    private void addCheckBoxListeners() {
        List<JCheckBox> boxes = getCheckBoxes();

        boxes.forEach(box ->
                box.addItemListener(e -> {
                    updateCheckBoxes();
                    updateCheckBoxStyles();
                    updateTotalLabel();
                })
        );
    }

    private void updateCheckBoxes() {
        List<JCheckBox> boxes = getCheckBoxes();

        long selectedCount = boxes.stream()
                .filter(JCheckBox::isSelected)
                .count();

        boolean limitReached = selectedCount >= 3;

        boxes.forEach(box ->
                box.setEnabled(box.isSelected() || !limitReached)
        );
    }

    private void updateCheckBoxStyles() {
        Color selectedColor = new Color(200, 235, 200);
        Color defaultColor = UIManager.getColor("CheckBox.background");
        Font plainFont = new Font("SansSerif", Font.PLAIN, 13);
        Font boldFont = new Font("SansSerif", Font.BOLD, 13);

        getCheckBoxes().forEach(box -> {
            boolean selected = box.isSelected();
            box.setOpaque(true);
            box.setBackground(selected ? selectedColor : defaultColor);
            box.setFont(selected ? boldFont : plainFont);
        });
    }

    private void updateTotalLabel() {
        Dish dish = buildDish();
        totalLabel.setText("Итоговая стоимость: " + dish.getCost() + " септимов");
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

        historyButton.addActionListener(e -> {
            String filePath = orderService.getHistoryFilePath();

            JOptionPane.showMessageDialog(
                    this,
                    "Ваша история заказов также хранится в:\n" + filePath,
                    "Путь к файлу истории",
                    JOptionPane.INFORMATION_MESSAGE
            );

            String history = orderService.getHistory();
            outputArea.setText(history);
        });
    }

    private Dish buildDish() {
        Dish dish = new NordStew();

        List<JCheckBox> boxes = getCheckBoxes();

        List<Function<Dish, Dish>> decorators = List.of(
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

    private List<JCheckBox> getCheckBoxes() {
        return List.of(
                fireSauceCheckBox,
                meatCheckBox,
                berriesCheckBox,
                flatbreadCheckBox
        );
    }

    private void resetCheckBoxes() {
        getCheckBoxes().forEach(box -> {
            box.setSelected(false);
            box.setEnabled(true);
        });

        updateCheckBoxStyles();
        updateTotalLabel();
    }
}
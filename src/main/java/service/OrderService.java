package service;

import model.Order;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class OrderService {

    private static final String FILE_NAME = "history.txt";

    public void saveOrder(Order order) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(order.format());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getHistory() {
        StringBuilder history = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = reader.readLine()) != null) {
                history.append(line).append("\n");
            }

        } catch (IOException e) {
            history.append("История пуста или файл не найден.");
        }

        return history.toString();
    }

    public String getHistoryFilePath() {
        return new File(FILE_NAME).getAbsolutePath();
    }
}
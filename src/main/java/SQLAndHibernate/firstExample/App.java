package SQLAndHibernate.firstExample;

import java.sql.*;
import java.util.LinkedHashMap;

public class App {
    protected static String URL = "jdbc:mysql://127.0.0.1:3306/skillbox";
    protected static String USERNAME = "root";
    protected static String PASSWORD = "24022002sqlmarian";

    public static void main(String[] args) {
        String sql = """
                Select pl.course_name,
                COUNT(*) / COUNT(DISTINCT MONTH(pl.subscription_date)) as avg_purchase_per_month
                FROM purchaselist pl
                GROUP BY pl.course_name
                ORDER BY avg_purchase_per_month DESC
                """;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            LinkedHashMap<String, Double> courseAvgPerMonth = new LinkedHashMap<>();

            while (resultSet.next()) {
                String courseName = resultSet.getString("course_name");
                Double avgPurchasePerMonth = resultSet.getDouble("avg_purchase_per_month");
                courseAvgPerMonth.put(courseName,avgPurchasePerMonth);
            }

            courseAvgPerMonth.forEach((courseName, avgPurchasePerMonth) -> System.out.println(courseName + " - " + avgPurchasePerMonth));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

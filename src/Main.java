import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.util.Scanner;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String driver = "org.postgresql.Driver";
        String url = "jdbc:postgresql://localhost/postgres";
        String user = "postgres"; // имя пользователя по умолчанию
        String password = "12345"; // пароль к серверу базы данных
        //
        try {
            Class.forName(driver);
            System.out.println("Connected JDBC Driver");
        } catch (ClassNotFoundException e) {
            //
            System.out.println("JDBC Driver is not found. Include it in your library path ");
            throw new RuntimeException("JDBC Driver is not found. Include it in your library path");
        }
        Connection connection = null; // соединение
        Statement statement = null; // оператор запроса
        ResultSet rs = null; // результат запроса
        try {
            connection = DriverManager.getConnection(url, user, password); //открытие соединения с базой данных

            // 1) Ведомость регистратуры для разноса мед.карт по кабнетам на день (? сортировка по кабинету)
            String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());// текущая дата
            System.out.print("Ведомость регистратуры для разноса мед.карт по кабнетам на день ");
            System.out.println(date);
            statement = connection.createStatement(); // оператор запроса
            rs = statement.executeQuery("SELECT * FROM Записи_пациента;");
            while (rs.next()) { // пока есть данные
                if(date.equals(rs.getString("Дата_приема"))) {
                    System.out.print("Кабинет №" + rs.getString("Кабинет") + ": ");
                    System.out.print("мед.карта №" + rs.getString("Карта_пациента") + " в");
                    System.out.print(rs.getString("Время_приема"));
                    System.out.println();
                }
            }
            System.out.println();

            // 2) Расписание работы врачей (? сортировка по ФИО врачей или дате)
            System.out.println("Расписание работы врачей ");
            statement = connection.createStatement(); // оператор запроса
            rs = statement.executeQuery("SELECT * FROM Расписание_врачей;"); //результат запроса на поиск
            while (rs.next()) { // пока есть данные
                System.out.print(rs.getString("ФИО")+"  ");
                System.out.print(rs.getString("Начало_работы")+"  ");
                System.out.print(rs.getString("Конец_работы"));
                System.out.println();
            }
            System.out.println();
            // ?3) Ведомость приема врача за месяц (сколько, когда врач принял пациентов и кого конкретно)
            statement = connection.createStatement(); // оператор запроса
            rs = statement.executeQuery("SELECT * FROM Записи_пациента;"); //результат запроса на поиск
            Scanner in = new Scanner (System.in);
            System.out.println("Введите месяц");
            int m= in.nextInt();//ввод месяца
            System.out.println("Ведомость приема врача за месяц "+m);
            while (rs.next()) { // пока есть данные
                if (rs.getBoolean("Отметка")==true) {
                    System.out.print("Врач " + rs.getString("ФИО_врача"));
                    System.out.print(" принял пациента с мед.картой №"+rs.getString("Карта_пациента") + " ");
                    System.out.print(rs.getString("Время_приема")+" ");
                    System.out.print(rs.getString("Дата_приема"));
                    System.out.println();
                }
            }
            System.out.println();
            // 4) Какая карта чаще всего встречается
            System.out.println("Чаще всего ходит по врачам ");
            statement = connection.createStatement(); // оператор запроса
            rs = statement.executeQuery("SELECT * FROM Записи_пациента;"); //результат запроса на поиск
            int maxFrequent = 0;
            int num = 0;
            int count = rs.getMetaData().getColumnCount();
            for (int i = 0; i < count; i++) {
            }

            // закрываем соединение с базой данных
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


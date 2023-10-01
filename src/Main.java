import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

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

            // 1) Ведомость регистратуры для разноса мед.карт по кабнетам на день
            // сейчас выводит ведомость на сегодняшний день (? сортировка по кабинету)
            String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());// текущая дата
            statement = connection.createStatement(); // оператор запроса
            rs = statement.executeQuery("SELECT * FROM Записи_пациентов;");
            boolean check1=false;
            System.out.print("Ведомость регистратуры для разноса мед.карт по кабнетам на сегодняшний день "+date);
            while (rs.next()) { // пока есть данные
                if (date.equals(rs.getString("Дата_приема"))) {
                    System.out.println();
                    System.out.print("Кабинет №" + rs.getString("Кабинет") + ": ");
                    System.out.print("мед.карта №" + rs.getString("Карта_пациента") + " в");
                    System.out.print(rs.getString("Время_приема"));
                    check1=true;
                }
            }
            if (check1==false){
                System.out.println(" отсутствует");
            }
            System.out.println();
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
            // 3) Ведомость приема врача за месяц (сколько, когда врач принял пациентов и кого конкретно)
            statement = connection.createStatement(); // оператор запроса
            rs = statement.executeQuery("SELECT * FROM Записи_пациентов;"); //результат запроса на поиск
            Scanner scanner = new Scanner (System.in);
            System.out.println("Введите месяц:");
            int m = Integer.parseInt(scanner.nextLine());//ввод месяца
            System.out.println("Введите год:");
            int y = Integer.parseInt(scanner.nextLine());//ввод года
            System.out.println("Введите ФИО врача:");
            String fio = scanner.nextLine();
            boolean check3=false;
            System.out.print("Ведомость приема врача " + fio + " за " + m + " месяц " + y + " года");
            while (rs.next()) { // пока есть данные
                if (rs.getString("ФИО_врача").equals(fio) && (rs.getDate("Дата_приема").getMonth() + 1) == m && (rs.getDate("Дата_приема").getYear() + 1900) == y && rs.getBoolean("Отметка") == true) {
                    System.out.println();
                    System.out.print("Врач " + rs.getString("ФИО_врача"));
                    System.out.print(" принял пациента с мед.картой №" + rs.getString("Карта_пациента") + " ");
                    System.out.print(rs.getString("Дата_приема") + " ");
                    System.out.print(rs.getString("Время_приема"));
                    check3=true;
                    }
                }
            if (check3==false) {
                System.out.println(" отсутствует");
            }
            System.out.println();
            System.out.println();

            // 4) Кто больше всего ходит по врачам
            statement = connection.createStatement(); // оператор запроса
            List<Integer> listOfNumCards = new ArrayList<>();
            rs = statement.executeQuery("SELECT * FROM Записи_пациентов;"); //результат запроса на поиск

            //отбор записей пациентов с отметкой о посещениии true
            while (rs.next()){
                if (rs.getBoolean("Отметка")==true){
                    listOfNumCards.add(rs.getInt("Карта_пациента"));
                }
            }

            //максимальное количество посещений среди всех записей
            int maxCount = 0;

            //список номеров карт поциентов с максимальным числом посещений
            ArrayList <Integer>mostRepeatedPatientsNumbers = new ArrayList<>();

            //проверка наличия посещений
            if (listOfNumCards.isEmpty()){
                System.out.println("Записей пациентов нет");
            } else {

                //нахождения максимального числа посещений пациента у врача
                for (int i : listOfNumCards) {
                    int count = Collections.frequency(listOfNumCards, i); // нахождение количества определённого элемента в коллекции
                    if (count > maxCount) {
                        maxCount = count;
                    }
                }

                //определение номера карт пациентов с максимальным числом посещений
                for (int i : listOfNumCards){
                    int count = Collections.frequency(listOfNumCards, i);
                    if(count == maxCount){
                        if (!(mostRepeatedPatientsNumbers.contains(i))){
                            mostRepeatedPatientsNumbers.add(i);
                        }
                    }
                }

                //вывод пациентов с максимальным числом посещений
                System.out.println("Чаще всего ходит по врачам: ");

                for (int i : mostRepeatedPatientsNumbers){
                    String query = "SELECT * FROM Пациенты WHERE Номер_карты = ?"; //создание запроса на получение записи Пациента с конкретным номером карты
                    PreparedStatement preparedStatement = connection.prepareStatement(query); //создание параметризированного запроса
                    preparedStatement.setInt(1, i); //замена 1-го знака вопроса переменной i (номер карты)
                    rs = preparedStatement.executeQuery();
                    while (rs.next()){
                        System.out.println("Пациент " +rs.getString("ФИО")+" с № карты "+ rs.getString("Номер_карты"));
                    }
                }
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
        // Создание каркаса
        JFrame frame = new JFrame("Клиника");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 200);

        // Создание панели меню и добавление компонентов
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("Ведомость регистратуры");
        JMenu m2 = new JMenu("Расписание работы врачей");
        JMenu m3 = new JMenu("Ведомость приема врача");
        JMenu m4 = new JMenu("Часто ходит по врачам");
        mb.add(m1);
        mb.add(m2);
        mb.add(m3);
        mb.add(m4);
        JMenuItem m11 = new JMenuItem("Открыть");
        m1.add(m11);
        JMenuItem m21 = new JMenuItem("Открыть");
        m2.add(m21);
        JMenuItem m31 = new JMenuItem("Открыть");
        m3.add(m31);
        JMenuItem m41 = new JMenuItem("Открыть");
        m4.add(m41);
        // Создание панели внизу и добавление компонентов
        JPanel panel = new JPanel(); // панель не видна при выводе
        JLabel lb1 = new JLabel("Введите № истории");
        JTextField tf1 = new JTextField(10); // принимает до 10 символов
        JLabel lb2 = new JLabel("Специальность врача");
        JTextField tf2 = new JTextField(20); // принимает до 20 символов
        JButton bt1 = new JButton("Добавить");;
        panel.add(lb1); // Компоненты, добавленные с помощью макета Flow Layout
        panel.add(tf1);
        panel.add(lb2); // Компоненты, добавленные с помощью макета Flow Layout
        panel.add(tf2);
        panel.add(bt1);

        // Текстовая область по центру
        JTextArea ta = new JTextArea();

        // Добавление компонентов в рамку.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);
    }
}
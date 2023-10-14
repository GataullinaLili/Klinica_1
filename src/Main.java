import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.sql.Date;
public class Main {
    // Метод для подключения к базе данных
    private static Connection getConnection() throws SQLException {
        String driver = "org.postgresql.Driver";
        String url = "jdbc:postgresql://localhost/postgres";
        String user = "postgres";// имя пользователя
        String password = "12345";// пароль к серверу базы данных

        try {
            Class.forName(driver);
            System.out.println("Connected JDBC Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver is not found. Include it in your library path ");
            throw new RuntimeException("JDBC Driver is not found. Include it in your library path");
        }

        return DriverManager.getConnection(url, user, password);
    }
    public static void main(String[] args) {

        // Создание каркаса
        JFrame frame = new JFrame("Клиника");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 700);// Размеры
        frame.setLocationRelativeTo(null); // установить JFrame в центре экрана

        // Создание панели меню и добавление компонентов
        JMenuBar mb = new JMenuBar();
        JMenu m01 = new JMenu("Список врачей");
        JMenu m02 = new JMenu("Список пациентов");
        JMenu m03 = new JMenu("Список кабинетов");
        JMenu m04 = new JMenu("Расписание врачей");
        JMenu m05 = new JMenu("Записи пациентов");
        JMenu m06 = new JMenu("Приемы пациентов");
        JMenu m1 = new JMenu("Ведомость регистратуры"); // задание 1
        JMenu m2 = new JMenu("Расписание работы врачей");// задание 2 (m04)
        JMenu m3 = new JMenu("Ведомость приема врача");// задание 3
        JMenu m4 = new JMenu("Часто ходит по врачам");// задание 4
        mb.setBackground(new Color(255, 242, 229));  // Установка цвета текста панели
        mb.add(m01);
        mb.add(m02);
        mb.add(m03);
        mb.add(m04);
        mb.add(m05);
        mb.add(m06);
        mb.add(m1);
        mb.add(m2);
        mb.add(m3);
        mb.add(m4);
        JMenuItem m011 = new JMenuItem("Открыть");
        m01.add(m011);
        JMenuItem m012 = new JMenuItem("Закрыть");
        m01.add(m012);
        JMenuItem m021 = new JMenuItem("Открыть");
        m02.add(m021);
        JMenuItem m022 = new JMenuItem("Закрыть");
        m02.add(m022);
        JMenuItem m031 = new JMenuItem("Открыть");
        m03.add(m031);
        JMenuItem m032 = new JMenuItem("Закрыть");
        m03.add(m032);
        JMenuItem m041 = new JMenuItem("Открыть");
        m04.add(m041);
        JMenuItem m042 = new JMenuItem("Закрыть");
        m04.add(m042);
        JMenuItem m051 = new JMenuItem("Открыть");
        m05.add(m051);
        JMenuItem m052 = new JMenuItem("Закрыть");
        m05.add(m052);
        JMenuItem m061 = new JMenuItem("Открыть");
        m06.add(m061);
        JMenuItem m062 = new JMenuItem("Закрыть");
        m06.add(m062);
        JMenuItem m11 = new JMenuItem("Открыть");
        m1.add(m11);
        JMenuItem m21 = new JMenuItem("Открыть");
        m2.add(m21);
        JMenuItem m31 = new JMenuItem("Открыть");
        m3.add(m31);
        JMenuItem m41 = new JMenuItem("Открыть");
        m4.add(m41);
        JMenuItem m12 = new JMenuItem("Закрыть");
        m1.add(m12);
        JMenuItem m22 = new JMenuItem("Закрыть");
        m2.add(m22);
        JMenuItem m32 = new JMenuItem("Закрыть");
        m3.add(m32);
        JMenuItem m42 = new JMenuItem("Закрыть");
        m4.add(m42);

        //Создание панелей
        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JPanel panel01 = new JPanel();
        JPanel panel02 = new JPanel();
        JPanel panel03 = new JPanel();
        JPanel panel04 = new JPanel();
        JPanel panel05 = new JPanel();
        JPanel panel06 = new JPanel();
        // Видимость панелей
        panel.setVisible(true);
        panel1.setVisible(false);
        panel2.setVisible(false);
        panel3.setVisible(false);
        panel4.setVisible(false);
        panel01.setVisible(false);
        panel02.setVisible(false);
        panel03.setVisible(false);
        panel04.setVisible(false);
        panel05.setVisible(false);
        panel06.setVisible(false);
        panel05.setPreferredSize(new Dimension(1000, 80));// размер панели

        // добавление компонентов (задание 3)
        JLabel lb1 = new JLabel("Введите месяц");
        JTextField tf1 = new JTextField(2); // принимает до 2 символов
        JLabel lb2 = new JLabel("год");
        JTextField tf2 = new JTextField(4); // принимает до 4 символов
        JLabel lb3 = new JLabel("ФИО врача");
        JTextField tf3 = new JTextField(20); // принимает до 20 символов
        JButton bt31 = new JButton("Поиск");
        // Создание текстовых областей
        JTextArea ta = new JTextArea();
        JTextArea ta1 = new JTextArea();
        JTextArea ta2 = new JTextArea();
        JTextArea ta3 = new JTextArea();
        JTextArea ta4 = new JTextArea();
        // Добавление текстовой области в панель
        panel.add(ta);
        panel1.add(ta1);
        panel2.add(ta2);
        panel3.add(ta3);
        panel4.add(ta4);
        // Добавление компонентов в рамку.
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.CENTER, ta);// Текстовая область по центру
        frame.setVisible(true);

        // Создание таблицы для таблицы врачи
        DefaultTableModel model1 = new DefaultTableModel();
        JTable table1 = new JTable(model1);
        table1.setSelectionBackground(Color.pink);
        // добавление компонентов (таблица врачи)
        JLabel lb011 = new JLabel("Добавить врача: id");
        JTextField tf011 = new JTextField(5); // id принимает до 5 символов
        JLabel lb012 = new JLabel("ФИО");
        JTextField tf012 = new JTextField(30); // фио принимает до 30 символов
        JLabel lb013 = new JLabel("Специальность");
        JTextField tf013 = new JTextField(20); // специальность принимает до 20 символов
        JButton add01 = new JButton("Добавить");
        JButton del01 = new JButton("Удалить");

        // Создание таблицы пациенты
        DefaultTableModel model2 = new DefaultTableModel();
        JTable table2 = new JTable(model2);
        table2.setSelectionBackground(Color.pink);// выделение строки розовым
        // добавление компонентов (таблица пациенты)
        JLabel lb021 = new JLabel("Добавить пациента: Номер карты");
        JTextField tf021 = new JTextField(5); // номер карты принимает до 5 символов
        JLabel lb022 = new JLabel("ФИО");
        JTextField tf022 = new JTextField(30); // фио принимает до 30 символов
        JLabel lb023 = new JLabel("Дата рождения");
        JTextField tf023 = new JTextField(8); // полис до 8 символов
        JButton add02 = new JButton("Добавить");
        JButton del02 = new JButton("Удалить");

        // Создание таблицы кабинеты
        DefaultTableModel model3 = new DefaultTableModel();
        JTable table3 = new JTable(model3);
        table3.setSelectionBackground(Color.pink);// выделение строки
        // добавление компонентов (таблица пациенты)
        JLabel lb031 = new JLabel("№ Кабинета");
        JTextField tf031 = new JTextField(3); // номер кабинета до 3 символов
        JButton add03 = new JButton("Добавить");
        JButton del03 = new JButton("Удалить");

        // Создание таблицы расписание врачей
        DefaultTableModel model4 = new DefaultTableModel();
        JTable table4 = new JTable(model4);
        table4.setSelectionBackground(Color.pink);// выделение строки
        // добавление компонентов (таблица Расписание врачей)
        JLabel lb041 = new JLabel("id врача");
        JTextField tf041 = new JTextField(5); // id врача принимает до 5 символов
        JLabel lb042 = new JLabel("ФИО");
        JTextField tf042 = new JTextField(30); // фио принимает до 30 символов
        JLabel lb043 = new JLabel("Специальность");
        JTextField tf043 = new JTextField(20); // специальность до 20 символов
        JLabel lb044 = new JLabel("Начало работы");
        JTextField tf044 = new JTextField(8);
        JLabel lb045 = new JLabel("Конец работы");
        JTextField tf045 = new JTextField(8);
        JLabel lb046 = new JLabel("Кабинет");
        JTextField tf046 = new JTextField(3); // кабинет до 3 символов
        JButton add04 = new JButton("Добавить");
        JButton del04 = new JButton("Удалить");

        // Создание таблицы записи пациентов
        DefaultTableModel model5 = new DefaultTableModel();
        JTable table5 = new JTable(model5);
        table5.setSelectionBackground(Color.pink);// выделение строки
        // добавление компонентов (таблица Записи пациентов)
        JLabel lb051 = new JLabel("ID врача");
        JTextField tf051 = new JTextField(5); // id врача принимает до 5 символов
        JLabel lb052 = new JLabel("ФИО врача");
        JTextField tf052 = new JTextField(30); // фио принимает до 30 символов
        JLabel lb053 = new JLabel("Специальность врача");
        JTextField tf053 = new JTextField(20); // специальность до 20 символов
        JLabel lb054 = new JLabel("Кабинет");
        JTextField tf054 = new JTextField(3);
        JLabel lb055 = new JLabel("№ карты пациента");
        JTextField tf055 = new JTextField(10);
        JLabel lb056 = new JLabel("Дата приема");
        JTextField tf056 = new JTextField(8);
        JLabel lb057 = new JLabel("Время приема");
        JTextField tf057 = new JTextField(6);
        JLabel lb058 = new JLabel("Отметка о посещении");
        JCheckBox tf058 = new JCheckBox();
        JButton add05 = new JButton("Добавить");
        JButton del05 = new JButton("Удалить");

        // Создание таблицы Приемы
        DefaultTableModel model6 = new DefaultTableModel();
        JTable table6 = new JTable(model6);
        table6.setSelectionBackground(Color.pink);// выделение строки
        // добавление компонентов (таблицы Приемы)
        JLabel lb061 = new JLabel("id врача");
        JTextField tf061 = new JTextField(5); // id врача принимает до 5 символов
        JLabel lb062 = new JLabel("ФИО");
        JTextField tf062 = new JTextField(30); // фио принимает до 30 символов
        JLabel lb063 = new JLabel("Специальность");
        JTextField tf063 = new JTextField(20); // специальность до 20 символов
        JLabel lb064 = new JLabel("Начало работы");
        JTextField tf064 = new JTextField(8);
        JLabel lb065 = new JLabel("Конец работы");
        JTextField tf065 = new JTextField(8);
        JLabel lb066 = new JLabel("Кабинет");
        JTextField tf066 = new JTextField(3); // кабинет до 3 символов
        JButton add06 = new JButton("Добавить");
        JButton del06 = new JButton("Удалить");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// формат для даты

        // выводит врачей
        m011.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная-флаг
            @Override
            public void actionPerformed (ActionEvent a){
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel01.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel01);
                    frame.getContentPane().add(BorderLayout.CENTER, table1);
                    panel01.add(lb011);//id врача
                    panel01.add(tf011);
                    panel01.add(lb012);// ФИО
                    panel01.add(tf012);
                    panel01.add(lb013);// Специальность
                    panel01.add(tf013);
                    panel01.add(add01);//добавить
                    panel01.add(del01);//удалить
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
                if (!resultDisplayed) { // проверка, что результат еще не был выведен
                    try {
                        Connection connection = getConnection(); //открытие соединения с базой данных
                        Statement statement = connection.createStatement(); // оператор запроса
                        ResultSet rs = statement.executeQuery("SELECT * FROM Врачи ORDER BY ФИО;");// Сортировка по ФИО
                        Object[] row = new Object[3];
                        model1.addColumn("id");
                        model1.addColumn("ФИО");
                        model1.addColumn("Специальность");
                        model1.addRow(new Object[]{"<html><b>id</b></html>", "<html><b>ФИО</b></html>", "<html><b>Специальнсоть</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                        while (rs.next()) { // пока есть данные
                            String[] rowData = {String.valueOf(rs.getInt("id")), rs.getString("ФИО"), rs.getString("Специальность")};
                            model1.addRow(rowData);
                        }
                        table1.setModel(model1);
                        statement.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    resultDisplayed = true; // установка значения переменной-флага
                }
            }
        });
        //добавление строки в таблице Врачи
        add01.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = getConnection();
                    // Создание SQL-запроса INSERT (добавление)
                    String sql = "INSERT INTO Врачи (id, ФИО, Специальность) VALUES (?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);

                    int id = Integer.parseInt(tf011.getText());// Получаем id из JTextField
                    String fio = tf012.getText();  // Получаем фио из JTextField
                    String spec = tf013.getText();  // Получаем специальность

                    statement.setInt(1, (int) id);// Установка значения id для вставки
                    statement.setString(2, (String) fio);// Установка значения фио
                    statement.setString(3, (String) spec);// Установка значения специальность

                    model1.addRow(new Object[]{id, fio, spec}); // добавление новой строки в таблицу
                    statement.executeUpdate();// обновление таблицы в postgre
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Удаление строки таблицы Врачи
        del01.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i01 = table1.getSelectedRow();// Номер выделенной строки
                try {
                    Connection connection = getConnection();
                    // Создание SQL-запроса delete (на удаление)
                    String sql = "DELETE FROM Врачи WHERE id=? AND ФИО=? AND Специальность=?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    int id = Integer.parseInt(model1.getValueAt(i01, 0).toString());// Получаем id удаляемой записи
                    String fio = (String) model1.getValueAt(i01, 1); // Получаем фио
                    String spec = (String) model1.getValueAt(i01, 2); // Получаем специальность

                    statement.setInt(1, (int) id);// Установка значения id для удаления
                    statement.setString(2, (String) fio);// Установка значения фио
                    statement.setString(3, (String) spec);// Установка значения специальности

                    statement.executeUpdate();// обновление таблицы в postgre
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                model1.removeRow(i01);// Удаление строки из модели таблицы
                model1.fireTableDataChanged(); // Обновление модели таблицы
            }
        });
        m012.addActionListener(new ActionListener() { // закрыть таблицу врачи
            @Override
            public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel.setVisible(true);
                    ta.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel);
                    frame.getContentPane().add(BorderLayout.CENTER, ta);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
            }
        });

        // выводит таблицу пациенты
        m021.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная-флаг
            @Override
            public void actionPerformed (ActionEvent a){
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel02.setVisible(true);
                    table2.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel02);
                    frame.getContentPane().add(BorderLayout.CENTER, table2);
                    panel02.add(lb021);//номер карты
                    panel02.add(tf021);
                    panel02.add(lb022);// ФИО
                    panel02.add(tf022);
                    panel02.add(lb023);// Дата рождения
                    panel02.add(tf023);
                    panel02.add(add02);//добавить
                    panel02.add(del02);//удалить
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
                if (!resultDisplayed) { // проверка, что результат еще не был выведен
                    try {
                        Connection connection = getConnection(); //открытие соединения с базой данных
                        Statement statement = connection.createStatement(); // оператор запроса
                        ResultSet rs = statement.executeQuery("SELECT * FROM Пациенты ORDER BY Номер_карты;");// Сортировка по ФИО
                        Object[] row = new Object[3];
                        model2.addColumn("Номер_карты");
                        model2.addColumn("ФИО");
                        model2.addColumn("Дата_рождения");
                        model2.addRow(new Object[]{"<html><b>Номер карты</b></html>","<html><b>ФИО пациента</b></html>", "<html><b>Дата рождения (год-месяц-день) </b></html>"});//жирный шрифт для 1 строки (название столбцов)
                        while (rs.next()) { // пока есть данные
                            String[] rowData = {String.valueOf(rs.getInt("Номер_карты")), rs.getString("ФИО"), dateFormat.format(rs.getDate("Дата_рождения")),};
                            model2.addRow(rowData);
                        }
                        table2.setModel(model2);
                        statement.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    resultDisplayed = true; // установка значения переменной-флага
                }
            }
        });
        //добавление строки в таблице Пациенты
        add02.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = getConnection();
                    // Создание SQL-запроса INSERT (добавление)
                    String sql = "INSERT INTO Пациенты (Номер_карты, ФИО, Дата_рождения) VALUES (?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    int mk = Integer.parseInt(tf021.getText());// Получаем № карты из JTextField
                    String fio_pac = tf022.getText();  // Получаем фио
                    Date dt = Date.valueOf(tf023.getText()); // Получаем дату рождения

                    statement.setInt(1, (int) mk);// Установка значения № карты для вставки
                    statement.setString(2, (String) fio_pac);// Установка значения фио
                    statement.setDate(3, new java.sql.Date(dt.getTime())); // Установка значения дата рождения

                    model2.addRow(new Object[]{mk, fio_pac, dt}); // добавление новой строки в таблицу
                    statement.executeUpdate();// обновление таблицы в postgre
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Удаление строки таблицы Пациенты
        del02.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i02 = table2.getSelectedRow();// Номер выделенной строки
                try {
                    Connection connection = getConnection();
                    // Создание SQL-запроса delete (на удаление)
                    String sql = "DELETE FROM Пациенты WHERE Номер_карты=? AND ФИО=? AND Дата_рождения=?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    int mk = Integer.parseInt(model2.getValueAt(i02, 0).toString());// Получаем id удаляемой записи
                    String fio = (String) model2.getValueAt(i02, 1); // Получаем фио
                    String birthDateStr = (String) model2.getValueAt(i02, 2); // Получаем дату рождения в виде строки
                    Date dt = Date.valueOf(birthDateStr ); // Преобразуем строку в объект типа java.sql.Date

                    statement.setInt(1, (int) mk);// Установка значения id для удаления
                    statement.setString(2, (String) fio);// Установка значения фио
                    statement.setDate(3, dt);// Установка значения даты рождения

                    statement.executeUpdate();// обновление таблицы в postgre
                    model2.removeRow(i02);// Удаление строки из модели таблицы
                    model2.fireTableDataChanged(); // Обновление модели таблицы
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        m022.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel.setVisible(true);
                    ta.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel);
                    frame.getContentPane().add(BorderLayout.CENTER, ta);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
            }
        });
        // выводит врачей
        m031.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная-флаг
            @Override
            public void actionPerformed (ActionEvent a){
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel03.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel03);
                    frame.getContentPane().add(BorderLayout.CENTER, table3);
                    panel03.add(lb031);//кабинет
                    panel03.add(tf031);
                    panel03.add(add03);//добавить
                    panel03.add(del03);//удалить
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
                if (!resultDisplayed) { // проверка, что результат еще не был выведен
                    try {
                        Connection connection = getConnection(); //открытие соединения с базой данных
                        Statement statement = connection.createStatement(); // оператор запроса
                        ResultSet rs = statement.executeQuery("SELECT * FROM Кабинеты ORDER BY Кабинет;");// Сортировка по кабинетам
                        Object[] row = new Object[1];
                        model3.addColumn("Кабинет");
                        model3.addRow(new Object[]{ "<html><b>№ кабинета</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                        while (rs.next()) { // пока есть данные
                            String[] rowData = {String.valueOf(rs.getInt("Кабинет"))};
                            model3.addRow(rowData);
                        }
                        table3.setModel(model3);
                        statement.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    resultDisplayed = true; // установка значения переменной-флага
                }
            }
        });
        //добавление строки в таблице Кабинеты
        add03.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = getConnection();
                    // Создание SQL-запроса INSERT (добавление)
                    String sql = "INSERT INTO Кабинеты (Кабинет) VALUES (?)";
                    PreparedStatement statement = connection.prepareStatement(sql);

                    int cab = Integer.parseInt(tf031.getText());// Получаем кабинет из JTextField
                    statement.setInt(1, (int) cab);// Установка значения id для вставки
                    model3.addRow(new Object[]{cab}); // добавление новой строки в таблицу
                    statement.executeUpdate();// обновление таблицы в postgre
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Удаление строки таблицы Кабинеты
        del03.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i03 = table3.getSelectedRow();// Номер выделенной строки
                try {
                    Connection connection = getConnection();
                    // Создание SQL-запроса delete (на удаление)
                    String sql = "DELETE FROM Кабинеты WHERE Кабинет=?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    int cab = Integer.parseInt(model3.getValueAt(i03, 0).toString());// Получаем id удаляемой записи
                    statement.setInt(1, (int) cab);// Установка значения кабинета для удаления
                    statement.executeUpdate();// обновление таблицы в postgre
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                model3.removeRow(i03);// Удаление строки из модели таблицы
                model3.fireTableDataChanged(); // Обновление модели таблицы
            }
        });
        m032.addActionListener(new ActionListener() { // закрыть таблицу Кабинеты
            @Override
            public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel.setVisible(true);
                    ta.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel);
                    frame.getContentPane().add(BorderLayout.CENTER, ta);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
            }
        });
        // Расписание врачей
        m041.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная-флаг
            @Override
            public void actionPerformed (ActionEvent a){
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel04.setVisible(true);
                    table4.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel04);
                    frame.getContentPane().add(BorderLayout.CENTER, table4);
                    panel04.add(lb041);//id_врача
                    panel04.add(tf041);
                    panel04.add(lb042);// ФИО
                    panel04.add(tf042);
                    panel04.add(lb043);// специальность
                    panel04.add(tf043);
                    panel04.add(lb044);//начало работы
                    panel04.add(tf044);
                    panel04.add(lb045);// конец работы
                    panel04.add(tf045);
                    panel04.add(lb046);// кабинет
                    panel04.add(tf046);
                    panel04.add(add04);//добавить
                    panel04.add(del04);//удалить
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
                if (!resultDisplayed) { // проверка, что результат еще не был выведен
                    try {
                        Connection connection = getConnection(); //открытие соединения с базой данных
                        Statement statement = connection.createStatement(); // оператор запроса
                        ResultSet rs = statement.executeQuery("SELECT * FROM Расписание_врачей ORDER BY Начало_работы;");// Сортировка по началу работы
                        Object[] row = new Object[6];
                        model4.addColumn("id_врача");
                        model4.addColumn("ФИО");
                        model4.addColumn("Специальность");
                        model4.addColumn("Начало_работы");
                        model4.addColumn("Конец_работы");
                        model4.addColumn("Кабинет");
                        model4.addRow(new Object[]{"<html><b>ID врача</b></html>","<html><b>ФИО</b></html>","<html><b>Специальность</b></html>","<html><b>Начало_работы</b></html>", "<html><b>Конец_работы</b></html>","<html><b>Кабинет</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        while (rs.next()) { // пока есть данные
                            String[] rowData = {String.valueOf(rs.getInt("id_врача")), rs.getString("ФИО"),rs.getString("Специальность"), timestampFormat.format(rs.getTimestamp("Начало_работы")),  timestampFormat.format(rs.getTimestamp("Конец_работы")), String.valueOf(rs.getInt("Кабинет"))};
                            model4.addRow(rowData);
                        }
                        table4.setModel(model4);
                        statement.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    resultDisplayed = true; // установка значения переменной-флага
                }
            }
        });
        //добавление строки в таблице Расписание врачей
        add04.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = getConnection();
                    // Создание SQL-запроса INSERT (добавление)
                    String sql = "INSERT INTO Расписание_врачей (id_врача, ФИО, Специальность, Начало_работы, Конец_работы, Кабинет) VALUES (?,?,?,?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);

                    int id = Integer.parseInt(tf041.getText());// Получаем id из JTextField
                    String fio = tf042.getText();  // Получаем фио
                    String spec = tf043.getText();  // Получаем специальность
                    Timestamp dt1 = Timestamp.valueOf(tf044.getText());  // Получаем Начало работы
                    Timestamp dt2 = Timestamp.valueOf(tf045.getText());  // Получаем Конец_работы
                    int cab = Integer.parseInt(tf046.getText());  // Получаем кабинет

                    statement.setInt(1, (int) id);// Установка значения № карты для вставки
                    statement.setString(2, (String) fio);// Установка значения фио
                    statement.setString(3, (String) spec);// Установка значения специальность
                    statement.setTimestamp(4, new java.sql.Timestamp(dt1.getTime()));// Установка значения начало_работы
                    statement.setTimestamp(5, new java.sql.Timestamp(dt2.getTime()));// Установка значения конец_работы
                    statement.setInt(6, (int) cab);// Установка значения кабинет


                    model4.addRow(new Object[]{id, fio, spec, dt1, dt2, cab}); // добавление новой строки в таблицу
                    statement.executeUpdate();// обновление таблицы в postgre
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Удаление строки таблицы Расписание врачей
        del04.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i04 = table4.getSelectedRow();// Номер выделенной строки
                try {
                    Connection connection = getConnection();
                    // Создание SQL-запроса delete (на удаление)
                    String sql = "DELETE FROM Расписание_врачей WHERE id_врача=? AND ФИО=? AND Специальность=? AND Начало_работы=? AND Конец_работы=? AND Кабинет=?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    int id = Integer.parseInt(model4.getValueAt(i04, 0).toString());// Получаем id удаляемой записи
                    String fio = (String) model4.getValueAt(i04, 1); // Получаем фио
                    String spec = (String) model4.getValueAt(i04, 2); // Получаем специальность
                    String dateBegin = (String) model4.getValueAt(i04, 3); // Получаем начало работы
                    Date dt1 = Date.valueOf(dateBegin); // Преобразуем строку в объект типа java.sql.Date
                    String dateEnd = (String) model4.getValueAt(i04, 4); // Получаем конец работы
                    Date dt2 = Date.valueOf(dateEnd); // Преобразуем строку в объект типа java.sql.Date
                    int cab = Integer.parseInt(model4.getValueAt(i04, 5).toString());// Получаем кабинет

                    statement.setInt(1, (int) id);// Установка значения id для удаления
                    statement.setString(2, (String) fio);// Установка значения фио
                    statement.setString(3, (String) spec);// Установка значения специальности
                    statement.setDate(4, dt1);// Установка значения начало работы
                    statement.setDate(5, dt2);// Установка значения конец работы
                    statement.setInt(6, (int) cab);// Установка значения специальности

                    statement.executeUpdate();// обновление таблицы в postgre
                    model4.removeRow(i04);// Удаление строки из модели таблицы
                    model4.fireTableDataChanged(); // Обновление модели таблицы
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        m042.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel.setVisible(true);
                    ta.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel);
                    frame.getContentPane().add(BorderLayout.CENTER, ta);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
            }
        });
        // Записи пациентов
        m051.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная-флаг
            @Override
            public void actionPerformed (ActionEvent a){
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel05.setVisible(true);
                    table5.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel05);
                    frame.getContentPane().add(BorderLayout.CENTER, table5);
                    panel05.add(lb051);//id_врача
                    panel05.add(tf051);
                    panel05.add(lb052);// ФИО
                    panel05.add(tf052);
                    panel05.add(lb053);// специальность
                    panel05.add(tf053);
                    panel05.add(lb054);//кабинет
                    panel05.add(tf054);
                    panel05.add(lb055);// номер карты
                    panel05.add(tf055);
                    panel05.add(lb056);// дата приема
                    panel05.add(tf056);
                    panel05.add(lb057);// время приема
                    panel05.add(tf057);
                    panel05.add(lb058);// отметка
                    panel05.add(tf058);
                    panel05.add(add05);//добавить
                    panel05.add(del05);//удалить
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
                if (!resultDisplayed) { // проверка, что результат еще не был выведен
                    try {
                        Connection connection = getConnection(); //открытие соединения с базой данных
                        Statement statement = connection.createStatement(); // оператор запроса
                        ResultSet rs = statement.executeQuery("SELECT * FROM Записи_пациентов ORDER BY Дата_приема, Время_приема;");// Сортировка по началу работы
                        Object[] row = new Object[8];
                        model5.addColumn("id_врача");
                        model5.addColumn("ФИО");
                        model5.addColumn("Специальность");
                        model5.addColumn("Кабинет");
                        model5.addColumn("Номер_карты");
                        model5.addColumn("Дата_приема");
                        model5.addColumn("Время_приема");
                        model5.addColumn("Отметка");
                        model5.addRow(new Object[]{"<html><b>ID врача</b></html>","<html><b>ФИО врача</b></html>","<html><b>Специальность</b></html>","<html><b>Кабинет</b></html>","<html><b>Номер карты пациента</b></html>","<html><b>Дата приема</b></html>", "<html><b>Время приема</b></html>","<html><b>Отметка</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        while (rs.next()) { // пока есть данные
                            String[] rowData = {String.valueOf(rs.getInt("id_врача")), rs.getString("ФИО_врача"),rs.getString("Специальность_врача"), String.valueOf(rs.getInt("Кабинет")),String.valueOf(rs.getInt("Карта_пациента")),dateFormat.format(rs.getDate("Дата_приема")),  timeFormat.format(rs.getTime("Время_приема")), String.valueOf(rs.getBoolean("Отметка"))};
                            model5.addRow(rowData);
                        }
                        table5.setModel(model5);
                        statement.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    resultDisplayed = true; // установка значения переменной-флага
                }
            }
        });
//добавление строки в таблице Расписание врачей
        add05.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = getConnection();
                    // Создание SQL-запроса INSERT (добавление)
                    String sql = "INSERT INTO Записи_пациентов (id_врача, ФИО_врача, Специальность_врача, Кабинет, Карта_пациента, Дата_приема, Время_приема,Отметка) VALUES (?,?,?,?,?,?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(sql);

                    int id = Integer.parseInt(tf051.getText());// Получаем id из JTextField
                    String fio = tf052.getText();  // Получаем фио
                    String spec = tf053.getText();  // Получаем специальность
                    int cab = Integer.parseInt(tf054.getText());  // Получаем кабинет
                    int mk = Integer.parseInt(tf055.getText());  // Получаем номер карты
                    Date dt = Date.valueOf(tf056.getText());  // Получаем Дата приема
                    Time time = Time.valueOf(tf057.getText());  // Получаем время приема
                    boolean otmetka = Boolean.parseBoolean(tf058.getText());  // Получаем Отметка

                    statement.setInt(1, (int) id);// Установка значения № карты для вставки
                    statement.setString(2, (String) fio);// Установка значения фио
                    statement.setString(3, (String) spec);// Установка значения специальность
                    statement.setInt(4, (int) cab);// Установка значения кабинет
                    statement.setInt(5, (int) mk);// Установка значения номер карты
                    statement.setDate(6, new java.sql.Date(dt.getTime()));// Установка значения дата приема
                    statement.setTime(7, new java.sql.Time(time.getTime()));// Установка значения время приема
                    statement.setBoolean(8, (boolean) otmetka);// Установка значения отметка

                    model5.addRow(new Object[]{id, fio, spec,cab, mk, dt, time, otmetka}); // добавление новой строки в таблицу
                    statement.executeUpdate();// обновление таблицы в postgre
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

// Удаление строки таблицы Расписание врачей
        del05.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i05 = table5.getSelectedRow();// Номер выделенной строки
                try {
                    Connection connection = getConnection();
                    // Создание SQL-запроса delete (на удаление)
                    String sql = "DELETE FROM Записи_пациентов WHERE id_врача=? AND ФИО_врача=? AND Специальность_врача=? AND Кабинет=? AND Карта_пациента=? AND Дата_приема=? AND Время_приема=? AND Отметка=?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    int id = Integer.parseInt(model5.getValueAt(i05, 0).toString());// Получаем id удаляемой записи
                    String fio = (String) model5.getValueAt(i05, 1); // Получаем фио
                    String spec = (String) model5.getValueAt(i05, 2); // Получаем специальность
                    int cab = Integer.parseInt(model5.getValueAt(i05, 3).toString());// кабинет
                    int mk = Integer.parseInt(model5.getValueAt(i05, 4).toString());// номер карты
                    String d = (String) model5.getValueAt(i05, 5); // Получаем дату приема
                    Date date = Date.valueOf(d); // Преобразуем строку в объект типа java.sql.Date
                    String t= (String) model5.getValueAt(i05, 6); // Получаем время приема
                    Time time = Time.valueOf(t); // Преобразуем строку в объект типа java.sql.Date
                    boolean otmetka = Boolean.parseBoolean(model5.getValueAt(i05, 7).toString());// отметка

                    statement.setInt(1, (int) id);// Установка значения id для удаления
                    statement.setString(2, (String) fio);// Установка значения фио
                    statement.setString(3, (String) spec);// Установка значения специальности
                    statement.setInt(4,(int) cab);// Установка значения кабинета
                    statement.setInt(5, (int)mk);// Установка значения медкарта
                    statement.setDate(6, date);// Установка значения даты
                    statement.setTime(7, time);// Установка значения время
                    statement.setBoolean(8, (boolean) otmetka);// Установка отметки

                    statement.executeUpdate();// обновление таблицы в postgre
                    model5.removeRow(i05);// Удаление строки из модели таблицы
                    model5.fireTableDataChanged(); // Обновление модели таблицы
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        m052.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel05,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel.setVisible(true);
                    ta.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel);
                    frame.getContentPane().add(BorderLayout.CENTER, ta);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
            }
        });
        // выводит таблицу Приемы
        m061.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная-флаг
            @Override
            public void actionPerformed (ActionEvent a){
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea t : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(t);}
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);}
                    panel02.setVisible(true);
                    table2.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel02);
                    frame.getContentPane().add(BorderLayout.CENTER, table2);
                    panel02.add(lb021);//номер карты
                    panel02.add(tf021);
                    panel02.add(lb022);// ФИО
                    panel02.add(tf022);
                    panel02.add(lb023);// полис
                    panel02.add(tf023);
                    panel02.add(add02);//добавить
                    panel02.add(del02);//удалить
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
                if (!resultDisplayed) { // проверка, что результат еще не был выведен
                    try {
                        Connection connection = getConnection(); //открытие соединения с базой данных
                        Statement statement = connection.createStatement(); // оператор запроса
                        ResultSet rs = statement.executeQuery("SELECT * FROM Пациенты ORDER BY Номер_карты;");// Сортировка по ФИО
                        Object[] row = new Object[3];
                        model2.addColumn("Номер_карты");
                        model2.addColumn("ФИО");
                        model2.addColumn("Полис");
                        model2.addRow(new Object[]{"<html><b>Номер карты</b></html>","<html><b>ФИО пациента</b></html>", "<html><b>Полис</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                        while (rs.next()) { // пока есть данные
                            String[] rowData = {String.valueOf(rs.getInt("Номер_карты")), rs.getString("ФИО"), String.valueOf(rs.getInt("Полис"))};
                            model2.addRow(rowData);
                        }
                        table2.setModel(model2);
                        statement.close();
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    resultDisplayed = true; // установка значения переменной-флага
                }
            }
        });
        //добавление строки в таблице Пациенты
        add06.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = getConnection();
                    // Создание SQL-запроса INSERT (добавление)
                    String sql = "INSERT INTO Пациенты (Номер_карты, ФИО, Полис) VALUES (?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);

                    int mk = Integer.parseInt(tf021.getText());// Получаем № карты из JTextField
                    String fio_pac = tf022.getText();  // Получаем фио
                    int polis = Integer.parseInt(tf023.getText());  // Получаем полис

                    statement.setInt(1, (int) mk);// Установка значения № карты для вставки
                    statement.setString(2, (String) fio_pac);// Установка значения фио
                    statement.setInt(3, (int) polis);// Установка значения Полис

                    model2.addRow(new Object[]{mk, fio_pac, polis}); // добавление новой строки в таблицу
                    statement.executeUpdate();// обновление таблицы в postgre
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Удаление строки таблицы Пациенты
        del06.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i02 = table2.getSelectedRow();// Номер выделенной строки
                try {
                    Connection connection = getConnection();
                    // Создание SQL-запроса delete (на удаление)
                    String sql = "DELETE FROM Пациенты WHERE Номер_карты=? AND ФИО=? AND Полис=?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    int mk = Integer.parseInt(model2.getValueAt(i02, 0).toString());// Получаем id удаляемой записи
                    String fio = (String) model2.getValueAt(i02, 1); // Получаем фио
                    int polis = Integer.parseInt(model2.getValueAt(i02, 2).toString());// Получаем полис

                    statement.setInt(1, (int) mk);// Установка значения id для удаления
                    statement.setString(2, (String) fio);// Установка значения фио
                    statement.setInt(3, (int) polis);// Установка значения специальности

                    statement.executeUpdate();// обновление таблицы в postgre
                    model2.removeRow(i02);// Удаление строки из модели таблицы
                    model2.fireTableDataChanged(); // Обновление модели таблицы
                    statement.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        m062.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel.setVisible(true);
                    ta.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel);
                    frame.getContentPane().add(BorderLayout.CENTER, ta);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
            }
        });

        //1) Ведомость регистратуры для разноса мед.карт по кабнетам на день
        // выводит ведомость на сегодняшний день
        m11.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная-флаг
            @Override
            public void actionPerformed (ActionEvent a){
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel1.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel1);
                    frame.getContentPane().add(BorderLayout.CENTER, ta1);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
                if (!resultDisplayed) { // проверка, что результат еще не был выведен
                    try {
                        Connection connection = getConnection(); //открытие соединения с базой данных
                        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());// текущая дата
                        Statement statement = connection.createStatement(); // оператор запроса
                        ResultSet rs = statement.executeQuery("SELECT * FROM Записи_пациентов ORDER BY Кабинет;");// Сортировка по каинетам
                        boolean check1 = false;
                        ta1.append("Ведомость регистратуры для разноса мед.карт по кабнетам на сегодняшний день " + date);
                        while (rs.next()) { // пока есть данные
                            if (date.equals(rs.getString("Дата_приема"))) { // проверка, что дата приема сегодня
                                ta1.append("\n");
                                ta1.append("Кабинет №" + rs.getString("Кабинет") + ": ");
                                ta1.append("мед.карта №" + rs.getString("Карта_пациента") + " в ");
                                ta1.append(rs.getString("Время_приема"));
                                check1 = true;
                            }
                        }
                        if (check1 == false) {
                            ta1.append(" отсутствует");
                        }
                        ta1.append("\n");
                        statement.close();
                        connection.close();
                    } catch (SQLException e) {
                        ta1.append("SQLState: " + e.getSQLState());
                        ta1.append("Error Code: " + e.getErrorCode());
                        ta1.append("Message: " + e.getMessage());
                        e.printStackTrace();
                    }
                    resultDisplayed = true; // установка значения переменной-флага
                }
            }
        });
        m12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel.setVisible(true);
                    ta.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel);
                    frame.getContentPane().add(BorderLayout.CENTER, ta);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
            }
        });

        // 2) Расписание работы врачей
        m21.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная-флаг
            @Override
            public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel2.setVisible(true);
                    ta2.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel2);
                    frame.getContentPane().add(BorderLayout.CENTER, ta2);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
                if (!resultDisplayed) {
                    try {
                        Connection connection = getConnection();
                        Statement statement = connection.createStatement(); // оператор запроса
                        ta2.append("Расписание работы врачей: ");
                        ResultSet rs = statement.executeQuery("SELECT * FROM Расписание_врачей ORDER BY ФИО,Начало_работы ;"); //результат запроса на поиск + сортировка
                        while (rs.next()) { // пока есть данные
                            ta2.append("\nВрач "+rs.getString("ФИО")+" с ");
                            ta2.append(rs.getString("Начало_работы")+" по ");
                            ta2.append(rs.getString("Конец_работы"));
                        }
                        statement.close();
                        connection.close();
                    } catch (SQLException e) {
                        ta2.append("SQLState: " + e.getSQLState());
                        ta2.append("Error Code: " + e.getErrorCode());
                        ta2.append("Message: " + e.getMessage());
                        e.printStackTrace();
                    }
                    resultDisplayed = true; // установка значения переменной-флага
                }
            }
        });
        m22.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel.setVisible(true);
                    ta.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel);
                    frame.getContentPane().add(BorderLayout.CENTER, ta);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
            }
        });
        // 3) Ведомость приема врача за месяц (сколько, когда врач принял пациентов и кого конкретно)
        m31.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel3.setVisible(true);
                    ta3.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel3);
                    frame.getContentPane().add(BorderLayout.CENTER, ta3);
                    panel3.add(lb1); // Компоненты, добавленные с помощью макета Flow Layout
                    panel3.add(tf1);
                    panel3.add(lb2); // Компоненты, добавленные с помощью макета Flow Layout
                    panel3.add(tf2);
                    panel3.add(lb3); // Компоненты, добавленные с помощью макета Flow Layout
                    panel3.add(tf3);
                    panel3.add(bt31);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
            }
        });
        bt31.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная
            @Override
            public void actionPerformed(ActionEvent a) {
                if (!resultDisplayed) { // проверка
                    try {
                        Connection connection = getConnection();//открытие соединения с базой данных
                        Statement statement = connection.createStatement(); // оператор запроса
                        ResultSet rs = statement.executeQuery("SELECT * FROM Записи_пациентов ORDER BY Дата_приема, Время_приема;"); //результат запроса на поиск
                        Scanner scanner = new Scanner(System.in);
                        int m = Integer.parseInt(tf1.getText());// Получаем месяц из JTextField
                        int y = Integer.parseInt(tf2.getText());// Получаем год из JTextField
                        String fio = tf3.getText();  // Получаем текст из JTextField
                        boolean check3 = false;
                        ta3.append("Ведомость приема врача " + fio + " за " + m + " месяц " + y + " года");
                        while (rs.next()) { // пока есть данные
                            if (rs.getString("ФИО_врача").equals(fio) && (rs.getDate("Дата_приема").getMonth() + 1) == m && (rs.getDate("Дата_приема").getYear() + 1900) == y && rs.getBoolean("Отметка") == true) {
                                ta3.append("\n");
                                ta3.append("Врач " + rs.getString("ФИО_врача"));
                                ta3.append(" принял пациента с мед.картой №" + rs.getString("Карта_пациента") + " ");
                                ta3.append(rs.getString("Дата_приема") + " ");
                                ta3.append(rs.getString("Время_приема"));
                                check3 = true;
                            }
                        }
                        if (check3 == false) {
                            ta3.append(" отсутствует");
                        }
                        ta3.append("\n");
                        ta3.append("\n");
                        statement.close();
                        connection.close();
                    } catch (SQLException e) {
                        ta3.append("SQLState: " + e.getSQLState());
                        ta3.append("Error Code: " + e.getErrorCode());
                        ta3.append("Message: " + e.getMessage());
                        e.printStackTrace();
                    }
                    resultDisplayed = true; // установка значения переменной-флага
                }
            }
        });

        m32.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel.setVisible(true);
                    ta.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel);
                    frame.getContentPane().add(BorderLayout.CENTER, ta);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
            }
        });
        // 4) Кто больше всего ходит по врачам
        m41.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная
            @Override
            public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel4.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel4);
                    frame.getContentPane().add(BorderLayout.CENTER, ta4);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
                if (!resultDisplayed) { // проверка
                    try {
                        Connection connection = getConnection();//открытие соединения с базой данных
                        Statement statement = connection.createStatement(); // оператор запроса
                        List<Integer> listOfNumCards = new ArrayList<>();
                        ResultSet rs = statement.executeQuery("SELECT * FROM Записи_пациентов ORDER BY Карта_пациента;"); //результат запроса на поиск
                        while (rs.next()) {//отбор записей пациентов с отметкой о посещениии true
                            if (rs.getBoolean("Отметка") == true) {
                                listOfNumCards.add(rs.getInt("Карта_пациента"));
                            }
                        }
                        int maxCount = 0;//максимальное количество посещений среди всех записей
                        ArrayList<Integer> mostRepeatedPatientsNumbers = new ArrayList<>();//список номеров карт поциентов с максимальным числом посещений
                        if (listOfNumCards.isEmpty()) {//проверка наличия посещений
                            ta4.append("Записей пациентов нет");
                        } else {
                            //нахождения максимального числа посещений пациента у врача
                            for (int i : listOfNumCards) {
                                int count = Collections.frequency(listOfNumCards, i); // нахождение количества определённого элемента в коллекции
                                if (count > maxCount) {
                                    maxCount = count;
                                }
                            }
                            //определение номера карт пациентов с максимальным числом посещений
                            for (int i : listOfNumCards) {
                                int count = Collections.frequency(listOfNumCards, i);
                                if (count == maxCount) {
                                    if (!(mostRepeatedPatientsNumbers.contains(i))) {
                                        mostRepeatedPatientsNumbers.add(i);
                                    }
                                }
                            }
                            ta4.append("Чаще всего ходит по врачам: "); //вывод пациентов с максимальным числом посещений
                            for (int i : mostRepeatedPatientsNumbers) {
                                String query = "SELECT * FROM Пациенты WHERE Номер_карты = ?"; //создание запроса на получение записи Пациента с конкретным номером карты
                                PreparedStatement preparedStatement = connection.prepareStatement(query); //создание параметризированного запроса
                                preparedStatement.setInt(1, i); //замена 1-го знака вопроса переменной i (номер карты)
                                rs = preparedStatement.executeQuery();
                                while (rs.next()) {
                                    ta4.append("\n");
                                    ta4.append("Пациент " + rs.getString("ФИО") + " с № карты " + rs.getString("Номер_карты"));
                                }
                            }
                        }
                        statement.close();
                        connection.close();
                    } catch (SQLException e) {
                        ta4.append("SQLState: " + e.getSQLState());
                        ta4.append("Error Code: " + e.getErrorCode());
                        ta4.append("Message: " + e.getMessage());
                        e.printStackTrace();
                    }
                    resultDisplayed = true;
                }
            }
        });
        m42.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04,panel05, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    JTable[] tables = {table1, table2, table3, table4, table5};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea tt : textAreas) { // удаление текстового поля
                        frame.getContentPane().remove(tt);
                    }
                    for (JTable t : tables) { // удаление таблиц
                        frame.getContentPane().remove(t);
                    }
                    panel.setVisible(true);
                    ta.setVisible(true);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel);
                    frame.getContentPane().add(BorderLayout.CENTER, ta);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
            }
        });

    }
}


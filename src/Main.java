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
        frame.setSize(1400, 700);// Размеры
        frame.setLocationRelativeTo(null); // установить JFrame в центре экрана

        // Создание панели меню и добавление компонентов
        JMenuBar mb = new JMenuBar();
        JMenu m01 = new JMenu("Список врачей");
        JMenu m02 = new JMenu("Список пациентов");
        JMenu m03 = new JMenu("Список кабинетов");
        JMenu m04 = new JMenu("Расписание врачей");
        JMenu m05 = new JMenu("Записи пациентов");
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
        JTextField tf012 = new JTextField(40); // фио принимает до 40 символов
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
        JTextField tf022 = new JTextField(40); // фио принимает до 40 символов
        JLabel lb023 = new JLabel("Полис");
        JTextField tf023 = new JTextField(16); // полис до 16 символов
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
        JLabel lb041 = new JLabel("Добавить расписание врача: id врача");
        JTextField tf041 = new JTextField(5); // id врача принимает до 5 символов
        JLabel lb042 = new JLabel("ФИО");
        JTextField tf042 = new JTextField(40); // фио принимает до 40 символов
        JLabel lb043 = new JLabel("Специальность");
        JTextField tf043 = new JTextField(20); // специальность до 20 символов
        JLabel lb044 = new JLabel("Начало работы");
        JTextField tf044 = new JTextField(14);
        JLabel lb045 = new JLabel("Конец работы");
        JTextField tf045 = new JTextField(14);
        JLabel lb046 = new JLabel("Кабинет");
        JTextField tf046 = new JTextField(3); // кабинет до 3 символов
        JButton add04 = new JButton("Добавить");
        JButton del04 = new JButton("Удалить");

        // Создание таблицы записи пациентов
        DefaultTableModel model5 = new DefaultTableModel();
        JTable table5 = new JTable(model5);
        table5.setSelectionBackground(Color.pink);// выделение строки

        // выводит врачей
        m011.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная-флаг
            @Override
            public void actionPerformed (ActionEvent a){
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel1, panel2, panel3, panel4};
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

        // выводит таблицу пациенты
        m021.addActionListener(new ActionListener() {
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
        add02.addActionListener(new ActionListener() {
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
        del02.addActionListener(new ActionListener() {
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
        m022.addActionListener(new ActionListener() {
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
        // выводит врачей
        m031.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная-флаг
            @Override
            public void actionPerformed (ActionEvent a){
                SwingUtilities.invokeLater(() -> {
                    JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel1, panel2, panel3, panel4};
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
                    panel01.add(lb031);//кабинет
                    panel01.add(tf031);
                    panel01.add(add03);//добавить
                    panel01.add(del03);//удалить
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

                    int cab = Integer.parseInt(tf031.getText());// Получаем id из JTextField
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
                    JPanel[] panels = {panel, panel1, panel2, panel3, panel4};
                    JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                    for (JPanel p : panels) { // закрытие панелей
                        p.setVisible(false);
                        frame.getContentPane().remove(p);
                    }
                    for (JTextArea t : textAreas) { // удаление текстового поля
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

        // 2) Расписание работы врачей
        m21.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная-флаг
            @Override
            public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    panel1.setVisible(false);
                    panel3.setVisible(false);
                    panel4.setVisible(false);
                    panel.setVisible(false);
                    panel2.setVisible(true);
                    frame.getContentPane().remove(panel);
                    frame.getContentPane().remove(ta);
                    frame.getContentPane().remove(panel1);
                    frame.getContentPane().remove(ta1);
                    frame.getContentPane().remove(panel3);
                    frame.getContentPane().remove(ta3);
                    frame.getContentPane().remove(panel4);
                    frame.getContentPane().remove(ta4);
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
                            ta2.append("\n Врач "+rs.getString("ФИО")+" с ");
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
        // 3) Ведомость приема врача за месяц (сколько, когда врач принял пациентов и кого конкретно)
        m31.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    panel1.setVisible(false);
                    panel2.setVisible(false);
                    panel4.setVisible(false);
                    panel.setVisible(false);
                    panel3.setVisible(true);
                    frame.getContentPane().remove(panel);
                    frame.getContentPane().remove(ta);
                    frame.getContentPane().remove(panel1);
                    frame.getContentPane().remove(ta1);
                    frame.getContentPane().remove(panel2);
                    frame.getContentPane().remove(ta2);
                    frame.getContentPane().remove(panel4);
                    frame.getContentPane().remove(ta4);
                    frame.getContentPane().add(BorderLayout.SOUTH, panel3);
                    frame.getContentPane().add(BorderLayout.CENTER, ta3);
                    frame.revalidate();
                    frame.repaint();//Обновление компонентов фрейма
                });
                panel3.add(lb1); // Компоненты, добавленные с помощью макета Flow Layout
                panel3.add(tf1);
                panel3.add(lb2); // Компоненты, добавленные с помощью макета Flow Layout
                panel3.add(tf2);
                panel3.add(lb3); // Компоненты, добавленные с помощью макета Flow Layout
                panel3.add(tf3);
                panel3.add(bt31);
                frame.repaint();//Обновление компонентов фрейма
            }
        });
        bt31.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная-флаг
            @Override
            public void actionPerformed(ActionEvent a) {
                if (!resultDisplayed) { // проверка
                    try {
                        Connection connection = getConnection();//открытие соединения с базой данных
                        Statement statement = connection.createStatement(); // оператор запроса
                        ResultSet rs = statement.executeQuery("SELECT * FROM Записи_пациентов ORDER BY Дата_приема, Время_приема;"); //результат запроса на поиск
                        Scanner scanner = new Scanner (System.in);
                        int m = Integer.parseInt(tf1.getText());// Получаем месяц из JTextField
                        int y = Integer.parseInt(tf2.getText());// Получаем год из JTextField
                        String fio = tf3.getText();  // Получаем текст из JTextField
                        boolean check3=false;
                        ta3.append("Ведомость приема врача " + fio + " за " + m + " месяц " + y + " года");
                        while (rs.next()) { // пока есть данные
                            if (rs.getString("ФИО_врача").equals(fio) && (rs.getDate("Дата_приема").getMonth() + 1) == m && (rs.getDate("Дата_приема").getYear() + 1900) == y && rs.getBoolean("Отметка") == true) {
                                ta3.append("\n");
                                ta3.append("Врач " + rs.getString("ФИО_врача"));
                                ta3.append(" принял пациента с мед.картой №" + rs.getString("Карта_пациента") + " ");
                                ta3.append(rs.getString("Дата_приема") + " ");
                                ta3.append(rs.getString("Время_приема"));
                                check3=true;
                            }
                        }
                        if (check3==false) {
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
        // 4) Кто больше всего ходит по врачам
        m41.addActionListener(new ActionListener() {
            boolean resultDisplayed = false; // переменная
            @Override
            public void actionPerformed(ActionEvent a) {
                SwingUtilities.invokeLater(() -> {
                    panel1.setVisible(false);
                    panel2.setVisible(false);
                    panel3.setVisible(false);
                    panel.setVisible(false);
                    panel4.setVisible(true);
                    frame.getContentPane().remove(panel);
                    frame.getContentPane().remove(ta);
                    frame.getContentPane().remove(panel2);
                    frame.getContentPane().remove(ta2);
                    frame.getContentPane().remove(panel3);
                    frame.getContentPane().remove(ta3);
                    frame.getContentPane().remove(panel1);
                    frame.getContentPane().remove(ta1);
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

    }
}


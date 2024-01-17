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

public class Main extends JFrame {
    private String currentUserProfile;
    private JMenuBar mb1 = new JMenuBar();
    private JMenuBar mbA = new JMenuBar(); //меню админ
    private JMenuBar mbR = new JMenuBar();//меню регистратор
    private JMenuBar mbP = new JMenuBar(); //меню пациент
    private JMenuBar mbD = new JMenuBar(); //меню врач

    //Создание панелей
    private JPanel panel = new JPanel();
    private JPanel panel1 = new JPanel();
    private JPanel panel2 = new JPanel();
    private JPanel panel3 = new JPanel();
    private JPanel panel4 = new JPanel();
    private JPanel panel01 = new JPanel();
    private JPanel panel02 = new JPanel();
    private JPanel panel03 = new JPanel();
    private JPanel panel04 = new JPanel();
    private JPanel panel05 = new JPanel();
    private JPanel panel06 = new JPanel();
    // Создание текстовых областей
    private JTextArea ta = new JTextArea();
    private JTextArea ta1 = new JTextArea();
    private JTextArea ta2 = new JTextArea();
    private JTextArea ta3 = new JTextArea();
    private JTextArea ta4 = new JTextArea();

    // Создание таблицы врачи (1)
    private DefaultTableModel model1 = new DefaultTableModel();
    private JTable table1 = new JTable(model1);
    // Создание таблицы пациенты (2)
    private DefaultTableModel model2 = new DefaultTableModel();
    private JTable table2 = new JTable(model2);
    // Создание таблицы Кабинеты (3)
    private DefaultTableModel model3 = new DefaultTableModel();
    private JTable table3 = new JTable(model3);
    // Создание таблицы расписание врачей (4)
    private DefaultTableModel model4 = new DefaultTableModel();
    private JTable table4 = new JTable(model4);
    // Создание таблицы записи пациентов (5)
    private DefaultTableModel model5 = new DefaultTableModel();
    private JTable table5 = new JTable(model5);
    // Создание таблицы Прием (6)
    private DefaultTableModel model6 = new DefaultTableModel();
    private JTable table6 = new JTable(model6);
    // таблица приемы
    private JLabel lb061 = new JLabel("ID врача");
    private JTextField tf061 = new JTextField(5); // id врача принимает до 5 символов
    private JLabel lb062 = new JLabel("№ карты пациента");
    private JTextField tf062 = new JTextField(10);
    private JLabel lb063 = new JLabel("№ кабинета");
    private JTextField tf063 = new JTextField(10);
    private JLabel lb064 = new JLabel("Дата приема");
    private JTextField tf064 = new JTextField(10);
    private JLabel lb065 = new JLabel("Время приема");
    private JTextField tf065 = new JTextField(6);
    private JLabel lb066 = new JLabel("Отметка о посещении");
    private JCheckBox tf066 = new JCheckBox();
    private JLabel lb067 = new JLabel("Заключение");
    private JTextField tf067 = new JTextField(30); // принимает до 30 символов
    private JLabel lb068 = new JLabel("Рекомендации");
    private JTextField tf068 = new JTextField(50); // до 50 символов
    private JButton add06 = new JButton("Добавить");
    private JButton del06 = new JButton("Удалить");
    private JButton update06 = new JButton("Обновить");

    private static Connection getConnection() throws SQLException {
        String driver = "org.postgresql.Driver";
        String url = "jdbc:postgresql://localhost/Klinica";
        String user = "postgres";// имя пользователя
        String password = "password";// пароль к серверу базы данных

        try {
            Class.forName(driver);
            System.out.println("Connected JDBC Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver is not found. Include it in your library path ");
            throw new RuntimeException("JDBC Driver is not found. Include it in your library path");
        }

        return DriverManager.getConnection(url, user, password);
    }

    public Main() {
        // Создание каркаса
        super("Клиника");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 700);
        setLocationRelativeTo(null); //установить JFrame в центре экран

        // Создание меню и добавление пунктов
        JMenu profileMenu = new JMenu("Выбор профиля");
        JMenuItem doctorItem = new JMenuItem("Врач");
        JMenuItem registrItem = new JMenuItem("Регистратор");
        JMenuItem patientItem = new JMenuItem("Пациент");
        JMenuItem adminItem = new JMenuItem("Администратор");
        profileMenu.add(doctorItem);
        profileMenu.add(registrItem);
        profileMenu.add(patientItem);
        profileMenu.add(adminItem);
        mb1.add(profileMenu);
        setJMenuBar(mb1);

        // Обработчики для выбора профиля
        doctorItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentUserProfile = "Врач";
                displayDoctorPanel();
            }
        });
        registrItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentUserProfile = "Регистратор";
                displayRegistrPanel();
            }
        });
        patientItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentUserProfile = "Пациент";
                displayPatientPanel();
            }
        });
        adminItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentUserProfile = "Администратор";
                displayAdminPanel();
            }
        });
        setVisible(true);
        // Отображение сообщения с предложением авторизоваться
        JOptionPane.showMessageDialog(null, "Выберите профиль и выполните авторизацию");

    }

    private void displayDoctorPanel() {// ДОКТОР
        String result = checkAuthorization();        // Проверка авторизации
        if (!result.equals("0")) {
            // закрытие меню
            JMenuBar[] menus = {mbA, mbR, mbD, mbP};
            for (JMenuBar m : menus) { // закрытие меню
                m.setVisible(false);
                getContentPane().remove(m);
            }
            mbD.setVisible(true);

            JMenu m06 = new JMenu("Приемы");
            JMenu m1 = new JMenu("Мед.карты сегодня"); // задание 1
            JMenu m2 = new JMenu("Расписание работы");// задание 2 (m04)
            JMenu m3 = new JMenu("Ведомость за месяц");// задание 3
            mbD.setBackground(new Color(255, 242, 229));  // Установка цвета текста панели
            mbD.add(m06);
            mbD.add(m1);
            mbD.add(m2);
            mbD.add(m3);
            JMenuItem m11 = new JMenuItem("Открыть");
            m1.add(m11);
            JMenuItem m21 = new JMenuItem("Открыть");
            m2.add(m21);
            JMenuItem m31 = new JMenuItem("Открыть");
            m3.add(m31);
            JMenuItem m12 = new JMenuItem("Закрыть");
            m1.add(m12);
            JMenuItem m22 = new JMenuItem("Закрыть");
            m2.add(m22);
            JMenuItem m32 = new JMenuItem("Закрыть");
            m3.add(m32);
            // Видимость панелей
            JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
            for (JPanel p : panels) { // закрытие панелей
                p.setVisible(false);
            }
            panel.setVisible(true);
            panel04.setPreferredSize(new Dimension(1000, 80));// размер панели
            panel05.setPreferredSize(new Dimension(1000, 80));// размер панели
            panel06.setPreferredSize(new Dimension(1000, 80));// размер панели
            // Добавление текстовой области в панель
            panel.add(ta);
            panel1.add(ta1);
            panel2.add(ta2);
            panel3.add(ta3);
            panel4.add(ta4);

            // добавление компонентов (задание 3)
            JLabel lb1 = new JLabel("Введите месяц");
            JTextField tf1 = new JTextField(2); // принимает до 2 символов
            JLabel lb2 = new JLabel("год");
            JTextField tf2 = new JTextField(4); // принимает до 4 символов
            JButton bt31 = new JButton("Поиск");

            // Создание таблицы Прием (6)
            DefaultTableModel model6 = new DefaultTableModel();
            JTable table6 = new JTable(model6);
            table6.setSelectionBackground(Color.pink);// выделение строки

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// формат для даты
            // Добавление компонентов в рамку.
            getContentPane().add(BorderLayout.NORTH, mbD);
            getContentPane().add(BorderLayout.SOUTH, panel);
            getContentPane().add(BorderLayout.CENTER, ta);// Текстовая область по центру
            setVisible(true);
            table1.setSelectionBackground(Color.pink);

            // Таблица "Прием"
            m06.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг

                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel06.setVisible(true);
                        table6.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel06);
                        getContentPane().add(BorderLayout.CENTER, table6);
                        panel06.add(lb061);//id_врача
                        panel06.add(tf061);
                        panel06.add(lb062);// номер_карты
                        panel06.add(tf062);
                        panel06.add(lb063);// номер_кабинета
                        panel06.add(tf063);
                        panel06.add(lb064);// дата приема
                        panel06.add(tf064);
                        panel06.add(lb065);// время приема
                        panel06.add(tf065);
                        panel06.add(lb066);// отметка
                        panel06.add(tf066);
                        panel06.add(lb067);// заключение
                        panel06.add(tf067);
                        panel06.add(lb068);// рекомендации
                        panel06.add(tf068);
                        panel06.add(add06);//добавить
                        panel06.add(del06);//удалить
                        panel06.add(update06);//обновить
                        panel06.add(update06);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка, что результат еще не был выведен
                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            Statement statement = connection.createStatement(); // оператор запроса
                            ResultSet rs = statement.executeQuery("SELECT * FROM Прием_пациентов ORDER BY Дата_приема, Время_приема;");// Сортировка по началу работы
                            Object[] row = new Object[8];
                            model6.addColumn("id_врача");
                            model6.addColumn("Карта_пациента");
                            model6.addColumn("Номер_кабинета");
                            model6.addColumn("Дата_приема");
                            model6.addColumn("Время_приема");
                            model6.addColumn("Отметка");
                            model6.addColumn("Заключение");
                            model6.addColumn("Рекомендации");
                            model6.addRow(new Object[]{"<html><b>ID врача</b></html>",  "<html><b>Номер карты пациента</b></html>", "<html><b>Номер кабинета</b></html>","<html><b>Дата приема</b></html>", "<html><b>Время приема</b></html>", "<html><b>Отметка</b></html>","<html><b>Заключение</b></html>", "<html><b>Рекомендации</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            while (rs.next()) { // пока есть данные
                                String[] rowData = {String.valueOf(rs.getInt("id_врача")),  String.valueOf(rs.getInt("Карта_пациента")),String.valueOf(rs.getInt("Номер_кабинета")), dateFormat.format(rs.getDate("Дата_приема")), timeFormat.format(rs.getTime("Время_приема")), String.valueOf(rs.getBoolean("Отметка")), rs.getString("Заключение"), rs.getString("Рекомендации")};
                                model6.addRow(rowData);
                            }
                            table6.setModel(model6);
                            statement.close();
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        resultDisplayed = true; // установка значения переменной-флага
                    }
                }
            });
//добавление строки в таблице прием
            add06.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        Connection connection = getConnection();
                        // Создание SQL-запроса INSERT (добавление)
                        String sql = "INSERT INTO Прием_пациентов (id_врача, Карта_пациента, Номер_кабинета, Дата_приема, Время_приема, Отметка, Заключение, Рекомендации) VALUES (?,?,?,?,?,?,?,?)";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int id = Integer.parseInt(tf061.getText());// Получаем id из JTextField
                        int mk = Integer.parseInt(tf062.getText());  // Получаем номер карты
                        int cab = Integer.parseInt(tf063.getText());  // Получаем номер кабинета
                        Date dt = Date.valueOf(tf064.getText());  // Получаем Дата приема
                        Time time = Time.valueOf(tf065.getText());  // Получаем время приема
                        boolean otmetka = Boolean.parseBoolean(tf066.getText());  // Получаем Отметка
                        String zac = tf067.getText();  // Получаем заключение
                        String rec = tf068.getText();  // Получаем рекомендации


                        statement.setInt(1, (int) id);// Установка значения № карты для вставки
                        statement.setInt(2, (int) mk);// Установка значения номер карты
                        statement.setInt(3, (int) cab);// Установка значения номер карты
                        statement.setDate(4, new java.sql.Date(dt.getTime()));// Установка значения дата приема
                        statement.setTime(5, new java.sql.Time(time.getTime()));// Установка значения время приема
                        statement.setBoolean(6, (boolean) otmetka);// Установка значения отметка
                        statement.setString(7, (String) zac);// Установка значения заключения
                        statement.setString(8, (String) rec);// Установка знач рекомендации


                        model6.addRow(new Object[]{id, mk,cab, dt, time,otmetka, zac, rec}); // добавление новой строки в таблицу
                        statement.executeUpdate();// обновление таблицы в postgre
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            del06.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int i06 = table6.getSelectedRow();// Номер выделенной строки
                    try {
                        Connection connection = getConnection();
                        // Создание SQL-запроса delete (на удаление)
                        String sql = "DELETE FROM Прием_пациентов WHERE id_врача=? AND Карта_пациента=? AND Номер_кабинета=? AND Дата_приема=? AND Время_приема=? AND Отметка=? AND Заключение=? AND Рекомендации=? ";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int id = Integer.parseInt(model6.getValueAt(i06, 0).toString());// Получаем id удаляемой записи
                        int mk = Integer.parseInt(model6.getValueAt(i06, 1).toString());// номер карты
                        int cab = Integer.parseInt(model6.getValueAt(i06, 2).toString());// номер кабинета
                        String d = (String) model6.getValueAt(i06, 3); // Получаем дату приема
                        Date date = Date.valueOf(d); // Преобразуем строку в объект типа java.sql.Date
                        String t = (String) model6.getValueAt(i06, 4); // Получаем время приема
                        Time time = Time.valueOf(t); // Преобразуем строку в объект типа java.sql.Date
                        boolean otmetka = Boolean.parseBoolean(model6.getValueAt(i06, 5).toString());// отметка
                        String zac = (String) model6.getValueAt(i06, 6); // Получаем заключение
                        String rec = (String) model6.getValueAt(i06, 7); // Получаем рекомендацию

                        statement.setInt(1, (int) id);// Установка значения id для удаления
                        statement.setInt(2, (int) mk);// Установка значения медкарта
                        statement.setInt(3, (int) cab);// Установка значения кабинета
                        statement.setDate(4, date);// Установка значения даты
                        statement.setTime(5, time);// Установка значения время
                        statement.setBoolean(6, (boolean) otmetka);// Установка отметки
                        statement.setString(7, (String) zac);// Установка значения заключения
                        statement.setString(8, (String) rec);// Установка значения рекомендации

                        statement.executeUpdate();// обновление таблицы в postgres
                        model6.removeRow(i06);// Удаление строки из модели таблицы
                        model6.fireTableDataChanged(); // Обновление модели таблицы
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            update06.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int i06 = table6.getSelectedRow();// Номер выделенной строки
                    try {
                        Connection connection = getConnection();
// Создание SQL-запроса update (на обновление данных)
                        String sql = "UPDATE Прием_пациентов WHERE id_врача=? AND Карта_пациента=? AND Номер_кабинета=? AND Дата_приема=? AND Время_приема=?";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        boolean otmetka = Boolean.parseBoolean(model6.getValueAt(i06, 5).toString());// отметка
                        int id = Integer.parseInt(model6.getValueAt(i06, 0).toString());// Получаем id обновляемой записи
                        int mk = Integer.parseInt(model6.getValueAt(i06, 1).toString());// номер карты
                        int cab = Integer.parseInt(model6.getValueAt(i06, 2).toString());// номер кабинета
                        String d = (String) model6.getValueAt(i06, 3); // Получаем дату приема
                        Date date = Date.valueOf(d); // Преобразуем строку в объект типа java.sql.Date
                        String t = (String) model6.getValueAt(i06, 4); // Получаем время приема
                        Time time = Time.valueOf(t); // Преобразуем строку в объект типа java.sql.Date
                        statement.setBoolean(1, (boolean) otmetka);// Установка отметки
                        statement.setInt(2, (int) id);// Установка значения id для обновления
                        statement.setInt(3, (int) mk);// Установка значения медкарта
                        statement.setInt(4, (int) cab);// Установка значения кабинета
                        statement.setDate(5, date);// Установка значения даты
                        statement.setTime(6, time);// Установка значения время

                        statement.executeUpdate();// обновление таблицы в postgres
                        model6.setValueAt(otmetka, i06, 8);// Обновление значения отметки в модели таблицы
                        model6.fireTableDataChanged(); // Обновление модели таблицы
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            //1) Ведомость регистратуры для разноса мед.карт по кабинетам на день
            // выводит ведомость на сегодняшний день
            m11.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг

                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel1.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel1);
                        getContentPane().add(BorderLayout.CENTER, ta1);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка, что результат еще не был выведен
                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());// текущая дата
                            Statement statement = connection.createStatement(); // оператор запроса
                            // Добавленное условие для проверки id_врача из таблицы "Приемы_пациентов" равен id_врача из таблицы "Врачи"
                            ResultSet rs1 = statement.executeQuery("SELECT id_врача FROM Врачи WHERE Логин = '" + result + "'");
                            int doctorId = -1;
                            if (rs1.next()) {
                                doctorId = rs1.getInt("id_врача");
                            }
                            rs1.close();

                            if (doctorId != -1) { // Если id_врача был найден
                                ResultSet rs = statement.executeQuery("SELECT * FROM Прием_пациентов WHERE id_врача = " + doctorId + " ORDER BY Время_приема;");// Сортировка по кабинетам
                                boolean check1 = false;

                                ta1.append("Ведомость регистратуры для разноса мед.карт по кабинетам на сегодняшний день " + date);
                                while (rs.next()) { // пока есть данные
                                    if (date.equals(rs.getString("Дата_приема"))) { // проверка, что дата приема сегодня
                                        ta1.append("\n");
                                        ta1.append("Мед.карта №" + rs.getString("Карта_пациента") + " в ");
                                        ta1.append(rs.getString("Время_приема"));
                                        check1 = true;
                                    }
                                }
                                if (!check1) {
                                    ta1.append(" отсутствует");
                                }
                                rs.close();
                            }
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
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel.setVisible(true);
                        ta.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel);
                        getContentPane().add(BorderLayout.CENTER, ta);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                }
            });

            // 3) Ведомость приема врача за месяц (сколько, когда врач принял пациентов и кого конкретно)
            m31.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг
                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel3.setVisible(true);
                        ta3.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel3);
                        getContentPane().add(BorderLayout.CENTER, ta3);
                        panel3.add(lb1); // Компоненты, добавленные с помощью макета Flow Layout
                        panel3.add(tf1);
                        panel3.add(lb2); // Компоненты, добавленные с помощью макета Flow Layout
                        panel3.add(tf2);
                        panel3.add(bt31);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                }
            });
            bt31.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная
                @Override
                public void actionPerformed(ActionEvent a) {
                    if (!resultDisplayed) { // проверка

                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            Statement statement = connection.createStatement(); // оператор запроса
                            // Добавленное условие для проверки id_врача из таблицы "Приемы_пациентов" равен id_врача из таблицы "Врачи"
                            ResultSet rs1 = statement.executeQuery("SELECT id_врача FROM Врачи WHERE Логин = '" + result + "'");
                            int doctorId = -1;
                            if (rs1.next()) {
                                doctorId = rs1.getInt("id_врача");
                            }
                            rs1.close();

                            if (doctorId != -1) { // Если id_врача был найден
                                ResultSet rs = statement.executeQuery("SELECT * FROM Прием_пациентов WHERE id_врача = " + doctorId + " ORDER BY Дата_приема, Время_приема;");// Сортировка по кабинетам
                                Scanner scanner = new Scanner(System.in);
                                int m = Integer.parseInt(tf1.getText());// Получаем месяц из JTextField
                                int y = Integer.parseInt(tf2.getText());// Получаем год из JTextField
                                boolean check3 = false;

                                ta3.append("Ведомость приемов за " + m + " месяц " + y + " года:");
                                while (rs.next()) { // пока есть данные
                                    if ((rs.getDate("Дата_приема").getMonth() + 1) == m && (rs.getDate("Дата_приема").getYear() + 1900) == y && rs.getBoolean("Отметка") == true) {
                                        ta3.append("\n");
                                        ta3.append("Пациент с мед.картой №" + rs.getString("Карта_пациента") + " ");
                                        ta3.append(rs.getString("Дата_приема") + " ");
                                        ta3.append(rs.getString("Время_приема"));
                                        check3 = true;
                                    }
                                }
                                if (check3 == false) {
                                    ta3.append(" отсутствует");

                                }
                                rs.close();
                            }
                            statement.close();
                            connection.close();
                        } catch (SQLException e) {
                            ta3.append("SQLState: " + e.getSQLState());
                            ta3.append("Error Code: " + e.getErrorCode());
                            ta3.append("Message: " + e.getMessage());
                            e.printStackTrace();
                        }
                        resultDisplayed = true; // установка значения переменной-флага
                    }}
            });
            m32.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel.setVisible(true);
                        ta.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel);
                        getContentPane().add(BorderLayout.CENTER, ta);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                }
            });
        }
    }

    private void displayRegistrPanel() {// РЕГИСТРАТОР
        String result = checkAuthorization();        // Проверка авторизации
        if (!result.equals("0")) {
            // закрытие меню
            JMenuBar[] menus = {mbA, mbR, mbD, mbP};
            for (JMenuBar m : menus) { // закрытие меню
                m.setVisible(false);
                getContentPane().remove(m);
            }
            mbR.setVisible(true);

            JMenu m04 = new JMenu("Расписание врачей");
            JMenu m06 = new JMenu("Записи пациентов");
            JMenu m1 = new JMenu("Ведомость регистратуры");
            mbR.setBackground(new Color(255, 242, 229));  // Установка цвета текста панели
            mbR.add(m04);
            mbR.add(m06);
            mbR.add(m1);
            JMenuItem m11 = new JMenuItem("Открыть");
            m1.add(m11);
            JMenuItem m12 = new JMenuItem("Закрыть");
            m1.add(m12);
            // Видимость панелей
            JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
            for (JPanel p : panels) { // закрытие панелей
                p.setVisible(false);
            }
            panel.setVisible(true);
            panel04.setPreferredSize(new Dimension(1000, 80));// размер панели
            panel05.setPreferredSize(new Dimension(1000, 80));// размер панели
            panel06.setPreferredSize(new Dimension(1000, 80));// размер панели
            // Добавление текстовой области в панель
            panel.add(ta);
            panel1.add(ta1);
            panel2.add(ta2);
            panel3.add(ta3);
            panel4.add(ta4);

            // Создание таблицы расписание врачей (4)
            DefaultTableModel model4 = new DefaultTableModel();
            JTable table4 = new JTable(model4);
            table4.setSelectionBackground(Color.pink);// выделение строки
            // добавление компонентов (таблица Расписание врачей)
            JLabel lb041 = new JLabel("id врача");
            JTextField tf041 = new JTextField(10); // id врача принимает до 5 символов
            JLabel lb042 = new JLabel("Начало работы");
            JTextField tf042 = new JTextField(10);
            JLabel lb043 = new JLabel("Конец работы");
            JTextField tf043 = new JTextField(10);
            JLabel lb044 = new JLabel("Номер кабинета");
            JTextField tf044 = new JTextField(3); // Номер_кабинета до 3 символов
            JButton add04 = new JButton("Добавить");
            JButton del04 = new JButton("Удалить");
            JButton update04 = new JButton("Обновить");

            // Создание таблицы Прием (6)
            DefaultTableModel model6 = new DefaultTableModel();
            JTable table6 = new JTable(model6);
            table6.setSelectionBackground(Color.pink);// выделение строки

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// формат для даты
            // Добавление компонентов в рамку.
            getContentPane().add(BorderLayout.NORTH, mbR);
            getContentPane().add(BorderLayout.SOUTH, panel);
            getContentPane().add(BorderLayout.CENTER, ta);// Текстовая область по центру
            setVisible(true);
            table1.setSelectionBackground(Color.pink);

            // Расписание врачей
            m04.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг

                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel04.setVisible(true);
                        table4.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel04);
                        getContentPane().add(BorderLayout.CENTER, table4);
                        panel04.add(lb041);//id_врача
                        panel04.add(tf041);
                        panel04.add(lb042);//начало работы
                        panel04.add(tf042);
                        panel04.add(lb043);// конец работы
                        panel04.add(tf043);
                        panel04.add(lb044);// Номер_кабинета
                        panel04.add(tf044);
                        panel04.add(add04);//добавить
                        panel04.add(del04);//удалить
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка, что результат еще не был выведен
                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            Statement statement = connection.createStatement(); // оператор запроса
                            ResultSet rs = statement.executeQuery("SELECT * FROM Расписание_врачей ORDER BY Начало_работы;");// Сортировка по началу работы
                            Object[] row = new Object[6];
                            model4.addColumn("id_врача");
                            model4.addColumn("Начало_работы");
                            model4.addColumn("Конец_работы");
                            model4.addColumn("Номер_кабинета");
                            model4.addRow(new Object[]{"<html><b>ID врача</b></html>", "<html><b>Начало работы</b></html>", "<html><b>Конец работы</b></html>", "<html><b>Номер кабинета</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                            SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            while (rs.next()) { // пока есть данные
                                String[] rowData = {String.valueOf(rs.getInt("id_врача")), timestampFormat.format(rs.getTimestamp("Начало_работы")), timestampFormat.format(rs.getTimestamp("Конец_работы")), String.valueOf(rs.getInt("Номер_кабинета"))};
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
                        String sql = "INSERT INTO Расписание_врачей (id_врача, Начало_работы, Конец_работы, Номер_кабинета) VALUES (?,?,?,?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(sql);

                        int id = Integer.parseInt(tf041.getText());// Получаем id из JTextField
                        Timestamp dt1 = Timestamp.valueOf(tf042.getText());  // Получаем Начало работы
                        Timestamp dt2 = Timestamp.valueOf(tf043.getText());  // Получаем Конец_работы
                        int cab = Integer.parseInt(tf044.getText());  // Получаем Номер_кабинета

                        statement.setInt(1, (int) id);// Установка значения № карты для вставки
                        statement.setTimestamp(2, new java.sql.Timestamp(dt1.getTime()));// Установка значения начало_работы
                        statement.setTimestamp(3, new java.sql.Timestamp(dt2.getTime()));// Установка значения конец_работы
                        statement.setInt(4, (int) cab);// Установка значения Номер_кабинета


                        model4.addRow(new Object[]{id, dt1, dt2, cab}); // добавление новой строки в таблицу
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
                        String sql = "DELETE FROM Расписание_врачей WHERE id_врача=? AND Начало_работы=? AND Конец_работы=? AND Номер_кабинета=?";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int id = Integer.parseInt(model4.getValueAt(i04, 0).toString());// Получаем id удаляемой записи
                        String dateBegin = (String) model4.getValueAt(i04, 1); // Получаем начало работы
                        Date dt1 = Date.valueOf(dateBegin); // Преобразуем строку в объект типа java.sql.Date
                        String dateEnd = (String) model4.getValueAt(i04, 2); // Получаем конец работы
                        Date dt2 = Date.valueOf(dateEnd); // Преобразуем строку в объект типа java.sql.Date
                        int cab = Integer.parseInt(model4.getValueAt(i04, 3).toString());// Получаем Номер_кабинета

                        statement.setInt(1, (int) id);// Установка значения id для удаления
                        statement.setDate(2, dt1);// Установка значения начало работы
                        statement.setDate(3, dt2);// Установка значения конец работы
                        statement.setInt(4, (int) cab);// Установка значения специальности

                        statement.executeUpdate();// обновление таблицы в postgres
                        model4.removeRow(i04);// Удаление строки из модели таблицы
                        model4.fireTableDataChanged(); // Обновление модели таблицы
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            // Таблица "Прием"
            m06.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг

                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel06.setVisible(true);
                        table6.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel06);
                        getContentPane().add(BorderLayout.CENTER, table6);
                        panel06.add(lb061);//id_врача
                        panel06.add(tf061);
                        panel06.add(lb062);// номер_карты
                        panel06.add(tf062);
                        panel06.add(lb063);// номер_кабинета
                        panel06.add(tf063);
                        panel06.add(lb064);// дата приема
                        panel06.add(tf064);
                        panel06.add(lb065);// время приема
                        panel06.add(tf065);
                        panel06.add(lb066);// отметка
                        panel06.add(tf066);
                        panel06.add(lb067);// заключение
                        panel06.add(tf067);
                        panel06.add(lb068);// рекомендации
                        panel06.add(tf068);
                        panel06.add(add06);//добавить
                        panel06.add(del06);//удалить
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка, что результат еще не был выведен
                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            Statement statement = connection.createStatement(); // оператор запроса
                            ResultSet rs = statement.executeQuery("SELECT * FROM Прием_пациентов ORDER BY Дата_приема, Время_приема;");// Сортировка по началу работы
                            Object[] row = new Object[8];
                            model6.addColumn("id_врача");
                            model6.addColumn("Карта_пациента");
                            model6.addColumn("Номер_кабинета");
                            model6.addColumn("Дата_приема");
                            model6.addColumn("Время_приема");
                            model6.addColumn("Отметка");
                            model6.addColumn("Заключение");
                            model6.addColumn("Рекомендации");
                            model6.addRow(new Object[]{"<html><b>ID врача</b></html>",  "<html><b>Номер карты пациента</b></html>", "<html><b>Номер кабинета</b></html>","<html><b>Дата приема</b></html>", "<html><b>Время приема</b></html>", "<html><b>Отметка</b></html>","<html><b>Заключение</b></html>", "<html><b>Рекомендации</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            while (rs.next()) { // пока есть данные
                                String[] rowData = {String.valueOf(rs.getInt("id_врача")),  String.valueOf(rs.getInt("Карта_пациента")),String.valueOf(rs.getInt("Номер_кабинета")), dateFormat.format(rs.getDate("Дата_приема")), timeFormat.format(rs.getTime("Время_приема")), String.valueOf(rs.getBoolean("Отметка")), rs.getString("Заключение"), rs.getString("Рекомендации")};
                                model6.addRow(rowData);
                            }
                            table6.setModel(model6);
                            statement.close();
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        resultDisplayed = true; // установка значения переменной-флага
                    }
                }
            });
//добавление строки в таблице прием
            add06.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        Connection connection = getConnection();
                        // Создание SQL-запроса INSERT (добавление)
                        String sql = "INSERT INTO Прием_пациентов (id_врача, Карта_пациента, Номер_кабинета, Дата_приема, Время_приема, Отметка, Заключение, Рекомендации) VALUES (?,?,?,?,?,?,?,?)";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int id = Integer.parseInt(tf061.getText());// Получаем id из JTextField
                        int mk = Integer.parseInt(tf062.getText());  // Получаем номер карты
                        int cab = Integer.parseInt(tf063.getText());  // Получаем номер кабинета
                        Date dt = Date.valueOf(tf064.getText());  // Получаем Дата приема
                        Time time = Time.valueOf(tf065.getText());  // Получаем время приема
                        boolean otmetka = Boolean.parseBoolean(tf066.getText());  // Получаем Отметка
                        String zac = tf067.getText();  // Получаем заключение
                        String rec = tf068.getText();  // Получаем рекомендации


                        statement.setInt(1, (int) id);// Установка значения № карты для вставки
                        statement.setInt(2, (int) mk);// Установка значения номер карты
                        statement.setInt(3, (int) cab);// Установка значения номер карты
                        statement.setDate(4, new java.sql.Date(dt.getTime()));// Установка значения дата приема
                        statement.setTime(5, new java.sql.Time(time.getTime()));// Установка значения время приема
                        statement.setBoolean(6, (boolean) otmetka);// Установка значения отметка
                        statement.setString(7, (String) zac);// Установка значения заключения
                        statement.setString(8, (String) rec);// Установка знач рекомендации


                        model6.addRow(new Object[]{id, mk,cab, dt, time,otmetka, zac, rec}); // добавление новой строки в таблицу
                        statement.executeUpdate();// обновление таблицы в postgre
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            del06.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int i06 = table6.getSelectedRow();// Номер выделенной строки
                    try {
                        Connection connection = getConnection();
                        // Создание SQL-запроса delete (на удаление)
                        String sql = "DELETE FROM Прием_пациентов WHERE id_врача=? AND Карта_пациента=? AND Номер_кабинета=? AND Дата_приема=? AND Время_приема=? AND Отметка=? AND Заключение=? AND Рекомендации=? ";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int id = Integer.parseInt(model6.getValueAt(i06, 0).toString());// Получаем id удаляемой записи
                        int mk = Integer.parseInt(model6.getValueAt(i06, 1).toString());// номер карты
                        int cab = Integer.parseInt(model6.getValueAt(i06, 2).toString());// номер кабинета
                        String d = (String) model6.getValueAt(i06, 3); // Получаем дату приема
                        Date date = Date.valueOf(d); // Преобразуем строку в объект типа java.sql.Date
                        String t = (String) model6.getValueAt(i06, 4); // Получаем время приема
                        Time time = Time.valueOf(t); // Преобразуем строку в объект типа java.sql.Date
                        boolean otmetka = Boolean.parseBoolean(model6.getValueAt(i06, 5).toString());// отметка
                        String zac = (String) model6.getValueAt(i06, 6); // Получаем заключение
                        String rec = (String) model6.getValueAt(i06, 7); // Получаем рекомендацию

                        statement.setInt(1, (int) id);// Установка значения id для удаления
                        statement.setInt(2, (int) mk);// Установка значения медкарта
                        statement.setInt(3, (int) cab);// Установка значения кабинета
                        statement.setDate(4, date);// Установка значения даты
                        statement.setTime(5, time);// Установка значения время
                        statement.setBoolean(6, (boolean) otmetka);// Установка отметки
                        statement.setString(7, (String) zac);// Установка значения заключения
                        statement.setString(8, (String) rec);// Установка значения рекомендации

                        statement.executeUpdate();// обновление таблицы в postgres
                        model6.removeRow(i06);// Удаление строки из модели таблицы
                        model6.fireTableDataChanged(); // Обновление модели таблицы
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            //1) Ведомость регистратуры для разноса мед.карт по кабинетам на день
            // выводит ведомость на сегодняшний день
            m11.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг

                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel1.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel1);
                        getContentPane().add(BorderLayout.CENTER, ta1);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка, что результат еще не был выведен
                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());// текущая дата
                            Statement statement = connection.createStatement(); // оператор запроса
                            ResultSet rs = statement.executeQuery("SELECT * FROM Прием_пациентов WHERE Дата_приема = '" + date + "' ORDER BY Номер_кабинета, Время_приема");
                            Map<String, List<String>> map = new HashMap<String, List<String>>();
                            while (rs.next()) {
                                String cabinet = rs.getString("Номер_кабинета");
                                String cardNumber = rs.getString("Карта_пациента");
                                String time = rs.getString("Время_приема");
                                if (!map.containsKey(cabinet)) {
                                    map.put(cabinet, new ArrayList<String>());
                                }
                                map.get(cabinet).add("\n" + "Мед.карта №" + cardNumber + " в " + time);
                            }
                            ta1.append("Ведомость регистратуры для разноса мед.карт по кабинетам на сегодняшний день " + date);
                            for (String cabinet : map.keySet()) {
                                ta1.append("\n");
                                ta1.append("Кабинет №" + cabinet + ": ");
                                for (String cardInfo : map.get(cabinet)) {
                                    ta1.append(cardInfo);
                                }
                            }
                            if (map.isEmpty()) {
                                ta1.append(" отсутствует");
                            }
                            rs.close();
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
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel.setVisible(true);
                        ta.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel);
                        getContentPane().add(BorderLayout.CENTER, ta);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                }
            });


        }
    }

    private void displayPatientPanel() { // ПАЦИЕНТ
        String result = checkAuthorization();        // Проверка авторизации
        if (!result.equals("0")) {
            // закрытие меню
            JMenuBar[] menus = {mbA, mbR, mbD, mbP};
            for (JMenuBar m : menus) { // закрытие меню
                m.setVisible(false);
                getContentPane().remove(m);
            }
            mbP.setVisible(true);

            JMenu m06 = new JMenu("Приемы");
            JMenu m1 = new JMenu("Записи сегодня"); // задание 1
            JMenu m3 = new JMenu("Ведомость за месяц");// задание 3
            mbP.setBackground(new Color(255, 242, 229));  // Установка цвета текста панели
            mbP.add(m06);
            mbP.add(m1);
            mbP.add(m3);
            JMenuItem m11 = new JMenuItem("Открыть");
            m1.add(m11);;
            JMenuItem m31 = new JMenuItem("Открыть");
            m3.add(m31);
            JMenuItem m12 = new JMenuItem("Закрыть");
            m1.add(m12);
            JMenuItem m32 = new JMenuItem("Закрыть");
            m3.add(m32);
            // Видимость панелей
            JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
            for (JPanel p : panels) { // закрытие панелей
                p.setVisible(false);
            }
            panel.setVisible(true);

            panel04.setPreferredSize(new Dimension(1000, 80));// размер панели
            panel05.setPreferredSize(new Dimension(1000, 80));// размер панели
            panel06.setPreferredSize(new Dimension(1000, 80));// размер панели
            // Добавление текстовой области в панель
            panel.add(ta);
            panel1.add(ta1);
            panel2.add(ta2);
            panel3.add(ta3);
            panel4.add(ta4);
            // добавление компонентов (задание 3)
            JLabel lb1 = new JLabel("Введите месяц");
            JTextField tf1 = new JTextField(2); // принимает до 2 символов
            JLabel lb2 = new JLabel("год");
            JTextField tf2 = new JTextField(4); // принимает до 4 символов
            JButton bt31 = new JButton("Поиск");
            // Добавление компонентов в рамку.
            getContentPane().add(BorderLayout.NORTH, mbP);
            getContentPane().add(BorderLayout.SOUTH, panel);
            getContentPane().add(BorderLayout.CENTER, ta);// Текстовая область по центру
            setVisible(true);

            // Создание таблицы Прием (6)
            DefaultTableModel model6 = new DefaultTableModel();
            JTable table6 = new JTable(model6);
            table6.setSelectionBackground(Color.pink);// выделение строки
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// формат для даты
            //1) Ведомость регистратуры для разноса мед.карт по кабинетам на день
            // выводит ведомость на сегодняшний день
            m11.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг

                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel1.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel1);
                        getContentPane().add(BorderLayout.CENTER, ta1);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка, что результат еще не был выведен
                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());// текущая дата
                            Statement statement = connection.createStatement(); // оператор запроса
                            // Добавленное условие для проверки id_врача из таблицы "Приемы_пациентов" равен id_врача из таблицы "Врачи"
                            ResultSet rs1 = statement.executeQuery("SELECT Номер_карты FROM Пациенты WHERE Логин = '" + result + "'");
                            int patientId = -1;
                            if (rs1.next()) {
                                patientId = rs1.getInt("Номер_карты");
                            }
                            rs1.close();

                            if (patientId != -1) { // Если id_врача был найден
                                ResultSet rs = statement.executeQuery("SELECT * FROM Прием_пациентов WHERE Карта_пациента = " + patientId + " ORDER BY Время_приема;");// Сортировка по кабинетам
                                boolean check1 = false;

                                ta1.append("Записи на сегодняшний день " + date);
                                while (rs.next()) { // пока есть данные
                                    if (date.equals(rs.getString("Дата_приема"))) { // проверка, что дата приема сегодня
                                        ta1.append("\n");
                                        ta1.append("Записан " + rs.getString("Время_приема"));
                                        check1 = true;
                                    }
                                }
                                if (!check1) {
                                    ta1.append(" отсутствует");
                                }
                                rs.close();
                            }

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
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel.setVisible(true);
                        ta.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel);
                        getContentPane().add(BorderLayout.CENTER, ta);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                }
            });
            // 3) Ведомость приема врача за месяц (сколько, когда врач принял пациентов и кого конкретно)
            m31.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг
                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel3.setVisible(true);
                        ta3.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel3);
                        getContentPane().add(BorderLayout.CENTER, ta3);
                        panel3.add(lb1); // Компоненты, добавленные с помощью макета Flow Layout
                        panel3.add(tf1);
                        panel3.add(lb2); // Компоненты, добавленные с помощью макета Flow Layout
                        panel3.add(tf2);
                        panel3.add(bt31);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                }
            });
            bt31.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная
                @Override
                public void actionPerformed(ActionEvent a) {
                    if (!resultDisplayed) { // проверка

                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            Statement statement = connection.createStatement(); // оператор запроса
                            // Добавленное условие для проверки id_врача из таблицы "Приемы_пациентов" равен id_врача из таблицы "Врачи"
                            ResultSet rs1 = statement.executeQuery("SELECT Номер_карты FROM Пациенты WHERE Логин = '" + result + "'");
                            int patientId = -1;
                            if (rs1.next()) {
                                patientId = rs1.getInt("Номер_карты");
                            }
                            rs1.close();

                            if (patientId != -1) { // Если id_врача был найден
                                ResultSet rs = statement.executeQuery("SELECT * FROM Прием_пациентов WHERE Карта_пациента = " + patientId + " ORDER BY Дата_приема, Время_приема;");// Сортировка по кабинетам
                                Scanner scanner = new Scanner(System.in);
                                int m = Integer.parseInt(tf1.getText());// Получаем месяц из JTextField
                                int y = Integer.parseInt(tf2.getText());// Получаем год из JTextField
                                boolean check3 = false;

                                ta3.append("Ведомость приемов за " + m + " месяц " + y + " года:");
                                while (rs.next()) { // пока есть данные
                                    if ((rs.getDate("Дата_приема").getMonth() + 1) == m && (rs.getDate("Дата_приема").getYear() + 1900) == y && rs.getBoolean("Отметка") == true) {
                                        ta3.append("\n");
                                        ta3.append("Прием " + rs.getString("Дата_приема") + " ");
                                        ta3.append(rs.getString("Время_приема"));
                                        ta3.append("- заключение " + rs.getString("Заключение"));
                                        check3 = true;
                                    }
                                }
                                if (check3 == false) {
                                    ta3.append(" отсутствует");

                                }
                                rs.close();
                            }
                            statement.close();
                            connection.close();
                        } catch (SQLException e) {
                            ta3.append("SQLState: " + e.getSQLState());
                            ta3.append("Error Code: " + e.getErrorCode());
                            ta3.append("Message: " + e.getMessage());
                            e.printStackTrace();
                        }
                        resultDisplayed = true; // установка значения переменной-флага
                    }}
            });
            m32.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel.setVisible(true);
                        ta.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel);
                        getContentPane().add(BorderLayout.CENTER, ta);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                }
            });
            // Таблица "Прием"
            m06.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг

                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel06.setVisible(true);
                        table6.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel06);
                        getContentPane().add(BorderLayout.CENTER, table6);
                        panel06.add(lb061);//id_врача
                        panel06.add(tf061);
                        panel06.add(lb062);// номер_карты
                        panel06.add(tf062);
                        panel06.add(lb063);// номер_кабинета
                        panel06.add(tf063);
                        panel06.add(lb064);// дата приема
                        panel06.add(tf064);
                        panel06.add(lb065);// время приема
                        panel06.add(tf065);
                        panel06.add(lb066);// отметка
                        panel06.add(tf066);
                        panel06.add(lb067);// заключение
                        panel06.add(tf067);
                        panel06.add(lb068);// рекомендации
                        panel06.add(tf068);
                        panel06.add(add06);//добавить
                        panel06.add(del06);//удалить
                        panel06.add(update06);//обновить
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка, что результат еще не был выведен
                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            Statement statement = connection.createStatement(); // оператор запроса
                            ResultSet rs = statement.executeQuery("SELECT * FROM Прием_пациентов ORDER BY Дата_приема, Время_приема;");// Сортировка по началу работы
                            Object[] row = new Object[8];
                            model6.addColumn("id_врача");
                            model6.addColumn("Карта_пациента");
                            model6.addColumn("Номер_кабинета");
                            model6.addColumn("Дата_приема");
                            model6.addColumn("Время_приема");
                            model6.addColumn("Отметка");
                            model6.addColumn("Заключение");
                            model6.addColumn("Рекомендации");
                            model6.addRow(new Object[]{"<html><b>ID врача</b></html>",  "<html><b>Номер карты пациента</b></html>", "<html><b>Номер кабинета</b></html>","<html><b>Дата приема</b></html>", "<html><b>Время приема</b></html>", "<html><b>Отметка</b></html>","<html><b>Заключение</b></html>", "<html><b>Рекомендации</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            while (rs.next()) { // пока есть данные
                                String[] rowData = {String.valueOf(rs.getInt("id_врача")),  String.valueOf(rs.getInt("Карта_пациента")),String.valueOf(rs.getInt("Номер_кабинета")), dateFormat.format(rs.getDate("Дата_приема")), timeFormat.format(rs.getTime("Время_приема")), String.valueOf(rs.getBoolean("Отметка")), rs.getString("Заключение"), rs.getString("Рекомендации")};
                                model6.addRow(rowData);
                            }
                            table6.setModel(model6);
                            statement.close();
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        resultDisplayed = true; // установка значения переменной-флага
                    }
                }
            });
//добавление строки в таблице прием
            add06.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        Connection connection = getConnection();
                        // Создание SQL-запроса INSERT (добавление)
                        String sql = "INSERT INTO Прием_пациентов (id_врача, Карта_пациента, Номер_кабинета, Дата_приема, Время_приема, Отметка, Заключение, Рекомендации) VALUES (?,?,?,?,?,?,?,?)";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int id = Integer.parseInt(tf061.getText());// Получаем id из JTextField
                        int mk = Integer.parseInt(tf062.getText());  // Получаем номер карты
                        int cab = Integer.parseInt(tf063.getText());  // Получаем номер кабинета
                        Date dt = Date.valueOf(tf064.getText());  // Получаем Дата приема
                        Time time = Time.valueOf(tf065.getText());  // Получаем время приема
                        boolean otmetka = Boolean.parseBoolean(tf066.getText());  // Получаем Отметка
                        String zac = tf067.getText();  // Получаем заключение
                        String rec = tf068.getText();  // Получаем рекомендации


                        statement.setInt(1, (int) id);// Установка значения № карты для вставки
                        statement.setInt(2, (int) mk);// Установка значения номер карты
                        statement.setInt(3, (int) cab);// Установка значения номер карты
                        statement.setDate(4, new java.sql.Date(dt.getTime()));// Установка значения дата приема
                        statement.setTime(5, new java.sql.Time(time.getTime()));// Установка значения время приема
                        statement.setBoolean(6, (boolean) otmetka);// Установка значения отметка
                        statement.setString(7, (String) zac);// Установка значения заключения
                        statement.setString(8, (String) rec);// Установка знач рекомендации


                        model6.addRow(new Object[]{id, mk,cab, dt, time,otmetka, zac, rec}); // добавление новой строки в таблицу
                        statement.executeUpdate();// обновление таблицы в postgre
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            del06.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int i06 = table6.getSelectedRow();// Номер выделенной строки
                    try {
                        Connection connection = getConnection();
                        // Создание SQL-запроса delete (на удаление)
                        String sql = "DELETE FROM Прием_пациентов WHERE id_врача=? AND Карта_пациента=? AND Номер_кабинета=? AND Дата_приема=? AND Время_приема=? AND Отметка=? AND Заключение=? AND Рекомендации=? ";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int id = Integer.parseInt(model6.getValueAt(i06, 0).toString());// Получаем id удаляемой записи
                        int mk = Integer.parseInt(model6.getValueAt(i06, 1).toString());// номер карты
                        int cab = Integer.parseInt(model6.getValueAt(i06, 2).toString());// номер кабинета
                        String d = (String) model6.getValueAt(i06, 3); // Получаем дату приема
                        Date date = Date.valueOf(d); // Преобразуем строку в объект типа java.sql.Date
                        String t = (String) model6.getValueAt(i06, 4); // Получаем время приема
                        Time time = Time.valueOf(t); // Преобразуем строку в объект типа java.sql.Date
                        boolean otmetka = Boolean.parseBoolean(model6.getValueAt(i06, 5).toString());// отметка
                        String zac = (String) model6.getValueAt(i06, 6); // Получаем заключение
                        String rec = (String) model6.getValueAt(i06, 7); // Получаем рекомендацию

                        statement.setInt(1, (int) id);// Установка значения id для удаления
                        statement.setInt(2, (int) mk);// Установка значения медкарта
                        statement.setInt(3, (int) cab);// Установка значения кабинета
                        statement.setDate(4, date);// Установка значения даты
                        statement.setTime(5, time);// Установка значения время
                        statement.setBoolean(6, (boolean) otmetka);// Установка отметки
                        statement.setString(7, (String) zac);// Установка значения заключения
                        statement.setString(8, (String) rec);// Установка значения рекомендации

                        statement.executeUpdate();// обновление таблицы в postgres
                        model6.removeRow(i06);// Удаление строки из модели таблицы
                        model6.fireTableDataChanged(); // Обновление модели таблицы
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }


    private void displayAdminPanel() {
        String result = checkAuthorization();        // Проверка авторизации
        if (!result.equals("0")) {
            // закрытие меню
            // Создание панели меню и добавление компонентов
            JMenuBar mbA = new JMenuBar();
            JMenu m01 = new JMenu("Таблицы");
            JMenu m1 = new JMenu("Ведомость регистратуры"); // задание 1
            JMenu m2 = new JMenu("Расписание работы врачей");// задание 2 (m04)
            JMenu m3 = new JMenu("Ведомость приема врача");// задание 3
            JMenu m4 = new JMenu("Часто ходит по врачам");// задание 4
            mbA.setBackground(new Color(255, 242, 229));  // Установка цвета текста панели
            mbA.add(m01);
            mbA.add(m1);
            mbA.add(m2);
            mbA.add(m3);
            mbA.add(m4);
            JMenuItem m051 = new JMenuItem("Профили");
            m01.add(m051);
            JMenuItem m011 = new JMenuItem("Список врачей");
            m01.add(m011);
            JMenuItem m021 = new JMenuItem("Список пациентов");
            m01.add(m021);
            JMenuItem m031 = new JMenuItem("Список кабинетов");
            m01.add(m031);
            JMenuItem m041 = new JMenuItem("Расписание врачей");
            m01.add(m041);
            JMenuItem m061 = new JMenuItem("Приемы пациентов");
            m01.add(m061);

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
            panel04.setPreferredSize(new Dimension(1000, 80));// размер панели
            panel05.setPreferredSize(new Dimension(1000, 80));// размер панели
            panel06.setPreferredSize(new Dimension(1000, 80));// размер панели

            // добавление компонентов (задание 3)
            JLabel lb1 = new JLabel("Введите месяц");
            JTextField tf1 = new JTextField(2); // принимает до 2 символов
            JLabel lb2 = new JLabel("год");
            JTextField tf2 = new JTextField(4); // принимает до 4 символов
            JLabel lb3 = new JLabel("ФИО врача");
            JTextField tf3 = new JTextField(20); // принимает до 20 символов
            JButton bt31 = new JButton("Поиск");
            // Добавление текстовой области в панель
            panel.add(ta);
            panel1.add(ta1);
            panel2.add(ta2);
            panel3.add(ta3);
            panel4.add(ta4);
            // Добавление компонентов в рамку.
            getContentPane().add(BorderLayout.NORTH, mbA);
            getContentPane().add(BorderLayout.SOUTH, panel);
            getContentPane().add(BorderLayout.CENTER, ta);// Текстовая область по центру
            setVisible(true);

            // Создание таблицы для таблицы врачи (1)
            DefaultTableModel model1 = new DefaultTableModel();
            JTable table1 = new JTable(model1);
            table1.setSelectionBackground(Color.pink);
            // добавление компонентов (таблица врачи)
            JLabel lb011 = new JLabel("Добавить врача: id");
            JTextField tf011 = new JTextField(5); // id принимает до 5 символов
            JLabel lb012 = new JLabel("ФИО");
            JTextField tf012 = new JTextField(30); // фио принимает до 30 символов
            JLabel lb013 = new JLabel("Должность");
            JTextField tf013 = new JTextField(20); // специальность принимает до 20 символов
            JLabel lb014 = new JLabel("Логин");
            JTextField tf014 = new JTextField(20); // логин принимает до 20 символов
            JButton add01 = new JButton("Добавить");
            JButton del01 = new JButton("Удалить");
            JButton update01 = new JButton("Обновить");

            // Создание таблицы пациенты (2)
            DefaultTableModel model2 = new DefaultTableModel();
            JTable table2 = new JTable(model2);
            table2.setSelectionBackground(Color.pink);// выделение строки розовым
            // добавление компонентов (таблица пациенты)
            JLabel lb021 = new JLabel("Добавить пациента: Номер карты");
            JTextField tf021 = new JTextField(5); // номер карты принимает до 5 символов
            JLabel lb022 = new JLabel("ФИО");
            JTextField tf022 = new JTextField(30); // фио принимает до 30 символов
            JLabel lb023 = new JLabel("Дата рождения");
            JTextField tf023 = new JTextField(10); // дата рождения до 10 символов
            JLabel lb024 = new JLabel("Логин");
            JTextField tf024 = new JTextField(20); // логин до 20 символов
            JButton add02 = new JButton("Добавить");
            JButton del02 = new JButton("Удалить");
            JButton update02 = new JButton("Обновить");

            // Создание таблицы Кабинеты (3)
            DefaultTableModel model3 = new DefaultTableModel();
            JTable table3 = new JTable(model3);
            table3.setSelectionBackground(Color.pink);// выделение строки
            // добавление компонентов (таблица пациенты)
            JLabel lb031 = new JLabel("№ кабинета");
            JTextField tf031 = new JTextField(3); // номер кабинета до 5 символов
            JLabel lb032 = new JLabel("Этаж");
            JTextField tf032 = new JTextField(2); // этаж до 2 символов
            JButton add03 = new JButton("Добавить");
            JButton del03 = new JButton("Удалить");
            JButton update03 = new JButton("Обновить");

            // Создание таблицы расписание врачей (4)
            DefaultTableModel model4 = new DefaultTableModel();
            JTable table4 = new JTable(model4);
            table4.setSelectionBackground(Color.pink);// выделение строки
            // добавление компонентов (таблица Расписание врачей)
            JLabel lb041 = new JLabel("id врача");
            JTextField tf041 = new JTextField(10); // id врача принимает до 5 символов
            JLabel lb042 = new JLabel("Дата работы");
            JTextField tf042 = new JTextField(10);
            JLabel lb043 = new JLabel("Начало работы");
            JTextField tf043 = new JTextField(10);
            JLabel lb044 = new JLabel("Конец работы");
            JTextField tf044 = new JTextField(10);
            JLabel lb045 = new JLabel("Номер кабинета");
            JTextField tf045 = new JTextField(3); // Номер_кабинета до 3 символов
            JButton add04 = new JButton("Добавить");
            JButton del04 = new JButton("Удалить");
            JButton update04 = new JButton("Обновить");

            // Создание таблицы ПРОФИЛИ
            DefaultTableModel model5 = new DefaultTableModel();
            JTable table5 = new JTable(model5);
            table5.setSelectionBackground(Color.pink);// выделение строки
            // добавление компонентов
            JLabel lb051 = new JLabel("Логин");
            JTextField tf051 = new JTextField(20); // id врача принимает до 20 символов
            JLabel lb052 = new JLabel("Пароль");
            JTextField tf052 = new JTextField(5); // фио принимает до 5 символов
            JLabel lb053 = new JLabel("Профиль");
            JTextField tf053 = new JTextField(20); // специальность до 20 символов
            JButton add05 = new JButton("Добавить");
            JButton del05 = new JButton("Удалить");
            JButton update05 = new JButton("Обновить");

            // Создание таблицы Прием (6)
            DefaultTableModel model6 = new DefaultTableModel();
            JTable table6 = new JTable(model6);
            table6.setSelectionBackground(Color.pink);// выделение строки

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// формат для даты

            // выводит врачей
            m011.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг

                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel01.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel01);
                        getContentPane().add(BorderLayout.CENTER, table1);
                        panel01.add(lb011);//id врача
                        panel01.add(tf011);
                        panel01.add(lb012);// ФИО
                        panel01.add(tf012);
                        panel01.add(lb013);// должность
                        panel01.add(tf013);
                        panel01.add(lb014);// логин
                        panel01.add(tf014);
                        panel01.add(add01);//добавить
                        panel01.add(del01);//удалить
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка, что результат еще не был выведен
                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            Statement statement = connection.createStatement(); // оператор запроса
                            ResultSet rs = statement.executeQuery("SELECT * FROM Врачи ORDER BY ФИО;");// Сортировка по ФИО
                            Object[] row = new Object[4];
                            model1.addColumn("id_врача");
                            model1.addColumn("ФИО");
                            model1.addColumn("Должность");
                            model1.addColumn("Логин");
                            model1.addRow(new Object[]{"<html><b>id</b></html>", "<html><b>ФИО</b></html>", "<html><b>Должность</b></html>", "<html><b>Логин</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                            while (rs.next()) { // пока есть данные
                                String[] rowData = {String.valueOf(rs.getInt("id_врача")), rs.getString("ФИО"), rs.getString("Должность"), rs.getString("Логин")};
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
                        String sql = "INSERT INTO Врачи (id_врача, ФИО, Должность, Логин) VALUES (?, ?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(sql);

                        int id = Integer.parseInt(tf011.getText());// Получаем id из JTextField
                        String fio = tf012.getText();  // Получаем фио из JTextField
                        String spec = tf013.getText();  // Получаем специальность
                        String log = tf014.getText();  // Получаем специальность

                        statement.setInt(1, (int) id);// Установка значения id для вставки
                        statement.setString(2, (String) fio);// Установка значения фио
                        statement.setString(3, (String) spec);// Установка значения специальность
                        statement.setString(4, (String) log);// Установка значения логин

                        model1.addRow(new Object[]{id, fio, spec, log}); // добавление новой строки в таблицу
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
                        String sql = "DELETE FROM Врачи WHERE id_врача=? AND ФИО=? AND Должность=? AND Логин=?";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int id = Integer.parseInt(model1.getValueAt(i01, 0).toString());// Получаем id удаляемой записи
                        String fio = (String) model1.getValueAt(i01, 1); // Получаем фио
                        String spec = (String) model1.getValueAt(i01, 2); // Получаем специальность
                        String log = (String) model1.getValueAt(i01, 3); // Получаем логин

                        statement.setInt(1, (int) id);// Установка значения id для удаления
                        statement.setString(2, (String) fio);// Установка значения фио
                        statement.setString(3, (String) spec);// Установка значения специальности
                        statement.setString(4, (String) log);// Установка значения логин

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
            // выводит таблицу пациенты
            m021.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг

                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel02.setVisible(true);
                        table2.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel02);
                        getContentPane().add(BorderLayout.CENTER, table2);
                        panel02.add(lb021);//номер карты
                        panel02.add(tf021);
                        panel02.add(lb022);// ФИО
                        panel02.add(tf022);
                        panel02.add(lb023);// Дата рождения
                        panel02.add(tf023);
                        panel02.add(lb024);// логин
                        panel02.add(tf024);
                        panel02.add(add02);//добавить
                        panel02.add(del02);//удалить
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка, что результат еще не был выведен
                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            Statement statement = connection.createStatement(); // оператор запроса
                            ResultSet rs = statement.executeQuery("SELECT * FROM Пациенты ORDER BY Номер_карты;");// Сортировка по номеру карты
                            Object[] row = new Object[4];
                            model2.addColumn("Номер_карты");
                            model2.addColumn("ФИОП");
                            model2.addColumn("Дата_рождения");
                            model2.addColumn("Логин");
                            model2.addRow(new Object[]{"<html><b>Номер карты</b></html>", "<html><b>ФИО пациента</b></html>", "<html><b>Дата рождения (год-месяц-день) </b></html>", "<html><b>Логин</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                            while (rs.next()) { // пока есть данные
                                String[] rowData = {String.valueOf(rs.getInt("Номер_карты")), rs.getString("ФИОП"), dateFormat.format(rs.getDate("Дата_рождения")),rs.getString("Логин")};
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
                        String sql = "INSERT INTO Пациенты (Номер_карты, ФИОП, Дата_рождения, Логин) VALUES (?, ?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int mk = Integer.parseInt(tf021.getText());// Получаем № карты из JTextField
                        String fio_pac = tf022.getText();  // Получаем фио
                        Date dt = Date.valueOf(tf023.getText()); // Получаем дату рождения
                        String log = tf024.getText();  // Получаем логин

                        statement.setInt(1, (int) mk);// Установка значения № карты для вставки
                        statement.setString(2, (String) fio_pac);// Установка значения фио
                        statement.setDate(3, new java.sql.Date(dt.getTime())); // Установка значения дата рождения
                        statement.setString(4, (String) log);// Установка значения фио

                        model2.addRow(new Object[]{mk, fio_pac, dt, log}); // добавление новой строки в таблицу
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
                        String sql = "DELETE FROM Пациенты WHERE Номер_карты=? AND ФИОП=? AND Дата_рождения=? AND Логин=?";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int mk = Integer.parseInt(model2.getValueAt(i02, 0).toString());// Получаем id удаляемой записи
                        String fio = (String) model2.getValueAt(i02, 1); // Получаем фио
                        String birthDateStr = (String) model2.getValueAt(i02, 2); // Получаем дату рождения в виде строки
                        Date dt = Date.valueOf(birthDateStr); // Преобразуем строку в объект типа java.sql.Date
                        String log = (String) model2.getValueAt(i02, 3); // Получаем фио

                        statement.setInt(1, (int) mk);// Установка значения id для удаления
                        statement.setString(2, (String) fio);// Установка значения фио
                        statement.setDate(3, dt);// Установка значения даты рождения
                        statement.setString(4, (String) log);// Установка значения фио

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

            // выводит кабинеты
            m031.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг
                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel03.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel03);
                        getContentPane().add(BorderLayout.CENTER, table3);
                        panel03.add(lb031);//Номер_кабинета
                        panel03.add(tf031);
                        panel03.add(lb032);//этаж
                        panel03.add(tf032);
                        panel03.add(add03);//добавить
                        panel03.add(del03);//удалить
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка, что результат еще не был выведен
                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            Statement statement = connection.createStatement(); // оператор запроса
                            ResultSet rs = statement.executeQuery("SELECT * FROM Кабинеты ORDER BY Номер_кабинета;");// Сортировка по Номер_кабинета
                            Object[] row = new Object[1];
                            model3.addColumn("Номер_кабинета");
                            model3.addColumn("Этаж");
                            model3.addRow(new Object[]{"<html><b>№ кабинета</b></html>", "<html><b>Этаж</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                            while (rs.next()) { // пока есть данные
                                String[] rowData = {String.valueOf(rs.getInt("Номер_кабинета")), String.valueOf(rs.getInt("Этаж"))};
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
            //добавление строки в таблице Номер_кабинета
            add03.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        Connection connection = getConnection();
                        // Создание SQL-запроса INSERT (добавление)
                        String sql = "INSERT INTO Кабинеты (Номер_кабинета, Этаж) VALUES (?, ?)";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int cab = Integer.parseInt(tf031.getText());// Получаем Номер_кабинета из JTextField
                        statement.setInt(1, (int) cab);// Установка значения id для вставки
                        int level = Integer.parseInt(tf032.getText());// Получаем Номер_кабинета из JTextField
                        statement.setInt(2, (int) level);// Установка значения id для вставки
                        model3.addRow(new Object[]{cab, level}); // добавление новой строки в таблицу
                        statement.executeUpdate();// обновление таблицы в postgre
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Удаление строки таблицы Номер_кабинет
            del03.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int i03 = table3.getSelectedRow();// Номер выделенной строки
                    try {
                        Connection connection = getConnection();
                        // Создание SQL-запроса delete (на удаление)
                        String sql = "DELETE FROM Кабинеты WHERE Номер_кабинета=? AND Этаж=?";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int cab = Integer.parseInt(model3.getValueAt(i03, 0).toString());// Получаем Номер_кабинета удаляемой записи
                        int level = Integer.parseInt(model3.getValueAt(i03, 1).toString());// Получаем этаж удаляемой записи
                        statement.setInt(1, (int) cab);// Установка значения кабинета для удаления
                        statement.setInt(2, (int) level);// Установка значения этаж для удаления
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

            // Расписание врачей
            m041.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг

                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel04.setVisible(true);
                        table4.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel04);
                        getContentPane().add(BorderLayout.CENTER, table4);
                        panel04.add(lb041);//id_врача
                        panel04.add(tf041);
                        panel04.add(lb042);//Дата работы
                        panel04.add(tf042);
                        panel04.add(lb043);//начало работы
                        panel04.add(tf043);
                        panel04.add(lb044);// конец работы
                        panel04.add(tf044);
                        panel04.add(lb045);// Номер_кабинета
                        panel04.add(tf045);
                        panel04.add(add04);//добавить
                        panel04.add(del04);//удалить
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка, что результат еще не был выведен
                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            Statement statement = connection.createStatement(); // оператор запроса
                            ResultSet rs = statement.executeQuery("SELECT * FROM Расписание_врачей ORDER BY Дата_работы, Начало_работы;");// Сортировка по началу работы
                            Object[] row = new Object[6];
                            model4.addColumn("id_врача");
                            model4.addColumn("Дата_работы");
                            model4.addColumn("Начало_работы");
                            model4.addColumn("Конец_работы");
                            model4.addColumn("Номер_кабинета");
                            model4.addRow(new Object[]{"<html><b>ID врача</b></html>", "<html><b>Дата работы</b></html>","<html><b>Начало работы</b></html>", "<html><b>Конец работы</b></html>", "<html><b>Номер кабинета</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            while (rs.next()) { // пока есть данные
                                String[] rowData = {String.valueOf(rs.getInt("id_врача")), dateFormat.format(rs.getDate("Дата_работы")), timeFormat.format(rs.getTime("Начало_работы")), timeFormat.format(rs.getTime("Конец_работы")),  String.valueOf(rs.getInt("Номер_кабинета"))};
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
                        String sql = "INSERT INTO Расписание_врачей (id_врача, Дата_работы, Начало_работы, Конец_работы, Номер_кабинета) VALUES (?,?,?, ?,?)";
                        PreparedStatement statement = connection.prepareStatement(sql);

                        int id = Integer.parseInt(tf041.getText());// Получаем id из JTextField
                        Date dt = Date.valueOf(tf042.getText());  // Получаем Дата приема
                        Time time1 = Time.valueOf(tf043.getText());  // Получаем время приема
                        Time time2 = Time.valueOf(tf044.getText());  // Получаем время приема
                        int cab = Integer.parseInt(tf045.getText());  // Получаем Номер_кабинета

                        statement.setInt(1, (int) id);// Установка значения № карты для вставки
                        statement.setDate(2, new java.sql.Date(dt.getTime()));// Установка значения дата приема
                        statement.setTime(3, new java.sql.Time(time1.getTime()));// Установка значения время приема
                        statement.setTime(4, new java.sql.Time(time2.getTime()));// Установка значения время приема
                        statement.setInt(5, (int) cab);// Установка значения Номер_кабинета

                        model4.addRow(new Object[]{id, dt, time1, time2, cab}); // добавление новой строки в таблицу
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
                        String sql = "DELETE FROM Расписание_врачей WHERE id_врача=? AND Дата_работы=? AND Начало_работы=? AND Конец_работы=? AND Номер_кабинета=?";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int id = Integer.parseInt(model4.getValueAt(i04, 0).toString());// Получаем id удаляемой записи
                        String dt = (String) model4.getValueAt(i04, 1); // Получаем дату приема
                        Date date = Date.valueOf(dt); // Преобразуем строку в объект типа java.sql.Date
                        String t1 = (String) model4.getValueAt(i04, 2); // Получаем время приема
                        Time time1 = Time.valueOf(t1); // Преобразуем строку в объект типа java.sql.Date
                        String t2 = (String) model4.getValueAt(i04, 3); // Получаем время приема
                        Time time2 = Time.valueOf(t2); // Преобразуем строку в объект типа java.sql.Date
                        int cab = Integer.parseInt(model4.getValueAt(i04, 4).toString());// Получаем Номер_кабинета

                        statement.setInt(1, (int) id);// Установка значения id для удаления
                        statement.setDate(2, date);// Установка значения начало работы
                        statement.setTime(3, time1);// Установка значения конец работы
                        statement.setTime(4, time2);// Установка значения конец работы
                        statement.setInt(5, (int) cab);// Установка значения специальности

                        statement.executeUpdate();// обновление таблицы в postgres
                        model4.removeRow(i04);// Удаление строки из модели таблицы
                        model4.fireTableDataChanged(); // Обновление модели таблицы
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // ПРОФИЛИ
            m051.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг

                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel05.setVisible(true);
                        table5.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel05);
                        getContentPane().add(BorderLayout.CENTER, table5);
                        panel05.add(lb051);//логин
                        panel05.add(tf051);
                        panel05.add(lb052);// пароль
                        panel05.add(tf052);
                        panel05.add(lb053);// профиль
                        panel05.add(tf053);
                        panel05.add(add05);//добавить
                        panel05.add(del05);//удалить
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка, что результат еще не был выведен
                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            Statement statement = connection.createStatement(); // оператор запроса
                            ResultSet rs = statement.executeQuery("SELECT * FROM Профили ORDER BY Профиль, Логин;");// Сортировка по началу работы
                            Object[] row = new Object[8];
                            model5.addColumn("Логин");
                            model5.addColumn("Пароль");
                            model5.addColumn("Профиль");
                            model5.addRow(new Object[]{"<html><b>Логин</b></html>", "<html><b>Пароль</b></html>", "<html><b>Профиль</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                            while (rs.next()) { // пока есть данные
                                String[] rowData = {rs.getString("Логин"), rs.getString("Пароль"),rs.getString("Профиль")};
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
//добавление строки в таблице Профили
            add05.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        Connection connection = getConnection();
                        // Создание SQL-запроса INSERT (добавление)
                        String sql = "INSERT INTO Профили (Логин, Пароль, Профиль) VALUES (?,?,?)";
                        PreparedStatement statement = connection.prepareStatement(sql);

                        String log = tf051.getText();  // Получаем фио
                        String par = tf052.getText();  // Получаем специальность
                        String prof = tf053.getText();  // Получаем специальность

                        statement.setString(1, (String) log);// Установка значения логин
                        statement.setString(2, (String) par);// Установка значения пароль
                        statement.setString(3, (String) prof);// Установка значения профиль

                        model5.addRow(new Object[]{log, par, prof}); // добавление новой строки в таблицу
                        statement.executeUpdate();// обновление таблицы в postgre
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

// Удаление строки таблицы Профили
            del05.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int i05 = table5.getSelectedRow();// Номер выделенной строки
                    try {
                        Connection connection = getConnection();
                        // Создание SQL-запроса delete (на удаление)
                        String sql = "DELETE FROM Профили WHERE Логин=? AND Пароль=? AND Профиль=? ";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        String log = (String) model5.getValueAt(i05, 0); // Получаем логин
                        String par = (String) model5.getValueAt(i05, 1); // Получаем пароль
                        String prof = (String) model5.getValueAt(i05, 2); // Получаем профиль

                        statement.setString(1, (String) log);// Установка значения фио
                        statement.setString(2, (String) par);// Установка значения пароль
                        statement.setString(3, (String) prof);// Установка значения профиль

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

            // Таблица "Прием"
            m061.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг

                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel06.setVisible(true);
                        table6.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel06);
                        getContentPane().add(BorderLayout.CENTER, table6);
                        panel06.add(lb061);//id_врача
                        panel06.add(tf061);
                        panel06.add(lb062);// номер_карты
                        panel06.add(tf062);
                        panel06.add(lb063);// номер_кабинета
                        panel06.add(tf063);
                        panel06.add(lb064);// дата приема
                        panel06.add(tf064);
                        panel06.add(lb065);// время приема
                        panel06.add(tf065);
                        panel06.add(lb066);// отметка
                        panel06.add(tf066);
                        panel06.add(lb067);// заключение
                        panel06.add(tf067);
                        panel06.add(lb068);// рекомендации
                        panel06.add(tf068);
                        panel06.add(add06);//добавить
                        panel06.add(del06);//удалить
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка, что результат еще не был выведен
                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            Statement statement = connection.createStatement(); // оператор запроса
                            ResultSet rs = statement.executeQuery("SELECT * FROM Прием_пациентов ORDER BY Дата_приема, Время_приема;");// Сортировка по началу работы
                            Object[] row = new Object[8];
                            model6.addColumn("id_врача");
                            model6.addColumn("Карта_пациента");
                            model6.addColumn("Номер_кабинета");
                            model6.addColumn("Дата_приема");
                            model6.addColumn("Время_приема");
                            model6.addColumn("Отметка");
                            model6.addColumn("Заключение");
                            model6.addColumn("Рекомендации");
                            model6.addRow(new Object[]{"<html><b>ID врача</b></html>",  "<html><b>Номер карты пациента</b></html>", "<html><b>Номер кабинета</b></html>","<html><b>Дата приема</b></html>", "<html><b>Время приема</b></html>", "<html><b>Отметка</b></html>","<html><b>Заключение</b></html>", "<html><b>Рекомендации</b></html>"});//жирный шрифт для 1 строки (название столбцов)
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                            while (rs.next()) { // пока есть данные
                                String[] rowData = {String.valueOf(rs.getInt("id_врача")),  String.valueOf(rs.getInt("Карта_пациента")),String.valueOf(rs.getInt("Номер_кабинета")), dateFormat.format(rs.getDate("Дата_приема")), timeFormat.format(rs.getTime("Время_приема")), String.valueOf(rs.getBoolean("Отметка")), rs.getString("Заключение"), rs.getString("Рекомендации")};
                                model6.addRow(rowData);
                            }
                            table6.setModel(model6);
                            statement.close();
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        resultDisplayed = true; // установка значения переменной-флага
                    }
                }
            });
//добавление строки в таблице прием
            add06.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        Connection connection = getConnection();
                        // Создание SQL-запроса INSERT (добавление)
                        String sql = "INSERT INTO Прием_пациентов (id_врача, Карта_пациента, Номер_кабинета, Дата_приема, Время_приема, Отметка, Заключение, Рекомендации) VALUES (?,?,?,?,?,?,?,?)";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int id = Integer.parseInt(tf061.getText());// Получаем id из JTextField
                        int mk = Integer.parseInt(tf062.getText());  // Получаем номер карты
                        int cab = Integer.parseInt(tf063.getText());  // Получаем номер кабинета
                        Date dt = Date.valueOf(tf064.getText());  // Получаем Дата приема
                        Time time = Time.valueOf(tf065.getText());  // Получаем время приема
                        boolean otmetka = Boolean.parseBoolean(tf066.getText());  // Получаем Отметка
                        String zac = tf067.getText();  // Получаем заключение
                        String rec = tf068.getText();  // Получаем рекомендации


                        statement.setInt(1, (int) id);// Установка значения № карты для вставки
                        statement.setInt(2, (int) mk);// Установка значения номер карты
                        statement.setInt(3, (int) cab);// Установка значения номер карты
                        statement.setDate(4, new java.sql.Date(dt.getTime()));// Установка значения дата приема
                        statement.setTime(5, new java.sql.Time(time.getTime()));// Установка значения время приема
                        statement.setBoolean(6, (boolean) otmetka);// Установка значения отметка
                        statement.setString(7, (String) zac);// Установка значения заключения
                        statement.setString(8, (String) rec);// Установка знач рекомендации


                        model6.addRow(new Object[]{id, mk,cab, dt, time,otmetka, zac, rec}); // добавление новой строки в таблицу
                        statement.executeUpdate();// обновление таблицы в postgre
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            del06.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int i06 = table6.getSelectedRow();// Номер выделенной строки
                    try {
                        Connection connection = getConnection();
                        // Создание SQL-запроса delete (на удаление)
                        String sql = "DELETE FROM Прием_пациентов WHERE id_врача=? AND Карта_пациента=? AND Номер_кабинета=? AND Дата_приема=? AND Время_приема=? AND Отметка=? AND Заключение=? AND Рекомендации=? ";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        int id = Integer.parseInt(model6.getValueAt(i06, 0).toString());// Получаем id удаляемой записи
                        int mk = Integer.parseInt(model6.getValueAt(i06, 1).toString());// номер карты
                        int cab = Integer.parseInt(model6.getValueAt(i06, 2).toString());// номер кабинета
                        String d = (String) model6.getValueAt(i06, 3); // Получаем дату приема
                        Date date = Date.valueOf(d); // Преобразуем строку в объект типа java.sql.Date
                        String t = (String) model6.getValueAt(i06, 4); // Получаем время приема
                        Time time = Time.valueOf(t); // Преобразуем строку в объект типа java.sql.Date
                        boolean otmetka = Boolean.parseBoolean(model6.getValueAt(i06, 5).toString());// отметка
                        String zac = (String) model6.getValueAt(i06, 6); // Получаем заключение
                        String rec = (String) model6.getValueAt(i06, 7); // Получаем рекомендацию

                        statement.setInt(1, (int) id);// Установка значения id для удаления
                        statement.setInt(2, (int) mk);// Установка значения медкарта
                        statement.setInt(3, (int) cab);// Установка значения кабинета
                        statement.setDate(4, date);// Установка значения даты
                        statement.setTime(5, time);// Установка значения время
                        statement.setBoolean(6, (boolean) otmetka);// Установка отметки
                        statement.setString(7, (String) zac);// Установка значения заключения
                        statement.setString(8, (String) rec);// Установка значения рекомендации

                        statement.executeUpdate();// обновление таблицы в postgres
                        model6.removeRow(i06);// Удаление строки из модели таблицы
                        model6.fireTableDataChanged(); // Обновление модели таблицы
                        statement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            //1) Ведомость регистратуры для разноса мед.карт по кабинетам на день
            // выводит ведомость на сегодняшний день
            m11.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг
                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel1.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel1);
                        getContentPane().add(BorderLayout.CENTER, ta1);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка, что результат еще не был выведен
                        try {
                            Connection connection = getConnection(); //открытие соединения с базой данных
                            String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now());// текущая дата
                            Statement statement = connection.createStatement(); // оператор запроса
                            ResultSet rs = statement.executeQuery("SELECT * FROM Прием_пациентов WHERE Дата_приема = '" + date + "' ORDER BY Номер_кабинета, Время_приема");
                            Map<String, List<String>> map = new HashMap<String, List<String>>();
                            while (rs.next()) {
                                String cabinet = rs.getString("Номер_кабинета");
                                String cardNumber = rs.getString("Карта_пациента");
                                String time = rs.getString("Время_приема");
                                if (!map.containsKey(cabinet)) {
                                    map.put(cabinet, new ArrayList<String>());
                                }
                                map.get(cabinet).add("\n" + "Мед.карта №" + cardNumber + " в " + time);
                            }
                            ta1.append("Ведомость регистратуры для разноса мед.карт по кабинетам на сегодняшний день " + date);
                            for (String cabinet : map.keySet()) {
                                ta1.append("\n");
                                ta1.append("Кабинет №" + cabinet + ": ");
                                for (String cardInfo : map.get(cabinet)) {
                                    ta1.append(cardInfo);
                                }
                            }
                            if (map.isEmpty()) {
                                ta1.append(" отсутствует");
                            }
                            rs.close();
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
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel.setVisible(true);
                        ta.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel);
                        getContentPane().add(BorderLayout.CENTER, ta);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                }
            });

            // 2) Расписание работы врачей
            m21.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная-флаг

                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel2.setVisible(true);
                        ta2.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel2);
                        getContentPane().add(BorderLayout.CENTER, ta2);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) {
                        try {
                            Connection connection = getConnection();
                            Statement statement = connection.createStatement(); // оператор запроса
                            ta2.append("Расписание работы врачей: ");
                            ResultSet rs = statement.executeQuery("SELECT * FROM Расписание_врачей ORDER BY id_врача,Начало_работы ;"); //результат запроса на поиск + сортировка
                            while (rs.next()) { // пока есть данные
                                ta2.append("\nВрач " + rs.getString("id_врача") + " с ");
                                ta2.append(rs.getString("Начало_работы") + " по ");
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
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel.setVisible(true);
                        ta.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel);
                        getContentPane().add(BorderLayout.CENTER, ta);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                }
            });
            // 3) Ведомость приема врача за месяц (сколько, когда врач принял пациентов и кого конкретно)
            m31.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel3.setVisible(true);
                        ta3.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel3);
                        getContentPane().add(BorderLayout.CENTER, ta3);
                        panel3.add(lb1); // Компоненты, добавленные с помощью макета Flow Layout
                        panel3.add(tf1);
                        panel3.add(lb2); // Компоненты, добавленные с помощью макета Flow Layout
                        panel3.add(tf2);
                        panel3.add(lb3); // Компоненты, добавленные с помощью макета Flow Layout
                        panel3.add(tf3);
                        panel3.add(bt31);
                        revalidate();
                        repaint(); // Обновление компонентов фрейма
                    });
                }
            });

            bt31.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная

                @Override
                public void actionPerformed(ActionEvent a) {
                    if (!resultDisplayed) { // проверка
                        try {
                            Connection connection = getConnection(); // открытие соединения с базой данных
                            Statement statement = connection.createStatement(); // оператор запроса
                            ResultSet rs = statement.executeQuery("SELECT Врачи.ФИО, Прием_пациентов.Дата_приема, Прием_пациентов.Время_приема, Пациенты.ФИОП, Пациенты.Номер_карты FROM Прием_пациентов INNER JOIN Врачи ON Прием_пациентов.id_врача = Врачи.id_врача INNER JOIN Пациенты ON Прием_пациентов.Карта_пациента = Пациенты.Номер_карты WHERE EXTRACT(MONTH FROM Прием_пациентов.Дата_приема) = " + tf1.getText() + " AND EXTRACT(YEAR FROM Прием_пациентов.Дата_приема) = " + tf2.getText() + " AND Врачи.ФИО = '" + tf3.getText() + "' AND Прием_пациентов.Отметка = true ORDER BY Прием_пациентов.Дата_приема, Прием_пациентов.Время_приема;");
                            int m = Integer.parseInt(tf1.getText());// Получаем месяц из JTextField
                            int y = Integer.parseInt(tf2.getText());// Получаем год из JTextField
                            String fio = tf3.getText(); // Получаем текст из JTextField
                            boolean check3 = false;
                            ta3.append("Ведомость приема врача " + fio + " за " + m + " месяц " + y + " года");
                            while (rs.next()) { // пока есть данные
                                ta3.append("\n");
                                ta3.append("Врач " + rs.getString("ФИО"));
                                ta3.append(" принял пациента " + rs.getString("ФИОП"));
                                ta3.append(" с мед.картой №" + rs.getString("Номер_карты") + " ");
                                ta3.append(rs.getString("Дата_приема") + " ");
                                ta3.append(rs.getString("Время_приема"));
                                check3 = true;
                            }
                            if (!check3) {
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
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel.setVisible(true);
                        ta.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel);
                        getContentPane().add(BorderLayout.CENTER, ta);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                }
            });
            // 4) Кто больше всего ходит по врачам
            m41.addActionListener(new ActionListener() {
                boolean resultDisplayed = false; // переменная

                @Override
                public void actionPerformed(ActionEvent a) {
                    SwingUtilities.invokeLater(() -> {
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel4.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel4);
                        getContentPane().add(BorderLayout.CENTER, ta4);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                    if (!resultDisplayed) { // проверка
                        try {
                            Connection connection = getConnection();//открытие соединения с базой данных
                            Statement statement = connection.createStatement(); // оператор запроса
                            List<Integer> listOfNumCards = new ArrayList<>();
                            ResultSet rs = statement.executeQuery("SELECT * FROM Прием_пациентов WHERE Отметка = true ORDER BY Карта_пациента;"); //результат запроса на поиск
                            while (rs.next()) {//отбор записей пациентов с отметкой о посещениии true
                                listOfNumCards.add(rs.getInt("Карта_пациента"));
                            }
                            int maxCount = 0;//максимальное количество посещений среди всех записей
                            ArrayList<Integer> mostRepeatedPatientsNumbers = new ArrayList<>();//список номеров карт поциентов с максимальным числом посещений
                            if (listOfNumCards.isEmpty()) {//проверка наличия посещений
                                ta4.append("Записей пациентов нет или пациент не приходил");
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
                                        ta4.append("Пациент " + rs.getString("ФИОП") + " с № карты " + rs.getString("Номер_карты"));
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
                        JPanel[] panels = {panel, panel01, panel02, panel03, panel04, panel05, panel06, panel1, panel2, panel3, panel4};
                        JTextArea[] textAreas = {ta, ta1, ta2, ta3, ta4};
                        JTable[] tables = {table1, table2, table3, table4, table5, table6};
                        for (JPanel p : panels) { // закрытие панелей
                            p.setVisible(false);
                            getContentPane().remove(p);
                        }
                        for (JTextArea tt : textAreas) { // удаление текстового поля
                            getContentPane().remove(tt);
                        }
                        for (JTable t : tables) { // удаление таблиц
                            getContentPane().remove(t);
                        }
                        panel.setVisible(true);
                        ta.setVisible(true);
                        getContentPane().add(BorderLayout.SOUTH, panel);
                        getContentPane().add(BorderLayout.CENTER, ta);
                        revalidate();
                        repaint();//Обновление компонентов фрейма
                    });
                }
            });
        }

    }
    private String checkAuthorization() {
        // Окно авторизации
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
                "Логин:", usernameField,
                "Пароль:", passwordField
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Авторизация", JOptionPane.OK_CANCEL_OPTION);
        if (option != JOptionPane.OK_OPTION) {
            return "0";
        }
        // Проверка логина и пароля в базе данных
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        try {
            Connection connection = getConnection();
            String sql = "SELECT Профиль FROM Профили WHERE Логин = ? AND Пароль = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String profile = rs.getString("Профиль");
                if (!profile.equals(currentUserProfile)) {
                    JOptionPane.showMessageDialog(null, "Неверный профиль пользователя");
                    return "0";
                }
                JOptionPane.showMessageDialog(null, "Авторизация прошла успешно");
                return username;
            } else {
                JOptionPane.showMessageDialog(null, "Неверный логин или пароль");
                return "0";
            }
        } catch (SQLException e) {
            return "0";
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}


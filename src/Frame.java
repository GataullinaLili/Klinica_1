import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Frame extends JFrame{

    private List<JPanel> panelList;
    private List<JTextArea> areaList;
    private List<JTable> tableList;

    public static void createFrame (){

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
    }


}

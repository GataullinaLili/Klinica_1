import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class KlinicaGui {
    public void Gui(){
        JFrame frame = new JFrame("Клиника");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 700);// Размеры
        frame.setLocationRelativeTo(null); // установить JFrame в центре экрана

        //элементы панели меню; в комментах указаны старые названия полей
        ArrayList<JMenu> menuItems = new ArrayList<>();
        menuItems.add(new JMenu("Список врачей"));  //m01
        menuItems.add(new JMenu("Список пациентов"));  //m02
        menuItems.add(new JMenu("Список кабинетов"));  //m03
        menuItems.add(new JMenu("Расписание врачей"));  //m04
        menuItems.add(new JMenu("Записи пациентов"));  //m05
        menuItems.add(new JMenu("Ведомость регистратуры"));  //m1
        menuItems.add(new JMenu("Расписание работы врачей"));  //m2
        menuItems.add(new JMenu("Ведомость приема врача"));  //m3
        menuItems.add(new JMenu("Часто ходит по врачам"));  //m4
        
        //добавление открыть-закрыть к каждому из элементов меню
        for (JMenu menuItem : menuItems
             ) {
            menuItem.add(new JMenuItem("открыть"));  //m..1
            menuItem.add(new JMenuItem("закрыть"));  //m..2
        }

        //создание панели меню с элементами
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(255, 242, 229));  // Установка цвета текста панели
        for (JMenu menuItem : menuItems
             ) {
            menuBar.add(menuItem);
        }

        //создание панелей
        ArrayList<JPanel> panels = new ArrayList<>();

        //скрытие панелей кроме первой
        panels.get(0).setVisible(true);
        for (int i = 1; i < 9; i++){
            panels.get(i).setVisible(false);
        }

        // добавление компонентов (задание 3)
        ArrayList<JLabel> labels_Ex3 = new ArrayList<>();
        labels_Ex3.add(new JLabel("Введите месяц"));  //lb1
        labels_Ex3.add(new JLabel("год"));  //lb2
        labels_Ex3.add(new JLabel("ФИО врача"));  //lb3
        ArrayList<JTextField> textfields = new ArrayList<>();
        textfields.add(new JTextField(2));  //tf1; значение "месяц" до 2 символов
        textfields.add(new JTextField(4));  //tf2; значение "год" до 4 символов
        textfields.add(new JTextField(20));  //tf3; ФИО врача до 20 символов
        JButton searchButton = new JButton("Поиск");  //bt31

        // Создание текстовой области в каждой панели
        for (int i = 0; i < 5; i++){
            panels.get(i).add(new JTextArea());
        }

        // Добавление компонентов в рамку.
        frame.getContentPane().add(BorderLayout.NORTH, menuItems.get(0));
        frame.getContentPane().add(BorderLayout.SOUTH, panels.get(0));
        frame.getContentPane().add(BorderLayout.CENTER, panels.get(0).getComponent(0));// Текстовая область по центру
        frame.setVisible(true);

        //ВРАЧИ
        // Создание таблицы "врачи"
        DefaultTableModel modelDoctors = new DefaultTableModel();  //model1
        JTable tableDoctors = new JTable(modelDoctors);  //table1
        tableDoctors.setSelectionBackground(Color.pink);
        // добавление компонентов (таблица "врачи")
        ArrayList<JLabel> labelsDoctors = new ArrayList<>();
        labelsDoctors.add(new JLabel("Добавить врача: id"));  //lb011
        labelsDoctors.add(new JLabel("ФИО"));  //lb012
        labelsDoctors.add(new JLabel("Специальность"));  //lb013
        ArrayList<JTextField> textFieldsDoctors = new ArrayList<>();
        textFieldsDoctors.add(new JTextField(5));  //tf011; id до 5 символов
        textFieldsDoctors.add(new JTextField(30));  //tf012; фио врача до 30 символов
        textFieldsDoctors.add(new JTextField(20));  //tf013; специальность до 20 символов
        JButton addDoctor = new JButton("Добавить");  // add01
        JButton delDoctor = new JButton("Удалить");  // del01

        //ПАЦИЕНТЫ
        // Создание таблицы "пациенты"
        DefaultTableModel modelPatients = new DefaultTableModel();  //model2
        JTable tablePatients = new JTable(modelPatients);  //table2
        tablePatients.setSelectionBackground(Color.pink);// выделение строки розовым
        ArrayList<JLabel> labelsPatients = new ArrayList<>();
        labelsPatients.add(new JLabel("Добавить пациента: Номер карты"));  //lb021
        labelsPatients.add(new JLabel("ФИО"));  //lb022
        labelsPatients.add(new JLabel("Дата рождения"));  //lb023 !!АХТУНГ!! не хватает текстового поля
        labelsPatients.add(new JLabel("Полис"));
        ArrayList<JTextField> textFieldsPatients = new ArrayList<>();
        textFieldsPatients.add(new JTextField(5));  //tf021; номер карты принимает до 5 символов
        textFieldsPatients.add(new JTextField(30));  //tf022; фио принимает до 30 символов
        textFieldsPatients.add(new JTextField(10));  //дата рождения
        textFieldsPatients.add(new JTextField(8));  //tf023; полис до 8 символов  !!АХТУНГ!! не хватает лейбла
        JButton addPatient = new JButton("Добавить");
        JButton delPatient = new JButton("Удалить");

        //КАБИНЕТЫ
        // Создание таблицы "кабинеты"
        DefaultTableModel modelCabinet = new DefaultTableModel();  //model3
        JTable tableCabinet = new JTable(modelCabinet);  //table3
        tableCabinet.setSelectionBackground(Color.pink);// выделение строки
        // добавление компонентов (таблица "кабинеты")
        JLabel labelCabinet = new JLabel("№ Кабинета");  //lb031
        JTextField textFieldCabinet = new JTextField(3); // tf031; номер кабинета до 3 символов
        JButton addCabinet = new JButton("Добавить");  //add03
        JButton delCabinet = new JButton("Удалить");  //del03

        //РАСПИСАНИЕ ВРАЧЕЙ
        // Создание таблицы расписание врачей
        DefaultTableModel modelSchedule = new DefaultTableModel();  //model4
        JTable tableSchedule = new JTable(modelSchedule);  //table4
        tableSchedule.setSelectionBackground(Color.pink);// выделение строки
        // добавление компонентов (таблица Расписание врачей)
        ArrayList<JLabel> labelsSchedule = new ArrayList<>();
        labelsSchedule.add(new JLabel("id врача"));  //lb041
        labelsSchedule.add(new JLabel("ФИО"));  //lb042
        labelsSchedule.add(new JLabel("Специальность"));  //lb043
        labelsSchedule.add(new JLabel("Начало работы"));  //lb044
        labelsSchedule.add(new JLabel("Конец работы"));  //lb045
        labelsSchedule.add(new JLabel("Кабинет"));  //lb046
        ArrayList<JTextField> textFieldsSchedule = new ArrayList<>();
        textFieldsSchedule.add(new JTextField(5));  //tf041
        textFieldsSchedule.add(new JTextField(30));  //tf042
        textFieldsSchedule.add(new JTextField(20));  //tf043
        textFieldsSchedule.add(new JTextField(8));  //tf044
        textFieldsSchedule.add(new JTextField(8));  //tf045
        textFieldsSchedule.add(new JTextField(3));  //tf046
        JButton add04 = new JButton("Добавить");
        JButton del04 = new JButton("Удалить");

        //ЗАПИСЬ НА ПРИЕМ
        // Создание таблицы записи пациентов
        DefaultTableModel modelAppointments = new DefaultTableModel();  //model5
        JTable tableAppointments = new JTable(modelAppointments);  //table5
        tableAppointments.setSelectionBackground(Color.pink);// выделение строки
        // добавление компонентов (таблица Записи пациентов)
        ArrayList<JLabel> labelsAppointments = new ArrayList<>();
        labelsAppointments.add(new JLabel("ID врача"));  //lb051
        labelsAppointments.add(new JLabel("ФИО врача"));  //lb052
        labelsAppointments.add(new JLabel("Специальность врача"));  //lb053
        labelsAppointments.add(new JLabel("Кабинет"));  //lb054
        labelsAppointments.add(new JLabel("№ карты пациента"));  //lb055
        labelsAppointments.add(new JLabel("Дата приема"));  //lb056
        labelsAppointments.add(new JLabel("Время приема"));  //lb057
        labelsAppointments.add(new JLabel("Отметка о посещении"));  //lb058
        ArrayList<JTextField> textFieldsAppointments = new ArrayList<>();
        textFieldsAppointments.add(new JTextField(5));  //tf051
        textFieldsAppointments.add(new JTextField(30));  //tf052
        textFieldsAppointments.add(new JTextField(20));  //tf053
        textFieldsAppointments.add(new JTextField(3));  //tf054
        textFieldsAppointments.add(new JTextField(10));  //tf055
        textFieldsAppointments.add(new JTextField(8));  //tf056
        textFieldsAppointments.add(new JTextField(6));  //tf057
        JCheckBox textFieldsAppointments_Checkbox = new JCheckBox();  //tf058
        JButton addAppointment = new JButton("Добавить");  //add05
        JButton delAppointment = new JButton("Удалить");  //del05

        // формат для даты
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }
}

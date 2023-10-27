import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        textfields.add(new JTextField(2));  //tf1;  значение "месяц" до 2 символов
        textfields.add(new JTextField(4));  //tf2;  значение "год" до 4 символов
        textfields.add(new JTextField(20));  //tf3;  ФИО врача до 20 символов
        JButton searchButton = new JButton("Поиск");  //bt31

        // Создание текстовых областей
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
        textFieldsDoctors.add(new JTextField(5));  //tf011;  i++++++++++++++d до 5 символов
        textFieldsDoctors.add(new JTextField(30));  //tf012;  фио врача до 30 символов
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
        labelsPatients.add(new JLabel("Дата рождения"));  //lb023

        // добавление компонентов (таблица "пациенты")
        JLabel lb021 = new JLabel("Добавить пациента: Номер карты");
        JTextField tf021 = new JTextField(5); // номер карты принимает до 5 символов
        JLabel lb022 = new JLabel("ФИО");
        JTextField tf022 = new JTextField(30); // фио принимает до 30 символов
        JLabel lb023 = new JLabel("Дата рождения");
        JTextField tf023 = new JTextField(8); // полис до 8 символов
        JButton add02 = new JButton("Добавить");
        JButton del02 = new JButton("Удалить");
    }
}

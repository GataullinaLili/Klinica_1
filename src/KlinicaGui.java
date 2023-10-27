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
        JMenuBar mb = new JMenuBar();
        mb.setBackground(new Color(255, 242, 229));  // Установка цвета текста панели
        for (JMenu menuItem : menuItems
             ) {
            mb.add(menuItem);
        }

        //создание панелей
        ArrayList<JPanel> panels = new ArrayList<>();

        //скрытие панелей кроме первой
        panels.get(0).setVisible(true);
        for (int i = 1; i < 9; i++){
            panels.get(i).setVisible(false);
        }

        // добавление компонентов
        // в (задание 3)
        ArrayList<JLabel> labels = new ArrayList<>();
        labels.add(new JLabel("Введите месяц"));  //lb1
        labels.add(new JLabel("год"));  //lb2
        labels.add(new JLabel("ФИО врача"));  //lb3
        ArrayList<JTextField> textfields = new ArrayList<>();
        textfields.add(new JTextField(2));  //tf1
        textfields.add(new JTextField(4));  //tf2
        textfields.add(new JTextField(20));  //tf3
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

        // Создание таблицы "врачи"
        DefaultTableModel model1 = new DefaultTableModel();
        JTable table1 = new JTable(model1);
        table1.setSelectionBackground(Color.pink);

        // добавление компонентов (таблица "врачи")
        JLabel lb011 = new JLabel("Добавить врача: id");
        JTextField tf011 = new JTextField(5); // id принимает до 5 символов
        JLabel lb012 = new JLabel("ФИО");
        JTextField tf012 = new JTextField(30); // фио принимает до 30 символов
        JLabel lb013 = new JLabel("Специальность");
        JTextField tf013 = new JTextField(20); // специальность принимает до 20 символов
        JButton add01 = new JButton("Добавить");
        JButton del01 = new JButton("Удалить");

    }
}

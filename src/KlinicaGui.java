import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class KlinicaGui {
    public void Gui(){
        JFrame frame = new JFrame("Клиника");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 700);// Размеры
        frame.setLocationRelativeTo(null); // установить JFrame в центре экрана

        //элементы панели меню
        ArrayList<JMenu> menuItems = new ArrayList<>();
        menuItems.add(new JMenu("Список врачей"));
        menuItems.add(new JMenu("Список пациентов"));
        menuItems.add(new JMenu("Список кабинетов"));
        menuItems.add(new JMenu("Расписание врачей"));
        menuItems.add(new JMenu("Записи пациентов"));
        menuItems.add(new JMenu("Ведомость регистратуры"));
        menuItems.add(new JMenu("Расписание работы врачей"));
        menuItems.add(new JMenu("Ведомость приема врача"));
        menuItems.add(new JMenu("Часто ходит по врачам"));
        
        //добавление открыть-закрыть к каждому из элементов меню
        for (JMenu menuItem : menuItems
             ) {
            menuItem.add(new JMenuItem("открыть"));
            menuItem.add(new JMenuItem("закрыть"));
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
        for (int i = 0; i == 9; i++){
            panels.add(new JPanel());
        }

        //скрытие панелей кроме первой
        panels.get(0).setVisible(true);
        for (int i = 1; i < 9; i++){
            panels.get(i).setVisible(false);
        }

        // добавление компонентов
        // в (задание 3)
        JLabel lb1 = new JLabel("Введите месяц");
        JTextField tf1 = new JTextField(2); // принимает до 2 символов
        JLabel lb2 = new JLabel("год");
        JTextField tf2 = new JTextField(4); // принимает до 4 символов
        JLabel lb3 = new JLabel("ФИО врача");
        JTextField tf3 = new JTextField(20); // принимает до 20 символов
        JButton bt31 = new JButton("Поиск");

        // Создание текстовых областей
        for (int i = 0; i < 5; i++){
            panels.get(i).add(new JTextArea());
        }

        // Добавление компонентов в рамку.
        frame.getContentPane().add(BorderLayout.NORTH, menuItems.get(0));
        frame.getContentPane().add(BorderLayout.SOUTH, panels.get(0));
        frame.getContentPane().add(BorderLayout.CENTER, panels.get(0).getComponent(0));// Текстовая область по центру
        frame.setVisible(true);

        //одлждэр.нсшжжмгшжгщж.м


    }
}

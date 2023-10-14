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


    }
}

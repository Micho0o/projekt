import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class Main extends JFrame
{
    private List<czlonekzespolu> czlonekzespolu;
    private List<zadanie> zadanie;
    private JTextArea listazadan;
    private JComboBox<String> boxzadania;
    private JComboBox<String> boxczlonek;

    public Main()
    {

     czlonekzespolu = new ArrayList<>();
        zadanie = new ArrayList<>();

        setTitle("HelpDesk");
        setSize(800, 600);
        setLayout(new GridLayout(6, 2, 5, 5));

        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel taskLabel = new JLabel("Zadanie:");
        JTextField nazwapolatekstowegozadania = new JTextField(25);
        JButton przyciskdodaniazadania = new JButton("Dodaj Zadanie");
        przyciskdodaniazadania.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String nazwazadania = nazwapolatekstowegozadania.getText();
                if (!nazwazadania.isEmpty())
                {
                    zadanie.add(new zadanie(nazwazadania));
                    zmianalistyzadan();
                    zmianaboxzadania();
                }
            }
        });
        panel1.add(taskLabel);
        panel1.add(nazwapolatekstowegozadania);
        panel1.add(przyciskdodaniazadania);

        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel memberLabel = new JLabel("Nazwa pracownika:");
        JTextField poletekstoweczlonka = new JTextField(25);
        JButton przyciskdodaniapracownika = new JButton("Dodaj pracownika");
        przyciskdodaniapracownika.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String nazwapracownika = poletekstoweczlonka.getText();
                if (!nazwapracownika.isEmpty())
                {
                 czlonekzespolu.add(new czlonekzespolu(nazwapracownika));
                    aktualizacjaczlonekbox();
                }
            }
        });
        panel2.add(memberLabel);
        panel2.add(poletekstoweczlonka);
        panel2.add(przyciskdodaniapracownika);

        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel assignTaskLabel = new JLabel("Przypisz zgłoszenie:");
        boxzadania = new JComboBox<>();
        zmianaboxzadania();
        boxczlonek = new JComboBox<>();
        aktualizacjaczlonekbox();
        JButton assignTaskButton = new JButton("Zatwierdz przypisanie zgloszenia");
        assignTaskButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String nazwazadania = (String) boxzadania.getSelectedItem();
                String nazwapracownika = (String) boxczlonek.getSelectedItem();
                zadanie task = findTask(nazwazadania);
                czlonekzespolu member = findTeamMember(nazwapracownika);
                if (task != null && member != null)
                {
                    member.addTask(task);
                    JOptionPane.showMessageDialog(null, "Zgłoszenie zostało przypisane");
                }
            }
        });
        panel3.add(assignTaskLabel);
        panel3.add(boxzadania);
        panel3.add(boxczlonek);
        panel3.add(assignTaskButton);

        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel taskListLabel = new JLabel("Lista zgloszen:");
        listazadan = new JTextArea(8, 25);
        JScrollPane scrollPane = new JScrollPane(listazadan);
        panel4.add(taskListLabel);
        panel4.add(scrollPane);

        JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton oznaczzgloszenieprzycisk = new JButton("Oznacz zgloszenie");
        oznaczzgloszenieprzycisk.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String nazwazadania = JOptionPane.showInputDialog("Podaj nazwe zgloszenia:");
                if (nazwazadania != null && !nazwazadania.isEmpty())
                {
                    String[] options = {"Do realizacji", "W realizacji", "Zrealizowane"};
                    String status = (String) JOptionPane.showInputDialog(null, "Wybierz jaki status ma mieć zgloszenie:",
                            "Oznacz zgloszenie", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (status != null)
                    {
                        zadanie task = findTask(nazwazadania);
                        if (task != null)
                        {
                            task.setStatus(status);
                            JOptionPane.showMessageDialog(null, "Zmieniono status zgłoszenia");
                        } 
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Nie znaleziono zgłoszenia");
                        }
                    }
                }
            }
        });

        JButton generateRaportButton = new JButton("Raport zgloszen");
        generateRaportButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                StringBuilder report = new StringBuilder();
                for (czlonekzespolu member : czlonekzespolu)
                {
                    report.append("Pracownik: ").append(member.getName()).append("\n");
                    report.append("Liczba przypisanych zgloszen: ").append(member.getTasks().size()).append("\n");
                    long totalTime = 0;
                    for (zadanie task : member.getTasks())
                    {
                        totalTime += task.getTimeSpent();
                    }
                    report.append("Czas spędzony nad zgloszeniem: ").append(formatTime(totalTime)).append("\n\n");
                }
                JOptionPane.showMessageDialog(null, report.toString(), "Raport zgloszen", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panel5.add(oznaczzgloszenieprzycisk);
        panel5.add(generateRaportButton);

        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);
        add(panel5);
    }

    private void zmianalistyzadan()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (zadanie task : zadanie)
        {
            stringBuilder.append(task.getName()).append("\n");
        }
        listazadan.setText(stringBuilder.toString());
    }

    private void zmianaboxzadania()
    {
        boxzadania.removeAllItems();
        for (zadanie task : zadanie)
        {
            boxzadania.addItem(task.getName());
        }
    }

    private void aktualizacjaczlonekbox()
    {
        boxczlonek.removeAllItems();
        for (czlonekzespolu member : czlonekzespolu)
        {
            boxczlonek.addItem(member.getName());
        }
    }

    private zadanie findTask(String name)
    {
        for (zadanie task : zadanie)
        {
            if (task.getName().equals(name))
            {
                return task;
            }
        }
        return null;
    }

    private czlonekzespolu findTeamMember(String name)
    {
        for (czlonekzespolu member : czlonekzespolu)
        {
            if (member.getName().equals(name))
            {
                return member;
            }
        }
        return null;
    }

    private String formatTime(long milliseconds)
    {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds %= 60;
        minutes %= 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                Main app = new Main();
                app.setVisible(true);
            }
        });
    }
}

class zadanie
{
    private String name;
    private String status;
    private long startTime;
    private long endTime;

    public zadanie(String name)
    {
        this.name = name;
        this.status = "Do realizacji";
        this.startTime = System.currentTimeMillis();
    }

    public String getName()
    {
        return name;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
        if (status.equals("Zrealizowane"))
        {
            this.endTime = System.currentTimeMillis();
        }
    }

    public long getTimeSpent()
    {
        if (status.equals("Zrealizowane"))
        {
            return endTime - startTime;
        } 
        else
        {
            return 0;
        }
    }
}

class czlonekzespolu
{
    private String name;
    private List<zadanie> zadanie;

    public czlonekzespolu(String name)
    {
        this.name = name;
        this.zadanie = new ArrayList<>();
    }

    public String getName()
    {
        return name;
    }

    public List<zadanie> getTasks()
    {
        return zadanie;
    }

    public void addTask(zadanie task)
    {
        zadanie.add(task);
    }
}
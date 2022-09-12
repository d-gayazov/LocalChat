package Local_chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class SecondUser extends JFrame {
    File file = new File("Chat.txt");
    FileReader fromFile = new FileReader(file);
    FileWriter toFile = new FileWriter(file, true);

    private JTextArea chatArea = new JTextArea();
    private JTextArea sendArea = new JTextArea();
    private JTextArea historyArea = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane();
    private JScrollPane scrollPaneArea = new JScrollPane();
    private Button btnSend = new Button("Отправить");
    private BufferedReader reader;

    public SecondUser() throws IOException {

        reader = new BufferedReader(new InputStreamReader(System.in));
        var scHistory = new Scanner(file);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        var c = getContentPane();
        var chatPanel = new JPanel();
        chatPanel.setLayout(null);
        setSize(1200,800);
        setTitle("Локальный чат");
        setVisible(true);

        String name = JOptionPane.showInputDialog("Введите ваше имя");
        JOptionPane.showMessageDialog(null, "Добро пожаловать в чат, " + name);

        chatArea.setFont(new Font("arial",Font.BOLD,20));
        chatArea.setForeground(Color.red);
        chatArea.setBounds(50,20,750,300);
        chatPanel.add(chatArea);

//Вариант с полосой прокрутки.
//        scrollPaneArea = new JScrollPane(chatArea);
//        scrollPaneArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//        scrollPaneArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        scrollPaneArea.setBounds(50,20,750,300);
//        chatPanel.add(scrollPaneArea);

        var label = new JLabel("Введите текст сообщения");
        label.setFont(new Font("serif",Font.ITALIC,30));
        label.setBounds(0,320,700,50);
        chatPanel.add(label);

        sendArea.setFont(new Font("arial",Font.BOLD,20));
        sendArea.setForeground(Color.blue);
        sendArea.setBounds(50,370,750,250);
        chatPanel.add(sendArea);

        var historyLabel = new JLabel("История чата");
        historyLabel.setFont(new Font("verdana",Font.ITALIC,30));
        historyLabel.setForeground(Color.BLUE);
        historyLabel.setBounds(850,20,300,50);
        chatPanel.add(historyLabel);

        historyArea.setFont(new Font("arial",Font.BOLD,15));
        historyArea.setForeground(Color.blue);
        historyArea.setBounds(850,80,300,540);
        scrollPane = new JScrollPane(historyArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(850,80,300,540);
        chatPanel.add(scrollPane);

        btnSend.setBounds(650,620,150,50);
        btnSend.setBackground(Color.blue);
        btnSend.setForeground(Color.white);
        btnSend.setFont(new Font("arial",Font.BOLD,20));

        chatPanel.add(btnSend);
        c.add(chatPanel);

        while (scHistory.hasNext()) {
            String strHistory = scHistory.nextLine() + "\n";
            historyArea.append(strHistory);
        }

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalTime time = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                if (!sendArea.getText().isEmpty()) {
                    try {
                        toFile.write(name + " " + time.format(formatter) + ": " + sendArea.getText() + "\n");
                        toFile.flush();
                        sendArea.setText("");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });
        new Thread(()-> {
            try {
                for (;;) {
                    var sc = new Scanner(file);
                    while (sc.hasNextLine()) {
                        String str = sc.nextLine();
                        chatArea.append(str + '\n');

//раскомментируется вместе со scrollPaneArea из строк 49-53. Для установки каретки на последнюю строку.
//                        chatArea.setSelectionStart(chatArea.getText().length());
//                        chatArea.setSelectionEnd(chatArea.getText().length());
                    }sc.close();
                    Thread.sleep(4000);
                    chatArea.setText("");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        new SecondUser();
    }
}
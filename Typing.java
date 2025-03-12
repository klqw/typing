import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Typing extends Frame implements Runnable {
    // フォームの部品
    JLabel selects = new JLabel();
    JTextArea display = new JTextArea();
    Label cnt = new Label();
    Label ok = new Label();
    Label ng = new Label();
    Label spd = new Label();
    Label time = new Label();
    JLabel rec = new JLabel();
    Label lb = new Label(); // ダミーラベル

    // 変数
    char c;
    // int wordCount;
    int selected = 0;
    String holdStrings = "";
    String dispStrings = "";
    int typeCount;
    int goodCount;
    int missCount;
    boolean selectable = true;  // 選択モード / タイピングモードの状態確認用
    boolean typing = false;
    int elapsedTime;
    double speed;
    String[][] menu = {{"", ""}, {"#b22222", "一般英単語"}, {"#32cd32", "プログラミング英単語"}, {"#4682b4", "混合英単語"}, {"#008b8b", "数字"}, {"#8b008b", "ランダム英数字"}, {"#696969", "終了"}};
    String[] generalWords = {
        "apple", "ball", "cat", "dog", "elephant", "fish", "garden", "house", "island", "jungle",
        "king", "lion", "monkey", "nest", "ocean", "park", "queen", "rainbow", "sun", "tree",
        "umbrella", "violin", "water", "xylophone", "yellow", "zebra", "active", "beautiful", "curious", "dangerous",
        "energetic", "friendly", "happy", "intelligent", "joyful", "kind", "lazy", "mindful", "nice", "optimistic",
        "peaceful", "quiet", "reliable", "strong", "talented", "unique", "vibrant", "wise", "eager", "brave",
        "card", "desk", "envelope", "fan", "glove", "hat", "ink", "journal", "key", "lamp",
        "mirror", "notebook", "pen", "quiz", "ruler", "scissors", "table", "umbrella", "vase", "watch",
        "airport", "beach", "city", "desert", "forest", "gym", "hotel", "office", "pizza", "restaurant",
        "school", "train", "university", "village", "worker", "youth", "zeal", "active", "basket", "chocolate",
        "dance", "egg", "football", "guitar", "hobby", "ice", "juice", "knife", "lemon", "mountain",
        "book", "car", "moon", "night", "paper", "river", "bag", "cloud", "red", "question",
        "kite", "vegetable", "ear", "fire", "gift", "home", "internet", "love", "music", "open",
        "rest", "ship", "wave", "party", "zero", "chair", "energy", "grape", "jump", "mouth",
        "ability", "address", "angry", "beauty", "blue", "green", "building", "camera", "child", "clean",
        "climb", "comfortable", "country", "dark", "dream", "easy", "exercise", "family", "flower", "friend",
        "game", "gentle", "healthy", "important", "language", "magic", "market", "memory", "moment", "person",
        "place", "rain", "road", "shop", "simple", "smile", "snow", "song", "sports", "star",
        "story", "summer", "teach", "test", "travel", "warm", "wonderful", "world", "answer", "zone"
    };
    String[] programWords = {
        "char", "int", "double", "long", "string", "boolean", "print", "void", "class", "super",
        "new", "static", "this", "final", "public", "private", "protected", "import", "extends", "interface",
        "package", "try", "catch", "append", "delete", "random", "round", "floor", "ceil", "length",
        "implements", "sleep", "add", "remove", "get", "sort", "size", "put", "main", "exit",
        "margin", "padding", "border", "bold", "italic", "plain", "wrap", "opacity", "div", "span",
        "null", "primary", "increment", "varchar", "datetime", "timestamp", "rollback", "commit", "trigger", "alter",
        "insert", "infile", "foreign", "index", "where", "change", "modify", "column", "collate", "count",
        "truncate", "show", "into", "source", "desc", "between", "limit", "having", "references", "inner",
        "throw", "function", "post", "get", "ajax", "alert", "prompt", "htmlspecialchars", "require", "prepare",
        "execute", "fetch", "header", "session", "cookie", "href", "unset", "dbconnect", "echo", "bind",
        "location", "isset", "trim", "sanitize", "encode", "decode", "json", "query", "method", "empty",
        "window", "box", "year", "month", "top", "bottom", "left", "right", "true", "false",
        "var", "let", "const", "width", "height", "start", "system", "continue", "return", "break"
    };
    char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    char[] randomChars = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'e', 'd', 'f', 'g', 'h', 'i',
        'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
        's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    public static void main(String[] args) {
        new Typing();
    }

    public Typing() {
        super("Typing");
        setVisible(true);
        setSize(1500, 1000);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        selects.setLocation(100, 100);
        selects.setSize(1200, 170);
        selects.setFont(new Font("SansSerif", Font.BOLD, 30));
        selects.setForeground(new Color(25, 25, 112));
        display.setLocation(100, 300);
        display.setSize(1100, 400);
        display.setFont(new Font("SansSerif", Font.BOLD, 32));
        display.setLineWrap(true);
        display.setEditable(false);
        display.setEnabled(false);
        display.setDisabledTextColor(new Color(33, 33, 33));
        cnt.setLocation(100, 700);
        cnt.setSize(200, 30);
        cnt.setFont(new Font("SansSerif", Font.BOLD, 30));
        cnt.setForeground(new Color(30, 144, 255));
        ok.setLocation(400, 700);
        ok.setSize(150, 35);
        ok.setFont(new Font("SansSerif", Font.BOLD, 30));
        ok.setForeground(new Color(34, 139, 34));
        ng.setLocation(700, 700);
        ng.setSize(150, 35);
        ng.setFont(new Font("SansSerif", Font.BOLD, 30));
        ng.setForeground(new Color(255, 69, 0));
        time.setLocation(100, 750);
        time.setSize(300, 35);
        time.setFont(new Font("SansSerif", Font.BOLD, 30));
        time.setForeground(new Color(89, 89, 171));
        spd.setLocation(550, 750);
        spd.setSize(350, 35);
        spd.setFont(new Font("SansSerif", Font.BOLD, 30));
        spd.setForeground(new Color(183, 134, 11));
        rec.setLocation(100, 800);
        rec.setSize(750, 35);
        rec.setFont(new Font("SansSerif", Font.BOLD, 30));
        // rec.setForeground(new Color(89, 89, 171));

        add(selects);
        add(display);
        add(cnt);
        add(ok);
        add(ng);
        add(time);
        add(spd);
        add(rec);
        add(lb);

        // 起動時の表示
        selects.setText(setSelectDisplay());
        resetDisplay();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                c = e.getKeyChar();
                if (selectable) {   // 選択モードの処理
                    switch (c) {
                        case '1':
                            resetDisplay();
                            selected = 1;
                            setWords(selected, 5, generalWords);
                            selects.setText(setSelectDisplay());
                            display.setText(dispStrings);
                            // timeDisplay();
                            recordDisplay(false);
                            break;
                        case '2':
                            resetDisplay();
                            selected = 2;
                            setWords(selected, 50, programWords);
                            selects.setText(setSelectDisplay());
                            display.setText(dispStrings);
                            recordDisplay(false);
                            break;
                        case '3':
                            resetDisplay();
                            selected = 3;
                            setWords(selected, 70, generalWords, programWords);
                            selects.setText(setSelectDisplay());
                            display.setText(dispStrings);
                            recordDisplay(false);
                            break;
                        case '4':
                            resetDisplay();
                            selected = 4;
                            setStrings(selected, 25, numbers);
                            selects.setText(setSelectDisplay());
                            display.setText(dispStrings);
                            recordDisplay(false);
                            break;
                        case '5':
                            resetDisplay();
                            selected = 5;
                            setStrings(selected, 25, randomChars);
                            selects.setText(setSelectDisplay());
                            display.setText(dispStrings);
                            recordDisplay(false);
                            break;
                        case '6':
                            System.exit(0);
                            break;
                        default:
                            break;
                    }
                } else {    // タイピングモードの処理
                    // System.out.println(c);
                    char s0 = holdStrings.charAt(0);
                    // System.out.println(s0);
                    StringBuffer tmpDisp = new StringBuffer();
                    tmpDisp.append(holdStrings);
                    if (!typing) {
                        started();
                    }
                    typing = true;

                    // Escキーで選択モードに移行
                    if (c == 0x1b) {
                        finished();
                        return;
                    }

                    // 入力されたキーに応じた処理
                    if (holdStrings.length() > 0) {
                        if (c == s0) {
                            tmpDisp.delete(0, 1);
                            holdStrings = tmpDisp.toString();
                            // dispStrings = holdStrings.toUpperCase();
                            dispStrings = replaceStrings(holdStrings, selected);
                            typeCount++;
                            goodCount++;
                            display.setText(dispStrings);
                            countDisplay();
                            goodDisplay();
                            speedDisplay();
                            // System.out.println("OK");
                            if (holdStrings.length() == 0) {   // タイピング終了後の処理
                                finished();
                            }
                        } else {
                            typeCount++;
                            missCount++;
                            countDisplay();
                            missDisplay();
                            // System.out.println("NG");
                        }
                    }
                }

            }
            @Override
            public void keyReleased(KeyEvent e) {
            }

        });
    }

    // 選択モード / タイピングモード表示画面の処理
    public String setSelectDisplay() {
        String dispMsg;
        if (selectable) {
            dispMsg = "<html><div style=\"border: 1px solid #ddd; padding: 5px 10px\">番号キーでタイピングモードを選択してください<br><span style=\"color: " + menu[1][0] + "\">1 " + menu[1][1] + "</span>　　<span style=\"color: " + menu[2][0] + "\">　2 " + menu[2][1] + "</span>　　<span style=\"color: " + menu[3][0] + "\">　3 " + menu[3][1] + "</span><br><span style=\"color: " + menu[4][0] + "\">4 " + menu[4][1] + "</span>　　　　　　<span style=\"color: " + menu[5][0] + "\">5 " + menu[5][1] + "</span>　　　　　　 <span style=\"color: " + menu[6][0] + "\">6 " + menu[6][1] + "</span></div></html>";
            return dispMsg;
        } else {
            dispMsg = "<html><div style=\"border: 1px solid #ddd; padding: 15px 10px\"><span style=\"color: " + menu[selected][0] + "\">" + menu[selected][1] + "</span> タイピング中... (Escキーで選択モードに戻ります)</div></html>";
            return dispMsg;
        }
    }

    // 英単語選択時の処理
    public void setWords(int sel, int qty, String[]... words) {
        StringBuffer tmpStrings = new StringBuffer();
        switch (sel) {
            case 1:
            case 2:
                for (int i = 0; i < qty; i++) {
                    int index = (int)(Math.random() * words[0].length);
                    tmpStrings.append(words[0][index]);
                    if (i != (qty - 1)) {
                        tmpStrings.append('\s');
                    }
                }
                break;
            case 3:
                for (int i = 0; i < qty; i++) {
                    int choose = (int)(Math.random() * words.length);
                    int index = (int)(Math.random() * words[choose].length);
                    tmpStrings.append(words[choose][index]);
                    if (i != (qty - 1)) {
                        tmpStrings.append('\s');
                    }
                }
                break;
            default:
                break;
        }
        holdStrings = tmpStrings.toString();
        dispStrings = replaceStrings(holdStrings, sel);
        selectable = false;
    }

    // 数字 or ランダム英数字選択時の処理
    public void setStrings(int sel, int qty, char[] chars) {
        int index = (int)(Math.random() * chars.length);
        int tmpIndex = index;
        StringBuffer tmpStrings = new StringBuffer();
        for (int i = 0; i < qty; i++) {
            for (int j = 0; j < 5; j++) {
                tmpStrings.append(chars[index]);
                if (j == 4 && i != (qty - 1)) {
                    tmpStrings.append('\s');
                }
                while (index == tmpIndex) {
                    index = (int)(Math.random() * chars.length);
                }
                tmpIndex = index;
            }
        }
        holdStrings = tmpStrings.toString();
        dispStrings = replaceStrings(holdStrings, sel);
        selectable = false;
    }

    // 文字列置換
    public String replaceStrings(String str, int sel) {
        String tmpStrings = "";
        switch (sel) {
            case 1:
            case 2:
            case 3:
                tmpStrings = str.replace('\s', '␣');
                break;
            case 4:
            case 5:
                tmpStrings = str.replace('\s', '␣');
                tmpStrings = tmpStrings.toUpperCase();
                break;
            default:
                tmpStrings = str;
                break;
        }
        // System.out.println(tmpStrings);
        return tmpStrings;
    }

    // ----------ラベル表示処理----------
    public void countDisplay() {
        cnt.setText("Count: " + typeCount);
    }

    public void goodDisplay() {
        ok.setText("Good: " + goodCount);
    }

    public void missDisplay() {
        ng.setText("Miss: " + missCount);
    }

    public void timeDisplay() {
        String min;
        String sec;

        if (elapsedTime / 60 < 10) {
            min = "0" + String.valueOf(elapsedTime / 60);
        } else if (elapsedTime / 60 > 99) {
            min = "99";
        } else {
            min = String.valueOf(elapsedTime / 60);
        }

        if (elapsedTime % 60 < 10) {
            sec = "0" + String.valueOf(elapsedTime % 60);
        } else {
            sec = String.valueOf(elapsedTime % 60);
        }

        time.setText("Time:  " + min + " min  " + sec + " sec" );
    }

    public void speedDisplay() {
        if (elapsedTime != 0) {
            speed = goodCount * (60.0 / elapsedTime);
            // 小数点第一位まで表示
            speed *= 10;
            speed = Math.round(speed);
            speed /= 10;
        } else {
            speed = 0.0;
        }
        spd.setText("Speed: " + speed + " key / min");
    }

    public void recordDisplay(boolean newFlag) {
        // System.out.println("Record: " + readRecord(selected));
        String newRecord = (newFlag) ? "New Record!!" : "";
        String recText = "<html><div style=\"color: " + menu[selected][0] + "\">Best Record: " + readRecord(selected) + " key / min　<span style=\"color: #ff0000\">" + newRecord + "</span></html>";
        rec.setText(recText);
    }

    // ラベル等の表示初期化
    public void resetDisplay() {
        selected = 0;
        typeCount = 0;
        goodCount = 0;
        missCount = 0;
        speed = 0.0;
        elapsedTime = 0;
        holdStrings = "";
        dispStrings = "ここに文字列が表示されます";
        display.setText(dispStrings);
        countDisplay(); 
        goodDisplay();
        missDisplay();
        timeDisplay();
        speedDisplay();
    }
    // ----------ラベル表示処理ここまで----------

    // タイピング開始時の処理
    public void started() {
        new Thread(() -> {
            while (!selectable) {
                try {
                    timeDisplay();
                    speedDisplay();
                    elapsedTime++;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
            }
        }).start();
    }

    @Override
    public void run() {
    }

    // タイピング終了時の処理
    public void finished() {
        String dispMsg = "<html><div style=\"border: 1px solid #ddd; padding: 15px 5px\">タイピング終了！！！(選択モードに戻ります...)</div></html>";
        new Thread(() -> {
            try {
                selects.setText(dispMsg);
                selectable = true;
                typing = false;
                writeRecord(selected, speed, holdStrings);
                Thread.sleep(1000);
                dispStrings = "ここに文字列が表示されます";
                display.setText(dispStrings);
                selects.setText(setSelectDisplay());
            } catch (InterruptedException e) {}
        }).start();
    }

    // 記録をファイルに書き出す
    public void writeRecord(int sel, double spd, String hld) {
        double tmpSpd;
        if (readRecord(sel) == "-----") {
            tmpSpd = 0.0;
        } else {
            tmpSpd = Double.parseDouble(readRecord(sel));
        }
        if (spd > tmpSpd && hld.length() == 0) {
            try {
                String filename = "record" + sel + ".txt";
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
                pw.println(spd);
                pw.close();
                recordDisplay(true);
            } catch (IOException e) {
                System.out.println("ファイル出力エラーです");
            }
        }
    }

    // 記録をファイルから読み出す
    public String readRecord(int sel) {
        String speedRecord = "-----";
        try {
            String filename = "record" + sel + ".txt";
            BufferedReader br = new BufferedReader(new FileReader(filename));
            speedRecord = br.readLine();
            Path path = Paths.get(filename);
            FileTime fileTime = Files.getLastModifiedTime(path);
            System.out.println(fileTime);
            // System.out.println(speedRecord);
        } catch (IOException e) {
            System.out.println("ファイルを読み込めませんでした");
        }
        return speedRecord;
    }
}
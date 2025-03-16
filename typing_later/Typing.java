import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.text.*;
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
    int selected = 0;
    String holdStrings = "";
    String dispStrings = "";
    int typeCount;
    int goodCount;
    int missCount;
    boolean keyable = true; // モード切り替え時のキー入力管理用
    boolean selectable = true;  // [選択モード / タイピングモード]の状態確認用
    boolean typing = false; // タイピングモード時のタイマー管理用
    int elapsedTime;
    double speed;
    // メニューのフォントカラー+文言格納用
    String[][] menu = {{"", ""}, {"#b22222", "一般英単語"}, {"#32cd32", "プログラミング単語"}, {"#4682b4", "混合英単語"}, {"#008b8b", "数字"}, {"#8b008b", "ランダム英数字"}, {"#696969", "終了"}};
    // 出題数調整用
    int[]quantities = {50, 50, 70, 25, 25};
    // 一般英単語格納用
    String[] generalWords = {
        "apple", "ball", "castle", "dog", "elephant", "fish", "garden", "house", "island", "jungle",
        "king", "lion", "monkey", "nest", "ocean", "park", "queen", "rainbow", "sun", "tree",
        "umbrella", "violin", "water", "xylophone", "yellow", "zebra", "arrow", "beautiful", "curious", "dangerous",
        "energetic", "friendly", "happy", "intelligent", "joyful", "kind", "lazy", "mindful", "nice", "optimistic",
        "peaceful", "quartz", "reliable", "strong", "talented", "uniform", "vibrant", "wise", "eager", "brave",
        "card", "desk", "envelope", "fan", "glove", "hat", "ink", "journal", "knight", "lamp",
        "mirror", "notebook", "pen", "quiz", "ruler", "scissors", "town", "uncle", "vase", "obsidian",
        "airport", "beach", "city", "desert", "forest", "gym", "hotel", "office", "pizza", "restaurant",
        "school", "train", "university", "village", "worker", "youth", "zeal", "earth", "basket", "chocolate",
        "dance", "egg", "football", "guitar", "hobby", "ice", "juice", "knife", "lemon", "mountain",
        "book", "car", "moon", "night", "paper", "river", "bag", "cloud", "red", "question",
        "kite", "vegetable", "ear", "fire", "gift", "heart", "internet", "love", "music", "octopus",
        "rest", "ship", "wave", "party", "zero", "chair", "energy", "grape", "jump", "mouth",
        "ability", "address", "angry", "beauty", "blue", "green", "building", "camera", "charcoal", "clean",
        "climb", "comfortable", "country", "dark", "dream", "easy", "exercise", "fight", "flower", "friend",
        "game", "gentle", "healthy", "important", "language", "magic", "market", "morning", "moment", "person",
        "place", "rain", "road", "shop", "simple", "smile", "snow", "song", "sport", "star",
        "banana", "orange", "strawberry", "pineapple", "peach", "watermelon", "cherry", "raspberry", "apricot", "blueberry",
        "purple", "pink", "black", "white", "gray", "brown", "gold", "silver", "turquoise", "lavender",
        "story", "summer", "teach", "test", "travel", "warm", "wonderful", "world", "answer", "zone"
    };
    // プログラミング単語格納用
    String[] programWords = {
        "char", "int", "double", "long", "string", "boolean", "print", "void", "class", "super",
        "new", "static", "this", "finally", "public", "private", "protected", "import", "extends", "interface",
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
        "var", "let", "const", "width", "height", "start", "system", "continue", "return", "break",
        "attribute", "then", "split", "slice", "array", "footer", "event", "printf", "scanf", "include",
        "for", "foreach", "while", "override", "block", "format", "write", "else", "default", "short",
        "float", "putchar", "register", "visibility", "clear", "position", "direction", "equals", "relative", "absolute",
        "max", "min", "pow", "clone", "from", "parse", "integer", "switch", "console", "case",
        "frame", "instanceof", "construct", "input", "textarea", "action", "type", "submit", "value", "option",
        "charset", "body", "meta", "namespace", "thread", "server", "path", "strlen", "substr", "hash"
    };
    // 数字格納用
    char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    // ランダム英数字格納用
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
        setSize(1300, 900);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        selects.setLocation(100, 100);
        selects.setSize(1100, 170);
        selects.setFont(new Font("SansSerif", Font.BOLD, 30));
        selects.setForeground(new Color(25, 25, 112));
        display.setLocation(100, 300);
        display.setSize(1100, 400);
        display.setFont(new Font("SansSerif", Font.BOLD, 32));
        display.setLineWrap(true);
        display.setEditable(false);
        display.setEnabled(false);
        display.setDisabledTextColor(new Color(33, 33, 33));
        cnt.setLocation(150, 700);
        cnt.setSize(200, 30);
        cnt.setFont(new Font("SansSerif", Font.PLAIN, 30));
        cnt.setForeground(new Color(30, 144, 255));
        ok.setLocation(450, 700);
        ok.setSize(150, 35);
        ok.setFont(new Font("SansSerif", Font.PLAIN, 30));
        ok.setForeground(new Color(34, 139, 34));
        ng.setLocation(750, 700);
        ng.setSize(150, 35);
        ng.setFont(new Font("SansSerif", Font.PLAIN, 30));
        ng.setForeground(new Color(255, 69, 0));
        time.setLocation(150, 750);
        time.setSize(300, 35);
        time.setFont(new Font("SansSerif", Font.PLAIN, 30));
        time.setForeground(new Color(89, 89, 171));
        spd.setLocation(600, 750);
        spd.setSize(350, 35);
        spd.setFont(new Font("SansSerif", Font.PLAIN, 30));
        spd.setForeground(new Color(183, 134, 11));
        rec.setLocation(150, 790);
        rec.setSize(1100, 80);
        rec.setFont(new Font("SansSerif", Font.PLAIN, 30));

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
                if (keyable) {
                    new Thread(() -> {
                        c = e.getKeyChar();
                        if (selectable) {   // 選択モードの処理
                            switch (c) {
                                case '1':
                                    resetDisplay();
                                    selected = 1;
                                    setWords(quantities[0], generalWords);
                                    selects.setText(setSelectDisplay());
                                    display.setText(dispStrings);
                                    recordDisplay(false);
                                    break;
                                case '2':
                                    resetDisplay();
                                    selected = 2;
                                    setWords(quantities[1], programWords);
                                    selects.setText(setSelectDisplay());
                                    display.setText(dispStrings);
                                    recordDisplay(false);
                                    break;
                                case '3':
                                    resetDisplay();
                                    selected = 3;
                                    setWords(quantities[2], generalWords, programWords);
                                    selects.setText(setSelectDisplay());
                                    display.setText(dispStrings);
                                    recordDisplay(false);
                                    break;
                                case '4':
                                    resetDisplay();
                                    selected = 4;
                                    setStrings(quantities[3], numbers);
                                    selects.setText(setSelectDisplay());
                                    display.setText(dispStrings);
                                    recordDisplay(false);
                                    break;
                                case '5':
                                    resetDisplay();
                                    selected = 5;
                                    setStrings(quantities[4], randomChars);
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
                                typing = true;
                                started();
                            }

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
                                    dispStrings = replaceStrings(holdStrings);
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
                    }).start();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }

    // 選択モード / タイピングモード表示画面の処理
    public String setSelectDisplay() {
        String dispMsg;
        if (selectable) {
            dispMsg = "<html><div style=\"border: 1px solid #ddd; padding: 5px 10px\">番号キーでタイピングモードを選択してください<br><span style=\"color: " + menu[1][0] + "\">1 " + menu[1][1] + "</span>　　<span style=\"color: " + menu[2][0] + "\">　2 " + menu[2][1] + "</span>　　<span style=\"color: " + menu[3][0] + "\">　3 " + menu[3][1] + "</span><br><span style=\"color: " + menu[4][0] + "\">4 " + menu[4][1] + "</span>　　　　　　<span style=\"color: " + menu[5][0] + "\">5 " + menu[5][1] + "</span>　　　　　 <span style=\"color: " + menu[6][0] + "\">6 " + menu[6][1] + "</span></div></html>";
            return dispMsg;
        } else {
            dispMsg = "<html><div style=\"border: 1px solid #ddd; padding: 15px 10px\"><span style=\"color: " + menu[selected][0] + "\">" + menu[selected][1] + "</span> タイピング中... (Escキーで選択モードに戻ります)</div></html>";
            return dispMsg;
        }
    }

    // 英単語選択時の処理
    public void setWords(int qty, String[]... words) {
        selectable = false;
        int count = 0;
        boolean duplicate = false;
        StringBuffer tmpStrings = new StringBuffer();
        ArrayList<String> tmpWords = new ArrayList<>();

        while (count < qty) {
            int choose = (int)(Math.random() * words.length);
            int index = (int)(Math.random() * words[choose].length);

            // 選出されたWordの重複確認
            for (String w : tmpWords) {
                if (w == words[choose][index]) {
                    duplicate = true;
                    break;
                } else {
                    duplicate = false;
                }
            }

            // 選出されたWordが重複していない場合のみArrayListとStringBufferに追加する
            if (!duplicate) {
                count++;
                tmpWords.add(words[choose][index]);
                tmpStrings.append(words[choose][index]);
                if (qty != count) {
                    tmpStrings.append('\s');
                }
            }
        }
        // System.out.println("tmpWords.size: " + tmpWords.size());
        // System.out.println(tmpWords);

        holdStrings = tmpStrings.toString();
        dispStrings = replaceStrings(holdStrings);
    }

    // 数字 or ランダム英数字選択時の処理
    public void setStrings(int qty, char[] chars) {
        selectable = false;
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
        dispStrings = replaceStrings(holdStrings);
    }

    // 文字列置換
    public String replaceStrings(String str) {
        String resStrings = "";
        StringBuffer tmpStrings = new StringBuffer();
        switch (selected) {
            case 1:
            case 2:
            case 3:
                resStrings = str.replace('\s', '␣');
                break;
            case 4:
            case 5:
                resStrings = str.toUpperCase();
                for (int i = 0; i < resStrings.length(); i++) {
                    char tmpChar = resStrings.charAt(i);
                    if (tmpChar != '\s') {
                        tmpChar += 0xfee0;
                    }
                    tmpStrings.append(tmpChar);
                }
                resStrings = tmpStrings.toString();
                resStrings = resStrings.replace('\s', '␣');
                break;
            default:
                resStrings = str;
                break;
        }
        // System.out.println(resStrings);
        return resStrings;
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

        if (elapsedTime >= 6000) {
            sec = "59";
        } else if (elapsedTime % 60 < 10) {
            sec = "0" + String.valueOf(elapsedTime % 60);
        } else {
            sec = String.valueOf(elapsedTime % 60);
        }

        time.setText("Time:  " + min + " min  " + sec + " sec" );
    }

    public void speedDisplay() {
        spd.setText("Speed: " + speed + " key / min");
    }

    public void recordDisplay(boolean newFlag) {
        String newRecord = (newFlag) ? "　New Record!!" : "";
        String recText = "<html><div style=\"border: 1px solid #ddd; padding: 5px 10px; color: " + menu[selected][0] + "\">Best Record: " + readRecordSpeed() + " key / min" + readRecordDate() + "<span style=\"color: #ff0000\">" + newRecord + "</span></html>";
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
            while (typing) {
                try {
                    setSpeed();
                    timeDisplay();
                    speedDisplay();
                    Thread.sleep(1000);
                    elapsedTime++;
                } catch (InterruptedException e) {}
            }
        }).start();
    }

    // タイピング終了時の処理1
    public void finished() {
        keyable = false;
        typing = false;
        selectable = true;
        String dispMsg;
        if (holdStrings.length() == 0) {
            dispMsg = "<html><div style=\"border: 1px solid #ddd; padding: 15px 5px\">タイピング終了！！！ お疲れさまでした (選択モードに戻ります...)</div></html>";
            selects.setText(dispMsg);
        } else {
            dispMsg = "<html><div style=\"border: 1px solid #ddd; padding: 15px 5px\">タイピング終了！！！ (選択モードに戻ります...)</div></html>";
            selects.setText(dispMsg);
        }
        Thread th = new Thread(this);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {}
        keyable = true;
    }

    // タイピング終了時の処理2
    @Override
    public void run() {
        try {
            if (holdStrings.length() == 0) {
                setSpeed();
                speedDisplay();
                writeRecord();
                Thread.sleep(3000);
                dispStrings = "ここに文字列が表示されます";
                display.setText(dispStrings);
                selects.setText(setSelectDisplay());
            } else {
                Thread.sleep(1000);
                dispStrings = "ここに文字列が表示されます";
                display.setText(dispStrings);
                selects.setText(setSelectDisplay());
                rec.setText("");
            }
        } catch (InterruptedException e) {}
    }

    // Speed計算
    public void setSpeed() {
        if (elapsedTime != 0) {
            speed = goodCount * (60.0 / elapsedTime);
            // 小数点第一位まで表示
            speed *= 10;
            speed = Math.round(speed);
            speed /= 10;
        } else {
            speed = 0.0;
        }
    }

    // 記録をファイルに書き出す
    public void writeRecord() {
        double tmpSpeed;
        if (readRecordSpeed() == "--------") {
            tmpSpeed = 0.0;
        } else {
            tmpSpeed = Double.parseDouble(readRecordSpeed());
        }
        if (speed > tmpSpeed) {
            try {
                String fileName = "record" + selected + ".txt";
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
                pw.println(speed);
                pw.close();
                recordDisplay(true);
            } catch (IOException e) {
                // System.out.println("ファイル出力エラーです");
            }
        }
    }

    // 記録をファイルから読み出す
    public String readRecordSpeed() {
        String recordSpeed = "";
        try {
            String fileName = "record" + selected + ".txt";
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            recordSpeed = br.readLine();
            br.close();
            if (!recordSpeed.matches(".*^[0-9]{1,3}\\.[0-9]{1}$.*")) {
                recordSpeed = "--------";
            }
            // System.out.println(recordSpeed);
        } catch (IOException e) {
            recordSpeed = "--------";
            // System.out.println("ファイルを読み込めませんでした");
        } catch (NullPointerException e) {
            recordSpeed = "--------";
            // System.out.println("ぬるぽ");
        } catch (NumberFormatException e) {
            recordSpeed = "--------";
            // System.out.println("ファイルの値が不正です");
        }
        return recordSpeed;
    }

    // ファイルの更新日時を取得する
    public String readRecordDate() {
        String recordDate = "";
        try {
            if (readRecordSpeed().matches(".*^[0-9]{1,3}\\.[0-9]{1}$.*")) {
                String fileName = "record" + selected + ".txt";
                // Pathでファイルの更新日時を取得
                Path path = Paths.get(fileName);
                FileTime fileTime = Files.getLastModifiedTime(path);
                long millis = fileTime.toMillis();
                // System.out.println("ミリ秒表示: " + millis);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                recordDate = "　" + sdf.format(millis);
                // System.out.println("年月日時分秒表示: " + recordDate);
            }
        } catch (IOException e) {
            // System.out.println("ファイルを読み込めませんでした");
        }
        return recordDate;
    }
}
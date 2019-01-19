import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpTest {
    public static void main(String[] args) {
        String[] text = {"Вопрос No 2 (4,3,2,1)\n",
                "В каких случаях любительская радиостанция может передавать коди-\n",
                "рованные сообщения?\n",
                "a) При участии в аварийно-спасательной связи.\n",
                "b) Только при работе вне любительских диапазонов.\n",
                "c) Не регламентируется.\n",
                "d) Ни в каких, при этом передача контрольного номера в соревнованиях по\n",
                "радиоспорту, а также управляющих команд и телеметрии любительских\n",
                "радиостанций наземного и космического базирования не относится к пе-\n",
                "редачам с использованием кодировки сигнала."};
        Pattern questionNumber = Pattern.compile("Вопрос.+\n");//Ищем совпадение от слова вопрос до конца строки
        Pattern questionText = Pattern.compile("^.+\n");//Текст вопроса от начала строки до переноса строки
        Pattern variant_1 = Pattern.compile("a.+\n");//1 вариант ответа
        Pattern variant_2 = Pattern.compile("b.+\n");//2 вариант ответа
        Pattern variant_3 = Pattern.compile("c.+\n");//3 вариант ответа
        Pattern variant_4_dot = Pattern.compile("d.+\\.");//4 вариант ответа, в случае если он оканчивается точкой
        Pattern variant_4 = Pattern.compile("d.+\n");//5 вариант ответа, если оканчивается переносом строки

        int last_line_number = 0;

        for (int i = 0; i < text.length; i++) {
            Matcher matcher = questionNumber.matcher(text[i]);
            if (matcher.find()) {
                System.out.println(text[i].substring(matcher.start(), matcher.end()));
                last_line_number = i;
                break;
            }
        }

        for (int i = last_line_number + 1; i < text.length; i++) {
            Matcher matcher = questionText.matcher(text[i]);
            if (matcher.find()) {
                StringBuilder question = new StringBuilder();
                question.append(text[i], 0, text[i].length() - 2);
                if (!(text[i].charAt(text[i].length() - 2) == '.') && !(text[i].charAt(text[i].length() - 2) == '?')) {
                    int j = i;
                    while (!(text[j].charAt(text[j].length() - 2) == '.') || !(text[j].charAt(text[j].length() - 2) == '?')) {
                        char c = text[j].charAt(text[j].length() - 2);
                        j++;
                        if (c == '?' || c == '.') break;
                        else {
                            if (text[j].charAt(text[j].length() - 2)== '-'){
                                question.append(text[j], 0, text[j].length() - 3);
                            }else{
                                question.append(text[j], 0, text[j].length() - 2);
                            }
                        }
                    }
                }
                else{
                    question.append(text[i], 0, text[i].length() - 2);
                }
                System.out.println(question);
                break;
            }
        }
        for (int i = 0; i < text.length; i++) {
            Matcher matcher = variant_1.matcher(text[i]);
            if (matcher.find()) {
                StringBuilder question = new StringBuilder(text[i]);
                if (!(text[i].charAt(text[i].length() - 2) == '.') && !(text[i].charAt(text[i].length() - 2) == '?')) {
                    int j = i;
                    while (!(text[j].charAt(text[j].length() - 2) == '.') || !(text[j].charAt(text[j].length() - 2) == '?')) {
                        char c = text[j].charAt(text[j].length() - 2);
                        j++;
                        if (c == '?' || c == '.') break;
                        else question.append(text[j], 0, text[j].length() - 2);
                    }
                }

                System.out.println(question);
                break;
            }
        }
        for (int i = 0; i < text.length; i++) {
            Matcher matcher = variant_2.matcher(text[i]);
            if (matcher.find()) {
                StringBuilder question = new StringBuilder(text[i]);
                if (!(text[i].charAt(text[i].length() - 2) == '.') && !(text[i].charAt(text[i].length() - 2) == '?')) {
                    int j = i;
                    while (!(text[j].charAt(text[j].length() - 2) == '.') || !(text[j].charAt(text[j].length() - 2) == '?')) {
                        char c = text[j].charAt(text[j].length() - 2);
                        j++;
                        if (c == '?' || c == '.') break;
                        else question.append(text[j], 0, text[j].length() - 2);
                    }
                }

                System.out.println(question);
                break;
            }
        }
        for (int i = 0; i < text.length; i++) {
            Matcher matcher = variant_3.matcher(text[i]);
            if (matcher.find()) {
                StringBuilder question = new StringBuilder(text[i]);
                if (!(text[i].charAt(text[i].length() - 2) == '.') && !(text[i].charAt(text[i].length() - 2) == '?')) {
                    int j = i;
                    while (!(text[j].charAt(text[j].length() - 2) == '.') || !(text[j].charAt(text[j].length() - 2) == '?')) {
                        char c = text[j].charAt(text[j].length() - 2);
                        j++;
                        if (c == '?' || c == '.') break;
                        else question.append(text[j], 0, text[j].length() - 2);
                    }
                }

                System.out.println(question);
                break;
            }
        }
        for (int i = 0; i < text.length; i++) {
            Matcher matcher = variant_4.matcher(text[i]);
            Matcher matcher_dot = variant_4_dot.matcher(text[i]);
            if (matcher.find() || matcher_dot.find()) {
                StringBuilder question = new StringBuilder(text[i]);
                if (!(text[i].charAt(text[i].length() - 2) == '.') && !(text[i].charAt(text[i].length() - 2) == '?')) {
                    int j = i;
                    while (!(text[j].charAt(text[j].length() - 2) == '.') || !(text[j].charAt(text[j].length() - 2) == '?')) {
                        char c = text[j].charAt(text[j].length() - 1);
                        j++;
                        if (c == '?' || c == '.') break;
                        else question.append(text[j], 0, text[j].length() - 1);
                    }
                }
                System.out.println(question);
                break;
            }
        }


    }
}

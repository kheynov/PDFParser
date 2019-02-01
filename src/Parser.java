import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.sqlite.core.DB;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static void main(String[] args) throws IOException {


        try (PDDocument document = PDDocument.load(new File("/home/vlad/Spravochnik.pdf"))) {//открываем файл для чтения

            if (!document.isEncrypted()) {//если на документе не установлен пароль

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);//сортировка по порядку

                PDFTextStripper tStripper = new PDFTextStripper();
                SQLiteDB database = new SQLiteDB();
                database.OpenDatabase();
                System.out.println("Database opened successfully");
                String pdfFileInText = tStripper.getText(document);

                String[] text = pdfFileInText.split("\\r?\\n");
                Pattern question_exp = Pattern.compile("Вопрос .+\\(");//Ищем совпадение от слова вопрос до конца строки

                Pattern questionText = Pattern.compile("^.+$");//Текст вопроса от начала строки до переноса строки
                Pattern variant_1 = Pattern.compile("a\\).+$");//1 вариант ответа
                Pattern variant_2 = Pattern.compile("b\\).+$");//2 вариант ответа
                Pattern variant_3 = Pattern.compile("c\\).+$");//3 вариант ответа
                Pattern variant_4_dot = Pattern.compile("d\\).+$");//4 вариант ответа, в случае если он оканчивается точкой
                Pattern variant_4 = Pattern.compile("d\\).+$");//5 вариант ответа, если оканчивается переносом строки

                int last_line_number;//номер последней строки
                int numberCount = 0;
                String var1 = "", var2 = "", var3 = "", var4 = "", DBtext = "";
                System.out.println("Reading pdf....");
                for (int textLines = 0; textLines < text.length; textLines++) {//перебор всех строк документа
                    Matcher matcher = question_exp.matcher(text[textLines]);

                    if (matcher.find()) {//если найдено ключевое слово "Вопрос"
                        last_line_number = textLines;//номер последней обработанной строки
                        numberCount++;
                        /*String textTest;
                        if(text[textLines].substring(matcher.start(), matcher.end() - 1).charAt(text[textLines].substring(matcher.start(), matcher.end() - 1).length()-1) != ' '){
                            textTest = text[textLines].substring(matcher.start(), matcher.end() - 1);
                        }else{
                            textTest = text[textLines].substring(matcher.start(), matcher.end() - 2);
                        }
                        if (textTest.charAt(textTest.length() - 1) == ' ') {
                            if (count >= 10 && count < 100) {
                                number = Integer.parseInt(text[textLines].substring(matcher.end() - 5, matcher.end() - 3));
                            } else if (count >= 100) {
                                number = Integer.parseInt(text[textLines].substring(matcher.end() - 6, matcher.end() - 3));
                            } else {
                                number = Integer.parseInt(text[textLines].substring(matcher.end() - 4, matcher.end() - 3));
                            }

                        } else {
                            if (count >= 10 && count < 100) {
                                number = Integer.parseInt(text[textLines].substring(matcher.end() - 4, matcher.end() - 2));
                            } else if (count >= 100) {
                                number = Integer.parseInt(text[textLines].substring(matcher.end() - 4, matcher.end() - 2));
                            } else {
                                number = Integer.parseInt(text[textLines].substring(matcher.end() - 3, matcher.end() - 2));
                            }
                        }
//                        System.out.println(number);*/
                        //Текст вопроса
                        for (int i = last_line_number + 1; i < last_line_number + 15; i++) {//ищем текст вопроса начиная со строки следующей за номером вопроса
                            Matcher matcherText = questionText.matcher(text[i]);

                            if (matcherText.find()) {//если найдено совпадение
                                StringBuilder question = new StringBuilder();//Собираем строку по кусочкам
                                if (text[i].charAt(text[i].length() - 1) == '-' || text[i].charAt(text[i].length() - 2) == '-') {
                                    question.append(text[i], 0, text[i].length() - 1);
                                } else {
                                    question.append(text[i], 0, text[i].length());
                                }
                                if (!(text[i].charAt(text[i].length() - 2) == '.') && !(text[i].charAt(text[i].length() - 2) == '?') && !(text[i].charAt(text[i].length() - 2) == ' ')) {
                                    int j = i;
                                    while (!(text[j].charAt(text[j].length() - 2) == '.') || !(text[j].charAt(text[j].length() - 2) == '?') && !(text[j].charAt(text[j].length() - 2) == ' ')) {
                                        char c = text[j].charAt(text[j].length() - 2);
                                        char d = text[j].charAt(text[j].length() - 1);
                                        j++;
                                        if (c == '?' || c == '.' || c == ' ') {
                                            break;
                                        } else if (d == ' ') {

                                            Matcher matcherNext = variant_1.matcher(text[j]);
                                            if (matcherNext.find()) {
                                                break;
                                            } else {
                                                question.append(text[j], 0, text[j].length());
                                            }
                                        } else if (d == '-' || c == '-') {
                                            question.append(text[j], 0, text[j].length() - 2);
                                        } else {
                                            question.append(text[j], 0, text[j].length());
                                        }
                                    }
                                } else {
                                    question.append(text[i], 0, text[i].length());
                                }
                                last_line_number = i;
                                DBtext = question.toString();
//                                System.out.println(question);
                                break;
                            }
                        }

                        //------------

                        for (int i = last_line_number + 1; i < last_line_number + 15; i++) {
                            Matcher matcherVariant1 = variant_1.matcher(text[i]);
                            if (matcherVariant1.find()) {
                                StringBuilder question = new StringBuilder();//Собираем строку по кусочкам
                                if (text[i].charAt(text[i].length() - 1) == '-' || text[i].charAt(text[i].length() - 2) == '-') {
                                    question.append(text[i], 0, text[i].length() - 1);
                                } else {
                                    question.append(text[i], 0, text[i].length());
                                }
                                if (!(text[i].charAt(text[i].length() - 2) == '.') && !(text[i].charAt(text[i].length() - 2) == '?') && !(text[i].charAt(text[i].length() - 2) == ' ')) {
                                    int j = i;
                                    while (!(text[j].charAt(text[j].length() - 2) == '.') || !(text[j].charAt(text[j].length() - 2) == '?') && !(text[j].charAt(text[j].length() - 2) == ' ')) {
                                        char c = text[j].charAt(text[j].length() - 2);
                                        char d = text[j].charAt(text[j].length() - 1);
                                        j++;
                                        if (c == '?' || c == '.' || c == ' ')
                                            break;

                                        else if (d == ' ') {
                                            Matcher matcherNext = variant_2.matcher(text[i + 1]);
                                            if (matcherNext.find()) {
                                                break;
                                            } else {
                                                question.append(text[j], 0, text[j].length());
                                            }
                                        } else if (d == '-' || c == '-') {
                                            question.append(text[j], 0, text[j].length() - 1);
                                        } else {
                                            question.append(text[j], 0, text[j].length());
                                        }
                                    }
                                }
                                last_line_number = i;
                                var1 = question.toString();
//                                System.out.println(question);
                                break;
                            }
                        }

                        //---------------------

                        for (int i = last_line_number; i < last_line_number + 15; i++) {
                            Matcher matcherVariant2 = variant_2.matcher(text[i]);
                            if (matcherVariant2.find()) {
                                StringBuilder question = new StringBuilder();//Собираем строку по кусочкам
                                if (text[i].charAt(text[i].length() - 1) == '-' || text[i].charAt(text[i].length() - 2) == '-') {
                                    question.append(text[i], 0, text[i].length() - 1);
                                } else {
                                    question.append(text[i], 0, text[i].length());
                                }
                                if (!(text[i].charAt(text[i].length() - 2) == '.') && !(text[i].charAt(text[i].length() - 2) == '?') && !(text[i].charAt(text[i].length() - 2) == ' ')) {
                                    int j = i;
                                    while (!(text[j].charAt(text[j].length() - 2) == '.') || !(text[j].charAt(text[j].length() - 2) == '?') && !(text[j].charAt(text[j].length() - 2) == ' ')) {
                                        char c = text[j].charAt(text[j].length() - 2);
                                        char d = text[j].charAt(text[j].length() - 1);
                                        j++;
                                        if (c == '?' || c == '.' || c == ' ') {
                                            break;
                                        } else if (d == ' ') {
                                            Matcher matcherNext = variant_3.matcher(text[i + 1]);
                                            if (matcherNext.find()) {
                                                break;
                                            } else {
                                                question.append(text[j], 0, text[j].length());
                                            }
                                        } else if (d == '-' || c == '-') {
                                            question.append(text[j], 0, text[j].length() - 1);
                                        } else {
                                            question.append(text[j], 0, text[j].length());
                                        }
                                    }
                                }

                                last_line_number = i;
                                var2 = question.toString();
//                                System.out.println(question);
                                break;
                            }
                        }

                        //---------------------

                        for (int i = last_line_number + 1; i < last_line_number + 15; i++) {
                            Matcher matcherVariant3 = variant_3.matcher(text[i]);
                            if (matcherVariant3.find()) {
                                StringBuilder question = new StringBuilder();//Собираем строку по кусочкам
                                if (text[i].charAt(text[i].length() - 1) == '-' || text[i].charAt(text[i].length() - 2) == '-') {
                                    question.append(text[i], 0, text[i].length() - 1);
                                } else {
                                    question.append(text[i], 0, text[i].length());
                                }
                                if (!(text[i].charAt(text[i].length() - 2) == '.') && !(text[i].charAt(text[i].length() - 2) == '?') && !(text[i].charAt(text[i].length() - 2) == ' ')) {
                                    int j = i;
                                    while (!(text[j].charAt(text[j].length() - 2) == '.') || !(text[j].charAt(text[j].length() - 2) == '?') && !(text[j].charAt(text[j].length() - 2) == ' ')) {
                                        char c = text[j].charAt(text[j].length() - 2);
                                        char d = text[j].charAt(text[j].length() - 1);
                                        j++;
                                        if (c == '?' || c == '.' || c == ' ')
                                            break;
                                        else if (d == ' ') {
                                            Matcher matcherNext = variant_4.matcher(text[i + 1]);
                                            if (matcherNext.find()) {
                                                break;
                                            } else {
                                                question.append(text[j], 0, text[j].length());
                                            }
                                        } else if (d == '-' || c == '-') {
                                            question.append(text[j], 0, text[j].length() - 1);
                                        } else {
                                            question.append(text[j], 0, text[j].length());
                                        }
                                    }
                                }
                                last_line_number = i;
                                var3 = question.toString();
//                                System.out.println(question);
                                break;
                            }
                        }

                        //----------------------------

                        for (int i = last_line_number + 1; i < last_line_number + 15; i++) {
                            Matcher matcherVariant4 = variant_4.matcher(text[i]);
                            Matcher matcher_dot_v4 = variant_4_dot.matcher(text[i]);

                            if (matcherVariant4.find() || matcher_dot_v4.find()) {
                                StringBuilder question = new StringBuilder();//Собираем строку по кусочкам
                                if (text[i].charAt(text[i].length() - 1) == '-' || text[i].charAt(text[i].length() - 2) == '-') {
                                    question.append(text[i], 0, text[i].length() - 1);
                                } else {
                                    question.append(text[i], 0, text[i].length());
                                }
                                if (!(text[i].charAt(text[i].length() - 2) == '.') && !(text[i].charAt(text[i].length() - 2) == '?') && !(text[i].charAt(text[i].length() - 2) == ' ')) {
                                    int j = i;
                                    while (!(text[j].charAt(text[j].length() - 2) == '.') || !(text[j].charAt(text[j].length() - 2) == '?') && !(text[j].charAt(text[j].length() - 2) == ' ')) {
                                        char c = text[j].charAt(text[j].length() - 2);
                                        char d = text[j].charAt(text[j].length() - 1);
                                        j++;
                                        if (c == '?' || c == '.' || c == ' ')
                                            break;
                                        else if (d == ' ' || c == '-') {
                                            if (text[j].charAt(0) == ' ') {
                                                break;
                                            } else {
                                                question.append(text[j], 0, text[j].length());
                                            }
                                        } else if (d == '-') {
                                            question.append(text[j], 0, text[j].length() - 1);
                                        } else {
                                            question.append(text[j], 0, text[j].length());
                                        }
                                    }
                                }
                                var4 = question.toString();
//                                System.out.println(question);
                                break;
                            }
                        }
                        /*System.out.println(DBtext);
                        System.out.println(var1);
                        System.out.println(var2); //Отладочный вывод значений
                        System.out.println(var3);
                        System.out.println(var4);*/
                        try {
                            database.InsertItem(numberCount, DBtext, var1, var2, var3, var4);
                        }catch (Exception e){
                            database.InsertItem(numberCount, "","", "", "","");
                            System.out.println(Arrays.toString(e.getStackTrace()));
                        }
//                        System.out.println("=================================================");
                    }
                }
                System.out.println("Writing successful, closing streams");
                database.CloseStreams();
                System.out.println("Streams closed, finishing");
            }
        }
    }
}
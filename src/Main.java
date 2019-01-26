import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {


        try (PDDocument document = PDDocument.load(new File("/home/vlad/Spravochnik.pdf"))) {//открываем файл для чтения

            if (!document.isEncrypted()) {//если на документе не установлен пароль

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);//сортировка по порядку

                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);

                String[] text = pdfFileInText.split("\\r?\\n");
                Pattern questionNumber = Pattern.compile("Вопрос .+\\(");//Ищем совпадение от слова вопрос до конца строки
                Pattern questionText = Pattern.compile("^.+$");//Текст вопроса от начала строки до переноса строки
                Pattern variant_1 = Pattern.compile("a\\).+$");//1 вариант ответа
                Pattern variant_2 = Pattern.compile("b\\).+$");//2 вариант ответа
                Pattern variant_3 = Pattern.compile("c\\).+$");//3 вариант ответа
                Pattern variant_4_dot = Pattern.compile("d\\).+$");//4 вариант ответа, в случае если он оканчивается точкой
                Pattern variant_4 = Pattern.compile("d\\).+$");//5 вариант ответа, если оканчивается переносом строки

                int last_line_number;//номер последней строки
                int count = 0;
                for (int textLines = 0; textLines < text.length; textLines++) {//перебор всех строк документа
                    Matcher matcher = questionNumber.matcher(text[textLines]);
                    if (matcher.find()) {//если найдено ключевое слово "Вопрос"
                        System.out.println(text[textLines].substring(matcher.start(), matcher.end() - 2));//выводим номер вопроса
                        last_line_number = textLines;//номер последней обработанной строки

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
                                System.out.println(question);
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
                                System.out.println(question);
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
                                        }
                                        else if (d == ' ') {
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
                                System.out.println(question);
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
                                System.out.println(question);
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
                                System.out.println(question);
                                break;
                            }
                        }
                        System.out.println("=================================================");
                        count++;
                    }
                }
            }
        }
    }
}
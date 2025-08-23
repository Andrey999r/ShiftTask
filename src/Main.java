import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        // Дефолтные названия файлов
        String nameIntFile = "integers.txt";
        String nameFloatFile = "floats.txt";
        String nameStrFile = "strings.txt";

        // Дефолтные параметры и переменные
        boolean rewrite = true;
        boolean chooseFiles = false;
        boolean wrongFile = false;
        boolean shortStat = false;
        boolean fullStat = false;
        String prefix = null;
        int quantityInt = 0;
        int quantityFLoat = 0;
        int quantityString = 0;

        Scanner scanner = null;// Сканнер для чтения файлов

        // Коллекции по типам данных
        ArrayList<Long> ints = new ArrayList<>();
        ArrayList<Float> floats = new ArrayList<>();
        ArrayList<Integer> strings = new ArrayList<>();
        ArrayList<String> filesToScan = new ArrayList<>();

        FileWriter intwriter = null;
        FileWriter flwriter = null;
        FileWriter strwriter = null;
        FileWriter writerToAnotherFile = null;

        // Пути к файлам
        String inputPath = "Input";
        String outputPath = "Output";

        // Директории
        File inputDir = new File(inputPath);
        if (inputDir.exists() & inputDir.isDirectory())// проверяем на корректность
        {
            if (inputDir.listFiles().length == 0) {
                System.out.println("В папке анализа файлов пусто");
                return;
            } else {
                for (File fileExtension : inputDir.listFiles())// проверяем файлы для анализа на соответствие расширению
                                                               // .txt
                {
                    String extension = fileExtension.getName();
                    int index = extension.indexOf(".");
                    String ext = "";
                    for (int i = index; i < extension.length(); i++) {
                        ext += extension.charAt(i);
                    }
                    if (!ext.equals(".txt")) {
                        System.out.println("Принимаются только текстовые файлы");
                        return;
                    }
                }
            }
        }
        File outputDir = new File(outputPath);
        if (!outputDir.exists())// проверяем на корректность
        {
            System.out.println("Создайте в корне папку для проанализированных файлов");
            return;
        }

        for (int i = 0; i < args.length; i++)// анализ args
        {
            if (args[i].contains(".txt"))// если в args есть текстовый файл, знпчит пользователь выбрал определенные
                                         // файлы для анализа
            {
                filesToScan.add(args[i]);
                chooseFiles = true;
            } else// сканируем аргументы
            {
                if (args[i].equals("-p")) {
                    prefix = args[i + 1];
                    nameIntFile = prefix + nameIntFile;
                    nameStrFile = prefix + nameStrFile;
                    nameFloatFile = prefix + nameFloatFile;
                }
                if (args[i].equals("-o")) {
                    outputPath = args[i + 1];
                }
                if (args[i].equals("-a")) {
                    rewrite = false;
                }
                if (args[i].equals("-s")) {
                    shortStat = true;
                }
                if (args[i].equals("-f")) {
                    fullStat = true;
                }
            }
        }

        // Выходные файлы
        File intfile = new File(outputPath + "//" + nameIntFile);
        File flfile = new File(outputPath + "//" + nameFloatFile);
        File strfile = new File(outputPath + "//" + nameStrFile);
        File anotherFile = null;
        if (rewrite) // Если сказано перезаписать, то убираем параметр append в FileWriter
        {
            if (outputDir.listFiles().length > 0) // удаляем файлы от прошлого запуска
            {
                for (File randomFiles : outputDir.listFiles()) {
                    randomFiles.delete();
                }
            }
            intwriter = new FileWriter(intfile);
            flwriter = new FileWriter(flfile);
            strwriter = new FileWriter(strfile);
        } else {
            if (outputDir.listFiles().length > 0) // если в папке есть файлы то берем из них инфу и записываем в наш
                                                  // временный файл чтобы потом оттуда распределить по типам, ненужные
                                                  // файлы удаляются, позволяя сохранить только нужные данные из файлов
                                                  // с существующими типами данных из входной директории и те, которые
                                                  // были до следующего запуска
            {
                anotherFile = new File(inputPath + "//anotherfile.txt");
                Scanner scannerForDelete = null;
                writerToAnotherFile = new FileWriter(anotherFile, true);
                for (File deleted : outputDir.listFiles()) {
                    scannerForDelete = new Scanner(deleted);
                    while (scannerForDelete.hasNext()) {
                        if (scannerForDelete.hasNextInt()) {
                            long smth = scannerForDelete.nextLong();
                            writerToAnotherFile.write(smth + "\n");
                        } else {
                            String smth = scannerForDelete.next();
                            writerToAnotherFile.write(smth + "\n");
                        }
                    }
                    scannerForDelete.close();
                    deleted.delete();
                }
                writerToAnotherFile.close();
            }
            intwriter = new FileWriter(intfile, true);
            flwriter = new FileWriter(flfile, true);
            strwriter = new FileWriter(strfile, true);
        }

        for (File inputFIle : inputDir.listFiles()) // Запись в файлы
        {
            if (chooseFiles) {
                for (String scan : filesToScan) {
                    if (inputFIle.getName() != scan) {
                        wrongFile = true;
                        break;
                    }
                }
                if (wrongFile) {
                    continue;
                }
            }
            scanner = new Scanner(inputFIle);
            while (scanner.hasNext()) {
                if (scanner.hasNextInt()) {
                    long val = scanner.nextLong();
                    intwriter.write(val + "\n");
                } else {
                    String val = scanner.next();

                    if (val.contains(".")) {
                        if (Float.valueOf(val).getClass().getSimpleName().equals("Float")) {
                            flwriter.write(val + "\n");
                        }
                    } else {
                        strwriter.write(val + "\n");
                    }
                }
            }
        }
        // Закрываем потоки
        intwriter.close();
        strwriter.close();
        flwriter.close();
        scanner.close();

        // Удаляем пустые файлы, если они есть
        if (intfile.length() == 0) {
            intfile.delete();
        }
        if (flfile.length() == 0) {
            flfile.delete();
        }
        if (strfile.length() == 0) {
            strfile.delete();
        }
        if (!rewrite) {
            anotherFile.delete();
        }
        if (shortStat | fullStat)// Краткая или полная статистика
        {
            for (File outputFile : outputDir.listFiles()) {
                scanner = new Scanner(outputFile);
                while (scanner.hasNext()) {
                    if (scanner.hasNextLong()) {
                        long val = scanner.nextLong();
                        Long temp = Long.valueOf(val);
                        quantityInt += temp.toString().length();
                        if (fullStat) {
                            ints.add(val);
                        }
                    } else {
                        String val = scanner.next();
                        if (val.contains(".")) {
                            quantityFLoat += val.length();
                            if (fullStat) {
                                Float temp = Float.valueOf(val);
                                floats.add(temp);
                            }
                        } else {
                            quantityString += val.length();
                            if (fullStat) {
                                strings.add(val.length());
                            }
                        }
                    }
                }
            }
            if (intfile.exists()) {
                System.out.println("Кол-во символов в типе Int: " + quantityInt);
            }
            if (strfile.exists()) {
                System.out.println("Кол-во символов в типе String: " + quantityString);
            }
            if (flfile.exists()) {
                System.out.println("Кол-во символов в типе Float: " + quantityFLoat);
            }
        }
        scanner.close();

        if (fullStat)// Полная статистика
        {
            int intSum = 0;
            float floatSum = 0;
            if (strfile.exists()) {
                Collections.sort(strings);
                System.out.println("Самая короткая строка насчитывает " + strings.get(0) + " символов");
                System.out.println("Самая длинная строка насчитывает " + strings.getLast() + " символов");
            }

            if (intfile.exists()) {
                Collections.sort(ints);
                for (int i = 0; i < ints.size(); i++) {
                    intSum += ints.get(i);
                }
                System.out.println("Минимальный элемент в Int: " + ints.get(0));
                System.out.println("Максимальный элемент в Int: " + ints.getLast());
                System.out.println("Сумма Int: " + intSum);
                System.out.println("Среднее арифметическое Int: " + Float.valueOf(intSum) / ints.size());
            }

            if (flfile.exists()) {
                Collections.sort(floats);
                for (int i = 0; i < floats.size(); i++) {
                    floatSum += floats.get(i);
                }
                System.out.println("Минимальный элемент во Float: " + floats.get(0));
                System.out.println("Максимальный элемент в Float: " + floats.getLast());
                System.out.println("Сумма FLoat: " + floatSum);
                System.out.println("Среднее арифметическое Float: " + Float.valueOf(floatSum) / floats.size());
            }
        }
    }
}
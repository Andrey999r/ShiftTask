import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException 
    {
        if (args.length == 0) //Обязательно при запуске указать хотя бы 1 параметр
        {
            System.out.println("Ошибка! \nВведите параметры запуска программы");
    
        }
        //Пути к файлам
        String inputPath = "C://Users//flow//IdeaProjects//ShiftTask//InputFiles";
        String outputPath = "C://Users//flow//IdeaProjects//ShiftTask//OutputFiles";

        //Дефолтные названия файлов
        String nameIntFile = "integers.txt";
        String nameFloatFile = "floats.txt";
        String nameStrFile = "strings.txt";

        //Дефолтные параметры и переменные
        boolean rewrite = true;
        boolean shortStat = false;
        boolean fullStat = false;
        String prefix = null;
        int quantityInt = 0;
        int quantityFLoat = 0;
        int quantityString = 0;

        Scanner scanner = null;// Сканнер для чтения файлов

        //Коллекции по типам данных
        ArrayList<Integer> ints = new ArrayList<>();
        ArrayList<Float> floats = new ArrayList<>();
        ArrayList<Integer> strings = new ArrayList<>();

        FileWriter intwriter = null;
        FileWriter flwriter = null;
        FileWriter strwriter = null;
        FileWriter writerToAnotherFile = null;

        //Директории
        File inputDir = new File(inputPath);
        File outputDir = new File(outputPath);

        if (inputDir.exists() & inputDir.isDirectory()) // Проверка сущ. директории и является ли это директорией
        {
            for(int i = 0;i<args.length;i++) //Ищем индекс первого файла
            {
                if(args[i].contains(".txt"))
                {
                    break;
                }
                else
                {
                    if(args[i].equals("-p"))
                    {
                        prefix=args[i+1];
                        nameIntFile = prefix+nameIntFile;
                        nameStrFile = prefix+nameStrFile;
                        nameFloatFile = prefix+nameFloatFile;
                    }
                    if(args[i].equals("-o"))
                    {
                        outputPath = args[i+1];
                        if (!outputDir.isDirectory()) //Если путь не заканчивается на //, то добавляем
                        {
                            System.out.println("Путь к папке для хранения выходных файлов не является папкой или не существует");
                            return;
                        }
                    }
                    if(args[i].equals("-a"))
                    {
                        rewrite = false;
                    }
                    if(args[i].equals("-s"))
                    {
                        shortStat = true;
                    }
                    if (args[i].equals("-f"))
                    {
                        fullStat = true;
                    }
                }
            }
        }
        else
        {
            System.out.println("Ошибка! \nПапка для хранения исходных файлов не найдена или это не папка");
            return;
        }
        
        //Выходные файлы
        File intfile = new File(outputPath+"//"+nameIntFile);
        File flfile = new File(outputPath+"//"+nameFloatFile);
        File strfile = new File(outputPath+"//"+nameStrFile);

        if(rewrite) //Если сказано перезаписать, то убираем параметр append в FileWriter
        {
            if(outputDir.listFiles().length>0) //удаляем файлы от прошлого запуска
            {
               for(File randomFiles :outputDir.listFiles())
               {
                    randomFiles.delete();
               }
            }
            intwriter = new FileWriter(intfile);
            flwriter = new FileWriter(flfile);
            strwriter = new FileWriter(strfile);
            
        }
        else
        {
            if(outputDir.listFiles().length>0) //если в папке есть файлы то берем из них инфу и записываем в наш input файл чтобы потом оттуда распределить по типам
            {
                Scanner scannerForDelete = null;
                for(File randomfiles : inputDir.listFiles())
                {
                    writerToAnotherFile = new FileWriter(randomfiles,true);
                    for(File deleted: outputDir.listFiles())
                    {
                        scannerForDelete = new Scanner(deleted);
                        while(scannerForDelete.hasNext())
                        {
                            if(scannerForDelete.hasNextInt())
                            {
                                int smth = scannerForDelete.nextInt();
                                writerToAnotherFile.write("\n"+smth+"\n");
                            }
                            else
                            {
                                String smth = scannerForDelete.next();
                                writerToAnotherFile.write("\n"+smth+"\n");
                            }
                        }
                        scannerForDelete.close();
                        deleted.delete();
                    }
                    break;
                }
                writerToAnotherFile.close();
            }
            intwriter = new FileWriter(intfile, true);
            flwriter = new FileWriter(flfile, true);
            strwriter = new FileWriter(strfile, true);
        }

        for (File inputFIle : inputDir.listFiles()) //Запись в файлы
        {
            scanner = new Scanner(inputFIle);
            while (scanner.hasNext()) 
            {
                if (scanner.hasNextInt()) 
                {
                    int val = scanner.nextInt();
                    intwriter.write(val + "\n");
                }  
                else 
                {
                    String val = scanner.next();
                    if(val.contains("."))
                    {
                        flwriter.write(val+"\n");
                    }
                    else
                    {
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

        //Удаляем пустые файлы, если они есть
        if (intfile.length() == 0)
        {
            intfile.delete();
        }
        if (flfile.length() == 0)
        {
            flfile.delete();
        }
        if (strfile.length() == 0)
        {
            strfile.delete();
        }
    
        if (shortStat|fullStat)//Краткая или полная статистика
        {
            for(File outputFile : outputDir.listFiles())
            {
                scanner = new Scanner(outputFile);
                while (scanner.hasNext()) 
                {
                    if (scanner.hasNextInt()) 
                    {
                        int val = scanner.nextInt();
                        Integer temp = Integer.valueOf(val);
                        quantityInt += temp.toString().length();
                        if(fullStat)
                        {
                            ints.add(val);
                        }
                    }  
                    else 
                    {
                        String val = scanner.next();
                        if(val.contains("."))
                        {
                            quantityFLoat+=val.length();
                            if(fullStat)
                            {
                                Float temp = Float.valueOf(val);
                                floats.add(temp);
                            }
                        }
                        else
                        {
                            quantityString+=val.length();
                            if(fullStat)
                            {
                                strings.add(val.length());
                            }
                        }
                    }
                }
            }
            if(intfile.exists())
            {
                System.out.println("Кол-во символов в типе Int: "+quantityInt);
            }
            if(strfile.exists())
            {
                System.out.println("Кол-во символов в типе String: "+quantityString);
            }
            if(flfile.exists())
            {
                System.out.println("Кол-во символов в типе Float: "+quantityFLoat+"\n\n");
            }
        }
        scanner.close();

        if(fullStat)//Полная статистика
        {
            int intSum = 0;
            int floatSum = 0;

            if(strfile.exists())
            {
                Collections.sort(strings);
                System.out.println("Самая короткая строка насчитывает "+strings.get(0)+" символов");
                System.out.println("Самая длинная строка насчитывает "+strings.getLast()+" символов");
            }

            if(intfile.exists())
            {
                Collections.sort(ints);
                for (int i=0;i<ints.size();i++)
                {
                    intSum+=ints.get(i);
                }
                System.out.println("Минимальный элемент в Int: "+ints.get(0));
                System.out.println("Максимальный элемент в Int: "+ints.getLast());
                System.out.println("Сумма Int: "+intSum);
                System.out.println("Среднее арифметическое Int: "+intSum/quantityInt+"\n\n");
            }

            if(flfile.exists())
            {
                Collections.sort(floats);
                for(int i =0;i<floats.size();i++)
                {
                    floatSum+=floats.get(i);
                }
                System.out.println("Минимальный элемент во Float: "+floats.get(0));
                System.out.println("Максимальный элемент в Float: "+floats.getLast());
                System.out.println("Сумма FLoat: "+floatSum);
                System.out.println("Среднее арифметическое Float: "+floatSum/quantityFLoat+"\n\n");
            }
        }
    }
}
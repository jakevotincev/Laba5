import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

/**
 * <p>Класс, управляющий элементами коллекции</p>
 *
 * @author Вотинцев Евгений
 * @version 1.0
 */
public class CollectionManager {
    static boolean save = true;

    /**
     * <p>Коллекция животных</p>
     */
    static LinkedHashSet<Animal> animals = new LinkedHashSet<>();

    /**
     * <p>Объект класса Date, используется для вывода даты инициализации коллекции</p>
     */
    static Date date = new Date();

    /**
     * <p>Добавляет элемент в коллекцию</p>
     *
     * @param animal Элемент типа Animal
     * @return Результат добавления, true или false
     */
    public static boolean add(Animal animal) {
        boolean result;
        result = animals.add(animal);
        List<Animal> list = new ArrayList<>(animals);
        Collections.sort(list);
        animals = new LinkedHashSet<>();
        animals.addAll(list);
        return result;
    }

    /**
     * <p>Выводит на экран все элементы коллекции</p>
     */
    public static void show() {
        for (Animal current : animals) {
            System.out.println(current.toString());
        }
        if (animals.isEmpty()) System.out.println("Коллекция не содержит объектов");
    }

    /**
     * <p>Удаляет элемент из коллекции по имени</p>
     *
     * @param animal Элемент типа Animal
     */
    public static void remove(Animal animal) {
        Iterator<Animal> iterator = animals.iterator();
        while (iterator.hasNext()) {
            for (Animal a : animals) {
                iterator.next();
                if (a.equals(animal)) {
                    iterator.remove();
                    break;
                }
            }
            break;
        }
    }

    /**
     * <p>Выводит информацию о коллекции</p>
     */
    public static void info() {
        System.out.println("Тип коллекции: linkedHashSet");
        System.out.println("Дата инициалтзации: " + date.toString());
        System.out.println("Количество элементов коллекции: " + animals.size());
    }

    /**
     * <p>Удаляет все элементы коллекции, превышающие заданный</p>
     *
     * @param animal Элемент типа Animal
     */
    public static void remove_greater(Animal animal) {
        int size = animals.size();
        int count = 0;
        for (Iterator<Animal> iterator = animals.iterator(); iterator.hasNext(); ) {
            if (iterator.next().compareTo(animal) > 0) {
                iterator.remove();
                count += 1;
            }

        }
        if (size != animals.size()) System.out.println("Удалено элементов: " + count);
        else System.out.println("Нет элементов превышающих элемент c именем: " + "\"" + animal.getName() + "\"");
    }

    /**
     * <p>Добавляет элемент в коллукцию, если его значение меньше, чем у минимального элемента</p>
     *
     * @param animal Элемент типа Animal
     * @return Результат добавления, true или false
     */
    public static boolean add_if_min(Animal animal) {
        boolean result = false;
        for (Animal current : animals) {
            if (animal.compareTo(current) < 0) {
                result = add(animal);
            }
            break;
        }
        return result;
    }

    /**
     * <p>Сохраняет все элементы коллекции в файл</p>
     *
     * @param animals  Коллекция LinkedHashSet элементов типа Animal
     * @param fileName Имя файла сохранения
     * @throws IOException При попытке сохраниния в несуществующий файл
     */
    public static void save(LinkedHashSet<Animal> animals, String fileName) throws IOException {
        ParserXML parser = new ParserXML();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(parser.parseCollection(animals));
            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            transformer.transform(source, sr);
            String outputstring = sw.toString();
            FileWriter writer = new FileWriter(fileName);
            writer.write(outputstring);
            writer.flush();
            writer.close();
            System.out.println("Изменения сохранены в коллекцию");
        } catch (ParserConfigurationException e) {
            System.err.println("Ошибка парсинга");
        } catch (TransformerConfigurationException e) {
            System.err.println("Ошибка парсинга");
        } catch (TransformerException e) {
            System.err.println("Ошибка парсинга");
        }
    }

    /**
     * <p>Выводит информацию о всех командах</p>
     */
    public static void help() {
        System.out.println("remove {element}: удалить элемент из коллекции по его значению\n" +
                "show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add_if_min {element}: добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
                "remove_greater {element}: удалить из коллекции все элементы, превышающие заданный\n" +
                "info: вывести в стандартный поток вывода информацию о коллекции \n" +
                "import {String path}: добавить в коллекцию все данные из файла\n" +
                "add {element}: добавить новый элемент в коллекцию\n" +
                "save: сохраняет элементы коллекции в файл\n" +
                "exit: выход без сохранения\n" +
                "Пример задания объекта: add {\"Sova\":{\"iq\":10}, \"name\":\"a\", \"wieght\":50, \"height\":50, \"width\":50, \"color\":PINK}");
    }

    /**
     * <p>Импортирует коллекцию элементов из указанного xml файла</p>
     *
     * @param path Путь до xml файла, откуда будет производиться импорт элементов
     */
    public static void import_(String path) {
        ParserXML parser = new ParserXML();
        if (path.equals("/dev/urandom") || path.equals("/dev/random")) {
            System.err.println("Ошибка xml файла");
            return;
        }
        try {
            String filestring = parser.readXML(path);
            parser.parseXML(filestring);
            System.out.println("Данные добавлены");
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден или к нему нет доступа");
            return;
        } catch (SAXException | IOException e) {
            System.err.println("Ошибка xml файла");
            return;
        }
    }

    /**
     * <p>Закрывает программу</p>
     */
    public static void exit() {
        save = false;
        System.exit(0);
    }
}
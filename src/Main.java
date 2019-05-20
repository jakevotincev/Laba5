import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;



public class Main {
    public static void main(String[] args) {
        String filename = System.getenv().get("FILENAME");
        //System.getenv().get("FILENAME");
        //"C:\\Users\\Evgenii\\IdeaProjects\\WinnieThePooh\\src\\storage.xml"

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (CollectionManager.save) {
                try {
                    CollectionManager.save(CollectionManager.animals, filename);

                } catch (IOException | NullPointerException e) {
                    System.err.println("Не удалось сохранить коллекцию");
                }
            } else System.out.println("Изменения не сохранены");
        }));
        ParserXML parser = new ParserXML();
        boolean error = true;
        try {
            String filestring = parser.readXML(filename);
            error = !filestring.isEmpty();
            parser.parseXML(filestring);
        } catch (FileNotFoundException | NullPointerException e) {
            System.err.println("Файл не найден или к нему нет доступа");
            CollectionManager.exit();
        } catch (IOException | SAXException e) {
            if (error) {
                System.err.println("Ошибка xml файла");
                CollectionManager.exit();
            }
        }

        CommandReader reader = new CommandReader();
        reader.runCom();

    }
}

import java.io.*;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {

        GameProgress gameProgress1 =
                new GameProgress(100, 4, 2, 300.44);
        GameProgress gameProgress2 =
                new GameProgress(50, 6, 3, 300.44);
        GameProgress gameProgress3 =
                new GameProgress(75, 5, 4, 300.44);

        saveGame(gameProgress1, gameProgress2, gameProgress3);
        zipFiles();
        deleteSaveGame();
    }

    private static void saveGame(GameProgress gameProgress1, GameProgress gameProgress2, GameProgress gameProgress3) {
        try (FileOutputStream fos = new FileOutputStream("/Users/vladislav/Games/savegames/save1.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress1);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        try (FileOutputStream fos = new FileOutputStream("/Users/vladislav/Games/savegames/save2.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress2);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        try (FileOutputStream fos = new FileOutputStream("/Users/vladislav/Games/savegames/save3.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress3);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void zipFiles() {
        try (ZipOutputStream zoup = new ZipOutputStream(
                new FileOutputStream("/Users/vladislav/Games/zip_output.zip"));
             FileInputStream fis = new FileInputStream("/Users/vladislav/Games/savegames/save1.dat")) {

            File files = new File("/Users/vladislav/Games/savegames");
            for (File item : Objects.requireNonNull(files.listFiles())) {
                ZipEntry entry = new ZipEntry(String.valueOf(item));
                zoup.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zoup.write(buffer);
                zoup.closeEntry();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void deleteSaveGame() {
        File myFile = new File("/Users/vladislav/Games/savegames");
        for (File item : myFile.listFiles()) {
            item.delete();
        }
    }
}

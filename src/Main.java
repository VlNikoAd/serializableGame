import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {

        GameProgress gameProgress1 =
                new GameProgress(100, 4, 2, 300.44);
        GameProgress gameProgress2 =
                new GameProgress(50, 6, 3, 300.44);
        GameProgress gameProgress3 =
                new GameProgress(75, 5, 4, 300.44);
        List<GameProgress> mySave = Arrays.asList(gameProgress1, gameProgress2, gameProgress3);

        saveGame(mySave);
        zipFiles();
        deleteSaveGame();
        openZip();
        openProgress();
    }

    private static void saveGame(List<GameProgress> mySave) {
        List<String> nameFiles = Arrays.asList("save1.dat", "save2.dat", "save3.dat");
        for (int i = 0; i < nameFiles.size(); i++) {
            try (FileOutputStream fos = new FileOutputStream("/Users/vladislav/Games/savegames/" + nameFiles.get(i));
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(mySave.get(i));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
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

    private static void openZip() {
        try (ZipInputStream zis = new ZipInputStream(
                new FileInputStream("/Users/vladislav/Games/zip_output.zip"))) {
            ZipEntry entry;
            String name;
            while ((entry = zis.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(name);
                for (int c = zis.read(); c != -1; c = zis.read()) {
                    fout.write(c);
                }
                fout.flush();
                zis.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void openProgress() {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream("/Users/vladislav/Games/savegames/save1.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(gameProgress);
    }
}

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WallGetter {
    private static String userHome = System.getProperty("user.home");
    private static String directoryPath = userHome + "\\AppData\\Local\\Packages" +
            "\\Microsoft.Windows.ContentDeliveryManager_cw5n1h2txyewy\\LocalState\\Assets";

    private static File directory = new File(directoryPath);

    //-----------------------------------------------------------------------------------------------

    // возвращает лист файлов >100 KB
    private static List<File> findFiles(List<File> fileList) {
        List<File> searchList = new ArrayList(fileList);
        List<File> resultList = new ArrayList();

        for(File file : searchList) {
            long fileSizeInBytes = file.length();
            long fileSizeInKB = fileSizeInBytes / 1024;

            if(fileSizeInKB > 100) {
                resultList.add(file);
            }
        }

        return resultList;
    }

    //-----------------------------------------------------------------------------------------------

    // возвращает уникальный путь для расположения файла
    private static Path getNewDir(String dir) {
        int count = 1;

        File file = new File(dir + "\\wall0.jpg");

        while(file.exists()){
            String name = "\\wall" + count++ + ".jpg";
            file = new File(dir + name);
        }

        return file.toPath();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        if(directory.exists() && directory.isDirectory()) {
            String desktopDir = userHome + "\\Desktop";

            File[] filesArray = directory.listFiles();
            List<File> files = Arrays.asList(filesArray);
            List<File> results = findFiles(files);

            try {
                for (File file : results) {
                    BufferedImage bimg = ImageIO.read(file);
                    int width = bimg.getWidth();
                    int height = bimg.getHeight();

                    if(width > height) {
                        Files.copy(file.toPath(), getNewDir(desktopDir));
                    }
                }
            }catch(IOException exc){
                exc.printStackTrace();
            }
        } else {
            System.out.println("Ничего не происходит...");
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

}
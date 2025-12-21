package iuh.fit.se.group1.config;

import org.apache.commons.math3.util.Pair;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class VersionChecker {

    private static final String URL_VERSION = "https://raw.githubusercontent.com/ngphthinh/version-learn-vocabulary/main/version.txt";

    public static String getLatestVersion() {
        return Objects.requireNonNull(getDataStream()).getKey();
    }

    public static boolean updateToLatestVersion() {
        Pair<String, String> data = getDataStream();
        if (data == null) {
            return false;
        }

        String latestVersion = data.getKey();
        String downloadLink = data.getValue();

        try {
            String currentJarPath = getCurrentJarPath();
            String parentDir = new File(currentJarPath).getParent();

            String fileName = new File(new URL(downloadLink).getPath()).getName();
            String downloadedJarPath = parentDir + File.separator + fileName;

            downloadFile(downloadLink, downloadedJarPath);

            writeUpdateScript(currentJarPath, downloadedJarPath);
            writeSilentLauncher();

            // Chạy update.bat ngầm
            Runtime.getRuntime().exec("wscript run_silent.vbs");

            System.out.println("Ứng dụng sẽ thoát để cập nhật...");
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void writeUpdateScript(String currentJarPath, String updateJarPath) throws IOException {
        String batchContent = ""
                + "@echo off\n"
                + "echo Waiting for application to exit...\n"
                + ":waitloop\n"
                + "timeout /T 2 /NOBREAK >nul\n"
                + "del /F /Q \"" + currentJarPath + "\"\n"
                + "if exist \"" + currentJarPath + "\" goto waitloop\n"
                + "move /Y \"" + updateJarPath + "\" \"" + currentJarPath + "\"\n"
                + "start \"\" javaw -jar \"" + currentJarPath + "\"\n"
                + "exit\n";

        try (FileWriter fw = new FileWriter("update.bat")) {
            fw.write(batchContent);
        }
    }

    public static void writeSilentLauncher() throws IOException {
        String vbsContent = ""
                + "Set WshShell = CreateObject(\"WScript.Shell\")\n"
                + "WshShell.Run chr(34) & \"update.bat\" & Chr(34), 0\n"
                + "Set WshShell = Nothing\n";

        try (FileWriter fw = new FileWriter("run_silent.vbs")) {
            fw.write(vbsContent);
        }
    }

    public static void downloadFile(String urlStr, String destinationPath) throws IOException {
        URL url = new URL(urlStr);
        try (InputStream in = url.openStream();
             FileOutputStream fos = new FileOutputStream(destinationPath)) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }

    public static String getCurrentJarPath() {
        try {
            return new File(VersionChecker.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()).getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Không thể lấy đường dẫn file jar", e);
        }
    }

    public static Pair<String, String> getDataStream() {
        try {
            String urlWithNoCache = URL_VERSION + "?t=" + System.currentTimeMillis();
            URL url = new URL(urlWithNoCache);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Pragma", "no-cache");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String version = in.readLine();
                String linkDownload = in.readLine();

                in.close();
                return new Pair<>(version, linkDownload);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static String getCurrentVersion() {
        String version = "1.0.0"; // Mặc định là 1.0.0, bạn có thể thay đổi theo phiên bản hiện tại của ứng dụng
        try (InputStream input = VersionChecker.class.getResourceAsStream("/version.txt")) {
            if (input != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                version = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return version;
    }
}

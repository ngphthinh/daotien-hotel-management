package iuh.fit.se.group1.config;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionChecker {

    // API GitHub Releases (latest)
    private static final String RELEASE_API =
            "https://api.github.com/repos/ngphthinh/daotien-hotel-management/releases/latest";

    public static String getCurrentVersion() {
        return AppInfo.getVersion();
    }

    public static String getLatestVersion() {
        try {
            String json = readUrl(RELEASE_API);

            // "tag_name": "v1.0.1"
            Pattern p = Pattern.compile("\"tag_name\"\\s*:\\s*\"v?(.*?)\"");
            Matcher m = p.matcher(json);

            if (m.find()) {
                return m.group(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLatestJarDownloadUrl() {
        try {
            String json = readUrl(RELEASE_API);

            // "browser_download_url": "https://github.com/.../hotel-management.jar"
            Pattern p = Pattern.compile("\"browser_download_url\"\\s*:\\s*\"(.*?)\"");
            Matcher m = p.matcher(json);

            if (m.find()) {
                return m.group(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isUpdateAvailable() {
        String current = getCurrentVersion();
        String latest = getLatestVersion();

        if (current == null || latest == null) return false;

        return compareVersion(latest, current) > 0;
    }

    public static boolean updateToLatestVersion() {
        try {
            if (!isUpdateAvailable()) return false;

            String downloadUrl = getLatestJarDownloadUrl();
            if (downloadUrl == null) return false;

            File currentJar = new File(getCurrentJarPath());
            File parentDir = currentJar.getParentFile();
            File newJar = new File(parentDir, "update.jar");

            downloadFile(downloadUrl, newJar);
            writeUpdateScript(currentJar, newJar);

            Runtime.getRuntime().exec("cmd /c start update.bat");
            System.exit(0);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ======================= SUPPORT =======================

    private static String readUrl(String urlStr) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestProperty("Accept", "application/vnd.github+json");

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

    private static void downloadFile(String url, File target) throws IOException {
        try (InputStream in = new URL(url).openStream()) {
            Files.copy(in, target.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void writeUpdateScript(File currentJar, File newJar) throws IOException {
        String script =
                "@echo off\n" +
                        "timeout /T 2 >nul\n" +
                        "del \"" + currentJar.getAbsolutePath() + "\"\n" +
                        "move \"" + newJar.getAbsolutePath() + "\" \"" + currentJar.getAbsolutePath() + "\"\n" +
                        "start \"\" javaw -jar \"" + currentJar.getAbsolutePath() + "\"\n";

        Files.write(new File("update.bat").toPath(), script.getBytes());
    }

    private static String getCurrentJarPath() throws Exception {
        return new File(VersionChecker.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()).getAbsolutePath();
    }

    private static int compareVersion(String v1, String v2) {
        String[] a = v1.split("\\.");
        String[] b = v2.split("\\.");

        for (int i = 0; i < Math.max(a.length, b.length); i++) {
            int x = i < a.length ? Integer.parseInt(a[i]) : 0;
            int y = i < b.length ? Integer.parseInt(b[i]) : 0;
            if (x != y) return x - y;
        }
        return 0;
    }
}

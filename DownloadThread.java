import java.io.*;
import java.net.*;

public class DownloadThread extends Thread {
    private String urlToDownload;
    private String fileName;
    private long startByte;
    private long endByte;
    private long downloadedBytes;

    public DownloadThread(String urlToDownload, String fileName, long startByte, long endByte) {
        this.urlToDownload = urlToDownload;
        this.fileName = fileName;
        this.startByte = startByte;
        this.endByte = endByte;
    }

    public void run() {
        try {
            URL u = new URL(urlToDownload);
            HttpURLConnection uc = (HttpURLConnection) u.openConnection();
            uc.setRequestProperty("Range", "bytes=" + startByte + "-" + endByte);
            InputStream in = uc.getInputStream();

            RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
            raf.seek(startByte);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                synchronized (ConsoleIDM.class) {
                    while (ConsoleIDM.paused) {
                        try {
                            ConsoleIDM.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace(); // Handle interruption as needed
                        }
                    }
                }
                raf.write(buffer, 0, bytesRead);
                downloadedBytes += bytesRead;
            }

            raf.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getDownloadedBytes() {
        return downloadedBytes;
    }
}

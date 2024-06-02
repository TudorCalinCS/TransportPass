package utils;

import repository.RepoImagineDB;
import server.SrvException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class OrareGenerator {

    public static void main(String[] args) throws SrvException {
        Properties serverProps = new Properties();
        try {
            serverProps.load(server.StartObjectServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties " + e);
            return;
        }
        RepoImagineDB repoImagineDB = new RepoImagineDB(serverProps);
        String imagePath = "C:\\Users\\ALEXANDRA\\Desktop\\MPP\\TransportPass\\Back-end\\src\\main\\resources\\102L.jpeg";

        byte[] continutImagine = citesteImagine(imagePath);
        if (continutImagine != null) {
            repoImagineDB.save("ORAR", "102L", continutImagine);
        }
    }
    public static byte[] citesteImagine(String caleImagine) {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;

        try {
            fis = new FileInputStream(new File(caleImagine));
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
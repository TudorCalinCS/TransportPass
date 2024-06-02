package utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HttpClientExample {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:5000/process_image");

        // Imaginea sub formă de byte array
        byte[] imageData = getImageData();

        // Adaugă imaginea la cerere
        ByteArrayBody byteArrayBody = new ByteArrayBody(imageData, ContentType.DEFAULT_BINARY, "image.jpg");

        // Crează entitatea cererii Multipart
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("file", byteArrayBody)
                .build();
        httpPost.setEntity(reqEntity);

        // Trimite cererea și primește răspunsul
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String result = EntityUtils.toString(responseEntity);
                System.out.println("Server response: " + result);
            }
        }
    }

    // Funcție pentru a obține byte array din imagine (aici poți citi imaginea dintr-o sursă)
    public static byte[] getImageData() throws IOException {
        InputStream inputStream = HttpClientExample.class.getClassLoader().getResourceAsStream("carnet3.jpeg");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }

}

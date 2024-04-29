package utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    public static void generateAndSaveQRCode(long number, String filePath) {
        try {
            String text = String.valueOf(number);
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 200, 200, hints);

            BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < 200; i++) {
                for (int j = 0; j < 200; j++) {
                    image.setRGB(i, j, bitMatrix.get(i, j) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            ImageIO.write(image, "PNG", new File(filePath));
            System.out.println("QR Code saved to: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long decodeQRCode(String filePath) {
        try {
            
            BufferedImage image = ImageIO.read(new File(filePath));
            HybridBinarizer binarizer = new HybridBinarizer(new BufferedImageLuminanceSource(image));
            com.google.zxing.BinaryBitmap binaryBitmap = new com.google.zxing.BinaryBitmap(binarizer);

            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            return Long.parseLong(result.getText());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) {
        //generateAndSaveQRCode(1234,"C:\\Users\\Tudor\\Desktop\\QR1.png");
        System.out.println(decodeQRCode("C:\\Users\\Tudor\\Desktop\\QR1.png"));
    }
}

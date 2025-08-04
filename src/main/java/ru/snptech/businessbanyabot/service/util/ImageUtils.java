package ru.snptech.businessbanyabot.service.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.UUID;

public class ImageUtils {

    private static final Base64.Decoder decoder = Base64.getDecoder();

    public static File decodeBase64ToFile(String base64Image) throws Exception {
        byte[] imageBytes = decoder.decode(base64Image);

        File file = new File(UUID.randomUUID().toString());

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageBytes);
        }

        return file;
    }
}

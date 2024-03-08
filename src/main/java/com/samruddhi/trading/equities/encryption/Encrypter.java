package com.samruddhi.trading.equities.encryption;

import com.samruddhi.trading.equities.services.TradeStationAuthImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.util.Base64;
import java.util.Map;

public class Encrypter {
    private static final Logger logger = LoggerFactory.getLogger(TradeStationAuthImpl.class);
    private final String LINE_SEPARATOR = System.getProperty("line.separator");

    public void encryptAndWriteToFile() throws Exception {
        SecretKey secretKey = generateKey();
        logger.info("Secret key" + secretKey.toString());

        saveSecretKey(secretKey, new File(System.getProperty("user.dir") + "/src/main/resources/secret.key"));
        //encrypt cient id key
        String clientIdEnc = encrypt("COKKzfMyHCbSncPo5LOXtPKEzo2z7VtC", secretKey);
        String secretEnc = encrypt("55DS4qsw8Ua76urM3sX-K4DVOIdw9gyDrZPs3vTM_k6tx_Wd4FpOJdoLwRH-wVFS", secretKey);

        writeUsingBufferedWriter(Map.of("CLIENT_ID", clientIdEnc, "SECRET", secretEnc));

    }

    private void saveSecretKey(SecretKey key, File file) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SecretKey loadSecretKey(File file) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (SecretKey) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Store encrypted broker credentials and my private KEY
     *
     * @param keyValues
     * @throws IOException
     */
    private void writeUsingBufferedWriter(Map<String, String> keyValues) throws IOException {
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/hashed.properties");
        logger.info("File" + file);
        //new File("/src/main/resources/hashed.properties");
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;

        try {
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            for (Map.Entry<String, String> entry : keyValues.entrySet()) {
                bufferedWriter.write(entry.getKey() + "=" + entry.getValue() + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }


    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // Use 256-bit AES encryption
        return keyGenerator.generateKey();
    }

    public String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }

    public static void main(String[] args) {
        try {
            Encrypter encrypter = new Encrypter();
            encrypter.encryptAndWriteToFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

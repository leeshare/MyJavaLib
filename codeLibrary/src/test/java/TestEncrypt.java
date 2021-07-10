
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.IllegalFormatCodePointException;

/**
 * 测试 AES 对称加密算法
 * Created by Administrator on 7/30/2019.
 */
public class TestEncrypt {

    //static final String PASSWORD = "123456";
    public static byte[] iv = {1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8};
    public static void main(String[] args){

        String content = "中国人";
        String type = "AES";

        try {
            String encrypt = encrypt4AES(content);
            String source = decrypt4AES(encrypt);
            System.out.println(content + " 密钥= " + encrypt + " 还原为= " + source);

            encrypt = doEncrypt(content, type);
            source = doDecrypt(encrypt, type);
            System.out.println(content + " 密钥= " + encrypt + " 还原为= " + source);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String encrypt4AES(String source) {
        try {

            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            SecretKeySpec key1 = new SecretKeySpec(iv, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key1, zeroIv);
            byte[] encryptedData = cipher.doFinal(source.getBytes());
            String encryptResultStr = parseByte2HexStr(encryptedData);
            return encryptResultStr; // 加密
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String decrypt4AES(String content) {
        try {
            byte[] decryptFrom = parseHexStr2Byte(content);

            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            SecretKeySpec key1 = new SecretKeySpec(iv, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key1, zeroIv);
            byte decryptedData[] = cipher.doFinal(decryptFrom);
            return new String(decryptedData); // 加密
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String parseByte2HexStr(byte buf[]){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < buf.length; i++){
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String s) {
        s = s.toUpperCase();
        int len = s.length() / 2;
        int ii = 0;
        byte[] bs = new byte[len];
        char c;
        int h;
        for (int i = 0; i < len; i++) {
            c = s.charAt(ii++);
            if (c <= '9') {
                h = c - '0';
            } else {
                h = c - 'A' + 10;
            }
            h <<= 4;
            c = s.charAt(ii++);
            if (c <= '9') {
                h |= c - '0';
            } else {
                h |= c - 'A' + 10;
            }
            bs[i] = (byte) h;
        }
        return bs;
    }


    /**
     * 有问题，无法使用
     * @param type      AES
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String doEncrypt(String content, String type) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException
    {
        //KeyGenerator kgen = KeyGenerator.getInstance(type);
        //kgen.init(128, new SecureRandom(iv));
        //SecretKey secretKey = kgen.generateKey();
        //byte[] enCodeFormat = secretKey.getEncoded();
        //SecretKeySpec key = new SecretKeySpec(enCodeFormat, type);
        SecretKeySpec key = new SecretKeySpec(iv, type);
        Cipher cipher = Cipher.getInstance(type + "/CBC/PKCS5Padding");
        //byte[] byteContent = content.getBytes("utf-8");
        //cipher.init(Cipher.ENCRYPT_MODE, key);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] byteContent = content.getBytes();
        byte[] result = cipher.doFinal(byteContent);
        String hexStr = parseByte2HexStr(result);

        return hexStr;
    }

    /**
     * 有问题，无法使用
     * @param encryptContent
     * @param type
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws UnsupportedEncodingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidAlgorithmParameterException
     */
    public static String doDecrypt(String encryptContent, String type) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException
    {
        SecretKeySpec key = new SecretKeySpec(iv, type);
        Cipher cipher = Cipher.getInstance(type + "/CBC/PKCS5Padding");
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        //byte[] byteContent = encryptContent.getBytes("utf-8");
        byte[] byteContent = parseHexStr2Byte(encryptContent);
        byte[] result = cipher.doFinal(byteContent);
        //return result.toString();
        return new String(result);
    }
}

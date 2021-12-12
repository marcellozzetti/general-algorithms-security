import model.rc4.InputRC4DTO;
import model.rc4.RC4Model;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by marcello.ozzetti on 12/12/21.
 */
public class RC4Main {

    public static DataInputStream inp = new DataInputStream(System.in);
    public static InputRC4DTO input = new InputRC4DTO();

    public static void main(String args[]) throws Exception {

        readInputData();

        dataValidation();

        processor();

        System.exit(0);

    }

    private static void processor() {

        byte[] bMessage = input.getMessage().getBytes();
        byte[] bKey = input.getKey().getBytes();

        RC4Model rc4 = new RC4Model(bKey);

        byte[] cipher = rc4.encrypt(bMessage);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cipher.length; i++) {
            sb.append(String.format("%02x:", cipher[i]));
        }
        System.out.println("Cipher Hex: " + sb.toString());
    }

    private static void dataValidation() {
        if (input.getKey().getBytes().length < 1 || input.getKey().getBytes().length > 256) {
            throw new IllegalArgumentException(
                    "key must be between 1 and 256 bytes");
        }

        if (input.getMessage().getBytes().length < 1 || input.getMessage().getBytes().length > 256) {
            throw new IllegalArgumentException(
                    "message must be between 1 and 256 bytes");
        }
    }

    private static void readInputData() throws IOException{

        String message;
        String key;

        System.out.print("Enter the Message: ");
        message = inp.readLine();

        System.out.print("Enter the Key: ");

        key = inp.readLine();

        input.setKey(key);
        input.setMessage(message);
    }
}

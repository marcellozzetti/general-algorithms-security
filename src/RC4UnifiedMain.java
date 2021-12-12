import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by marcello.ozzetti on 12/12/21.
 */
public class RC4UnifiedMain {

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
        System.out.println(sb.toString());
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

    private static void readInputData() throws IOException {

        Scanner in = new Scanner(System.in);
        List<String> lines = new ArrayList<String>();
        String lineNew;

        while (in.hasNextLine()) {
            lineNew = in.nextLine();
            if (lineNew.isEmpty()) {
                break;
            }
            lines.add(lineNew);
        }

        input.setMessage(lines.get(0).toString());
        input.setKey(lines.get(1).toString());
    }

    static class InputRC4DTO {

        String message;
        String key;

        public void setMessage(String message) {
            this.message = message;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getMessage() {
            return message;
        }

        public String getKey() {
            return key;
        }
    }

    static class RC4Model {

        private final byte[] S = new byte[256];
        private final byte[] T = new byte[256];
        private final int keylen;

        public RC4Model(final byte[] key) {
            keylen = key.length;
            for (int i = 0; i < 256; i++) {
                S[i] = (byte) i;
                T[i] = key[i % keylen];
            }

            int j = 0;
            for (int i = 0; i < 256; i++) {
                j = (j + S[i] + T[i]) & 0xFF;
                byte temp = S[i];
                S[i] = S[j];
                S[j] = temp;
            }
        }

        public byte[] encrypt(final byte[] plaintext) {
            final byte[] ciphertext = new byte[plaintext.length];
            int i = 0, j = 0, k, t;
            for (int counter = 0; counter < plaintext.length; counter++) {
                i = (i + 1) & 0xFF;
                j = (j + S[i]) & 0xFF;
                byte temp = S[i];
                S[i] = S[j];
                S[j] = temp;
                t = (S[i] + S[j]) & 0xFF;
                k = S[t];
                ciphertext[counter] = (byte) (plaintext[counter] ^ k);
            }
            return ciphertext;
        }

        public byte[] decrypt(final byte[] ciphertext) {
            return encrypt(ciphertext);
        }
    }
}

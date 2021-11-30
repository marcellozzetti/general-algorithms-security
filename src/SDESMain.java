import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by marcello.ozzetti on 29/11/21.
 */
public class SDESMain {

    public static ArrayList<InputDTO> array = new ArrayList<InputDTO>();
    public static SimplifiedDESController controllerSDES;
    public static int row = 0;

    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner(System.in);
        List<String> lines = new ArrayList<String>();
        String lineNew;

        while (input.hasNextLine()) {
            lineNew = input.nextLine();
            if (lineNew.isEmpty()) {
                break;
            }
            lines.add(lineNew);
        }

        mainProcessor(lines);
        System.exit(0);

    }

    private static void mainProcessor(List<String> lines) throws IOException {

        int operations = 0;
        try{
            operations = Integer.parseInt(lines.get(0));
        } catch(Exception e){
            System.exit(1);
        }

        if(operations == -1){
            System.exit(0);

        } else if(operations > 0 && operations <= 100){

            while (operations > 0){
                readInputData(lines);
                operations--;
            }

            sdesProcessor();

        } else {
            System.out.println("Wrong Number of Operations! Try Again ....");
            mainProcessor(lines);
        }
    }

    private static void sdesProcessor() {
        if(array != null){

            for (InputDTO inputDTO : array) {
                switch (inputDTO.getOperation()) {
                    case "C":
                        controllerSDES = new SimplifiedDESController(inputDTO.getKey());
                        controllerSDES.printData(controllerSDES.encrypt(inputDTO.getMessage()), 8);
                        break;

                    case "D":
                        controllerSDES = new SimplifiedDESController(inputDTO.getKey());
                        controllerSDES.printData(controllerSDES.decrypt(inputDTO.getMessage()), 8);
                        break;

                    default:
                        System.out.println("\nWrong Option!");
                        break;

                }
                System.out.println();
            }
        }
    }

    private static void readInputData(List<String> lines) throws IOException{
        int key;
        int message;
        String operation = "";

        try{
            row++;
            operation = lines.get(row);

            row ++;
            key = Integer.parseInt(lines.get(row),2);

            row++;
            message = Integer.parseInt(lines.get(row),2);

            InputDTO input = new InputDTO();
            input.setOperation(operation);
            input.setKey(key);
            input.setMessage(message);

            array.add(input);

        } catch(Exception e){
            System.exit(1);
        }
    }

    static class InputDTO {

        String operation;
        int key;
        int message;

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public void setMessage(int message) {
            this.message = message;
        }

        public String getOperation() {
            return operation;
        }

        public int getKey() {
            return key;
        }

        public int getMessage() {
            return message;
        }
    }

    static class SimplifiedDES {

        //Used in F1 Function
        public final int EP[] = { 4, 1, 2, 3, 2, 3, 4, 1};
        public final int EPmax = 4;
        public final int P4[] = { 2, 4, 3, 1};
        public final int P4max = 4;
        public final int S0[][] = {{ 1, 0, 3, 2},{ 3, 2, 1, 0},{ 0, 2, 1, 3},{ 3, 1, 3, 2}};
        public final int S1[][] = {{ 0, 1, 2, 3},{ 2, 0, 1, 3},{ 3, 0, 1, 2},{ 2, 1, 0, 3}};

        /**
         * Generic function for permutations
         * @param message
         * @param p
         * @param pMax
         */
        public static int permute(int message, int p[], int pMax) {
            int y=0;

            for(int i=0;i<p.length;i++) {
                y=y<<1;
                y=y|(message>>(pMax-p[i]))&1;
            }

            return y;
        }

        /**
         * Generic function F
         * @param message
         * @param key
         */
        public int F(int message, int key) {
            int t = permute( message, EP, EPmax) ^ key;
            int t0 = (t >> 4) & 0xF;
            int t1 = t & 0xF;
            t0 = S0[ ((t0 & 0x8) >> 2) | (t0 & 1) ][ (t0 >> 1) & 0x3 ];
            t1 = S1[ ((t1 & 0x8) >> 2) | (t1 & 1) ][ (t1 >> 1) & 0x3 ];
            t = permute( (t0 << 2) | t1, P4, P4max);
            return t;

        }

        /**
         * Generic function Fk
         * @param message
         * @param key
         */
        public int fK(int message, int key) {
            int L = (message >> 4) & 0xF;
            int R = message & 0xF;
            return ((L ^ F(R,key)) << 4) | R;
        }

        /**
         * Generic function SW
         * @param message
         */
        public int SW(int message) {
            return ((message & 0xF) << 4) | ((message >> 4) & 0xF);
        }
    }

    static class SimplifiedDESController {

        public SimplifiedDES modelSDES = new SimplifiedDES();
        public int K1, K2;
        //P10 permutation
        public final int P10[] = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
        public final int P10max = 10;
        //10 to 8 bit permutation
        public final int P8[] = { 6, 3, 7, 4, 8, 5, 10, 9};
        public final int P8max = 10;
        //Initial permutaion
        public final int IP[] = { 2, 6, 3, 1, 4, 8, 5, 7};
        public final int IPmax = 8;
        //Inverse of initial permutation
        public final int IPI[] = { 4, 1, 3, 5, 7, 2, 8, 6};
        public final int IPImax = 8;

        /**
         * Main constructor responsible for generate the Key K1 and K2
         * @param key
         */
        public SimplifiedDESController(int key) {
            key = modelSDES.permute(key, P10, P10max);
            int t1 = (key >> 5) & 0x1F;
            int t2 = key & 0x1F;

            t1 = ((t1 & 0xF) << 1) | ((t1 & 0x10) >> 4);
            t2 = ((t2 & 0xF) << 1) | ((t2 & 0x10) >> 4);
            K1 = modelSDES.permute( (t1 << 5)| t2, P8, P8max);
            t1 = ((t1 & 0x7) << 2) | ((t1 & 0x18) >> 3);
            t2 = ((t2 & 0x7) << 2) | ((t2 & 0x18) >> 3);
            K2 = modelSDES.permute( (t1 << 5)| t2, P8, P8max);

        }

        /**
         * Encrypt method used to generate the ciphertext
         * @param plaintext
         */
        public byte encrypt(int plaintext) {

            plaintext = modelSDES.permute( plaintext, IP, IPmax);
            plaintext = modelSDES.fK( plaintext, K1);
            plaintext = modelSDES.SW( plaintext );
            plaintext = modelSDES.fK( plaintext, K2);
            plaintext = modelSDES.permute( plaintext, IPI, IPImax);

            return (byte) plaintext;

        }

        /**
         * Decrypt method used to generate the plaintext
         * @param ciphertext
         */
        public byte decrypt(int ciphertext) {

            ciphertext = modelSDES.permute( ciphertext, IP, IPmax);
            ciphertext = modelSDES.fK( ciphertext, K2);
            ciphertext = modelSDES.SW( ciphertext );
            ciphertext = modelSDES.fK( ciphertext, K1);
            ciphertext = modelSDES.permute( ciphertext, IPI, IPImax);

            return (byte) ciphertext;

        }

        /**
         * print method used to print the bit from a text with a specific size
         * @param text
         * @param size
         */
        public void printData(int text, int size) {
            int mask = 1 << (size-1);
            while( mask > 0) {
                System.out.print( ((text & mask) == 0) ? '0' : '1');
                mask >>= 1;
            }
        }
    }
}

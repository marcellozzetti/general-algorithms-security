package control;

import model.SimplifiedDES;

/**
 * Created by marcello.ozzetti on 28/11/21.
 */
public class SimplifiedDESController {

    public static SimplifiedDES modelSDES;
    public int K1, K2;
    //P10 permutation
    public static final int P10[] = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    public static final int P10max = 10;
    //10 to 8 bit permutation
    public static final int P8[] = { 6, 3, 7, 4, 8, 5, 10, 9};
    public static final int P8max = 10;
    //Initial permutaion
    public static final int IP[] = { 2, 6, 3, 1, 4, 8, 5, 7};
    public static final int IPmax = 8;
    //Inverse of initial permutation
    public static final int IPI[] = { 4, 1, 3, 5, 7, 2, 8, 6};
    public static final int IPImax = 8;

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
    public static void printData(int text, int size) {
        int mask = 1 << (size-1);
        while( mask > 0) {
            System.out.print( ((text & mask) == 0) ? '0' : '1');
            mask >>= 1;
        }
    }
}

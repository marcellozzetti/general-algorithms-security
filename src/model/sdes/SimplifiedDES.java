package model.sdes;

/**
 * Created by marcello.ozzetti on 28/11/21.
 */
public class SimplifiedDES {


    //Used in F1 Function
    public static final int EP[] = { 4, 1, 2, 3, 2, 3, 4, 1};
    public static final int EPmax = 4;
    public static final int P4[] = { 2, 4, 3, 1};
    public static final int P4max = 4;
    public static final int S0[][] = {{ 1, 0, 3, 2},{ 3, 2, 1, 0},{ 0, 2, 1, 3},{ 3, 1, 3, 2}};
    public static final int S1[][] = {{ 0, 1, 2, 3},{ 2, 0, 1, 3},{ 3, 0, 1, 2},{ 2, 1, 0, 3}};

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
    public static int F(int message, int key) {
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
    public static int fK(int message, int key) {
        int L = (message >> 4) & 0xF;
        int R = message & 0xF;
        return ((L ^ F(R,key)) << 4) | R;
    }

    /**
     * Generic function SW
     * @param message
     */
    public static int SW(int message) {
        return ((message & 0xF) << 4) | ((message >> 4) & 0xF);
    }
}

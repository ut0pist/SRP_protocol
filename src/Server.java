import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class Server {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private final int k = 3, g = 2;
    private shaUtils shaUtils;

    private String I, s, u;
    private int v, A, B, n, b;

    private BigInteger S = new BigInteger("0");
    private BigInteger V;
    //private BigInteger U = new BigInteger(u);
    private BigInteger N;

    private randomNumber randomNumber = new randomNumber();

    private String K;
    private String M;




    public boolean authorizeClient(String I, int A) {



        //Фаза 2
        //Генерация подтверждения


        return true;
    }

    public boolean checkA(){
        if (A == 0) return false;
        return true;
    }

    public boolean checkU(){
        if (u.equals("0")) return false;//todo
        return true;
    }

    public boolean checkI(String I){
        if (!this.I.equals(I)) return false;
        return true;
    }

    public void setI(String I) {
        this.I = I;
    }

    public void setA(int A) {
        this.A = A;
    }

    public void setSalt(String s) {
        this.s = s;
    }

    public void setV(int v) {
        this.v = v;
    }

    public void setN(int n){
        this.n = n;
    }

    public String getSalt() {
        return s;
    }

    public int getB() {
        return B;
    }

    public void findB() {
        b = randomNumber.getRandomNumberInRange(2, 1000);
        BigInteger bigB = new BigInteger("0");
        BigInteger B = new BigInteger(Integer.toString(b));
        BigInteger K = new BigInteger(Integer.toString(k));
        V = new BigInteger(Integer.toString(v));
        BigInteger G = new BigInteger(Integer.toString(g));
        N = new BigInteger(Integer.toString(n));

        bigB = K.multiply(V);
        bigB = bigB.add(G.modPow(B, N));
        bigB = bigB.mod(N);

        this.B = bigB.intValue();
    }

    public boolean compareM(String clientM) {
        String HN, Hg, HI;

        shaUtils = new shaUtils(N.toByteArray(), "SHA-256");
        HN = shaUtils.getHashedString();
        shaUtils = new shaUtils(Integer.toString(g).getBytes(), "SHA-256");
        Hg = shaUtils.getHashedString();
        BigInteger biHN = new BigInteger(HN, 16);
        BigInteger biHg = new BigInteger(Hg, 16);
        BigInteger biM = biHN.xor(biHg);

        shaUtils = new shaUtils(I.getBytes(), "SHA-256");
        HI = shaUtils.getHashedString();

        M = biM.toString() + HI + S.toString() + A + B + k;
        shaUtils = new shaUtils(M.getBytes(), "SHA-256");
        M = shaUtils.getHashedString();

        if (clientM == M) {
            System.out.println("M = " + M + "\nM are the same");
            return true;
        }
        else {
            System.out.println("M are different");
            return false;
        }

    }

    public String getR() {
        String R;

        //R = H (A, M, K)
        String temp = A + M + K;
        shaUtils = new shaUtils(temp.getBytes(), "SHA-256");
        R = shaUtils.getHashedString();

        return R;
    }

    public void calculateU() {
        //Вычисляем u
        String AB = A + "|" + B;
        shaUtils shaUtils = new shaUtils(AB.getBytes(UTF_8), "SHA-256");
        u = shaUtils.getHashedString();
        //u = u.substring(0, 4);
        System.out.println("Server's u = " + u);
    }

    public void generateKey(){
        //Генерируем общий ключ сессии
        BigInteger bigA = new BigInteger(Integer.toString(A));
        BigInteger U = new BigInteger(u, 16);
        S = V.modPow(U, N);
        S = bigA.multiply(S);
        BigInteger B = new BigInteger(Integer.toString(b));
        S = S.modPow(B, N);
        //S = S.pow(b);
        //S = S.mod(N);

        System.out.println("Server's S = " + S.toString());

        shaUtils = new shaUtils(S.toByteArray(), "SHA-256");
        K = shaUtils.getHashedString();
        System.out.println("Server's key: " + K);
    }

}

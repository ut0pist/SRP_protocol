import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Client {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private randomNumber randomNumber = new randomNumber();
    private final int k = 3, g = 2;

    private String s, x, p, u, I = "alex";
    private int n, v, A, B;

    private BigInteger bigA = new BigInteger("0");
    private BigInteger G;
    private BigInteger N;
    private BigInteger bigB = new BigInteger(Integer.toString(B));
    private BigInteger bigK = new BigInteger(Integer.toString(k));
    private BigInteger X;
    private BigInteger S = new BigInteger("0");

    private Server server = new Server();
    private shaUtils shaUtils;

    private String K;
    private String M;

    int a = randomNumber.getRandomNumberInRange(2, 1000);
    BigInteger biA = new BigInteger(Integer.toString(a));

    public Client () {
    }

    void setN(int n){
        this.n = n;
    }

    public String getM () {
        return M;
    }


    public void registerOnServer() {

        /*for (int i = 0; i < 5; i++) {
            char temp = (char) randomNumber.getRandomNumberInRange(97, 122);
            s += temp;
        }*/
        s = "Hello";
        p = "password";
        String sP = s + "|" + p;

        shaUtils = new shaUtils(sP.getBytes(UTF_8), "SHA-256");
        x = shaUtils.getHashedString();
        //x = x.substring(0, 4);
        //Т.к. x является очень большим 256-битным числом, то возводить в степень этого числа на слабом железе не выйдет. Поэтому я обрежу x до 4-х цифр.

        X = new BigInteger(x, 16);
        G = new BigInteger(Integer.toString(g));
        BigInteger V = new BigInteger("0");
        N = new BigInteger((Integer.toString(n)));

        V = G.modPow(X, N);
        v = V.intValue();

    }

    public void authorizeOnServer() {



        //Фаза 2
        //Генерация подтверждения
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

        M = biM.toString() + HI + S.toString() + biA.toString() + B + k;
        shaUtils = new shaUtils(M.getBytes(), "SHA-256");
        M = shaUtils.getHashedString();


    }

    public String getI() {
        return I;
    }

    public int getA() {
        calculateA();
        return A;
    }

    public int getV() {
        return v;
    }

    public String getSalt() {
        return s;
    }

    public void setB(int B){
        this.B = B;
    }

    public void setSalt(String s) {
        this.s = s;
    }

    public boolean checkB(){
        if (B == 0) return false;
        return true;
    }

    public boolean checkU(){
        if (u.equals("0")) return false;//todo
        return true;
    }

    public boolean compareR(String serverR) {
        String R;

        //R = H (A, M, K)
        String temp = bigA.toString() + M + K;
        shaUtils = new shaUtils(temp.getBytes(), "SHA-256");
        R = shaUtils.getHashedString();

        if (R == serverR) {
            System.out.println("R are the same");
            return true;
        }
        else {
            System.out.println("R are different");
            return false;
        }
    }

    private void calculateA() {
        bigA = G.modPow(biA, N);
        A = bigA.intValue();
    }

    public void calculateU() {
        //Вычисляем u
        String AB = A + "|" + B;
        shaUtils = new shaUtils(AB.getBytes(UTF_8), "SHA-256");
        u = shaUtils.getHashedString();
        //u = u.substring(0, 4);
        System.out.println("Client's u = " + u);
    }

    public void generateKey(){
        //Генерируем общий ключ сессии
        S = G.modPow(X, N);
        S = S.multiply(bigK);
        S = bigB.subtract(S);
        BigInteger U = new BigInteger(u, 16);
        BigInteger bigPower = U.multiply(X);
        bigPower = bigPower.add(biA);
        S = S.modPow(bigPower,N);

        System.out.println("Client's S = " + S.toString());

        shaUtils = new shaUtils(S.toByteArray(), "SHA-256");
        K = shaUtils.getHashedString();
        System.out.println("Client's key: " + K);
    }

}

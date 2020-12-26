import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Client {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private final randomNumber randomNumber = new randomNumber();
    private shaUtils shaUtils;

    private final int k = 3, g = 2;
    private int n, v, a, A, B;
    private final String  p = "password", I = "alex";
    private String s, x, u, K, M;

    private BigInteger biga, bigA, G, N, bigB, X, S;
    private final BigInteger bigK = new BigInteger(Integer.toString(k));


    public Client() {
    }

    void setN(int n) {
        this.n = n;
    }

    public void registerOnServer() {

        for (int i = 0; i < 5; i++) {
            char temp = (char) randomNumber.getRandomNumberInRange(97, 122);
            s += temp;
        }
        //s = "Hello";
        String sP = s + "|" + p;

        shaUtils = new shaUtils(sP.getBytes(UTF_8), "SHA-256");
        x = shaUtils.getHashedString();

        X = new BigInteger(x, 16);
        G = new BigInteger(Integer.toString(g));
        BigInteger V;
        N = new BigInteger((Integer.toString(n)));

        V = G.modPow(X, N);
        v = V.intValue();

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

    public String getM() {
        return M;
    }

    public void setB(int B) {
        this.B = B;
        bigB = new BigInteger(Integer.toString(B));
    }

    public void setSalt(String s) {
        this.s = s;
    }

    public boolean checkB() {
        if (B == 0) return false;
        return true;
    }

    public boolean checkU() {
        if (u.equals("0")) return false;//todo
        return true;
    }

    public boolean compareR(String serverR) {
        String R;

        String temp = bigA.toString() + M + K;
        shaUtils = new shaUtils(temp.getBytes(), "SHA-256");
        R = shaUtils.getHashedString();
        System.out.println("Clients R = " + R);

        if (R.equals(serverR)) {
            return true;
        }
        return false;

    }

    private void calculateA() {
        a = randomNumber.getRandomNumberInRange(2, 1000);
        biga = new BigInteger(Integer.toString(a));
        bigA = G.modPow(biga, N);
        A = bigA.intValue();
    }

    public void calculateU() {
        //Вычисляем u
        String AB = A + "|" + B;
        shaUtils = new shaUtils(AB.getBytes(UTF_8), "SHA-256");
        u = shaUtils.getHashedString();
        //System.out.println("Client's u = " + u);
    }

    public void calculateM() {
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

        M = biM.toString() + "|" + HI+ "|" + S.toString() + "|" + A + "|" + B + "|" + k;
        shaUtils = new shaUtils(M.getBytes(), "SHA-256");
        M = shaUtils.getHashedString();
        System.out.println("Clients M = " + M);
    }

    public void generateKey() {
        //Генерируем общий ключ сессии
        S = G.modPow(X, N);
        S = S.multiply(bigK);
        S = bigB.subtract(S);
        BigInteger U = new BigInteger(u, 16);
        BigInteger bigPower = U.multiply(X);
        bigPower = bigPower.add(biga);
        S = S.modPow(bigPower, N);

        shaUtils = new shaUtils(S.toByteArray(), "SHA-256");
        K = shaUtils.getHashedString();
        System.out.println("Client's key: " + K);
    }

}

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Random;

public class randomNumber {


    public int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("Правая граница должна быть больше левой");
        }

        Random r = new Random();
        int result = 0;
        result = r.nextInt((max - min) + 1) + min;
        return result;
    }

    public int getPrimeNumber(int max) {
        int p = 0;
        do {
            p = getRandomNumberInRange(2, max);
        }
        while (!RabinMillerTest(p));

        return p;
    }

    private boolean RabinMillerTest(int p) {
        if (p % 2 == 0) return false;
        // ---тест Рабина - Миллера--- //
        //находим b
        int b = 0;
        int evenP = p - 1;
        do {
            b++;
            evenP /= 2;
        }
        while (evenP % 2 == 0);

        //находим m
        int m = (int) ((p - 1) / Math.pow(2, b));
        /*System.out.println("b = " + b);
        System.out.println("m = " + m);
        System.out.println("p = 1 + 2 ^ " + b + " * " + m);*/

        attemptsLoop:
        for (int attempt = 0; attempt < 5; attempt++) {
            int a = getRandomNumberInRange(1, p - 1);
            BigInteger A = new BigInteger(Integer.toString(a));
            BigInteger M = new BigInteger(Integer.toString(m));
            BigInteger P = new BigInteger(Integer.toString(p));
            BigInteger Z = new BigInteger("0");
            BigInteger TWO = new BigInteger(Integer.toString(2));
            //System.out.println("A = " + A);

            Z = A.modPow(M, P);
            //System.out.println("Z = " + Z);

            if (Z.intValue() == 1 || Z.intValue() == p - 1) continue;
            for (int j = 0; j < b; j++) {
                if (j > 0 && Z.equals(1)) return false;
                if (j < b && Z.intValue() < p - 1) {
                    Z = Z.modPow(TWO, P);
                    continue;
                }
                if (Z.intValue() == p - 1) continue attemptsLoop;
            }
            if (Z.intValue() != p - 1) return false;
        }

        //System.out.println("TRUE");
        return true;
    }

    private LinkedList<Integer> generateSmallPrimeNumbers(int RightLimit) {
        LinkedList<Integer> arr = new LinkedList();
        arr.add(2);
        boolean isprime;
        for (int i = 3; i < RightLimit; i += 2) {
            isprime = true;
            for (int j = 3; j <= i / j; j += 2) {
                if (i % j == 0) isprime = false;
            }
            if (isprime) arr.add(i);
        }
        return arr;
    }

}

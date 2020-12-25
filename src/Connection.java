public class Connection {

    private int q, n;
    private Server server;
    private Client client;
    private randomNumber randomNumber = new randomNumber();

    Connection(Server server, Client client){
        this.server = server;
        this.client = client;

        distributeN();
    }

    void distributeN(){
        int q;
        do{
            q = randomNumber.getPrimeNumber(1000);
            n = 2 * q + 1;
        }while (!randomNumber.RabinMillerTest(n));

        server.setN(n);
        client.setN(n);
        System.out.println("N = " + n);
    }

    void registerClientOnServer(){
        client.registerOnServer();
        server.setI(client.getI());
        server.setSalt(client.getSalt());
        server.setV(client.getV());
    }

    boolean authenticationPhase1(){
        if (!server.checkI(client.getI())) System.out.println("No such user");
        server.setA(client.getA());
        if (!server.checkA()) System.out.println("A = 0;");

        server.findB();
        client.setB(server.getB());
        client.setSalt(server.getSalt());
        if (!client.checkB()) System.out.println("B = 0;");

        server.calculateU();
        client.calculateU();
        if (!(server.checkU() && client.checkU())){
            System.out.println("u = 0;");
            return false;
        }

        client.generateKey();
        server.generateKey();
        System.out.println();

        return true;
    }

    boolean authenticationPhase2(){
        client.calculateM();
        if(!server.compareM(client.getM())) {
            System.out.println("M-s are different");
            return false;
        }
        System.out.println();

        server.calculateR();
        if(!client.compareR(server.getR())) {
            System.out.println("R-s are different");
            return false;
        }

        return true;
    }

}

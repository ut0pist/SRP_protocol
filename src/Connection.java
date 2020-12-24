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
        int q = randomNumber.getPrimeNumber(100);
        n = 2 * q + 1;
        server.setN(n);
        client.setN(n);
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

        return true;
    }

    boolean authenticationPhase2(){
        return true;
    }

}

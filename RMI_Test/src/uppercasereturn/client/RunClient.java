package uppercasereturn.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class RunClient {
    public static void main(String[] args) throws RemoteException, NotBoundException {

        RMIClient rmiClient = new RMIClient();
        rmiClient.startClient();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Input: ");
            String line = scanner.nextLine();

            if(line.equalsIgnoreCase("exit")) break;

            String result = rmiClient.toUpperCase(line);
            System.out.println("Result: " + result);
        }

    }
}

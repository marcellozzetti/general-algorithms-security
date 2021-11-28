import control.SimplifiedDESController;

import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {

        DataInputStream inp = new DataInputStream(System.in);

        System.out.print("Enter the Number of Operations: ");
        int operations = Integer.parseInt(inp.readLine());
        int key, message;
        String operation;
        SimplifiedDESController controllerSDES;

        while (operations > 0){

            System.out.print("Enter the Operation Type ('D' - Decrypt | 'C' - Crypt): ");
            operation = inp.readLine();

            System.out.print("Enter the 10 Bit Key: ");

            key = Integer.parseInt(inp.readLine(),2);

            System.out.print("Enter the 8 Bit message: ");

            message = Integer.parseInt(inp.readLine(),2);

            switch (operation) {
                case "C":

                    controllerSDES = new SimplifiedDESController(key);

                    message = controllerSDES.encrypt(message);

                    System.out.print("\nEncrypted Message: ");

                    controllerSDES.printData(message, 8);
                    break;

                case "D":

                    controllerSDES = new SimplifiedDESController(key);

                    message = controllerSDES.decrypt(message);

                    System.out.print("\nDecrypted Message: ");

                    controllerSDES.printData(message, 8);
                    break;

                default:
                    System.out.print("\nWrong Option!");
                    break;

            }
            System.out.println("\n");
            operations--;

        }
    }
}

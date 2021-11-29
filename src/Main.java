import control.SimplifiedDESController;
import model.InputDTO;

import java.io.*;
import java.util.ArrayList;

public class Main {

    public static DataInputStream inp = new DataInputStream(System.in);
    public static ArrayList<InputDTO> array = new ArrayList<InputDTO>();
    public static SimplifiedDESController controllerSDES;

    public static void main(String[] args) throws IOException {

        mainProcessor();

    }

    private static void mainProcessor() throws IOException {

        System.out.print("Enter the Number of Operations: ");
        int operations = Integer.parseInt(inp.readLine());

        if(operations == -1 ){
            System.out.println("End");

        } else if(operations > 0 && operations <= 100){
            while (operations > 0){

                readInputData();
                operations--;

            }

            sdesProcessor();

        } else {
            System.out.println("Wrong Number of Operations! Try Again ....");
            mainProcessor();
        }
    }

    private static void sdesProcessor() {
        if(array != null){

            for (InputDTO inputDTO : array) {
                switch (inputDTO.getOperation()) {
                    case "C":
                        controllerSDES = new SimplifiedDESController(inputDTO.getKey());
                        controllerSDES.printData(controllerSDES.encrypt(inputDTO.getMessage()), 8);
                        break;

                    case "D":
                        controllerSDES = new SimplifiedDESController(inputDTO.getKey());
                        controllerSDES.printData(controllerSDES.decrypt(inputDTO.getMessage()), 8);
                        break;

                    default:
                        System.out.println("\nWrong Option!");
                        break;

                }
                System.out.println();
            }
        }
    }

    private static void readInputData() throws IOException{
        int key, message;
        String operation;

        System.out.print("Enter the Operation Type ('D' - Decrypt | 'C' - Crypt): ");
        operation = inp.readLine();

        System.out.print("Enter the 10 Bit Key: ");

        key = Integer.parseInt(inp.readLine(),2);

        System.out.print("Enter the 8 Bit message: ");

        message = Integer.parseInt(inp.readLine(),2);

        InputDTO input = new InputDTO();
        input.setOperation(operation);
        input.setKey(key);
        input.setMessage(message);

        array.add(input);
    }
}

package model.sdes;

/**
 * Created by marcello.ozzetti on 29/11/21.
 */
public class InputSDESDTO {

    String operation;
    int key;
    int message;

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public String getOperation() {
        return operation;
    }

    public int getKey() {
        return key;
    }

    public int getMessage() {
        return message;
    }
}

package model.rc4;

/**
 * Created by marcello.ozzetti on 12/12/21.
 */
public class InputRC4DTO {

    String message;
    String key;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public String getKey() {
        return key;
    }
}

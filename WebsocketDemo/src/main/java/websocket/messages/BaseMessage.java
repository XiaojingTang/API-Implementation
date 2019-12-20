package websocket.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BaseMessage {
    private final static Gson gson = new GsonBuilder().create();
    protected String type;

    public BaseMessage(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return gson.toJson(this, this.getClass());
    }

}
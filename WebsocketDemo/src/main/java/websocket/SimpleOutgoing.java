package websocket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SimpleOutgoing implements EngineOutgoing {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private String name;

    public SimpleOutgoing(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void initialization(InitializationMessage initializationMessage) {
        System.out.println(name + ":: " + "InitializationMessage:  " + gson.toJson(initializationMessage));
    }

    @Override
    public void updateUserPresence(String user, boolean joined) {
        if (joined) {
            System.out.println(name + ":: " + "User " + user + " logged in!");
        } else {
            System.out.println(name + ":: " + "User " + user + " logged out!");
        }
    }

    @Override
    public void outgoingAcceptanceConfirmation(String originator, String rfqId) {
        System.out.println(name + ":: " + "RFQ from " + originator + ", rfqId: " + rfqId + ", status changed to Acceptance_Acknowledged.");
    }

    @Override
    public void outgoingSendMessage(String sender, String recipient, String message) {
        System.out.println(name + ":: " + "Message from " + sender + " to " + recipient + ": " + message);
    }
}

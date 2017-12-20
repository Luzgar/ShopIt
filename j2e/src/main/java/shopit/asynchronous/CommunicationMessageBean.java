package shopit.asynchronous;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONObject;
import shopit.DeviceCommunication;
import shopit.utils.MessageDevice;
import shopit.utils.NotificationType;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.json.Json;
import javax.xml.soap.Text;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by Charly on 07/06/2017.
 */

@MessageDriven
public class CommunicationMessageBean implements MessageListener
{
    @Resource private MessageDrivenContext mdctx;
    @EJB private DeviceCommunication deviceCommunication;

    private static final Logger log = Logger.getLogger(CommunicationMessageBean.class.getName());

    private static final String SERVER_KEY = "AAAAT7yAXl0:APA91bGZt7INVO6n9NVv2-W15H5vYnDzeE0x1Nm48NhzYqOVteD9F68sSV9X18v0F0ljlaMQhEBG94e-ybg7Kxhfgyjv-4yJv2K8P_FjdOsqgXABWuzc91axQtTU69QKTH0NKma8Epzb";

    @Override
    public void onMessage(Message message)
    {
        TextMessage messageText = null;
        JSONObject jsonObject = null;

        try {
            messageText = (TextMessage) message;
            jsonObject = new JSONObject(messageText.getText());
        } catch (Exception e) {
            e.printStackTrace();
            mdctx.setRollbackOnly();
        }

        MessageDevice messageDevice = new MessageDevice();
        messageDevice.setTokenDest(jsonObject.getString("token"));
        messageDevice.setListName(jsonObject.getString("list_name"));
        messageDevice.setMessageTitle(jsonObject.getString("message_title"));
        messageDevice.setMessageBody(jsonObject.getString("message_body"));
        messageDevice.setMessageType(NotificationType.valueOf(jsonObject.getString("message_type")));

        try{
            sendFCM(messageDevice);
        }
        catch(Exception e) { log.info(e.getMessage());}
    }

    private void sendFCM(MessageDevice messageDevice) throws Exception
    {
        URL url = new URL("https://fcm.googleapis.com/fcm/send");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "key=" + SERVER_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        String pushMessage = "{\"to\":\"" + messageDevice.getTokenDest() + "\"," +
                "\"data\":{\"title\":\"" + messageDevice.getMessageTitle() + "\"," +
                "\"message\":\"" + messageDevice.getMessageBody() + "\"," +
                "\"type\":\"" + messageDevice.getMessageType().getName() + "\"," +
                "\"list_id\":\"" + messageDevice.getListId() + "\"," +
                "\"list_title\":\"" + messageDevice.getListName() + "\"}}";

        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(pushMessage.getBytes());

        // Response code : 200 = success, 400 = bad JSON, 401 = server_key wrong, 5xx = internal FCM error
        if(conn.getResponseCode() != 200)
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
    }
}

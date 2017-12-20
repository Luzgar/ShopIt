package shopit.components;

import org.json.JSONObject;
import shopit.DeviceCommunication;
import shopit.TokenManaging;
import shopit.entities.User;
import shopit.utils.MessageDevice;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

/**
 * Created by Charly on 31/05/2017.
 */

@Stateless
public class MessagesPusherBean implements TokenManaging, DeviceCommunication
{
    @PersistenceContext private EntityManager manager;

    @Resource private ConnectionFactory connectionFactory;
    @Resource(name = "CommunicationMessageBean") private Queue queue;

    @Override
    public void setUserDeviceToken(User user, String token)
    {
        User u = manager.merge(user);
        u.setToken(token);
    }

    @Override
    public void sendPushMessage(MessageDevice message)
    {
        if(message.getTokenDest() == null) // No token -> do nothing
            return;

        try{
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer comm = session.createProducer(queue);
            comm.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("list_name", message.getListName());
            jsonObject.put("list_id", message.getListId());
            jsonObject.put("message_title", message.getMessageTitle());
            jsonObject.put("message_body", message.getMessageBody());
            jsonObject.put("message_type", message.getMessageType());
            jsonObject.put("token", message.getTokenDest());

            comm.send(session.createTextMessage(jsonObject.toString()));

            comm.close();
            session.close();
            connection.close();
        }
        catch(JMSException e)
        {
            e.printStackTrace();
        }
    }
}

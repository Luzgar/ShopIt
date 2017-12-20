package shopit;

import shopit.utils.MessageDevice;

import javax.ejb.Local;

/**
 * Created by Charly on 05/06/2017.
 */

@Local
public interface DeviceCommunication
{
    void sendPushMessage(MessageDevice message);
}

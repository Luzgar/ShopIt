package shopit.utils;

import java.io.Serializable;

/**
 * Created by Charly on 07/06/2017.
 */
public class MessageDevice implements Serializable
{
    private String tokenDest;
    private String listName;
    private String messageTitle;
    private String messageBody;

    private int listId;

    private NotificationType messageType;

    public MessageDevice()
    {
        tokenDest = null;
        listName = null;
        messageTitle = null;
        messageBody = null;
        messageType = null;
        listId = 0;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getTokenDest() {
        return tokenDest;
    }

    public void setTokenDest(String tokenDest) {
        this.tokenDest = tokenDest;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public NotificationType getMessageType() {
        return messageType;
    }

    public void setMessageType(NotificationType messageType) {
        this.messageType = messageType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageDevice that = (MessageDevice) o;

        if (tokenDest != null ? !tokenDest.equals(that.tokenDest) : that.tokenDest != null) return false;
        if (listName != null ? !listName.equals(that.listName) : that.listName != null) return false;
        if (messageTitle != null ? !messageTitle.equals(that.messageTitle) : that.messageTitle != null) return false;
        if (messageBody != null ? !messageBody.equals(that.messageBody) : that.messageBody != null) return false;
        return messageType == that.messageType;
    }

    @Override
    public int hashCode() {
        int result = tokenDest != null ? tokenDest.hashCode() : 0;
        result = 31 * result + (listName != null ? listName.hashCode() : 0);
        result = 31 * result + (messageTitle != null ? messageTitle.hashCode() : 0);
        result = 31 * result + (messageBody != null ? messageBody.hashCode() : 0);
        result = 31 * result + (messageType != null ? messageType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MessageDevice{" +
                "tokenDest='" + tokenDest + '\'' +
                ", listName='" + listName + '\'' +
                ", messageTitle='" + messageTitle + '\'' +
                ", messageBody='" + messageBody + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}

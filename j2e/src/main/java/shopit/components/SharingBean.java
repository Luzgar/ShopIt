package shopit.components;

import shopit.ContributorOperation;
import shopit.DeviceCommunication;
import shopit.MedalOperation;
import shopit.entities.ShopList;
import shopit.entities.ShopListElement;
import shopit.entities.User;
import shopit.exceptions.ContributorAlreadyExistingException;
import shopit.utils.MessageDevice;
import shopit.utils.NotificationType;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by thomas on 30/05/2017.
 */
@Stateless
public class SharingBean implements ContributorOperation{

    @PersistenceContext private EntityManager manager;
    @EJB private DeviceCommunication pusher;

    @EJB private MedalOperation medalOperation;

    @Override
    public void addContributorToShopList(ShopList shopList, User contributor) throws ContributorAlreadyExistingException
    {
        shopList = manager.merge(shopList);
        contributor = manager.merge(contributor);

        if(contributor.equals(shopList.getOwner())){
            throw new ContributorAlreadyExistingException("Owner is already a contributor !");
        }
        if(shopList.getContributors().contains(contributor)){
            throw new ContributorAlreadyExistingException("Contributor already exists for this list");
        }
        shopList.getContributors().add(contributor);
        if(!shopList.isShared()){
            shopList.setShared(true);
            medalOperation.progressionSharingShopListMedal(shopList.getOwner());
        }

        MessageDevice message = new MessageDevice();
        message.setListName(shopList.getName());
        message.setMessageTitle("Invitation");
        message.setMessageBody("Vous avez ete ajoute a la liste " + shopList.getName());
        message.setMessageType(NotificationType.INVITATION);
        message.setTokenDest(contributor.getToken());
        message.setListId(shopList.getId());

        pusher.sendPushMessage(message);
    }

    @Override
    public void removeContributorFromShopList(ShopList shopList, User contributor)
    {
        shopList = manager.merge(shopList);
        contributor = manager.merge(contributor);

        if(shopList.getContributors().contains(contributor)){
            for(ShopListElement elt : shopList.getElements()){
                if(elt.getUserInCharge().equals(contributor)){
                    elt.setUserInCharge(null);
                }
            }
            shopList.getContributors().remove(contributor);
        }

        MessageDevice message = new MessageDevice();
        message.setListName(shopList.getName());
        message.setMessageTitle("Expulsion");
        message.setMessageBody("Vous avez ete expulse de la liste " + shopList.getName());
        message.setMessageType(NotificationType.EXPULSION);
        message.setTokenDest(contributor.getToken());
        message.setListId(shopList.getId());

        pusher.sendPushMessage(message);
    }
}

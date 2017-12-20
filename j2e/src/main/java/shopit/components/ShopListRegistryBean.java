package shopit.components;

import shopit.*;
import shopit.entities.ShopList;
import shopit.entities.ShopListElement;
import shopit.entities.User;
import shopit.exceptions.NoItemFoundException;
import shopit.utils.MessageDevice;
import shopit.utils.NotificationType;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Created by thomas on 29/05/2017.
 */
@Stateless
public class ShopListRegistryBean implements ShopListFinder, ShopListRegistration, ShopListOperation
{
    @PersistenceContext private EntityManager manager;
    @EJB private DeviceCommunication pusher;
    @EJB private MedalOperation medalOperation;

    @Override
    public List<ShopList> getAllShopList(User user, boolean archive)
    {
        User u = manager.merge(user);

        // ShopList that the user own
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<ShopList> criteria = builder.createQuery(ShopList.class);
        Root<ShopList> root =  criteria.from(ShopList.class);
        criteria.select(root).where(
                builder.equal(root.get("owner"), u),
                builder.equal(root.get("isArchive"), archive)
        );
        TypedQuery<ShopList> query = manager.createQuery(criteria);

        // ShopList to wich the user contributes
        CriteriaBuilder builder2 = manager.getCriteriaBuilder();
        CriteriaQuery<ShopList> criteria2 = builder2.createQuery(ShopList.class);
        Root<ShopList> root2 =  criteria2.from(ShopList.class);
        criteria2.select(root2).where(
                builder2.isMember(u,root2.get("contributors")),
                builder2.equal(root2.get("isArchive"), archive)
        );
        TypedQuery<ShopList> query2 = manager.createQuery(criteria2);

        try{
            // ShopList that the user owned
            List<ShopList> list1 = query.getResultList();
            // ShopList to wich the user contributes
            List<ShopList> list2 = query2.getResultList();
            List<ShopList> result = new ArrayList<>(list1);
            result.addAll(list2);
            return result;
        }
        catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public HashMap<Integer, String> getAllTitleShopList(User user, boolean archive)
    {
        HashMap<Integer, String> result = new HashMap<>();
        List<ShopList> list = getAllShopList(user, archive);

        for(ShopList sl : list){
            result.put(sl.getId(),sl.getName());
        }

        return result;
    }

    @Override
    public ShopList getShopListById(int id){
        return manager.find(ShopList.class, id);
    }

    @Override
    public int createShopList(User owner, String name, List<ShopListElement> elements, Date dateCreation)
    {
        User o = manager.merge(owner);

        if(elements == null){
            elements = new ArrayList<ShopListElement>();
        }

        Random r = new Random();

        for(ShopListElement el : elements)
            el.setPrice(2.0 + (10.0 - 2.0) * r.nextDouble());

        ShopList shopList = new ShopList(name, o, dateCreation);
        shopList.getElements().addAll(elements);
        manager.persist(shopList);

        return shopList.getId();
    }

    @Override
    public void removeShopList(int id) {

        ShopList shopList = getShopListById(id);

        medalOperation.progressionCategoriesMedalsForShopList(shopList);

        manager.remove(shopList);
    }

    @Override
    public void addItemToShopList(int id, ShopListElement itemToAdd) {

        ShopList shopList = getShopListById(id);

        Random r = new Random();
        itemToAdd.setPrice(2.0 + (10.0 - 2.0) * r.nextDouble());

        shopList.getElements().add(itemToAdd);

        messageToMembers(shopList, "Modification liste",
                "Un element a ete ajoute a la liste " + shopList.getName());
    }

    @Override
    public void removeItemFromShopList(int id, String name) throws NoItemFoundException{

        ShopList shopList = getShopListById(id);

        int position = getItemPosition(shopList,name);
        if(position < 0){
            throw new NoItemFoundException("Item not found in the list");
        }

        shopList.getElements().remove(position);



        messageToMembers(shopList, "Modification liste",
                "Un element a ete retire de la liste " + shopList.getName());
    }

    @Override
    public void setItemQuantityFromShopList(int id, String name, int newQuantity) throws NoItemFoundException{

        ShopList shopList = getShopListById(id);

        int position = getItemPosition(shopList,name);
        if(position<0) {
            throw new NoItemFoundException("Item not found in the list");
        }

        shopList.getElements().get(position).setNumber(newQuantity);

        messageToMembers(shopList, "Modification liste",
                "Un element a ete ajoute a la liste " + shopList.getName());
    }

    @Override
    public void setItemTaken(int id, String name, boolean isTaken) throws NoItemFoundException{

        ShopList shopList = getShopListById(id);

        int position = getItemPosition(shopList,name);
        if(position < 0){
            throw new NoItemFoundException("Item not found in the list");
        }

        shopList.getElements().get(position).setTaken(isTaken);
        if(isTaken == true)
            shopList.getElements().get(position).setNotFound(false);

        messageToMembers(shopList, "Modification liste",
                "Un element de la liste " + shopList.getName() + " a ete pris");
    }

    @Override
    public void archiveShopList(int id) {

        ShopList shopList = getShopListById(id);

        medalOperation.progressionCategoriesMedalsForShopList(shopList);

        shopList.setArchive(true);
    }

    @Override
    public void setItemNotFound(int id, String name) throws NoItemFoundException
    {
        ShopList shopList = getShopListById(id);

        int position = getItemPosition(shopList,name);
        if(position < 0){
            throw new NoItemFoundException("Item not found in the list");
        }

        shopList.getElements().get(position).setNotFound(true);

        messageToMembers(shopList, "Modification liste",
                "Un element de la liste " + shopList.getName() + " est non trouve");
    }

    @Override
    public int getItemPosition(ShopList shopList, String name){
        int i=0;
        for(ShopListElement elt : shopList.getElements()){
            if(elt.getItem().equals(name)){
                return i;
            }
            i++;
        }
        return -1;
    }

    private void messageToMembers(ShopList shopList, String title, String body)
    {
        /** Message to owner **/
        MessageDevice message = new MessageDevice();
        message.setListName(shopList.getName());
        message.setMessageTitle(title);
        message.setMessageBody(body);
        message.setMessageType(NotificationType.LIST_MODIFICATION);
        message.setTokenDest(shopList.getOwner().getToken());
        message.setListId(shopList.getId());
        pusher.sendPushMessage(message);

        /** Message to contributors **/
        for(User u : shopList.getContributors())
        {
            MessageDevice temp = new MessageDevice();
            temp.setListName(shopList.getName());
            temp.setMessageTitle(title);
            temp.setMessageBody(body);
            temp.setMessageType(NotificationType.LIST_MODIFICATION);
            temp.setTokenDest(u.getToken());
            message.setListId(shopList.getId());

            pusher.sendPushMessage(temp);
        }
    }
}

package shopit.components;

import shopit.CriterionBuilder;
import shopit.DeviceCommunication;
import shopit.RepartitionProcessing;
import shopit.ShopListFinder;
import shopit.entities.ShopList;
import shopit.entities.ShopListElement;
import shopit.entities.User;
import shopit.exceptions.NoItemFoundException;
import shopit.exceptions.UserIsNotAContributorException;
import shopit.utils.MessageDevice;
import shopit.utils.NotificationType;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Created by thomas on 30/05/2017.
 */
@Stateless
public class RepartitionBean implements CriterionBuilder, RepartitionProcessing{

    @PersistenceContext private EntityManager manager;
    @EJB private DeviceCommunication pusher;

    @EJB
    ShopListFinder finder;

    @Override
    public void setUserInCharge(ShopList shopList, User contributor, String item) throws NoItemFoundException, UserIsNotAContributorException {

        shopList = manager.merge(shopList);
        if(contributor != null){
            contributor = manager.merge(contributor);
        }

        int position = finder.getItemPosition(shopList,item);
        if(position < 0){
            throw new NoItemFoundException("Item not found in the list");
        }
        if(contributor!=null && !contributor.equals(shopList.getOwner()) && !shopList.getContributors().contains(contributor)){
            throw new UserIsNotAContributorException("The User is not a contributor for this ShopList");
        }

        shopList.getElements().get(position).setUserInCharge(contributor);

        if(contributor != null)
            messageToMembers(shopList, "Repartition", contributor.getPseudo() + " veut acheter " + item);
        else
            messageToMembers(shopList, "Repartition", "responsabilite sur " + item + " retiree");
    }

    /**
     * Initialize the complete users ist with the owner and contributor.
     * @param shopList
     * @return
     */
    private List<User> initializeUserList(ShopList shopList){
        List<User> users = new ArrayList<User>();
        users.add(shopList.getOwner());
        users.addAll(shopList.getContributors());
        return users;
    }

    /**
     * Initialize the category in charge dictionary with the already taken items
     * @param shopList
     * @param categories
     * @param map_repartition_catergory
     */
    private void initializeCategoryInCharge(ShopList shopList, List<String> categories, Map<String,User> map_repartition_catergory){
        for (ShopListElement elt : shopList.getElements()) {
            if (elt.getCategory() == null) {
                elt.setCategory("");
            }
            if (!categories.contains(elt.getCategory()) && !map_repartition_catergory.containsKey(elt.getCategory())) {
                categories.add(elt.getCategory());
            }
            if(elt.getUserInCharge()!=null && !map_repartition_catergory.containsKey(elt.getCategory())){
                map_repartition_catergory.put(elt.getCategory(),elt.getUserInCharge());
                categories.remove(categories.indexOf(elt.getCategory()));
            }
        }
    }

    /**
     * Found the categories for each user to do a repartition per category.
     * @param map_repartition_catergory
     * @param number_category_per_user
     * @param users
     * @param categories
     */
    private void foundCategoryInCharge(Map<String, User> map_repartition_catergory, Integer[] number_category_per_user,
                                       List<User> users, List<String> categories){
        // Define the number of category per user
        Iterator<Map.Entry<String,User>> it = map_repartition_catergory.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,User> pair = it.next();
            number_category_per_user[users.indexOf(pair.getValue())]+=1;
        }
        // Found the min
        int min=number_category_per_user[0];
        for(int i=0; i<number_category_per_user.length; i++){
            if(min>number_category_per_user[i]){
                min=number_category_per_user[i];
            }
        }
        // Assign category to user
        while(!categories.isEmpty()){
            String current = categories.get(0);
            boolean found=false;
            while(!found){
                for(int i=0; i<number_category_per_user.length && !found; i++){
                    if(min==number_category_per_user[i]){
                        map_repartition_catergory.put(current,users.get(i));
                        number_category_per_user[i]+=1;
                        found=true;
                        break;
                    }
                }
                if(!found){
                    min++;
                }
            }
            categories.remove(0);
        }
    }

    /**
     * Set the userInCharge for each elements of the ShopList with a category repartition.
     * Version 1 : First Repartition, could make it fast ?
     * @param shopList
     */
    @Override
    public void doCategoryRepartition(ShopList shopList){

        shopList = manager.merge(shopList);

        List<String> categories = new ArrayList<String>();
        Map<String,User> map_repartition_catergory = new HashMap<String, User>();
        Integer[] number_category_per_user = new Integer[shopList.getContributors().size() + 1];
        // Take already category in charge
        initializeCategoryInCharge(shopList,categories,map_repartition_catergory);

        // Initialize the array for number of category per user
        for(int i=0; i<number_category_per_user.length; i++){
            number_category_per_user[i]=0;
        }
        // Initialize the user list
        List<User> users = initializeUserList(shopList);
        // Found the categories for each user
        foundCategoryInCharge(map_repartition_catergory,number_category_per_user,users,categories);
        // Do repartition
        for(ShopListElement elt : shopList.getElements()){
            elt.setUserInCharge(map_repartition_catergory.get(elt.getCategory()));
        }
        // End
        messageToMembers(shopList, "Repartition", "Repartiton categorie executee");
    }

    /**
     * Sort the ShopListElement of a ShopList by descending price.
     * @param shopList
     */
    private void sortShopListElementByPrice(ShopList shopList){
        shopList.getElements().sort(new Comparator<ShopListElement>() {
            @Override
            public int compare(ShopListElement o1, ShopListElement o2) {
                return Double.compare(o2.getPrice(),o1.getPrice());
            }
        });
    }

    /**
     * Set the userInCharge for each elements of the ShopList with a price repartition.
     * Version 1 :
     * @param shopList
     */
    @Override
    public void doPriceRepartition(ShopList shopList) {

        shopList = manager.merge(shopList);
        // Sorting the ShopListElement by price
        sortShopListElementByPrice(shopList);
        // Initialize the user list
        List<User> users = initializeUserList(shopList);
        // Array to keep the sum of each user
        float[] sum_array = new float[users.size()];
        for(int i=0; i<sum_array.length; i++){
            sum_array[i] = 0;
        }
        // Run through the ShopListElement
        int loopCpt = 0, index_user = 0, index_max = users.size()-1;
        int i=0;
        while(i<shopList.getElements().size()) {
            ShopListElement elt = shopList.getElements().get(i);
            // First loop, just add the most expensive items
            if(loopCpt == 0){
                elt.setUserInCharge(users.get(index_user));
                sum_array[index_user] += elt.getPrice();
                // Increment the user index
                index_user++;
                i++;
            }
            // Other loop, add items until the total value is greater or equal to the next total value.
            // Reverse
            else if(loopCpt%2 == 1){
                if(sum_array[index_max-index_user] <= sum_array[index_max-index_user-1]){
                    elt.setUserInCharge(users.get(index_max-index_user));
                    sum_array[index_max-index_user] += elt.getPrice();
                    i++;
                }else{
                    index_user++;
                }
            }
            // normal
            else{
                if(sum_array[index_user] <= sum_array[index_user+1]){
                    elt.setUserInCharge(users.get(index_user));
                    sum_array[index_user] += elt.getPrice();
                    i++;
                }else{
                    index_user++;
                }
            }
            // Check if we increment the loopCpt
            if(index_user == index_max){
                index_user=0;
                loopCpt++;
            }
        }
        // End

        messageToMembers(shopList, "Repartition", "Repartiton prix executee");
    }

    private void messageToMembers(ShopList shopList, String title, String body)
    {
        /** Message to owner **/
        MessageDevice message = new MessageDevice();
        message.setListName(shopList.getName());
        message.setMessageTitle(title);
        message.setMessageBody(body);
        message.setMessageType(NotificationType.REPARTITION);
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
            temp.setMessageType(NotificationType.REPARTITION);
            temp.setTokenDest(u.getToken());
            message.setListId(shopList.getId());

            pusher.sendPushMessage(temp);
        }
    }
}

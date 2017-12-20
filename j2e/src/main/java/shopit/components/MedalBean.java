package shopit.components;

import shopit.MedalFinder;
import shopit.MedalOperation;
import shopit.UserFinder;
import shopit.entities.Medal;
import shopit.entities.ShopList;
import shopit.entities.ShopListElement;
import shopit.entities.User;
import shopit.exceptions.InvalidMedalException;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Created by thomas on 12/06/2017.
 */
@Stateless
public class MedalBean implements MedalOperation, MedalFinder {

    @PersistenceContext
    private EntityManager manager;
    @EJB
    private UserFinder userFinder;

    @Override
    public void setUserMedal(User user, Medal medal) throws InvalidMedalException {

        user = manager.merge(user);
        if(medal==null){
            throw new InvalidMedalException("The medal is null");
        }
        if(!medal.equals(Medal.NOMEDAL) && !user.getUnlocked().contains(medal)){
            throw new InvalidMedalException("The medal is locked");
        }
        user.setExposed(medal);
    }

    @Override
    public void checkMedalProgression(User user) {

        user = manager.merge(user);
        List<Medal> medals = new ArrayList<>();
        medals.add(Medal.NOMEDAL);

        Iterator<Map.Entry<Medal,Integer>> it = user.getInProgress().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Medal,Integer> pair = it.next();
            if(pair.getValue()>=pair.getKey().getNbRequired()){
                medals.add(pair.getKey());
            }
        }

        user.setUnlocked(medals);
        user.setInProgress(new HashMap<Medal, Integer>());
    }

    /**
     * Add the number in parameter to the progression of the medal for the user.
     * @param user
     * @param medal
     * @param n
     */
    private void progressionMedal(User user, Medal medal, int n){

        user = manager.merge(user);
        if(user.getInProgress().containsKey(medal)) {
            user.getInProgress().replace(medal, user.getInProgress().get(medal) + n);
        }
        else{
            user.getInProgress().put(medal,n);
        }
    }

    @Override
    public void progressionAddRecipeMedal(User user) {
        progressionMedal(user,Medal.COOK,1);
    }

    @Override
    public void progressionSharingShopListMedal(User user) {
        progressionMedal(user,Medal.LEADER,1);
    }

    @Override
    public void progressionShopRecipeMedal(User user) {
        progressionMedal(user,Medal.PHOTOGRAPHER,1);
    }

    @Override
    public void progressionCategoriesMedalsForShopList(ShopList shopList) {
        User user = null;
        shopList = manager.merge(shopList);
        if(shopList==null || shopList.getElements()==null){
            return;
        }
        for(ShopListElement elt : shopList.getElements()){
            if(elt.isTaken() && (user = elt.getUserInCharge())!=null){
                try {
                    progressionMedal(user, Medal.getMedal(elt.getCategory()), elt.getNumber());
                }
                catch(Exception e){
                    ; // The medal is not found
                }
            }
        }
    }

    @Override
    public Medal getUserMedal(User user) {

        user = manager.merge(user);
        return user.getExposed();
    }

    @Override
    public List<String> getUserMedalsUnlocked(User user) {

        user = manager.merge(user);
        List<String> medals = new ArrayList<>();
        for(Medal m : user.getUnlocked()){
            medals.add(m.getName());
        }
        return medals;
    }

    @Override
    public List<String> getMedalOfContributors(ShopList shopList) {

        shopList = manager.merge(shopList);
        List<String> medals = new ArrayList<>();
        medals.add(shopList.getOwner().getExposed().getName());
        for(User user : shopList.getContributors()){
            medals.add(user.getExposed().getName());
        }
        return medals;
    }

    @Override
    public void calculateAllUsersRewards() {
        System.out.println("--- Calcul des médailles ! ---");
        List<User> users = userFinder.getAllUsers();
        for(User user : users){
            checkMedalProgression(user);
        }
    }

    //TODO : Cheat for demo
    @Override
    public void addMedalUser(User user, String name) {

        user = manager.merge(user);
        Medal medal = Medal.getMedal(name);
        if(medal!=null){
            if(!user.getUnlocked().contains(medal)){
                user.getUnlocked().add(medal);
            }
        }
    }
}

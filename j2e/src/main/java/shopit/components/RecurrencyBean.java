package shopit.components;

import shopit.RecurrencyProcessing;
import shopit.entities.ShopList;
import shopit.entities.ShopListElement;
import shopit.utils.RecurrencyStrategy;

import javax.ejb.Stateless;
import java.util.*;

/**
 * Created by Charly on 13/06/2017.
 */

@Stateless
public class RecurrencyBean implements RecurrencyProcessing
{
    private static final int NB_SUGGESTED_ITEMS = 5;

    @Override
    public List<String> findRecurrentItems(List<ShopList> lists, RecurrencyStrategy strategy)
    {
        List<String> suggestedItems = new ArrayList<>();
        switch(strategy)
        {
            case HIGHEST_OCCURRENCE: processHighestOccurrence(lists, suggestedItems); break;
        }

        return suggestedItems;
    }

    private void processHighestOccurrence(List<ShopList> lists, List<String> output)
    {
        Map<String, Integer> occurences = new HashMap<>();

        for(ShopList list : lists)
        {
            for(ShopListElement item : list.getElements())
            {
                if(!occurences.containsKey(item.getItem()))
                    occurences.put(item.getItem(), 1);
                else
                    occurences.put(item.getItem(), occurences.get(item.getItem()) + 1);
            }
        }

        Map sortedMap = new TreeMap(new ValueComparator(occurences));
        sortedMap.putAll(occurences);

        Set keys = sortedMap.keySet();

        int cpt = 0;
        for (Iterator i = keys.iterator(); i.hasNext() && cpt < NB_SUGGESTED_ITEMS; cpt++)
        {
            String key = (String) i.next();
            output.add(key);
        }
    }

    private static class ValueComparator implements Comparator {
        private Map map;

        public ValueComparator(Map map) {
            this.map = map;
        }

        public int compare(Object keyA, Object keyB) {
            Comparable valueA = (Comparable) map.get(keyA);
            Comparable valueB = (Comparable) map.get(keyB);
            int out = valueB.compareTo(valueA);
            if(out != 0)
                return out;
            else return 1;
        }
    }
}

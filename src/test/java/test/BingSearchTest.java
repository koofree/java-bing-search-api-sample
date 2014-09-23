package test;

import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchWebQuery;
import net.billylieurance.azuresearch.AzureSearchWebResult;
import org.junit.Test;

import java.util.*;

/**
 * Created by Koo Lee on 2014-09-23.
 */
public class BingSearchTest {

    private static final String APP_ID = "zrRErqOYI1z2uABmfzmd+8oCgkYpbPwrWSmMrMHYCpA";

    private static final String[] ENTITY_DICTIONARY = {
            "Republic of Korea", "South Korea", "Park Geun-hye", "US Justice Department",
            "Korean", "Republic of South Africa", "South Africa", "Korean Drama",
            "Canada", "North Korea"
    };

    @Test
    public void test() {
        AzureSearchWebQuery aq = new AzureSearchWebQuery();
        aq.setAppid(APP_ID);
        aq.setQuery("Korean President");
        aq.setPerPage(15);
        aq.doQuery();

        AzureSearchResultSet<AzureSearchWebResult> results = aq.getQueryResult();
        TreeMap<String, Integer> resultMap = new TreeMap<>();

        for (AzureSearchWebResult aResult : results) {
            String desc = aResult.getDescription();

            for (String key : ENTITY_DICTIONARY) {
                int existCount = 0;
                if (resultMap.containsKey(key)) existCount = resultMap.get(key);
                resultMap.put(key, existCount + countString(desc, key));
            }
        }

        for (Map.Entry<String, Integer> result : entriesSortedByValues(resultMap)) {
            System.out.println(result.getKey() + ":" + result.getValue());
        }
    }


    private int countString(String desc, String key) {
        int count = 0;
        int idx = 0;

        while ((idx = desc.indexOf(key, idx)) != -1) {
            idx++;
            count++;
        }

        return count;
    }

    static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
                new Comparator<Map.Entry<K, V>>() {
                    @Override
                    public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                        int res = e2.getValue().compareTo(e1.getValue());
                        return res != 0 ? res : 1; // Special fix to preserve items with equal values
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }
}

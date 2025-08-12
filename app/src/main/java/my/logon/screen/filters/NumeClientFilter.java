package my.logon.screen.filters;

import java.util.ArrayList;
import java.util.List;

import my.logon.screen.beans.BeanClient;

public class NumeClientFilter {


    public List<BeanClient> getClientFilter(List<BeanClient> listClienti, String numeClient){

        List<BeanClient> filteredList = new ArrayList<>();

        for (BeanClient client : listClienti) {
            if (client.getNumeClient().toLowerCase().startsWith(numeClient.toLowerCase()))
                filteredList.add(client);
        }


        return filteredList;
    }


}

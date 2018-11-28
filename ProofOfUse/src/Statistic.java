import java.time.Duration;

import java.util.ArrayList;
import java.util.HashSet;
//Controller
public class Statistic {
    ArrayList<ClientData> client;
    HashSet<Module> modules = new HashSet<Module>();
    HashSet<String> address = new HashSet<>();
    Pairs[] pairUser,pairTime,pairTU,pairAdress; // pairUser - для функции SortingByUsers, pairTime - TimePerModule, pairTU - Time per Users

    Statistic(ArrayList<ClientData> client){
        this.client = client;
    }
    void NumberofModules(){
        for(int i = 0;i<client.size();i++){
            for(int j = 0; j < client.get(i).modules.size(); j++)
            modules.add(client.get(i).modules.get(j));//cоздаем хэшсет с уникальными модулями(имеется ввиду отбрасываем повторения)
        }
    }
    void SortingByUsers(){// для графика (кол-во человек(у)/ по каждому модулю (х)
        this.pairUser = new Pairs[modules.size()];
        for(int i=0;i<modules.size();i++){
            int count=0;
            for(int j=0;j<client.size();j++){
                for(int y=0;y<client.get(i).modules.size();y++) {
                    if (((Module) modules.toArray()[j]).module.equals(client.get(j).modules.get(y)))count++;
                }
            }
            pairUser[i] = new Pairs<String,Integer>(((Module)modules.toArray()[i]).module,count);
        }
    }
    void TimePerModule(){// время использования каждого модуля
        this.pairTime = new Pairs[modules.size()];
        for(int i=0;i<modules.size();i++){
            Duration time = null;
            for(int j=0;j<client.size();j++){
                for(int y=0;y<client.get(i).modules.size();y++) {
                    if (((Module) modules.toArray()[j]).module.equals(client.get(j).modules.get(y))){
                        time.plus(client.get(j).modules.get(y).moduleUsage);
                    }
                }
            }
            pairTime[i] = new Pairs<String,Duration>(((Module)modules.toArray()[i]).module,time);
        }
    }
    void TimePerUsers(){//функция предполагает вызов двух предыдущих
        for(int i=0;i<modules.size();i++){
            Duration avr = null;
            for(int j=0;j<modules.size();j++) {
                if (pairTime[i].equals(pairUser[j])) {
                    avr = pairTime[i].div((long) pairUser[i]._2);
                }
            }
          pairTU[i] = new Pairs<String,Duration>(((Module)modules.toArray()[i]).module,avr);
        }
    }
    void UsersPerCities() {
        for (int i = 0; i < client.size(); i++) {
            address.add(client.get(i).addr.city);//cоздаем хэшсет с уникальными городами(имеется ввиду отбрасываем повторения)
        }
        for (int i = 0;i < address.size();i++){
            int count = 0;
            for(int j = 0; j < client.size(); j++){
                if(address.toArray()[i].equals(client.get(j).addr.city))count++;
            }
            pairAdress[i] = new Pairs<String,Integer>((String)address.toArray()[i],count);//пара хранится в формате город/кол-во юзеров
            // для графика можно ипользовать только 3-4 наиболее популярных города
        }
    }
}


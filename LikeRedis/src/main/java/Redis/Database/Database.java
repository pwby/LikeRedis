package Redis.Database;

import java.util.*;

public class Database {
    private static Database database = new Database();
    private Database() {

    }
    /*
     * 使用单例模式，让对象只能被创建一次，保持一致性操作
     * */
    public static Database getDatabase() {
        return database;
    }
    public static Map<String, String> str = new HashMap<>();
    public static Map<String, HashMap<String, String>> map = new HashMap<>();
    public static Map<String, List<String>> list = new HashMap<>();
    public static Map<String, Set<String>> set = new HashMap<>();
    public static Map<String, LinkedList<String>> zset = new HashMap<>();

    public static HashMap<String, String> getMap(String key) {
        HashMap<String, String> RMap = map.get(key);
        if (RMap == null) {
            //必须创建一个列表，否则返回的是NPE，程序无法进行
            RMap = new HashMap<>();
            map.put(key, RMap);
            return RMap;
        }
        return RMap;

    }

    public static Set<String> getSet(String key) {
        Set<String> hashSet = set.get(key);
        if (hashSet == null) {
            hashSet = new HashSet<>();
            set.put(key, hashSet);
            return hashSet;
        }
        return hashSet;


    }

    public static List<String> getList(String key) {

        List<String> lists = list.computeIfAbsent(key, k -> {
            return new ArrayList<>();
        });

        return lists;
    }


}

package Redis.Command;

import Redis.Database.Database;
import Redis.Procotol.Protocolcode;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LRANGECommand implements Command {
    public List<Object> args;

    //redis 127.0.0.1:6379> LRANGE runoobkey 0 10
    //
    //1) "mysql"
    //2) "mongodb"
    //3) "redis"
    @Override
    public void run(OutputStream write) {
        //判断长度
        if (args.size() == 3) {
            String key = new String((byte[]) args.remove(0));
            List<String> dataBase = Database.getDatabase().getList(key);
            int start = Integer.parseInt(new String((byte[]) args.remove(0)));
            int end = Integer.parseInt(new String((byte[]) args.remove(0)));
            List<String> list=new ArrayList<>();
            try {
                if (start >= 0 && start <dataBase.size()) {
                    if (end < 0) {
                        end = dataBase.size() + end;
                        for(int i=end;i<dataBase.size();i++){
                            list.add(dataBase.get(i));
                        }
                        Protocolcode.writeArray(write, list);
                    } else {
                        if (end >= dataBase.size()) {
                            Protocolcode.writeArray(write, dataBase);
                        } else {
                            for(int i=start;i<end+1;i++){
                                list.add(dataBase.get(i));
                            }
                            Protocolcode.writeArray(write, list);
                        }
                    }
                } else {
                    for(int i=dataBase.size()+start;i<dataBase.size();i++){
                        list.add(dataBase.get(i));
                    }
                    Protocolcode.writeArray(write, dataBase.subList(dataBase.size() + start, dataBase.size()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Protocolcode.writeError(write, "Wrong Format");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setArgs(List<Object> args) {
        this.args = args;
    }
}

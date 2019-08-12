package Redis.Command;

import Redis.Database.Database;
import Redis.Database.Permanent;
import Redis.Procotol.Protocolcode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class LPUSHCommand implements Command {
    Database database = Database.getDatabase();
    List<Object> args;

    @Override
    public void run(OutputStream write) {
        //接收到的是列表，列表拆解
        //StringCode会解码，new String（byte[] b）
        if (args.size() == 2) {
            String key = new String((byte[]) args.get(0));
            String value = new String((byte[]) args.get(1));
            //  String value=(String) args.get(1);必须使用匿名String，不能强转
            List<String> dateList = database.getList(key);
            dateList.add(0, value);
            try {
                Protocolcode.writeInteger(write, dateList.size());
              Permanent.getPermanent().writetoListProfile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Protocolcode.writeError(write, "Wrong Format");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setArgs(List<Object> args) {
        this.args = args;
    }
}

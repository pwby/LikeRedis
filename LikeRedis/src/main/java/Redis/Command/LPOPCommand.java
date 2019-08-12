package Redis.Command;

import Redis.Database.Database;
import Redis.Database.Permanent;
import Redis.Procotol.Protocolcode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class LPOPCommand implements Command {
    Database database = Database.getDatabase();
    List<Object> args;

    @Override
    public void run(OutputStream write) {
        //接收到的是列表，列表拆解
        //StringCode会解码，new String（byte[] b）
        if (args.size() == 1) {
            String key = new String((byte[]) args.get(0));
            List<String> dateList = database.getList(key);
            try {
                String value=Database.getList(key).remove(0);
                Protocolcode.writeBulkString(write,value);
                Permanent.getPermanent().clearListProfile();
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

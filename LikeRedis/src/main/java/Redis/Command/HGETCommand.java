package Redis.Command;

import Redis.Database.Database;
import Redis.Procotol.Protocolcode;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class HGETCommand implements Command {
    List<Object> args;

    @Override
    public void run(OutputStream write) {
        if (args.size() == 2) {
            String key = new String((byte[]) args.remove(0));
            String field = new String((byte[]) args.remove(0));
            HashMap<String, String> map = Database.getDatabase().getMap(key);
            String value = map.get(field);
            try {
                Protocolcode.writeBulkString(write, value);
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

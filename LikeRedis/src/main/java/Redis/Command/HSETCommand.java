package Redis.Command;
import Redis.Database.Database;
import Redis.Database.Permanent;
import Redis.Procotol.Protocolcode;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class HSETCommand implements Command {
    List<Object> args;

    @Override
    public void run(OutputStream write) {
        if (args.size() == 3) {
            String key = new String((byte[]) args.remove(0));
            String field = new String((byte[]) args.remove(0));
            String value = new String((byte[]) args.remove(0));
            HashMap<String, String> map = Database.getDatabase().getMap(key);
            try {
                if (map.containsKey(field)) {
                    Protocolcode.writeInteger(write, 0);
                } else {
                    Protocolcode.writeInteger(write, 1);
                }
                map.put(field, value);
                Permanent.getPermanent().writetoMapProfile();
            } catch (Exception ex) {
                ex.printStackTrace();
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

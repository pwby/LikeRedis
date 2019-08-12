package Redis.Command;

import Redis.Database.Database;
import Redis.Procotol.Protocolcode;

import java.io.OutputStream;
import java.util.List;
import java.util.Set;

public class SADDCommand implements Command {
    public List<Object> args;

    @Override
    public void run(OutputStream write) {
        if (args.size() == 2) {
            String key = new String((byte[]) args.remove(0));
            String value = new String((byte[]) args.remove(0));
            Set<String> set = Database.getSet(key);
            try {
                if (set.contains(value)) {
                    //0代表将原有的数据更新
                    Protocolcode.writeInteger(write, 0);
                } else {
                    //1代表插入一条语句
                    set.add(value);
                    Protocolcode.writeInteger(write, 1);
                }
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

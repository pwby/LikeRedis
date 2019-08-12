package Redis.Command;

import java.io.OutputStream;
import java.util.List;

public class ExitCommand implements Command {
    public List<Object> args;
    @Override
    public void run(OutputStream write) {
        if (args.size() == 1 && ("exit").equals((String) args.get(0))) {
            System.exit(0);
        }
    }

    @Override
    public void setArgs(List<Object> args) {
        this.args = args;
    }
}

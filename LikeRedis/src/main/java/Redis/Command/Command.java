package Redis.Command;

import java.io.OutputStream;
import java.util.List;

public interface Command {

    void run(OutputStream write);
    void setArgs(List<Object> args);
}

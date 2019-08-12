package Redis.Exception;
public class ServerWrong extends RuntimeException {
    public ServerWrong(String str) {
        System.out.println(str);
    }
}

package Redis.LaunchRedis;

import Redis.Command.Command;
import Redis.Database.Permanent;
import Redis.Procotol.ProtocolDecode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * 服务器入口
 * */
public class RedisServer {
    public static void main(String[] args) {
        //从文件中加载资源
        Permanent.getPermanent().readFromListProfile();
        Permanent.getPermanent().readFromMapProfile();
        //使用线程池处理并发用户情况
        ExecutorService executorService = Executors.newFixedThreadPool(20000);
        ServerSocket serverSocket = null;
        try {
            //循环接收来自客户端的连接
            serverSocket = new ServerSocket(6379);
            while (true) {
                final Socket socket = serverSocket.accept();
                executorService.execute(()->{

                    try {
                        //持续提供业务服务
                        while (true) {
                            InputStream read = socket.getInputStream();
                            OutputStream write = socket.getOutputStream();

                            Command command = new ProtocolDecode(read, write).readCommand();
                            if(command!=null) {
                                command.run(write);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


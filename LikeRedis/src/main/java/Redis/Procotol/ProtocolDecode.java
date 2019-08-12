package Redis.Procotol;

import Redis.Command.Command;
import Redis.Exception.ServerWrong;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProtocolDecode {
    InputStream is;
    OutputStream write;

    public ProtocolDecode(InputStream is, OutputStream write) throws IOException {
        protocolInputStream = new protocolInputStream(is);
        this.is = is;
        this.write = write;
    }

    Redis.Procotol.protocolInputStream protocolInputStream;

    public Command readCommand() throws Exception {
        Object o = null;
        o = process();
        if (!(o instanceof List)) {
            Protocolcode.writeBulkString(write, "Server too tired,please wait .....");
            throw new ServerWrong("内部解析错误，服务器故障");
            //由于程序未处理成功，故这是程序故障，与用户无关，应该抛出异常
        }
        List<Object> list = (List<Object>) o;
        if (list.size() < 1) {
            Protocolcode.writeBulkString(write, "Server too tired,please wait .....");
            throw new ServerWrong("内部解析错误，服务器故障");

        }
        Object o2 = list.remove(0);
        if (!(o2 instanceof byte[])) {
            Protocolcode.writeBulkString(write, "Server too tired,please wait .....");
            throw new ServerWrong("内部解析错误，服务器故障");
        }

        String commandName = String.format("%sCommand", new String((byte[]) (o2)).trim().toUpperCase());

        Class<?> cls = null;
        Command command = null;

        try {
            cls = Class.forName("Redis.Command." + commandName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (cls==null ||!Command.class.isAssignableFrom(cls)) {
            Protocolcode.writeError(write, "Wrong Input,Please try again");
        } else {
            try {
                command = (Command) cls.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            command.setArgs(list);
        }
        //根据类找到对应对象
        return command;
    }


    public String processError() throws IOException {

        return protocolInputStream.readLine();
    }

    public String processSimpleString() throws IOException {
        return protocolInputStream.readLine();
    }

    public long processInteger() throws IOException {
        return protocolInputStream.readInteger();
    }

    //'$6\r\nfoobar\r\n' 或者 '$-1\r\n'
    public byte[] processBulkString() {
        int len = 0;
        byte[] bytes;
        String str = null;
        try {
            len = (int) protocolInputStream.readInteger();
            bytes = new byte[len];
            str = protocolInputStream.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.getBytes();
    }

    public List<byte[]> processArray() {
        int len = 0;
        List<byte[]> list = new ArrayList<byte[]>();
        try {
            len = (int) protocolInputStream.readInteger();

            //"*5\r\n
            // 5\r\nlpush\r\n$3\r\nkey\r\n$1\r\n1\r\n$1\r\n2\r\n$1\r\n3\r\n";
            //
            for (int i = 0; i < len; i++) {
                byte[] bytes = (byte[]) process();
                list.add(bytes);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Object process() throws IOException {

        int b = 0;
        try {
            b = is.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (b == -1) {
            throw new RuntimeException("程序无法继续..........");
        }
        switch ((char) b) {
            case '+':
                return processSimpleString();

            case '-':
                return processError();

            case ':':
                return processInteger();

            case '$':
                return processBulkString();

            case '*':
                return processArray();

            default:
                Protocolcode.writeError(write, "Unresolve this commond");

        }
        return null;

    }
}







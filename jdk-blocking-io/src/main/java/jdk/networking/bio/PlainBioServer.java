package jdk.networking.bio;

import szu.vander.log.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author : caiwj
 * @date :   2020/1/15
 * @description :
 */
public class PlainBioServer {

    private final static Logger log = new Logger();

    public void serve(int port) throws IOException {
        //将服务器绑定到指定端口
        final ServerSocket serverSocket = new ServerSocket(port);
        try {
            for (; ; ) {
                // 一直阻塞到一个连接建立
                final Socket clientSocket = serverSocket.accept();
                log.info("Accepted connection from " + clientSocket);
                //创建一个新的线程来处理该连接
                new Thread(() -> {
                    OutputStream out;
                    try {
                        //将消息写给已连接的客户端
                        out = clientSocket.getOutputStream();
                        log.info("say hi!");
                        out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
                        out.flush();
                        //关闭连接
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException ex) {
                            // ignore on close
                        }
                    }
                    //启动线程
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new PlainBioServer().serve(8080);
    }

}

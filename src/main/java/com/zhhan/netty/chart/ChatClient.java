package com.zhhan.netty.chart;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

//聊天程序客户端
public class ChatClient {
    private final String host; //服务器端IP地址
    private final int port;  //服务器端端口号

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            //往pipeline链中添加一个解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            //往pipeline链中添加一个编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            //往pipeline链中添加自定义的handler(业务处理类)
                            pipeline.addLast(new ChatClientHandler());
                        }
                    });

            ChannelFuture cf = bootstrap.connect(host, port).sync();
            Channel channel = cf.channel();
            System.out.println("------" + channel.localAddress().toString().substring(1) + "------");
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg + "\r\n"); //使用通道发送出消息
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new ChatClient("127.0.0.1", 9999).run();
    }
}

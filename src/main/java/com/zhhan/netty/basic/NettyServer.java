package com.zhhan.netty.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //1.创建线程组,接收客户端连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //2.创建线程组,处理网络操作
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        //3-10基本都是搞配置bootstrap
        //3.创建服务器端启动助手,来配置参数
        ServerBootstrap b = new ServerBootstrap();
        //4.设置两个线程组
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class) //5.使用NioServerSocketChannel作为服务器端通道的实现
                .option(ChannelOption.SO_BACKLOG, 100) //6.设置线程队列中等待连接的个数
                .childOption(ChannelOption.SO_KEEPALIVE, true) //7.保持活动的连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {//8.创建一个通道初始化对象
                    @Override
                    //9.往Pipeline 链中添加自定义的业务处理handler
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyServerHandler());
                    }
                });
        System.out.println("...Server is ready...");
        //10.启动服务器端并绑定端口，等待接受客户端连接(非阻塞)
        ChannelFuture cf = b.bind(9999).sync();
        //bind方法是异步的,产生channelfuture对象,产生channelfuture对象,cf对象的sync()方法是同步的
        System.out.println("......Server is Starting......");
        //11.关闭通道，关闭线程池
        cf.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}

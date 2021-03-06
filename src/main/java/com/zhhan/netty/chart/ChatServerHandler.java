package com.zhhan.netty.chart;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.List;

//自定义一个服务器端业务处理类
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    public static List<Channel> channels = new ArrayList<>();

    @Override  //通道就绪(用户上线)
    public void channelActive(ChannelHandlerContext ctx) {
        Channel inChannel = ctx.channel();
        channels.add(inChannel);
        System.out.println("[Server]:" + inChannel.remoteAddress().toString().substring(1) + "上线");
    }

    @Override  //通道未就绪(用户下线)
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel inChannel = ctx.channel();
        channels.remove(inChannel);
        System.out.println("[Server]:" + inChannel.remoteAddress().toString().substring(1) + "离线");
    }

    @Override  //读取数据(从通道0,)
    protected void channelRead0(ChannelHandlerContext ctx, String s) {
        Channel inChannel = ctx.channel();
        for (Channel channel : channels) {
            if (channel != inChannel) { //排除当前发消息的通道
                //广播出去消息
                channel.writeAndFlush("[" + inChannel.remoteAddress().toString().substring(1) + "]" + "说：" + s + "\n");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        cause.printStackTrace();
        ctx.close();
    }

}

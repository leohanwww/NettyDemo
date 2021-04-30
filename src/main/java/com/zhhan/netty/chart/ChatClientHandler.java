package com.zhhan.netty.chart;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

//自定义一个客户端业务处理类
public class ChatClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        //接收服务器端广播过来的信息
        System.out.println(s.trim());
    }
}
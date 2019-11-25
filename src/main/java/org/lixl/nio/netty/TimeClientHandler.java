package org.lixl.nio.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.lixl.nio.nio.TimeClientHandle;

import java.util.logging.Logger;

/**
 * Created by Administrator on 11/25/2019.
 */
public class TimeClientHandler extends ChannelHandlerAdapter {
    private static final Logger logger = java.util.logging.Logger.getLogger(TimeClientHandle.class.getName());
    private final ByteBuf firstMessage;

    public TimeClientHandler() {
        byte[] req = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(firstMessage);
    }

    public void chanelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println("Now is : " + body);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //释放资源
        logger.warning("Unexpected exception from downstream : " + cause.getMessage());
        ctx.close();
    }

}

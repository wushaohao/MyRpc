package com.wh.myrpc;


import com.wh.myrpc.bean.RpcRequest;
import com.wh.myrpc.bean.RpcResponse;
import com.wh.myrpc.codec.RpcDecoder;
import com.wh.myrpc.codec.RpcEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author:wuhao
 * @Description:Rpc客户端
 * @Date:17/7/29
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse>{

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

    private final String host;
    private final int port;

    private RpcResponse response;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
        this.response=rpcResponse;
    }

    public RpcResponse send(RpcRequest request) throws Exception{
        EventLoopGroup workerGroup=new NioEventLoopGroup();

        try {
            Bootstrap bootstrap=new Bootstrap();

            bootstrap.group(workerGroup).option(ChannelOption.TCP_NODELAY,true).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new RpcEncoder(RpcRequest.class));
                    pipeline.addLast(new RpcDecoder(RpcResponse.class));
                    pipeline.addLast(RpcClient.this);
                }
            });
            // 连接 RPC 服务器
            ChannelFuture future = bootstrap.connect(host,port).sync();
            // 写入 RPC 请求数据并关闭连接
            Channel channel =future.channel();
            channel.writeAndFlush(request).sync();
            channel.closeFuture().sync();
            // 返回 RPC 响应对象
            return response;
        }finally {
            workerGroup.shutdownGracefully();
        }
    }
}

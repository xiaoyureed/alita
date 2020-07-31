package io.github.xiaoyureed.alita.alitasample;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaoyu
 * date: 2020/7/20
 */
public class AlitaSampleApp {
    private static final Logger log = LoggerFactory.getLogger(AlitaSampleApp.class);

    public static void main(String[] args) throws Exception {
        Server jettyServer = new Server(8090);
        jettyServer.setHandler(new AbstractHandler() {
            @Override
            public void handle(String s, Request baseRequest,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException, ServletException {
                log.info("Handler request start: {}", request.getRequestURL());
                //设置类型，指定编码utf8
                response.setContentType("text/html; charset=utf-8");
                //设置响应状态吗
                response.setStatus(HttpServletResponse.SC_OK);
                //写响应数据
                response.getWriter().write("<h1>Hello world!</h1>");
                //标记请求已处理，handle链
                baseRequest.setHandled(true);
                log.info("Handler request end");
            }
        });
        jettyServer.start();
        jettyServer.dumpStdErr();
        jettyServer.join();
    }
}

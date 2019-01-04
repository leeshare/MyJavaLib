package org.lixl.basic;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Administrator on 6/18/2018.
 */

//@WebServlet("/Subscribe")
public class JMSSubscriber extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public JMSSubscriber(){
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            InitialContext ctx = new InitialContext();
            Topic topic = (Topic)ctx.lookup("java:/comp/env/topic/topic0");
            TopicConnectionFactory connectionFactory = (TopicConnectionFactory)ctx.lookup("java:comp/env/topic/connectionFactory");
            TopicConnection topicConnection = connectionFactory.createTopicConnection();
            TopicSession topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);
            topicConnection.start();

            TextMessage message = (TextMessage)topicSubscriber.receive();
            out.write("Message received: " + message.getText());
            topicConnection.close();

        } catch(Exception e){
            e.printStackTrace();
        }

    }

    protected  void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

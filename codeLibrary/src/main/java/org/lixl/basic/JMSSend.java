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
//@WebServlet("/Publish")
public class JMSSend extends HttpServlet {
    public static final long serialVersionID = 1L;

    public JMSSend(){
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        try {
            InitialContext ctx = new InitialContext();
            Topic topic = (Topic)ctx.lookup("java:comp/env/topic/topic0");
            TopicConnectionFactory connectionFactory = (TopicConnectionFactory)ctx.lookup("java:comp/env/topic/connectionFactory");
            TopicConnection topicConnection = connectionFactory.createTopicConnection();
            TopicSession topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            TopicPublisher topicPublisher = topicSession.createPublisher(topic);
            topicPublisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            TextMessage message = topicSession.createTextMessage();
            message.setText("Hello World");
            topicPublisher.publish(message);

            out.write("Message published: " + message.getText());

            topicConnection.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

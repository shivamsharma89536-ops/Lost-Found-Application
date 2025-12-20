package com.Android.Android_App.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // This is where clients subscribe to
        config.setApplicationDestinationPrefixes("/app"); // This is where clients send messages to
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // This is the endpoint the Android client will connect to
        // Matches: ws://10.0.2.2:8080/ws/chat
        registry.addEndpoint("/ws/chat").withSockJS();
    }
    
    // TODO: We need to add JWT authentication to this WebSocket endpoint
}
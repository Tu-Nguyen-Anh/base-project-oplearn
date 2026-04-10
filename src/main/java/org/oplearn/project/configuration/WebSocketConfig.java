package org.oplearn.project.configuration;

import lombok.RequiredArgsConstructor;
import org.oplearn.project.filter.WebSocketAuthChannelInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static org.oplearn.project.constanst.OpLearnConstants.ChatConstants.APP_PREFIX;
import static org.oplearn.project.constanst.OpLearnConstants.ChatConstants.TOPIC_PREFIX;
import static org.oplearn.project.constanst.OpLearnConstants.ChatConstants.WEBSOCKET_ENDPOINT;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketAuthChannelInterceptor webSocketAuthChannelInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(TOPIC_PREFIX);
        registry.setApplicationDestinationPrefixes(APP_PREFIX);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WEBSOCKET_ENDPOINT)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     * Tăng thread pool cho inbound channel để xử lý đồng thời nhiều message từ client.
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.taskExecutor()
                .corePoolSize(4)
                .maxPoolSize(10)
                .queueCapacity(100);
        registration.interceptors(webSocketAuthChannelInterceptor);
    }

    /**
     * Tăng thread pool cho outbound channel để broadcast không bị hàng đợi khi nhiều client.
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor()
                .corePoolSize(4)
                .maxPoolSize(10)
                .queueCapacity(100);
    }
}

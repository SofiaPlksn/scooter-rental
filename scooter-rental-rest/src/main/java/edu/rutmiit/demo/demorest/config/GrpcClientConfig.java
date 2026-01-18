package edu.rutmiit.demo.demorest.config;

import grpc.demo.analytics.AnalyticsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Value("${grpc.client.analytics-service.address:localhost:9090}")
    private String grpcServerAddress;

    @Bean
    public ManagedChannel grpcChannel() {
        return ManagedChannelBuilder
                .forTarget(grpcServerAddress)
                .usePlaintext()
                .build();
    }

    @Bean
    public AnalyticsServiceGrpc.AnalyticsServiceBlockingStub analyticsServiceBlockingStub(ManagedChannel channel) {
        return AnalyticsServiceGrpc.newBlockingStub(channel);
    }
}
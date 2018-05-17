package org.activiti.cloud.gateway;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    private static final Log log = LogFactory.getLog(GatewayApplication.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    public DiscoveryClientRouteDefinitionLocator discoveryClientRouteLocator(DiscoveryClient discoveryClient,
                                                                             DiscoveryLocatorProperties properties) {
        return new DiscoveryClientRouteDefinitionLocator(discoveryClient, properties);
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,
                              args);
    }

    @Scheduled(fixedRate = 15000)
    public void watchServices() {
        List<String> services = this.discoveryClient.getServices();
        for (String s : services) {
            List<ServiceInstance> instances = this.discoveryClient.getInstances(s);
            for (ServiceInstance si : instances) {
                log.info("Service: " + si.getServiceId());
                log.info("Metadata:");
                for (String key : si.getMetadata().keySet()) {
                    log.info("\tkey:" + key +
                                     " - value: " + si.getMetadata().get(key));
                }
            }
        }
    }
}
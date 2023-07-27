package com.example.util;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.stereotype.Component;

import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.iam.v1.TestIamPermissionsRequest;
import com.google.iam.v1.TestIamPermissionsResponse;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class PubSubTopicInitializingBean implements InitializingBean {

    @Autowired
    private BindingServiceProperties bindingServiceProperties;

    private TopicAdminClient topicAdminClient;

    public PubSubTopicInitializingBean() throws IOException {
        topicAdminClient = TopicAdminClient.create();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // validate the topics exist and we can publish to them
        log.info(ToStringBuilder.reflectionToString(bindingServiceProperties));
        /* 
        if (bindingServiceProperties.getBinders() == null || bindingServiceProperties.getBindings().isEmpty()) {
            log.info("No Subscriptions configured ...");
            return;
        }

        for (final Entry<String, BindingProperties> topic : bindingServiceProperties.getBindings().entrySet()) {
            log.info(topic.getKey() + "=" + topic.getValue());
            final TestIamPermissionsRequest testReq = 
                TestIamPermissionsRequest.newBuilder()
                    .setResource(topic.getValue().getDestination())
                    .addPermissions("pubsub.subscriptions.consume")
                    .addPermissions("pubsub.subscriptions.get")
                    .build();

            final TestIamPermissionsResponse resp = topicAdminClient.testIamPermissions(testReq);

            if (resp.getPermissionsCount() == 0) {
                throw new Exception("No permission to publish to topic " + topic);
            }

            log.info("Subscribing to " + topic.getValue() + " as " + topic.getKey() + ": OK (" + resp.getPermissionsList() + ")");


        }
        */


    }
    
}

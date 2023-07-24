package com.example.util;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.config.PubSubProperties;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.iam.v1.TestIamPermissionsRequest;
import com.google.iam.v1.TestIamPermissionsResponse;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class PubSubTopicInitializingBean implements InitializingBean {

    @Autowired
    private PubSubProperties pubSubProperties;

    private TopicAdminClient topicAdminClient;

    public PubSubTopicInitializingBean() throws IOException {
        topicAdminClient = TopicAdminClient.create();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        // validate the topics exist and we can publish to them

        if (pubSubProperties.getTopics() == null) {
            log.info("No Topics configured ...");
            return;
        }

        for (final Map.Entry<String, String> topic : pubSubProperties.getTopics().entrySet()) {
            log.info(topic.getKey() + "=" + topic.getValue());
            final TestIamPermissionsRequest testReq = 
                TestIamPermissionsRequest.newBuilder()
                    .setResource(topic.getValue())
                    .addPermissions("pubsub.topics.publish")
                    .build();

            final TestIamPermissionsResponse resp = topicAdminClient.testIamPermissions(testReq);

            if (resp.getPermissionsCount() == 0) {
                throw new Exception("No permission to publish to topic " + topic);
            }

            log.info("Publishing to " + topic.getValue() + " as " + topic.getKey() + ": OK (" + resp.getPermissionsList() + ")");


        }


    }
    
}

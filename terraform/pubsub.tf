resource "random_id" "topic-suffix" {
    count = var.num_topics
    byte_length = 2
}

resource "google_project_iam_custom_role" "topic_router_pubsub" {
    project     = module.service_project.project_id
    role_id = "topic_router_custom"
    title = "topic router custom role"
    description = "custom role for topic router to publish to dynamic topics"
    permissions = [
        "pubsub.topics.get",
        "pubsub.topics.publish",
    ]
}

resource "google_pubsub_topic_iam_member" "topic_router_publish" {
    count       = var.num_topics
    topic = google_pubsub_topic.topic[count.index].id
    role = google_project_iam_custom_role.topic_router_pubsub.name
    member = "serviceAccount:${google_service_account.topic_router_sa.email}"
}

resource "google_pubsub_topic" "topic" {
    count       = var.num_topics
    project     = module.service_project.project_id
    name        = "topic-${random_id.topic-suffix[count.index].hex}"
}

resource "random_id" "sub-suffix" {
    count = var.num_topics * var.num_subs_per_topic
    byte_length = 2
}

resource "google_pubsub_subscription" "subscription" {
    project     = module.subscriber_project.project_id
    count = var.num_topics * var.num_subs_per_topic
    name = "sub-${random_id.topic-suffix[(count.index % var.num_topics)].hex}-${random_id.sub-suffix[count.index].hex}"
    topic = google_pubsub_topic.topic[(count.index % var.num_topics)].id

}
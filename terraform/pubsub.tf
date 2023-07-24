resource "random_id" "topic-suffix" {
    count = var.num_topics
    byte_length = 2
}

resource "google_pubsub_topic_iam_member" "topic_router_publish" {
    count       = var.num_topics
    topic = google_pubsub_topic.topic[count.index].id
    role = "roles/pubsub.publisher"
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
    project     = module.service_project.project_id
    count = var.num_topics * var.num_subs_per_topic
    name = "sub-${random_id.topic-suffix[(count.index % var.num_topics)].hex}-${random_id.sub-suffix[count.index].hex}"
    topic = google_pubsub_topic.topic[(count.index % var.num_topics)].name

}
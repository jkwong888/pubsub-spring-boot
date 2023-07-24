resource "google_service_account" "topic_router_sa" {
    project = module.service_project.project_id
    account_id = "topic-router"
}
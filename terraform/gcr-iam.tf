data "google_project" "registry_project" {
    project_id = var.registry_project_id
}

# take the GKE SA and allow storage object browser on the image registry bucket
resource "google_storage_bucket_iam_member" "registry_bucket" {
    bucket  = format("artifacts.%s.appspot.com", data.google_project.registry_project.project_id)
    role    = "roles/storage.objectViewer"
    member  = format("serviceAccount:service-%s@serverless-robot-prod.iam.gserviceaccount.com", module.service_project.number)
}

# take the GKE SA and allow storage object browser on the image registry bucket
resource "google_storage_bucket_iam_member" "registry_bucket_gke" {
    bucket  = format("artifacts.%s.appspot.com", data.google_project.registry_project.project_id)
    role    = "roles/storage.objectViewer"
    member  = "serviceAccount:${google_service_account.gke_sa.email}"
}
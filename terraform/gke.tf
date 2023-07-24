locals {
  gke_sa_roles = [
    "roles/monitoring.metricWriter",
    "roles/logging.logWriter",
    "roles/monitoring.viewer",
  ]
} 

resource "google_service_account" "gke_sa" {
  project       = module.service_project.project_id
  account_id    = format("%s-sa", var.gke_cluster.name)
  display_name  = format("%s cluster service account", var.gke_cluster.name)
}

resource "google_project_iam_member" "gke_sa_role" {
  count     = length(local.gke_sa_roles)

  project   = module.service_project.project_id
  role      = local.gke_sa_roles[count.index]
  member    = "serviceAccount:${google_service_account.gke_sa.email}"
}

resource "google_project_iam_member" "gke_host_service_agent_user" {
  depends_on = [
    module.service_project.enabled_apis,
  ]

  project     = data.google_project.host_project.project_id
  role        = "roles/container.hostServiceAgentUser"
  member      = format("serviceAccount:service-%d@container-engine-robot.iam.gserviceaccount.com", module.service_project.number)
}

resource "google_container_cluster" "primary" {
  lifecycle {
    ignore_changes = [
      # Ignore changes to tags, e.g. because a management agent
      # updates these based on some ruleset managed elsewhere.
      node_config,
    ]
  }

  depends_on = [
    module.service_project.enabled_apis,
    module.service_project.subnet_users,
    module.service_project.hostServiceAgentUser,
    google_project_iam_member.gke_sa_role,
    google_project_organization_policy.shielded_vm_disable,
    google_project_organization_policy.oslogin_disable,
  ]

  name     = var.gke_cluster.name
  location = var.gke_cluster.region
  project  = module.service_project.project_id

  release_channel  {
    channel = "REGULAR"
  }

  enable_autopilot = true

  private_cluster_config {
    enable_private_nodes = var.gke_cluster.private_cluster     # nodes have private IPs only
    enable_private_endpoint = false  # master nodes private IP only
    master_ipv4_cidr_block = var.gke_cluster.master_range
  }

  master_authorized_networks_config {
    cidr_blocks {
      cidr_block = "0.0.0.0/0"
      display_name = "eerbody"
    }
  }

  node_config {
    service_account = google_service_account.gke_sa.email
    oauth_scopes = [
      "https://www.googleapis.com/auth/cloud-platform"
    ]
  }

  network = data.google_compute_network.shared_vpc.self_link
  subnetwork = lookup(
    zipmap(
      module.service_project.subnets.*.name, 
      module.service_project.subnets.*.self_link),
    var.gke_cluster.subnet,
    ""
  )

  ip_allocation_policy {
    cluster_secondary_range_name = var.gke_cluster.subnet_pods_range_name
    services_secondary_range_name = var.gke_cluster.subnet_services_range_name
  }

  vertical_pod_autoscaling {
    enabled = true
  }
}
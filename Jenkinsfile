@Library(value='kids-first/aws-infra-jenkins-shared-libraries', changelog=false) _
ecs_service_type_1_standard {
    projectName = "riff"
    agentLabel = "terraform-testing"
    environments = "dev,qa,prd"
    docker_image_type = "alpine"
    entrypoint_command = "/srv/riff/exec/run.sh"
    deploy_scripts_version = "master"
    quick_deploy = "true"
    external_config_repo = "false"
    internal_app         = "false"
    container_port = "8081"
    vcpu_container             = "1024"
    memory_container           = "2048"
    vcpu_task                  = "1024"
    memory_task                = "2048"
    health_check_path = "/swagger-ui.html"
    dependencies = "ecr,postgres_rds"
}

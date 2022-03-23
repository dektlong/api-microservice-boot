SOURCE_IMAGE = os.getenv("SOURCE_IMAGE", default='your-registry.io/your-project/APP_NAME')
LOCAL_PATH = os.getenv("LOCAL_PATH", default='.')
NAMESPACE = os.getenv("NAMESPACE", default='default')

k8s_custom_deploy(
    'mood-sensors',
    apply_cmd="tanzu apps workload apply -f config/tap/workload.yaml --live-update" +
               " --local-path " + LOCAL_PATH +
               " --source-image " + SOURCE_IMAGE +
               " --namespace " + NAMESPACE +
               " --yes >/dev/null" +
               " && kubectl get workload APP_NAME --namespace " + NAMESPACE + " -o yaml",
    delete_cmd="tanzu apps workload delete -f workload.yaml --namespace " + NAMESPACE + " --yes",
    deps=['pom.xml', './target/classes'],
    container_selector='workload',
    live_update=[
      sync('./target/classes', '/workspace/BOOT-INF/classes')
    ]
)

k8s_resource('APP_NAME', port_forwards=["8080:8080"],
            extra_pod_selectors=[{'serving.knative.dev/service': 'APP_NAME'}])
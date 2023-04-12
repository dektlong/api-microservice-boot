# dynamic provisioning of developers services
tanzu service class-claim create CLAIM1_NAME \
    --class CLAIM1_TYPE \
    --parameter storageGB=1 \
    --namespace YOUR_DEV_NAMESPACE

tanzu service class-claim create CLAIM2_NAME \
    --class CLAIM2_TYPE \
    --parameter replicas=1 \
    --parameter storageGB=1 \
    --namespace YOUR_DEV_NAMESPACE
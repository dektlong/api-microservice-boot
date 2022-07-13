
## An Accelerator to create API-driven backend with Spring Boot.

### Deploy the workload

```
**tanzu apps workload create -f config/tap/workload.yaml -n mydev**
```

### Track workload progress

```
tanzu apps workload get mood-sensors -n mydev

tanzu apps workload tail mood-sensors --since 100m --timestamp  -n mydev
```

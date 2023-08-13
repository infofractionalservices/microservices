# One time set up activities
https://www.baeldung.com/spring-boot-minikube
```shell
$ kubectl config use-context minikube
$ kubectl cluster-info
```

# Ensure your minikube is able to use local build images.

```shell
$ eval $(minikube docker-env)
```

## Rebuild you docker images again from the same sheell terminal session
```shell
$ cd ..
$ cd ./docker-scripts/all.ps1
$ cd k8s
```
`

## Verify the images do exist

```shell
$ minikube image ls --format table
````


# Exposing services to outside world
```shell
$ minikube service backend-service-deployment

# In another terminal
$ minikube service jaeger-service-deployment
```
`
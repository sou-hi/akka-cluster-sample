3台構成のAkka Clusterを簡単に試してみる
----

# How to run
```
// run your guest
$ cd path
$ cd data/ClusterActor
$ sbt assembly
$ scp target/scala-2.10/HeavyPiClusterProject-assembly-1.0.jar 192.168.33.10:~/
$ scp target/scala-2.10/HeavyPiClusterProject-assembly-1.0.jar 192.168.33.11:~/
$ scp target/scala-2.10/HeavyPiClusterProject-assembly-1.0.jar 192.168.33.12:~/

// run your vms 
$ java -cp HeavyPiClusterProject-assembly-1.0.jar HeavyPiCluster 192.168.33.11 2551
$ java -cp HeavyPiClusterProject-assembly-1.0.jar HeavyPiCluster 192.168.33.12 2551
$ java -cp HeavyPiClusterProject-assembly-1.0.jar HeavyPiCluster 192.168.33.10 2551 pi

```
## Map-Reduce on Hadoop

* [Set up a single node cluster](https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/SingleCluster.html)

* [Hadoop cluster set up](https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/ClusterSetup.html)

### Hadoop basic

The Apache Hadoop software library is a framework that allows for the distributed processing of large data sets across
clusters of computers using simple programming models. It is designed to scale up from single servers to thousands of
machines, each offering local computation and storage. Rather than rely on hardware to deliver high-availability, the
library itself is designed to detect and handle failures at the application layer, so delivering a highly-available
service on top of a cluster of computers, each of which may be prone to failures.

The project includes these modules:

* Hadoop Common: The common utilities that support the other Hadoop modules.

* Hadoop Distributed File System (HDFS™): A distributed file system that provides high-throughput access to application
  data.

* Hadoop YARN: A framework for job scheduling and cluster resource management.

* Hadoop MapReduce: A YARN-based system for parallel processing of large data sets.

### Setting Up a Single Node Cluster

```shell
sudo apt-get install ssh
sudo apt-get install pdsh
```

### Prepare to Start the Hadoop Cluster

How to correctly set up the JAVA_HOME environment variable?

```shell
  # set to the root of your Java installation
  export JAVA_HOME=export JAVA_HOME=/lib/jvm/java-18-openjdk-amd64/bin
```

### Standalone Operation

By default, Hadoop is configured to run in a non-distributed mode, as a single Java process. This is useful for
debugging.

```shell
  $ mkdir input
  $ cp etc/hadoop/*.xml input
  $ bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.3.6.jar grep input output 'dfs[a-z.]+'
  $ cat output/*
```

### Pseudo-Distributed Operation

Hadoop can also be run on a single-node in a pseudo-distributed mode where each Hadoop daemon runs in a separate Java
process.

#### Configuration

etc/hadoop/core-site.xml

```xml

<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>
```

etc/hadoop/core-site.xml

```xml

<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
</configuration>
```

#### Setup passphrase-less ssh

```shell
ssh localhost
```

```shell
  $ ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
  $ cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
  $ chmod 0600 ~/.ssh/authorized_keys
```

### Fully Distributed Operation

TBD
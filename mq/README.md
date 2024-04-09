

## Setup Topic for RocketMQ

```bash
sh bin/mqnamesrv
sh bin/mqbroker -n localhost:9876 --enable-proxy 
bin/mqadmin updatetopic -n localhost:9876 -t statistic -c DefaultCluster
bin/mqadmin updatetopic -n localhost:9876 -t device -c DefaultCluster
```

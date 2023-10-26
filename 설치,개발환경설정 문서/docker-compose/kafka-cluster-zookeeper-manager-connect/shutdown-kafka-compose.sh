rm -rf volumes
docker-compose -f common.yml -f kafka_cluster.yml down
docker-compose -f common.yml -f zookeeper.yml down

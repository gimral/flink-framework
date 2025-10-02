package com.gimral.streaming.test.kafka;

public class EmbeddedKafkaConfiguration {
    private final int kafkaPort;
    private final int zookeeperPort;

    public EmbeddedKafkaConfiguration(int kafkaPort, int zookeeperPort) {
        this.kafkaPort = kafkaPort;
        this.zookeeperPort = zookeeperPort;
    }

    public int getKafkaPort() {
        return kafkaPort;
    }

    public int getZookeeperPort() {
        return zookeeperPort;
    }

    public static final class Builder {
        private int kafkaPort = 6001;
        private int zookeeperPort = 6002;

        public Builder setKafkaPort(int kafkaPort){
            this.kafkaPort = kafkaPort;
            return this;
        }

        public Builder setZookeeperPort(int zookeeperPort){
            this.zookeeperPort = zookeeperPort;
            return this;
        }

        public EmbeddedKafkaConfiguration build(){
            return new EmbeddedKafkaConfiguration(kafkaPort, zookeeperPort);
        }

    }
}

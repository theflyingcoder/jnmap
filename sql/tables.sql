CREATE TABLE `scan_job` (
  `scan_job_id`   BIGINT       NOT NULL AUTO_INCREMENT,
  `target`        VARCHAR(255) NOT NULL,
  `target_hash`   CHAR(32)     NOT NULL,
  `command`       VARCHAR(300) NOT NULL,
  `elapsed_secs`  FLOAT        NULL     DEFAULT NULL,
  `target_status` VARCHAR(20)  NULL     DEFAULT NULL,
  `create_time`   DATETIME     NOT NULL,
  `update_time`   DATETIME     NULL     DEFAULT NULL,
  PRIMARY KEY (`scan_job_id`),
  KEY `INDEX` (`target_hash`, `create_time`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `scan_port_result` (
  `scan_port_result_id` BIGINT      NOT NULL AUTO_INCREMENT,
  `scan_job_id`         BIGINT      NOT NULL,
  `port`                INT(11)     NOT NULL,
  `state`               VARCHAR(10) NOT NULL,
  `protocol`            VARCHAR(11) NOT NULL,
  `service`             VARCHAR(50) NULL,
  `create_time`         DATETIME    NOT NULL,
  PRIMARY KEY (`scan_port_result_id`),
  KEY `INDEX` (`scan_job_id`, `create_time`),
  CONSTRAINT `fk_scan_port_result2scan_job` FOREIGN KEY (`scan_job_id`) REFERENCES `scan_job` (`scan_job_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_vmr_namespace
-- ----------------------------
DROP TABLE IF EXISTS `t_vmr_namespace`;
CREATE TABLE `t_vmr_namespace`
(
    `id`           varchar(32)  NOT NULL,
    `code`         varchar(255) NOT NULL,
    `name`         varchar(255) NOT NULL,
    `preset`       bit(1)       NOT NULL,
    `create_time`  datetime(6) DEFAULT NULL,
    `update_time`  datetime(6) DEFAULT NULL,
    `lock_version` bigint(20)  DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for t_vmr_module
-- ----------------------------
DROP TABLE IF EXISTS `t_vmr_module`;
CREATE TABLE `t_vmr_module`
(
    `id`             varchar(32)  NOT NULL,
    `code`           varchar(255) NOT NULL,
    `name`           varchar(255) NOT NULL,
    `current`        varchar(255) NOT NULL,
    `description`    varchar(255) NOT NULL,
    `namespace_code` varchar(255) NOT NULL,
    `create_time`    datetime(6) DEFAULT NULL,
    `update_time`    datetime(6) DEFAULT NULL,
    `lock_version`   bigint(20)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKg4fhn16n8t5w5xbf5nynillty` (`name`, `namespace_code`),
    UNIQUE KEY `UKcymip7yw1cx24wtewit5kueh` (`code`, `namespace_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for t_vmr_package
-- ----------------------------
CREATE TABLE `t_vmr_package`
(
    `id`             varchar(32)  NOT NULL,
    `version`        varchar(255) NOT NULL,
    `description`    varchar(255) NOT NULL,
    `module_code`    varchar(255) NOT NULL,
    `namespace_code` varchar(255) NOT NULL,
    `create_time`    datetime(6) DEFAULT NULL,
    `update_time`    datetime(6) DEFAULT NULL,
    `lock_version`   bigint(20)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKdr9hlw8nt73ghkvis3kcdyu23` (`version`, `module_code`, `namespace_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for t_vmr_user
-- ----------------------------
DROP TABLE IF EXISTS `t_vmr_user`;
CREATE TABLE `t_vmr_user`
(
    `id`           varchar(32)  NOT NULL,
    `username`     varchar(255) NOT NULL,
    `password`     varchar(255) NOT NULL,
    `create_time`  datetime(6)  NULL DEFAULT NULL,
    `update_time`  datetime(6)  NULL DEFAULT NULL,
    `lock_version` bigint(20)   NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8;

SET FOREIGN_KEY_CHECKS = 1;
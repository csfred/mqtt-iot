/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.12
Source Server Version : 50733
Source Host           : 192.168.1.12:3306
Source Database       : iotdb

Target Server Type    : MYSQL
Target Server Version : 50733
File Encoding         : 65001

Date: 2021-05-27 07:40:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_device
-- ----------------------------
DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `f_md5` varchar(255) NOT NULL,
  `f_cmdId` int(4) NOT NULL,
  `f_devId` int(4) NOT NULL,
  `f_devNo` int(4) NOT NULL,
  `f_type` int(4) NOT NULL,
  `f_version` varchar(100) NOT NULL,
  PRIMARY KEY (`f_md5`),
  KEY `idx_1` (`f_cmdId`,`f_devId`,`f_type`,`f_version`) USING BTREE,
  KEY `f_md5` (`f_md5`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `t_device_ext`;
CREATE TABLE `t_device_ext` (
  `f_device_md5` varchar(255) NOT NULL,
  `f_var_list` varchar(500) NOT NULL,
  `f_var_list_md5` varchar(255) NOT NULL,
  PRIMARY KEY (`f_device_md5`,`f_var_list_md5`),
  KEY `idx1` (`f_device_md5`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `t_device_ext2`;
CREATE TABLE `t_device_ext2` (
  `f_var_list_md5` varchar(255) NOT NULL,
  `f_receive_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

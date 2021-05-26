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
  `F_cmdId` int(4) NOT NULL,
  `F_devId` int(4) NOT NULL,
  `F_devNo` int(4) NOT NULL,
  `F_type` int(4) NOT NULL,
  `F_ver` varchar(100) DEFAULT NULL,
  `F_time` timestamp NULL DEFAULT NULL,
  `F_varList` text NOT NULL,
  KEY `idx_1` (`F_cmdId`,`F_devId`,`F_type`,`F_ver`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

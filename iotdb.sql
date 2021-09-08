/*
Navicat MySQL Data Transfer

Source Server         : 8.140.106.126
Source Server Version : 50734
Source Host           : 8.140.106.126:3306
Source Database       : iotdb

Target Server Type    : MYSQL
Target Server Version : 50734
File Encoding         : 65001

Date: 2021-09-09 02:18:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_device
-- ----------------------------
DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `f_station_no` varchar(20) NOT NULL DEFAULT '',
  `f_dev_no` bigint(20) NOT NULL,
  `f_var_list_fields` text NOT NULL,
  `f_var_list_fields_md5` char(32) NOT NULL COMMENT '设备变量字段，默认排序规则存放JSON数组',
  `f_receive_count` int(11) NOT NULL DEFAULT '0',
  `f_start_receive_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `f_end_receive_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `f_is_show` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否显示',
  PRIMARY KEY (`f_station_no`,`f_dev_no`,`f_var_list_fields_md5`),
  KEY `idx1` (`f_station_no`,`f_dev_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_device
-- ----------------------------

-- ----------------------------
-- Table structure for t_device_info
-- ----------------------------
DROP TABLE IF EXISTS `t_device_info`;
CREATE TABLE `t_device_info` (
  `f_dev_no` bigint(20) NOT NULL AUTO_INCREMENT,
  `f_station_no` varchar(20) NOT NULL DEFAULT '',
  `f_dev_name` varchar(100) NOT NULL DEFAULT '',
  `f_dev_type` int(4) NOT NULL DEFAULT '0' COMMENT '设备种类，参考 t_device_type',
  `f_dev_var_fields` text NOT NULL COMMENT '设备变量字段，默认排序规则存放JSON数组',
  `f_dev_vector` text,
  PRIMARY KEY (`f_dev_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_device_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_device_type
-- ----------------------------
DROP TABLE IF EXISTS `t_device_type`;
CREATE TABLE `t_device_type` (
  `f_id` int(4) NOT NULL AUTO_INCREMENT,
  `f_name` varchar(250) NOT NULL DEFAULT '' COMMENT '设备类型名称',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_device_type
-- ----------------------------
INSERT INTO `t_device_type` VALUES ('1', '泵');
INSERT INTO `t_device_type` VALUES ('2', '流量计');
INSERT INTO `t_device_type` VALUES ('3', '液位计');
INSERT INTO `t_device_type` VALUES ('4', '加药泵');
INSERT INTO `t_device_type` VALUES ('5', '风机');
INSERT INTO `t_device_type` VALUES ('6', '搅拌机');
INSERT INTO `t_device_type` VALUES ('7', '格栅机');
INSERT INTO `t_device_type` VALUES ('8', '电动阀');
INSERT INTO `t_device_type` VALUES ('9', '气动阀');
INSERT INTO `t_device_type` VALUES ('10', '比例调节阀');
INSERT INTO `t_device_type` VALUES ('11', '压力传感器');
INSERT INTO `t_device_type` VALUES ('12', 'PH计');
INSERT INTO `t_device_type` VALUES ('13', 'ORP计');
INSERT INTO `t_device_type` VALUES ('14', '电导');
INSERT INTO `t_device_type` VALUES ('15', '浊度');
INSERT INTO `t_device_type` VALUES ('16', '温度仪表显示');
INSERT INTO `t_device_type` VALUES ('17', '开关量液位计');
INSERT INTO `t_device_type` VALUES ('18', '备用仪表显示');
INSERT INTO `t_device_type` VALUES ('19', '显示屏');

-- ----------------------------
-- Table structure for t_monitor_info
-- ----------------------------
DROP TABLE IF EXISTS `t_monitor_info`;
CREATE TABLE `t_monitor_info` (
  `f_id` bigint(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `f_station_no` varchar(20) NOT NULL DEFAULT '' COMMENT '站点编号',
  `f_monitoring_no` varchar(100) NOT NULL DEFAULT '' COMMENT '摄像机编号',
  `f_monitoring_name` varchar(255) NOT NULL DEFAULT '' COMMENT '摄像机名称',
  `f_type` varchar(10) NOT NULL DEFAULT '' COMMENT '类别',
  `f_device_Id` varchar(500) NOT NULL DEFAULT '' COMMENT '设备序列号 摄像机专用',
  `f_channel_Id` varchar(500) NOT NULL,
  `f_url` text NOT NULL COMMENT '视频URL',
  `f_remarks1` text NOT NULL COMMENT '备注1',
  `f_remarks2` text NOT NULL COMMENT '备注2',
  `f_remarks3` text NOT NULL COMMENT '备注3',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_monitor_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_station_info
-- ----------------------------
DROP TABLE IF EXISTS `t_station_info`;
CREATE TABLE `t_station_info` (
  `f_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `f_station_no` varchar(20) NOT NULL DEFAULT '',
  `f_station_name` varchar(500) NOT NULL DEFAULT '',
  `f_station_lon` double(20,6) NOT NULL DEFAULT '0.000000' COMMENT '经度',
  `f_station_lat` double(20,6) NOT NULL DEFAULT '0.000000' COMMENT '纬度',
  `f_station_address` varchar(1000) NOT NULL DEFAULT '',
  `f_process_tech` varchar(500) NOT NULL DEFAULT '' COMMENT '处理工艺',
  `f_process_scale` int(4) NOT NULL DEFAULT '0' COMMENT '处理规模',
  `f_dev_ops_num` int(4) NOT NULL DEFAULT '0' COMMENT '运维人数',
  `f_contact_tel` varchar(20) NOT NULL DEFAULT '',
  `f_bg_dev_img_path` text NOT NULL COMMENT '设备底图背景图片存放路径',
  `f_bg_water_img_path` text NOT NULL COMMENT '水质背景图',
  `f_remarks1` varchar(500) NOT NULL DEFAULT '' COMMENT '备注1',
  `f_remarks2` varchar(500) NOT NULL DEFAULT '' COMMENT '备注2',
  `f_remarks3` varchar(500) NOT NULL DEFAULT '' COMMENT '备注3',
  `f_isOnline` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否在线',
  `f_onlineModifyTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `f_principal` varchar(255) NOT NULL DEFAULT '' COMMENT '站点负责人',
  PRIMARY KEY (`f_id`,`f_station_no`,`f_station_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_station_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_time_interval
-- ----------------------------
DROP TABLE IF EXISTS `t_time_interval`;
CREATE TABLE `t_time_interval` (
  `f_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `f_interval` int(4) NOT NULL DEFAULT '20' COMMENT '时间间隔',
  `f_unit` int(4) NOT NULL DEFAULT '0' COMMENT '时间单位0：秒 1：分钟 2：小时  3：天',
  PRIMARY KEY (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_time_interval
-- ----------------------------

-- ----------------------------
-- Table structure for t_water_quality
-- ----------------------------
DROP TABLE IF EXISTS `t_water_quality`;
CREATE TABLE `t_water_quality` (
  `f_station_no` varchar(20) NOT NULL DEFAULT '',
  `f_info_no` bigint(20) NOT NULL COMMENT 't_water_quality_info表主键f_no',
  `f_var_data` text NOT NULL,
  `f_var_field` varchar(500) NOT NULL DEFAULT '' COMMENT '水质变量字段',
  `f_receive_count` int(11) NOT NULL DEFAULT '0',
  `f_start_receive_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `f_end_receive_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`f_station_no`,`f_info_no`,`f_var_field`),
  KEY `idx1` (`f_station_no`,`f_info_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_water_quality
-- ----------------------------

-- ----------------------------
-- Table structure for t_water_quality_info
-- ----------------------------
DROP TABLE IF EXISTS `t_water_quality_info`;
CREATE TABLE `t_water_quality_info` (
  `f_no` bigint(20) NOT NULL AUTO_INCREMENT,
  `f_station_no` varchar(20) NOT NULL DEFAULT '' COMMENT '站点编号',
  `f_name` varchar(100) NOT NULL DEFAULT '' COMMENT '水质名称',
  `f_data_length` int(11) NOT NULL DEFAULT '0' COMMENT '水质变量数据的显示长度',
  `f_var_field` varchar(500) NOT NULL COMMENT '水质包含的变量字段',
  `f_vector` text COMMENT '水质组每个变量的相对位置，JSON数组，素组中MAP',
  PRIMARY KEY (`f_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_water_quality_info
-- ----------------------------

/*
Navicat MySQL Data Transfer

Source Server         : fish2
Source Server Version : 50560
Source Host           : localhost:3306
Source Database       : shardingmybatis

Target Server Type    : MYSQL
Target Server Version : 50560
File Encoding         : 65001

Date: 2021-01-13 23:11:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_order_0
-- ----------------------------
DROP TABLE IF EXISTS `tb_order_0`;
CREATE TABLE `tb_order_0` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_name` varchar(255) DEFAULT NULL,
  `order_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for tb_order_1
-- ----------------------------
DROP TABLE IF EXISTS `tb_order_1`;
CREATE TABLE `tb_order_1` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_name` varchar(255) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

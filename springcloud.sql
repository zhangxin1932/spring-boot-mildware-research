/*
Navicat MySQL Data Transfer

Source Server         : demo
Source Server Version : 50560
Source Host           : localhost:3306
Source Database       : springcloud

Target Server Type    : MYSQL
Target Server Version : 50560
File Encoding         : 65001

Date: 2020-03-21 21:20:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for excel_stu_score
-- ----------------------------
DROP TABLE IF EXISTS `excel_stu_score`;
CREATE TABLE `excel_stu_score` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `chineseScore` varchar(255) DEFAULT NULL,
  `mathScore` varchar(255) DEFAULT NULL,
  `englishScore` varchar(255) DEFAULT NULL,
  `totalScore` varchar(255) DEFAULT NULL,
  `classRanking` varchar(255) DEFAULT NULL,
  `schoolRanking` varchar(255) DEFAULT NULL,
  `examDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=187 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of excel_stu_score
-- ----------------------------

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of hibernate_sequence
-- ----------------------------
INSERT INTO `hibernate_sequence` VALUES ('1');
INSERT INTO `hibernate_sequence` VALUES ('1');
INSERT INTO `hibernate_sequence` VALUES ('1');
INSERT INTO `hibernate_sequence` VALUES ('1');

-- ----------------------------
-- Table structure for tb_department
-- ----------------------------
DROP TABLE IF EXISTS `tb_department`;
CREATE TABLE `tb_department` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '部门编号',
  `department_name` varchar(255) NOT NULL COMMENT '部门名称',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user` bigint(20) NOT NULL COMMENT '更新人工号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_department
-- ----------------------------
INSERT INTO `tb_department` VALUES ('1', '研发部', '2020-03-21 13:42:10', '0');
INSERT INTO `tb_department` VALUES ('2', '行政部', '2020-03-21 13:42:21', '0');
INSERT INTO `tb_department` VALUES ('3', '人力资源部', '2020-03-21 13:42:32', '0');
INSERT INTO `tb_department` VALUES ('4', '监察部', '2020-03-21 13:42:45', '0');
INSERT INTO `tb_department` VALUES ('5', '市场部', '2020-03-21 13:42:59', '0');
INSERT INTO `tb_department` VALUES ('6', '运营部', '2020-03-21 13:43:12', '0');
INSERT INTO `tb_department` VALUES ('7', '售后服务部', '2020-03-21 13:43:23', '0');
INSERT INTO `tb_department` VALUES ('8', '财务部', '2020-03-21 13:43:32', '0');
INSERT INTO `tb_department` VALUES ('9', '法务部', '2020-03-21 13:43:49', '0');
INSERT INTO `tb_department` VALUES ('10', '专利部', '2020-03-21 13:44:03', '0');
INSERT INTO `tb_department` VALUES ('11', '产品部', '2020-03-21 13:44:20', '0');
INSERT INTO `tb_department` VALUES ('12', '质量部', '2020-03-21 13:47:39', '0');

-- ----------------------------
-- Table structure for tb_employee
-- ----------------------------
DROP TABLE IF EXISTS `tb_employee`;
CREATE TABLE `tb_employee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '员工工号',
  `employee_name` varchar(255) NOT NULL COMMENT '员工姓名',
  `gender` varchar(255) NOT NULL COMMENT '员工性别: 1-男, 2-女',
  `birthday` datetime NOT NULL COMMENT '员工出生年月日',
  `department_id` bigint(20) NOT NULL COMMENT '员工所属部门 id',
  `department_name` varchar(255) NOT NULL COMMENT '员工所属部门名称',
  `job_id` bigint(20) NOT NULL COMMENT '员工职位id',
  `job_name` varchar(255) NOT NULL COMMENT '员工职位名称',
  `leader_id` bigint(20) DEFAULT NULL COMMENT '员工直属领导工号',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user` bigint(20) NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_employee
-- ----------------------------
INSERT INTO `tb_employee` VALUES ('1', '张三', '1', '2020-03-21 13:54:11', '1', '研发部', '2', '研发经理', null, '2020-03-21 13:54:58', '0');
INSERT INTO `tb_employee` VALUES ('2', '李四', '1', '2020-03-21 13:55:21', '1', '研发部', '2', '研发经理', null, '2020-03-21 13:55:36', '0');
INSERT INTO `tb_employee` VALUES ('3', '王五', '1', '2020-03-21 13:55:49', '1', '研发部', '2', '研发经理', null, '2020-03-21 13:56:03', '0');
INSERT INTO `tb_employee` VALUES ('4', '李丽', '0', '2020-03-21 13:56:17', '11', '产品部', '1', '产品经理', null, '2020-03-21 13:56:47', '0');
INSERT INTO `tb_employee` VALUES ('5', '陆琪', '0', '2020-03-21 13:57:06', '11', '产品部', '1', '产品经理', null, '2020-03-21 13:57:19', '0');
INSERT INTO `tb_employee` VALUES ('6', '陈莉莉', '0', '2020-03-21 13:57:44', '11', '产品部', '1', '产品经理', null, '2020-03-21 13:57:55', '0');
INSERT INTO `tb_employee` VALUES ('8', 'tom', '1', '1990-09-21 08:00:00', '1', '研发部', '2', '研发经理', null, '2020-03-21 16:10:35', '0');

-- ----------------------------
-- Table structure for tb_job
-- ----------------------------
DROP TABLE IF EXISTS `tb_job`;
CREATE TABLE `tb_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '职位id',
  `job_name` varchar(255) NOT NULL COMMENT '职位名称',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user` bigint(20) NOT NULL COMMENT '更新人工号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_job
-- ----------------------------
INSERT INTO `tb_job` VALUES ('1', '产品经理', '2020-03-21 13:39:08', '0');
INSERT INTO `tb_job` VALUES ('2', '研发经理', '2020-03-21 13:39:26', '0');
INSERT INTO `tb_job` VALUES ('3', '人力资源经理', '2020-03-21 13:40:10', '0');
INSERT INTO `tb_job` VALUES ('4', '薪酬经理', '2020-03-21 13:40:26', '0');
INSERT INTO `tb_job` VALUES ('5', '监察经理', '2020-03-21 13:40:48', '0');
INSERT INTO `tb_job` VALUES ('6', '行政经理', '2020-03-21 13:41:47', '0');
INSERT INTO `tb_job` VALUES ('7', '财务经理', '2020-03-21 13:45:43', '0');
INSERT INTO `tb_job` VALUES ('8', '市场经理', '2020-03-21 13:46:00', '0');
INSERT INTO `tb_job` VALUES ('9', '法务经理', '2020-03-21 13:46:33', '0');
INSERT INTO `tb_job` VALUES ('10', '专利经理', '2020-03-21 13:46:41', '0');
INSERT INTO `tb_job` VALUES ('11', '质量经理', '2020-03-21 13:46:53', '0');
INSERT INTO `tb_job` VALUES ('12', '售后服务经理', '2020-03-21 13:47:24', '0');

-- ----------------------------
-- Table structure for tb_performance
-- ----------------------------
DROP TABLE IF EXISTS `tb_performance`;
CREATE TABLE `tb_performance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '绩效id',
  `performance_year` varchar(255) NOT NULL COMMENT '绩效所在年份',
  `employee_id` bigint(11) NOT NULL COMMENT '员工工号',
  `q_degree` varchar(255) DEFAULT NULL COMMENT '年度绩效: 可自定义公式, 定时任务来计算',
  `q1_degree` varchar(255) DEFAULT NULL COMMENT 'q1绩效: A-优, B-良, C-中, D-差',
  `q2_degree` varchar(255) DEFAULT NULL COMMENT 'q2绩效: A-优, B-良, C-中, D-差',
  `q3_degree` varchar(255) DEFAULT NULL COMMENT 'q3绩效: A-优, B-良, C-中, D-差',
  `q4_degree` varchar(255) DEFAULT NULL COMMENT 'q4绩效: A-优, B-良, C-中, D-差',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_user` bigint(20) NOT NULL COMMENT '更新人工号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_performance
-- ----------------------------
INSERT INTO `tb_performance` VALUES ('1', '2020', '1', null, 'A', null, null, null, '2020-03-21 13:58:40', '0');
INSERT INTO `tb_performance` VALUES ('2', '2020', '2', null, 'B', null, null, null, '2020-03-21 13:58:59', '0');
INSERT INTO `tb_performance` VALUES ('3', '2020', '3', null, 'B', null, null, null, '2020-03-21 13:59:22', '0');
INSERT INTO `tb_performance` VALUES ('4', '2020', '4', null, 'A', null, null, null, '2020-03-21 13:59:46', '0');
INSERT INTO `tb_performance` VALUES ('5', '2020', '5', null, 'B', null, null, null, '2020-03-21 14:00:03', '0');
INSERT INTO `tb_performance` VALUES ('6', '2020', '6', null, 'B', null, null, null, '2020-03-21 14:00:16', '0');

-- ----------------------------
-- Table structure for tb_stu
-- ----------------------------
DROP TABLE IF EXISTS `tb_stu`;
CREATE TABLE `tb_stu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `age` int(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of tb_stu
-- ----------------------------
INSERT INTO `tb_stu` VALUES ('1', '婷婷', '20', 'female');
INSERT INTO `tb_stu` VALUES ('2', '饺子', '21', 'female');

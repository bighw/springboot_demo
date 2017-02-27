/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.5.9-log : Database - d_demo_card
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `t_card_info` */

CREATE TABLE `t_card_info` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_no` int(11) NOT NULL DEFAULT '0' COMMENT '批次号',
  `card_no` varchar(20) NOT NULL DEFAULT '' COMMENT '卡号',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`Id`),
  KEY `idx_cardno` (`card_no`),
  KEY `idx_createtime` (`create_at`)
) ENGINE=InnoDB AUTO_INCREMENT=160002 DEFAULT CHARSET=utf8;

/*Table structure for table `t_job_info` */

CREATE TABLE `t_job_info` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(100) NOT NULL DEFAULT '' COMMENT '任务名',
  `type` int(5) NOT NULL DEFAULT '0' COMMENT '业务类型 1：card',
  `batch` varchar(20) NOT NULL DEFAULT '' COMMENT '业务唯一标识',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '状态（-1 失败 0等待 1执行中 2被终止 9完成）',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='批处理任务表';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

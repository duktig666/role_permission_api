-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: localhost    Database: test
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `dept`
--

DROP TABLE IF EXISTS `dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `dept` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
  `pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '上级部门id（0L是顶级部门，默认为0）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除（0为未删除，1为删除，默认为0）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dept`
--

LOCK TABLES `dept` WRITE;
/*!40000 ALTER TABLE `dept` DISABLE KEYS */;
INSERT INTO `dept` VALUES (1,'河南科技学院',0,'2020-08-06 14:31:07','2020-08-06 14:31:07',_binary '\0'),(2,'信息工程学院',1,'2020-08-06 14:31:27','2020-08-06 14:31:27',_binary '\0');
/*!40000 ALTER TABLE `dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job`
--

DROP TABLE IF EXISTS `job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位名称',
  `sort` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '岗位排序（默认为0，不排序）',
  `dept_id` bigint(20) unsigned NOT NULL COMMENT '部门ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除（0为未删除，1为删除，默认为0）',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FKmvhj0rogastlctflsxf1d6k3i` (`dept_id`) USING BTREE COMMENT '根据部门id查询岗位信息'
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='岗位';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job`
--

LOCK TABLES `job` WRITE;
/*!40000 ALTER TABLE `job` DISABLE KEYS */;
INSERT INTO `job` VALUES (8,'人事专员',3,11,'2019-03-29 06:52:28','2020-08-06 05:48:47',_binary ''),(10,'产品经理',4,2,'2019-03-29 06:55:51','2020-08-06 05:48:47',_binary ''),(11,'全栈开发',2,2,'2019-03-31 05:39:30','2020-08-06 05:48:47',_binary ''),(12,'软件测试',5,2,'2019-03-31 05:39:43','2020-08-06 05:48:47',_binary ''),(13,'校团委主席',1,1,'2020-08-07 03:17:40','2020-08-07 03:17:40',_binary '\0');
/*!40000 ALTER TABLE `job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
  `component_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '-' COMMENT '组件名称',
  `component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '组件',
  `last_id` bigint(20) NOT NULL COMMENT '上级菜单ID（0为顶级菜单，默认为0）',
  `sort` int(11) DEFAULT NULL COMMENT '排序（默认为0，不排序）',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '图标',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '链接地址',
  `permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '权限',
  `type` tinyint(3) DEFAULT NULL COMMENT '类型',
  `is_link` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否外链（0为不外链，1为外链，默认为0）',
  `is_cache` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否缓存（0为不缓存，1位缓存，默认为0）',
  `is_hidden` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否隐藏（0为不隐藏，1为隐藏，默认为0）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更细时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除（0为未删除，1为删除，默认为0）',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FKqcf9gem97gqa5qjm4d3elcqt5` (`last_id`) USING BTREE COMMENT '根据上级菜单id，查询上层菜单信息'
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='系统菜单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (117,'系统管理','-',NULL,0,1,'system',NULL,NULL,NULL,_binary '\0',_binary '\0',_binary '\0','2020-08-07 13:56:52','2020-08-07 13:56:52',_binary '\0'),(118,'用户管理','system/user/index','User',1,2,'peoples',NULL,'user:list',NULL,_binary '\0',_binary '\0',_binary '\0','2020-08-07 13:56:52','2020-08-07 13:56:52',_binary '\0'),(119,'角色管理','system/role/index','Role',1,3,'role',NULL,'roles:list',NULL,_binary '\0',_binary '\0',_binary '\0','2020-08-07 13:56:52','2020-08-07 13:56:52',_binary '\0'),(120,'菜单管理','system/menu/index','Menu',1,4,'menu',NULL,'menu:list',NULL,_binary '\0',_binary '\0',_binary '\0','2020-08-07 13:56:52','2020-08-07 13:56:52',_binary '\0');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) NOT NULL COMMENT '角色名',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `data_scope` varchar(255) NOT NULL COMMENT '数据权限',
  `level` tinyint(3) unsigned NOT NULL COMMENT '角色级别',
  `permission` varchar(255) NOT NULL COMMENT '功能权限',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除（0为未删除，1为删除，默认为0）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'超级管理员','','全部',1,'admin','2020-08-06 14:27:58','2020-08-06 14:28:18',_binary '\0'),(2,'普通用户',NULL,'本级',2,'common','2020-08-06 14:27:58','2020-08-06 14:28:18',_binary '\0');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_dept`
--

DROP TABLE IF EXISTS `role_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `role_dept` (
  `role_id` bigint(20) unsigned NOT NULL COMMENT '角色id',
  `dept_id` bigint(20) unsigned NOT NULL COMMENT '部门id',
  PRIMARY KEY (`role_id`,`dept_id`) USING BTREE,
  KEY `FK7qg6itn5ajdoa9h9o78v9ksur` (`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='角色部门关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_dept`
--

LOCK TABLES `role_dept` WRITE;
/*!40000 ALTER TABLE `role_dept` DISABLE KEYS */;
INSERT INTO `role_dept` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `role_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_menu`
--

DROP TABLE IF EXISTS `role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `role_menu` (
  `menu_id` bigint(20) unsigned NOT NULL COMMENT '菜单ID',
  `role_id` bigint(20) unsigned NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`menu_id`,`role_id`) USING BTREE,
  KEY `FKcngg2qadojhi3a651a5adkvbq` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='角色菜单关联';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_menu`
--

LOCK TABLES `role_menu` WRITE;
/*!40000 ALTER TABLE `role_menu` DISABLE KEYS */;
INSERT INTO `role_menu` VALUES (117,1),(118,1),(119,1),(120,1);
/*!40000 ALTER TABLE `role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
  `sex` bit(1) NOT NULL DEFAULT b'0' COMMENT '性别（0为男，1为女，默认为0）',
  `phone` varchar(11) CHARACTER SET utf8 NOT NULL COMMENT '手机号',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `last_password_reset_time` timestamp NOT NULL COMMENT '最后密码修改时间',
  `dept_id` bigint(20) unsigned NOT NULL COMMENT '部门id',
  `job_id` bigint(20) unsigned NOT NULL COMMENT '岗位id',
  `open_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信小程序用户唯一标识',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除（0为未删除，1为删除，默认为0）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone_UNIQUE` (`phone`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `open_id_UNIQUE` (`open_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (3,'rsw','测试管理员',_binary '\0','15670533573','1487660836@qq.com',NULL,'$2a$10$HhxyGZy.ulf3RvAwaHUGb.k.2i9PBpv4YbLMJWp8pES7pPhTyRCF.','2020-08-06 15:12:41',1,13,'oOhYY5iT4zunqqgjb4XbNNqHeUm8','2020-08-06 15:12:56','2020-08-09 13:28:18',_binary '\0'),(4,'jqj','测试用户',_binary '\0','15611111111','14877777777@qq.com',NULL,'$2a$10$HhxyGZy.ulf3RvAwaHUGb.k.2i9PBpv4YbLMJWp8pES7pPhTyRCF.','2020-08-07 08:24:21',2,13,NULL,'2020-08-07 08:24:42','2020-08-07 08:26:00',_binary '\0');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-15 13:44:51

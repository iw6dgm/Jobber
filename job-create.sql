CREATE TABLE `project` (
  `project_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  `priority` smallint(6) DEFAULT '0',
  `status` smallint(6) DEFAULT '-1',
  PRIMARY KEY (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

CREATE TABLE `users` (
  `user_id` varchar(25) NOT NULL DEFAULT '',
  `password` varchar(50) NOT NULL DEFAULT '',
  `project_id` int(11) DEFAULT NULL,
  `name` varchar(125) DEFAULT '',
  `email` varchar(128) DEFAULT '',
  `status` int(11) DEFAULT NULL,
  `event_id` int(11) DEFAULT NULL,
  `portal_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`),
  KEY `fk_project_id3` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_projects` (
  `user_id` varchar(25) NOT NULL DEFAULT '',
  `project_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`project_id`,`user_id`),
  KEY `fk_user_id2` (`user_id`),
  CONSTRAINT `FK_user_projects_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_user_projects_2` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `user_jobs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(25) NOT NULL DEFAULT '',
  `project_id` int(11) NOT NULL DEFAULT '1',
  `startdate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `enddate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `status` smallint(6) DEFAULT NULL,
  `notes` varchar(225) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_id3` (`user_id`),
  KEY `fk_project_id2` (`project_id`),
  CONSTRAINT `FK_user_jobs_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FK_user_jobs_2` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

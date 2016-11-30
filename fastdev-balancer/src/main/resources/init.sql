#会员分区表
CREATE TABLE UserPartition(
	`userId` int auto_increment,
	`userName` varchar(50) NOT NULL,
	`merchant` varchar(50) NOT NULL,
	`partitionId` int ,
	 PRIMARY KEY (`userId`)
);


#分区信息表
CREATE TABLE PartitionInfo(
	`partitionId` int auto_increment,
	`merchant` varchar(50) NOT NULL,
	`userCount` int,
	 PRIMARY KEY (`partitionId`)
);

INSERT INTO PartitionInfo (partitionId,merchant,userCount) VALUES (1,'testm',0);
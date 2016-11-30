CREATE TABLE UserPartition(
	`userId` int auto_increment,
	`userName` varchar(50) NOT NULL,
	`merchant` varchar(50) NOT NULL,
	`partitionId` int,
	 PRIMARY KEY (`userId`)
);

CREATE TABLE PartitionInfo(
	`partitionId` int auto_increment,
	`merchant` varchar(50) NOT NULL,
	`userCount` int,
	 PRIMARY KEY (`partitionId`)
);
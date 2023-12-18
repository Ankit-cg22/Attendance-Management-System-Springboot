create table user(
	userid int primary key auto_increment ,
	firstname varchar(255) not null ,
	lastname varchar(255) ,
	email varchar(50) not null unique,
    password text not null ,
	role varchar(20) 
);

create table parent(
	parentid int primary key auto_increment ,
	childid int unique, 
	userid int ,
	foreign key (childid) references student(studentid) on delete set null ,
	foreign key (userid) references user(userid) on delete cascade 
);

create table student(
	studentId int primary key auto_increment ,
	userid int unique ,
	foreign key (userid) references user(userid) on delete cascade
);

create table course (
	courseid int primary key auto_increment ,
	coursetitle varchar(255) not null
);

create table enrollment (
	courseid int , 
	studentid int , 
	enrollmentdate date,
	enrollmentid int auto_increment unique , 
	primary key (courseid, studentid),
	foreign key (studentid) references student(studentid) on delete cascade ,
	foreign key (courseid) references course(courseid)	on delete cascade 
);

create table attendance(
	attendanceid int primary key auto_increment ,
	studentid int not null , 
	courseid int not null ,
	date date not null ,
	UNIQUE(studentid , courseid , date),
	foreign key ( courseid , studentid) references enrollment(courseid , studentid) on delete cascade
);

create table refresh_token (
	refreshToken varchar(255) primary key ,
	expiry datetime , 
	userid int unique,
	foreign key (userid) references user(userid) on delete cascade 
);
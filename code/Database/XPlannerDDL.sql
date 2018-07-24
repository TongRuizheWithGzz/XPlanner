drop table if exists user_food_eaten;
drop table if exists user_planner_settings;
drop table if exists sportsItem;
drop table if exists scheduleItem_from_website;
drop table if exists food;
drop table if exists food_type;
drop table if exists scheduleItme;
drop table if exists plannerStore;
drop table if exists user_role;
drop table if exists role;
drop table if exists JAccount_user;
drop table if exists Weixin_user;
drop table if exists keeper_recommand;
drop table if exists user;


CREATE TABLE user (
  user_id       INTEGER auto_increment,
  user_name     varchar(64) not null unique,
  user_password varchar(64),
  avatar_url    varchar(2056),
  enabled       boolean,
  last_keeper_fresh     datetime,

  PRIMARY KEY (user_id)
);

create table keeper_recommand(
  recommand_id    INTEGER auto_increment,
  user_id     INTEGER,
  start_time      DATETIME      not null,
  end_time        DATETIME      not null,
  title           varchar(1024) not null,
  description     varchar(1024),
  address         varchar(256),
  primary key (recommand_id),
  foreign key (user_id) references user (user_id)
);

create table JAccount_user (
  user_id       INTEGER,
  user_realName varchar(128),
  jaccount_name varchar(128),
  unique_id     varchar(128),
  student_id    varchar(128),
  class_number  varchar(128),
  access_token  varchar(256),
  refresh_token varchar(256),
  expires_in    timestamp,
  primary key (user_id),
  foreign key (user_id) references user (user_id)
);

create table Weixin_user (
  user_id   INTEGER,
  nick_name varchar(128),
  gender    enum ('0', '1') default '0',
  primary key (user_id),
  foreign key (user_id) references user (user_id)
);
create table role (
  role_id   INTEGER auto_increment,
  role_name varchar(64) not null unique,
  primary key (role_id)
);


create table user_role (
  user_role_id integer auto_increment,
  user_id      integer,
  role_id      integer,
  primary key (user_role_id),
  foreign key (user_id) references user (user_id),
  foreign key (role_id) references role (role_id)
);

create table food_type (
  food_type_id   integer auto_increment,
  food_type_name varchar(32),
  primary key (food_type_id)
);


CREATE TABLE food (
  food_id      INTEGER auto_increment,
  food_name    varchar(64) not null,
  calorie      INTEGER UNSIGNED,
  dininghall   varchar(64),
  food_type_id INTEGER,
  PRIMARY KEY (food_id),
  FOREIGN KEY (food_type_id) references food_type (food_type_id)
    on delete set null
    on UPDATE cascade
);


CREATE TABLE scheduleItme (
  scheduleItme_id INTEGER auto_increment,
  start_time      DATETIME      not null,
  end_time        DATETIME      not null,
  title           varchar(1024) not null,
  description     varchar(1024),
  address         varchar(256),
  user_id         INTEGER,
  completed       boolean       not null,
  imageUrl        varchar(1024),
  PRIMARY KEY (scheduleItme_id),
  FOREIGN KEY (user_id) REFERENCES user (user_id)
    on delete cascade
    on UPDATE cascade

);


create table plannerStore (
  planner_id        integer,
  planner_name      varchar(20),
  picture_path_name integer,
  description       varchar(1024),
  primary key (planner_id)
);


CREATE TABLE user_planner_settings (
  user_planner_settings_id INTEGER auto_increment,
  user_id                  INTEGER,
  plannerid                INTEGER,
  PRIMARY KEY (user_planner_settings_id),
  FOREIGN KEY (user_id) REFERENCES user (user_id)
    on delete cascade
    on update cascade,
  FOREIGN KEY (plannerid) references plannerStore (planner_id)
    on delete cascade
    on update cascade
);


CREATE TABLE sportsItem (
  sportsItem_id      INTEGER auto_increment,
  sport_name         varchar(64),
  calorie_per_minute INTEGER unsigned,
  description         varchar(1024),
  start_time      TIME      not null,
  end_time        TIME      not null,
  address            varchar(256),
  imageUrl           varchar(2048),
  PRIMARY KEY (sportsItem_id)
);

CREATE TABLE scheduleItem_from_website (
  schedule_time_from_website_id INTEGER auto_increment,
  publish_time                  DATETIME,
  url                           varchar(2048),
  description                   varchar(1024),
  start_time                    DATETIME,
  end_time                      DATETIME check (end_time >= start_time),
  address                       varchar(256),
  PRIMARY KEY (schedule_time_from_website_id)
);

CREATE TABLE user_food_eaten (
  user_food_eaten_id INTEGER auto_increment,
  user_id            Integer,
  food_name          varchar(64) not null,
  calorie            Integer unsigned,
  ate_time           DATETIME,
  PRIMARY KEY (user_food_eaten_id),
  FOREIGN KEY (user_id) REFERENCES user (user_id)
    on delete cascade
    on update cascade
);

# <-------------------  Planner Info  ------------------->

insert into plannerStore values (0, 'Reader', null, '随心所欲，以您喜爱的形式把新的待办事项告诉Reader。
无论是图片还是文字，Reader都会尽其所能梳理您的日程计划。');
insert into plannerStore values (1, 'Keeper', null, 'Keeper致力于为您提供健康管理服务。只需拍下您的一日三餐，
Keeper将据此为您量身定制锻炼计划，从此向肥宅生活Say Goodbye！');
insert into plannerStore values (2, 'Spider', null, '您曾经是否因为一时的忙碌而错过了来自校园网站的重要通知？是否因为处在百忙之中而
与重要活动的报名时间擦肩而过？是时候让Spider一展身手了！它将自动为您爬取网站最新发布的同时，让您的人生不再缺席。');

# <-------------------  Auth Info  ------------------->
#Test User
insert into role values (null, 'ROLE_JACCOUNT_USER');
insert into role values (null, 'ROLE_WEIXIN_USER');
insert into role values (null, 'ROLE_COMMON_USER');
insert into user values (NULL, 'tongruizhe', '$2a$10$ZSDkqq6AnVUQIwxrxaIiVevEGLTEhd7d8T8DosIXoPuTCEcyfWmQ2', null, 1,null );
insert into user values (NULL, 'lihu', '$2a$10$ZSDkqq6AnsVUQIwxrxaIiVevEGLTEhd7d8T8DosIXoPuTCEcyfWmQ2', null, 1,null );
#Give tongruizhe full authorities
insert into user_role values (null, 1, 3);
insert into user_role values (null, 1, 2);
insert into user_role values (null, 1, 1);

# <-------------------  Sports Info  ------------------->
insert into sportsItem values (null,'游泳',7,'游泳衣裤,泳帽,浴巾、拖鞋,备用衣裤','10:00:00','20:30:00','致远游泳馆',null);
insert into sportsItem values (null,'慢跑',10,'','06:00:00','23:30:00','南区体育馆操场',null);
insert into sportsItem values (null,'慢跑',10,'','06:00:00','21:00:00','光明体育场',null);
insert into sportsItem values (null,'打篮球',8,'','09:00:00','21:30:00','东转篮球场',null);
insert into sportsItem values (null,'骑自行车',11,'','06:00:00','23:30:00','校园',null);
insert into sportsItem values (null,'打乒乓球',6,'','09:00:00','21:30:00','南区体育馆',null);
insert into sportsItem values (null,'打羽毛球',12,'','09:00:00','21:30:00','霍英东体育馆',null);
insert into sportsItem values (null,'打高尔夫球',4,'','06:00:00','17:30:00','电院大草坪',null);


# <-------------------  Food Info  ------------------->
insert into food values (null,'炸鸡腿',261,'第五餐厅',null);
insert into food values (null,'炒蚕豆',134,'第五餐厅',null);
insert into food values (null,'香葱豆腐',53,'第五餐厅',null);
insert into food values (null,'麻婆豆腐',137,'第五餐厅',null);
insert into food values (null,'粉条',339,'第五餐厅',null);
insert into food values (null,'手撕包菜',37,'第五餐厅',null);

#<-------------------  user_food_eaten  ------------------->


#insert schedule itmes

drop table if exists user_food_eaten;
drop table if exists user_planner_settings;
drop table if exists sportsItem;
drop table if exists scheduleItem_from_website;
drop table if exists food;
drop table if exists food_type;
drop table if exists scheduleItme;
drop table if exists plannerStore;
drop table if exists user;

CREATE TABLE user (
  user_id       INTEGER auto_increment,
  user_name     varchar(20) not null unique,
  user_password varchar(20) not null,
  PRIMARY KEY (user_id)
);

create table food_type (
  food_type_id   integer auto_increment,
  food_type_name varchar(32),
  primary key (food_type_id)
);

CREATE TABLE food (
  food_id      INTEGER                                                              auto_increment,
  food_name    varchar(64) not null,
  calorie      INTEGER UNSIGNED,
  dininghall   enum ('第一餐厅', '第二餐厅', '第三餐厅', '第四餐厅', '第五餐厅', '第六餐厅', '哈乐餐厅', '大智居') default '第一餐厅',
  food_type_id INTEGER,
  PRIMARY KEY (food_id),
  FOREIGN KEY (food_type_id) references food_type (food_type_id)
    on delete set null
    on UPDATE cascade
);


CREATE TABLE scheduleItme (
  scheduleItme_id INTEGER auto_increment,
  start_time      DATETIME not null,
  end_time        DATETIME not null check (end_time >= start_time),
  description     varchar(1024),
  address         varchar(256),
  user_id         INTEGER,
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
  address            varchar(256),
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

# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table event (
  id                        bigint not null,
  event_name                varchar(255),
  constraint pk_event primary key (id))
;

create table event_picture (
  id                        bigint not null,
  event_id                  bigint not null,
  url                       varchar(255),
  constraint pk_event_picture primary key (id))
;

create table user (
  id                        bigint not null,
  password                  varchar(255),
  email                     varchar(255),
  confirmation_token        varchar(255),
  validated                 boolean,
  google_access_token       varchar(255),
  google_access_code        varchar(255),
  constraint pk_user primary key (id))
;


create table user_event (
  user_id                        bigint not null,
  event_id                       bigint not null,
  constraint pk_user_event primary key (user_id, event_id))
;
create sequence event_seq;

create sequence event_picture_seq;

create sequence user_seq;

alter table event_picture add constraint fk_event_picture_event_1 foreign key (event_id) references event (id) on delete restrict on update restrict;
create index ix_event_picture_event_1 on event_picture (event_id);



alter table user_event add constraint fk_user_event_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_event add constraint fk_user_event_event_02 foreign key (event_id) references event (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists event;

drop table if exists event_picture;

drop table if exists user;

drop table if exists user_event;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists event_seq;

drop sequence if exists event_picture_seq;

drop sequence if exists user_seq;


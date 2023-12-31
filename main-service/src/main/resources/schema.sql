DROP TABLE if exists categories, users, locations, events, requests, compilations, compilations_events, comments cascade;

create table if not exists categories (
    id bigint generated by default as identity not null,
    name varchar(50) not null,
    constraint pk_categories primary key (id),
    constraint up_categories_name unique (name)
);

create table if not exists users (
    id bigint generated by default as identity not null,
    name varchar(255) not null,
    email varchar(255) not null,
    constraint pk_users primary key (id),
    constraint uq_users_email unique (email)
);

create table if not exists locations (
    id bigint generated by default as identity not null,
    lat numeric not null,
    lon numeric not null,
    constraint pk_locations primary key (id)
);

create table if not exists events (
    id bigint generated by default as identity not null,
    annotation varchar(2000) not null,
    title varchar(120) not null,
    category_id bigint references categories(id),
    created_on timestamp without time zone not null,
    description varchar(7000) not null,
    event_date timestamp without time zone not null,
    initiator_id bigint references users(id),
    location_id bigint references locations(id),
    paid boolean not null,
    confirmed_requests bigint,
    participant_limit bigint,
    published_on timestamp without time zone,
    request_moderation boolean not null,
    state varchar(255) not null,
    constraint pk_events primary key (id)
);

create table if not exists compilations (
    id bigint generated by default as identity not null,
    pinned boolean not null,
    title varchar(255) not null,
    constraint pk_compilations primary key (id)
);

create table if not exists compilations_events (
    compilation_id bigint references compilations(id),
    event_id bigint references events(id)
);

create table if not exists requests (
    id bigint generated by default as identity not null,
    requester_id bigint references users(id),
    status varchar(20) not null,
    event_id bigint references events(id),
    created_date timestamp without time zone,
    constraint pk_requests primary key (id)
);

create table if not exists comments (
  id bigint generated by default as identity primary key,
  text varchar(2000),
  event_id bigint not null references events (id)  on delete cascade,
  author_id bigint not null references users (id) on delete cascade,
  created_date timestamp WITHOUT time zone not null,
  updated_date timestamp WITHOUT time zone default null,
  isupdated boolean not null
);
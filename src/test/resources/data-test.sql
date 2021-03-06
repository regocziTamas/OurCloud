create extension if not exists ltree;
--create sequence if not exists hibernate_sequence start 1 increment 1;
--create table if not exists file_system_element (dtype varchar(31) not null, id int8 not null, original_name varchar(255), relative_path ltree, size int8 not null, owner_id int8, parent_folder_id int8, primary key (id));
--create table if not exists ocuser (id int8 generated by default as identity, password varchar(255), role int4, used_bytes int8, username varchar(255), primary key (id));
--ALTER TABLE file_system_element DROP CONSTRAINT IF EXISTS FKm8t760k5p8cfs2ar6bssk4tyd;
--alter table if exists file_system_element add constraint FKm8t760k5p8cfs2ar6bssk4tyd foreign key (owner_id) references ocuser;
--ALTER TABLE file_system_element DROP CONSTRAINT IF EXISTS FK7xjrekt3vc2crp2bx590pc12e;
--alter table if exists file_system_element add constraint FK7xjrekt3vc2crp2bx590pc12e foreign key (parent_folder_id) references file_system_element;

--CREATE INDEX if not exists file_path_idx on public.file_system_element using gist (relative_path);
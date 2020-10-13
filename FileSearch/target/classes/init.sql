-- drop table if exists file_meta;

create table if not exists file_meta(-- 创建表操作 --如果不存在表创建这个表
    name varchar(50) not null,  --文件名称
    path varchar(1000) not null, --文件路径
    is_directory boolean not null, --判断是否是文件
    size bigint not null,  --文件大小
    last_modified timestamp not null, --最后一次更新时间 timestamp时间戳
    pinyin varchar(50), --拼音全拼
    pinyin_first varchar(50) --拼音首拼
);
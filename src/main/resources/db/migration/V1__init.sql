create table users (
    id            bigserial primary key,
    email         varchar(255) not null unique,
    password_hash varchar(255) not null,
    display_name  varchar(255) not null,
    role          varchar(20)  not null,
    created_at    timestamptz  not null default now()
);

create table problems (
    id             bigserial primary key,
    prompt         varchar(500) not null,
    correct_answer varchar(300) not null,
    type           varchar(30)  not null,
    topic          varchar(255) not null,
    hint           varchar(500),
    created_at     timestamptz  not null default now()
);

create table attempts (
    id               bigserial primary key,
    student_id       bigint       not null references users(id),
    problem_id       bigint       not null references problems(id),
    submitted_answer varchar(300) not null,
    correct          boolean      not null,
    created_at       timestamptz  not null default now()
);

-- The dashboard and the next-problem query both filter on student_id, and the
-- next-problem query joins on (problem_id, student_id). Without this index both
-- degrade to sequential scans once attempts grows.
create index idx_attempts_student on attempts (student_id);
create index idx_attempts_student_problem on attempts (student_id, problem_id);
create index idx_problems_topic on problems (topic);

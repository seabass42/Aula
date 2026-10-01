create table help_requests (
    id           bigserial primary key,
    student_id   bigint      not null references users(id),
    message      text        not null,
    reply        text,
    status       varchar(20) not null default 'OPEN',
    created_at   timestamptz not null default now(),
    replied_at   timestamptz
);

-- The tutor's queue filters on status, and a student's own history filters on
-- student_id -- both need an index or they degrade to sequential scans.
create index idx_help_requests_status on help_requests (status);
create index idx_help_requests_student on help_requests (student_id);

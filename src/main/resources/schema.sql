drop table public.users CASCADE;
drop table public.items CASCADE;

CREATE TABLE IF NOT EXISTS public.users
(
    user_id serial PRIMARY KEY,
    email   varchar(100) NOT NULL UNIQUE,
    name    varchar(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.items
(
    item_id     serial
        primary key,
    name        varchar(255) not null,
    description text         not null,
    available   boolean default true,
    owner_id    integer      not null
        constraint items_users_user_id_fk
            references public.users
);

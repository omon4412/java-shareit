drop table if exists public.users CASCADE;
drop table if exists public.items CASCADE;
drop table if exists public.bookings CASCADE;
drop table if exists public.comments CASCADE;

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

CREATE TABLE IF NOT EXISTS public.bookings
(
    booking_id serial
        primary key,
    start_date timestamp not null,
    end_date   timestamp not null,
    item_id    integer   not null
        constraint bookings_items_item_id_fk
            references public.items,
    booker_id  integer   not null
        constraint bookings_users_user_id_fk
            references public.users,
    status     varchar   not null
);

CREATE TABLE IF NOT EXISTS public.comments
(
    comment_id serial
        primary key,
    text       varchar(1000) not null,
    item_id    integer       not null
        constraint comments_items_item_id_fk
            references public.items,
    user_id    integer       not null
        constraint comments_users_user_id_fk
            references public.users,
    created    timestamp     not null
);
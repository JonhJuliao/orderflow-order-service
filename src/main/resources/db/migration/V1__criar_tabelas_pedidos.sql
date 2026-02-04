create table orders (
                        id uuid primary key,
                        customer_id uuid not null,
                        status varchar(30) not null,
                        total numeric(19,2) not null,
                        created_at timestamp not null
);

create table order_items (
                             id uuid primary key,
                             order_id uuid not null references orders(id),
                             product_id uuid not null,
                             quantity int not null,
                             unit_price numeric(19,2) not null
);

create index idx_order_items_order_id on order_items(order_id);

CREATE TABLE users (
	id int8 NOT NULL,
	created_at timestamp NULL,
	email varchar(255) NOT NULL,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	password_digest varchar(255) NOT NULL,
	updated_at timestamp NULL,
	CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE transactions (
	id int8 NOT NULL,
	created_at timestamp NULL,
	notes text NULL,
	title varchar(255) NOT NULL,
	total float4 NOT NULL,
	updated_at timestamp NULL,
	user_id int8 NULL,
	CONSTRAINT transactions_pkey PRIMARY KEY (id)
);

ALTER TABLE transactions ADD CONSTRAINT fkqwv7rmvc8va8rep7piikrojds FOREIGN KEY (user_id) REFERENCES public.users(id);

CREATE TABLE categories (
	id bigserial NOT NULL,
	"name" varchar(255) NULL,
	CONSTRAINT categories_pkey PRIMARY KEY (id)
);

CREATE TABLE roles (
	id bigserial NOT NULL,
	"name" varchar(20) NULL,
	CONSTRAINT roles_pkey PRIMARY KEY (id)
);

CREATE TABLE refreshtoken (
	id int8 NOT NULL,
	expiry_date timestamp NOT NULL,
	token varchar(255) NOT NULL,
	user_id int8 NULL,
	CONSTRAINT refreshtoken_pkey PRIMARY KEY (id),
	CONSTRAINT uk_or156wbneyk8noo4jstv55ii3 UNIQUE (token)
);

ALTER TABLE refreshtoken ADD CONSTRAINT fka652xrdji49m4isx38pp4p80p FOREIGN KEY (user_id) REFERENCES public.users(id);

CREATE TABLE transaction_categories (
	transaction_id int8 NOT NULL,
	category_id int8 NOT NULL,
	CONSTRAINT transaction_categories_pkey PRIMARY KEY (transaction_id, category_id)
);

ALTER TABLE transaction_categories ADD CONSTRAINT fk4w5cqbyyhe713v88k32809a5i FOREIGN KEY (transaction_id) REFERENCES public.transactions(id);
ALTER TABLE transaction_categories ADD CONSTRAINT fkl8nhemttklbafivosqgyv35rl FOREIGN KEY (category_id) REFERENCES public.categories(id);

CREATE TABLE user_roles (
	user_id int8 NOT NULL,
	role_id int8 NOT NULL,
	CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id)
);

ALTER TABLE user_roles ADD CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES public.roles(id);
ALTER TABLE user_roles ADD CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES public.users(id);
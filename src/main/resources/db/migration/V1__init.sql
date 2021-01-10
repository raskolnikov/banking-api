CREATE TABLE IF NOT EXISTS customers (
             id bigint NOT NULL,
             first_name character varying(50) NOT NULL,
             last_name character varying(50) NOT NULL,
             created_at timestamp NOT NULL,
             updated_at timestamp NOT NULL,
             CONSTRAINT customers_pk PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS accounts (
			id bigint NOT NULL,
			name character varying(50) NOT NULL,
			customer_id bigint NOT NULL REFERENCES customers(id) ON DELETE RESTRICT,
			balance bigint NOT NULL DEFAULT 0,
			created_at timestamp NOT NULL,
			updated_at timestamp NOT NULL,
			CONSTRAINT accounts_pk PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS balance_transactions (
             id bigint NOT NULL,
             amount bigint NOT NULL,
             balance_after bigint NOT NULL,
             description character varying(100),
             account_id bigint NOT NULL REFERENCES accounts(id) ON DELETE RESTRICT,
             source_id bigint,
             source_type character varying(50) NOT NULL,
             created_at timestamp NOT NULL,
             updated_at timestamp NOT NULL,
             CONSTRAINT balance_transactions_pk PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS transfers (
             id bigint NOT NULL,
             amount bigint NOT NULL,
             account_id bigint NOT NULL REFERENCES accounts(id) ON DELETE RESTRICT,
             destination_account_id bigint NOT NULL REFERENCES accounts(id) ON DELETE RESTRICT,
             description character varying(100),
             created_at timestamp NOT NULL,
             updated_at timestamp NOT NULL,
             CONSTRAINT transfers_pk PRIMARY KEY (id));











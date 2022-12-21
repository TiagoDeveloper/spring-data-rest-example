CREATE TABLE customer (
	id varchar(255) NOT NULL,
	name varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	active bool NULL DEFAULT true,
	birth_date date NOT NULL,
	CONSTRAINT customer_pkey PRIMARY KEY (id)
);

INSERT INTO customer
(id, "name", email, active, birth_date)
VALUES('e6f150c7-d7d6-47ac-96f7-8195bfba906e', 'Tânia Pereira da Silva', 'taniapereiradasilva@gmail.com', true, '2001-10-11');

INSERT INTO customer
(id, "name", email, active, birth_date)
VALUES('8215bfb7-875a-4712-a1bb-27cefd0c2053', 'To deleted', 'todeleted@gmail.com', true, '2001-10-11');

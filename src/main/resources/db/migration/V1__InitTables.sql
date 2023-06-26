CREATE TABLE movie (
    id BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    release_date DATE NOT NULL,
    unique (name)
);

CREATE TABLE actor (
    id bigserial primary key,
    name TEXT NOT NULL,
    unique (name)
);


CREATE TABLE actor_movie (
     id BIGSERIAL PRIMARY KEY,
     moveId BIGINT REFERENCES movie (id),
     actorId BIGINT REFERENCES actor (id)
);
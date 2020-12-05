DROP TABLE IF EXISTS byn_usd_rate;

CREATE TABLE byn_usd_rate
(
    id        bigint(20)     NOT NULL,
    curr_abbr varchar(255)   DEFAULT NULL,
    date      date           DEFAULT NULL,
    sum       decimal(19, 2) DEFAULT NULL,
    PRIMARY KEY (id)
);

INSERT INTO byn_usd_rate
VALUES (1, 'USD', '2020-12-05', 2.57);

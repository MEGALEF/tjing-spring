--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.0
-- Dumped by pg_dump version 9.4.0
-- Started on 2015-06-23 10:53:48

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 2099 (class 1262 OID 16393)
-- Name: myapp; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE myapp WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Swedish_Sweden.1252' LC_CTYPE = 'Swedish_Sweden.1252';


ALTER DATABASE myapp OWNER TO postgres;

\connect myapp

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 186 (class 3079 OID 11855)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2102 (class 0 OID 0)
-- Dependencies: 186
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 172 (class 1259 OID 84197)
-- Name: condition; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE condition (
    id integer NOT NULL,
    creation_time timestamp without time zone NOT NULL,
    modification_time timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    whatever character varying(255),
    share integer
);


ALTER TABLE condition OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 84390)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO postgres;

--
-- TOC entry 173 (class 1259 OID 84202)
-- Name: interaction; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE interaction (
    id integer NOT NULL,
    creation_time timestamp without time zone NOT NULL,
    modification_time timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    active boolean,
    status_accepted timestamp without time zone,
    status_cancelled timestamp without time zone,
    status_handed_over timestamp without time zone,
    status_requested timestamp without time zone,
    status_returned timestamp without time zone,
    borrower integer,
    borrower_rating integer,
    item integer,
    owner_rating integer
);


ALTER TABLE interaction OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 91096)
-- Name: interaction_message; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE interaction_message (
    id integer NOT NULL,
    creation_time timestamp without time zone NOT NULL,
    modification_time timestamp without time zone NOT NULL,
    version integer NOT NULL,
    text character varying(255),
    interaction integer,
    is_system_message boolean NOT NULL
);


ALTER TABLE interaction_message OWNER TO postgres;

--
-- TOC entry 174 (class 1259 OID 84207)
-- Name: item; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE item (
    id integer NOT NULL,
    creation_time timestamp without time zone NOT NULL,
    modification_time timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    description character varying(255),
    title character varying(255),
    active_interaction integer,
    owner integer,
    fb_available boolean
);


ALTER TABLE item OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 91078)
-- Name: item_picture; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE item_picture (
    id integer NOT NULL,
    creation_time timestamp without time zone NOT NULL,
    modification_time timestamp without time zone NOT NULL,
    version integer NOT NULL,
    data bytea,
    item integer
);


ALTER TABLE item_picture OWNER TO postgres;

--
-- TOC entry 175 (class 1259 OID 84215)
-- Name: item_request; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE item_request (
    id integer NOT NULL,
    creation_time timestamp without time zone NOT NULL,
    modification_time timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    text character varying(255),
    member integer
);


ALTER TABLE item_request OWNER TO postgres;

--
-- TOC entry 176 (class 1259 OID 84220)
-- Name: membership; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE membership (
    id integer NOT NULL,
    creation_time timestamp without time zone NOT NULL,
    modification_time timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    approved boolean,
    member integer,
    notify_pool integer,
    notify_user integer,
    pool integer,
    hidden boolean,
    administrator boolean
);


ALTER TABLE membership OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 91053)
-- Name: notification; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE notification (
    id integer NOT NULL,
    creation_time timestamp without time zone NOT NULL,
    modification_time timestamp without time zone NOT NULL,
    version integer NOT NULL,
    message character varying(255),
    type integer,
    interaction integer,
    itemrequest integer,
    membership integer,
    target integer
);


ALTER TABLE notification OWNER TO postgres;

--
-- TOC entry 177 (class 1259 OID 84240)
-- Name: person; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE person (
    id integer NOT NULL,
    creation_time timestamp without time zone NOT NULL,
    modification_time timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    first_name character varying(255),
    full_name character varying(255),
    last_name character varying(255),
    password character varying(255),
    username character varying(255)
);


ALTER TABLE person OWNER TO postgres;

--
-- TOC entry 178 (class 1259 OID 84248)
-- Name: pool; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE pool (
    id integer NOT NULL,
    creation_time timestamp without time zone NOT NULL,
    modification_time timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    approval integer,
    privacy integer,
    title character varying(255),
    facebook_id bigint
);


ALTER TABLE pool OWNER TO postgres;

--
-- TOC entry 179 (class 1259 OID 84253)
-- Name: rating; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE rating (
    id integer NOT NULL,
    creation_time timestamp without time zone NOT NULL,
    modification_time timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    text character varying(255),
    interaction integer,
    grade integer
);


ALTER TABLE rating OWNER TO postgres;

--
-- TOC entry 180 (class 1259 OID 84258)
-- Name: share; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE share (
    id integer NOT NULL,
    creation_time timestamp without time zone NOT NULL,
    modification_time timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    condition integer,
    item integer,
    pool integer
);


ALTER TABLE share OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 84263)
-- Name: userconnection; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE userconnection (
    providerid character varying(255) NOT NULL,
    provideruserid character varying(255) NOT NULL,
    userid character varying(255) NOT NULL,
    accesstoken character varying(255) NOT NULL,
    displayname character varying(255),
    expiretime bigint,
    imageurl character varying(255),
    profileurl character varying(255),
    rank integer,
    refreshtoken character varying(255),
    secret character varying(255),
    person integer
);


ALTER TABLE userconnection OWNER TO postgres;

--
-- TOC entry 1934 (class 2606 OID 84201)
-- Name: condition_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY condition
    ADD CONSTRAINT condition_pkey PRIMARY KEY (id);


--
-- TOC entry 1962 (class 2606 OID 91100)
-- Name: interaction_message_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY interaction_message
    ADD CONSTRAINT interaction_message_pkey PRIMARY KEY (id);


--
-- TOC entry 1936 (class 2606 OID 84206)
-- Name: interaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY interaction
    ADD CONSTRAINT interaction_pkey PRIMARY KEY (id);


--
-- TOC entry 1960 (class 2606 OID 91085)
-- Name: item_picture_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY item_picture
    ADD CONSTRAINT item_picture_pkey PRIMARY KEY (id);


--
-- TOC entry 1938 (class 2606 OID 84214)
-- Name: item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY item
    ADD CONSTRAINT item_pkey PRIMARY KEY (id);


--
-- TOC entry 1940 (class 2606 OID 84219)
-- Name: item_request_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY item_request
    ADD CONSTRAINT item_request_pkey PRIMARY KEY (id);


--
-- TOC entry 1942 (class 2606 OID 84224)
-- Name: membership_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY membership
    ADD CONSTRAINT membership_pkey PRIMARY KEY (id);


--
-- TOC entry 1958 (class 2606 OID 91057)
-- Name: notification_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- TOC entry 1946 (class 2606 OID 84247)
-- Name: person_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY person
    ADD CONSTRAINT person_pkey PRIMARY KEY (id);


--
-- TOC entry 1948 (class 2606 OID 84252)
-- Name: pool_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY pool
    ADD CONSTRAINT pool_pkey PRIMARY KEY (id);


--
-- TOC entry 1950 (class 2606 OID 84257)
-- Name: rating_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY rating
    ADD CONSTRAINT rating_pkey PRIMARY KEY (id);


--
-- TOC entry 1952 (class 2606 OID 84262)
-- Name: share_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY share
    ADD CONSTRAINT share_pkey PRIMARY KEY (id);


--
-- TOC entry 1954 (class 2606 OID 84274)
-- Name: uk_38ac39auoyh4ax8gno1coshcg; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY share
    ADD CONSTRAINT uk_38ac39auoyh4ax8gno1coshcg UNIQUE (pool, item);


--
-- TOC entry 1944 (class 2606 OID 84272)
-- Name: uk_e39fyvq0rk0sf88rdqpmidpoq; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY membership
    ADD CONSTRAINT uk_e39fyvq0rk0sf88rdqpmidpoq UNIQUE (pool, member);


--
-- TOC entry 1956 (class 2606 OID 84270)
-- Name: userconnection_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY userconnection
    ADD CONSTRAINT userconnection_pkey PRIMARY KEY (providerid, provideruserid, userid);


--
-- TOC entry 1963 (class 2606 OID 84275)
-- Name: fk_146k51cn50okxtg63r9o4cf97; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY condition
    ADD CONSTRAINT fk_146k51cn50okxtg63r9o4cf97 FOREIGN KEY (share) REFERENCES share(id);


--
-- TOC entry 1977 (class 2606 OID 84375)
-- Name: fk_1ahng1x8ehpvsd6mbu04eh3q3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY share
    ADD CONSTRAINT fk_1ahng1x8ehpvsd6mbu04eh3q3 FOREIGN KEY (item) REFERENCES item(id);


--
-- TOC entry 1979 (class 2606 OID 84385)
-- Name: fk_42imqkj39iwwfpebf7xvvwqut; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY userconnection
    ADD CONSTRAINT fk_42imqkj39iwwfpebf7xvvwqut FOREIGN KEY (person) REFERENCES person(id);


--
-- TOC entry 1968 (class 2606 OID 84300)
-- Name: fk_5l300omjldqow9pu7bvdor5ui; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item
    ADD CONSTRAINT fk_5l300omjldqow9pu7bvdor5ui FOREIGN KEY (active_interaction) REFERENCES interaction(id);


--
-- TOC entry 1967 (class 2606 OID 84295)
-- Name: fk_6vl9ima13qdqkm59alnfle63o; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY interaction
    ADD CONSTRAINT fk_6vl9ima13qdqkm59alnfle63o FOREIGN KEY (owner_rating) REFERENCES rating(id);


--
-- TOC entry 1982 (class 2606 OID 91068)
-- Name: fk_7giyixc820n73rt6e4gy4bjjt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY notification
    ADD CONSTRAINT fk_7giyixc820n73rt6e4gy4bjjt FOREIGN KEY (membership) REFERENCES membership(id);


--
-- TOC entry 1971 (class 2606 OID 84315)
-- Name: fk_822aofg0tc28jhmukv1gv189c; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY membership
    ADD CONSTRAINT fk_822aofg0tc28jhmukv1gv189c FOREIGN KEY (member) REFERENCES person(id);


--
-- TOC entry 1976 (class 2606 OID 84370)
-- Name: fk_b14doyqhsjdpk2i549g77rq5w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY share
    ADD CONSTRAINT fk_b14doyqhsjdpk2i549g77rq5w FOREIGN KEY (condition) REFERENCES condition(id);


--
-- TOC entry 1980 (class 2606 OID 91058)
-- Name: fk_bgqaqjdwxrhf0f7rtuh33lanf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY notification
    ADD CONSTRAINT fk_bgqaqjdwxrhf0f7rtuh33lanf FOREIGN KEY (interaction) REFERENCES interaction(id);


--
-- TOC entry 1970 (class 2606 OID 84310)
-- Name: fk_cagtjnqjtpq0s5kcrnopn0eoh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_request
    ADD CONSTRAINT fk_cagtjnqjtpq0s5kcrnopn0eoh FOREIGN KEY (member) REFERENCES person(id);


--
-- TOC entry 1985 (class 2606 OID 91101)
-- Name: fk_ecl5bqj0bulwt5ujlg6365ke; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY interaction_message
    ADD CONSTRAINT fk_ecl5bqj0bulwt5ujlg6365ke FOREIGN KEY (interaction) REFERENCES interaction(id);


--
-- TOC entry 1984 (class 2606 OID 91086)
-- Name: fk_eik9oarclrhbnn3feul3snvh4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_picture
    ADD CONSTRAINT fk_eik9oarclrhbnn3feul3snvh4 FOREIGN KEY (item) REFERENCES item(id);


--
-- TOC entry 1969 (class 2606 OID 84305)
-- Name: fk_h9m3dcsxt8442gyeenlm3ft6y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item
    ADD CONSTRAINT fk_h9m3dcsxt8442gyeenlm3ft6y FOREIGN KEY (owner) REFERENCES person(id);


--
-- TOC entry 1974 (class 2606 OID 84330)
-- Name: fk_i5griffqw19mvfw3x9a6guipf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY membership
    ADD CONSTRAINT fk_i5griffqw19mvfw3x9a6guipf FOREIGN KEY (pool) REFERENCES pool(id);


--
-- TOC entry 1972 (class 2606 OID 84320)
-- Name: fk_j48agmtbi3crs6blaf5v7qhp6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY membership
    ADD CONSTRAINT fk_j48agmtbi3crs6blaf5v7qhp6 FOREIGN KEY (notify_pool) REFERENCES pool(id);


--
-- TOC entry 1964 (class 2606 OID 84280)
-- Name: fk_jna6gks9jmttn61rmtk33kyr7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY interaction
    ADD CONSTRAINT fk_jna6gks9jmttn61rmtk33kyr7 FOREIGN KEY (borrower) REFERENCES person(id);


--
-- TOC entry 1978 (class 2606 OID 84380)
-- Name: fk_jsa8ubfhka8gwon3cr37n0rr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY share
    ADD CONSTRAINT fk_jsa8ubfhka8gwon3cr37n0rr FOREIGN KEY (pool) REFERENCES pool(id);


--
-- TOC entry 1973 (class 2606 OID 84325)
-- Name: fk_l7nbf4cf9vdo4gwh92smbu8p5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY membership
    ADD CONSTRAINT fk_l7nbf4cf9vdo4gwh92smbu8p5 FOREIGN KEY (notify_user) REFERENCES person(id);


--
-- TOC entry 1981 (class 2606 OID 91063)
-- Name: fk_ljrl3qr01uu7ymx1ogolqf093; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY notification
    ADD CONSTRAINT fk_ljrl3qr01uu7ymx1ogolqf093 FOREIGN KEY (itemrequest) REFERENCES item_request(id);


--
-- TOC entry 1966 (class 2606 OID 84290)
-- Name: fk_n7s49ewgwjaoajud56e4he7qk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY interaction
    ADD CONSTRAINT fk_n7s49ewgwjaoajud56e4he7qk FOREIGN KEY (item) REFERENCES item(id);


--
-- TOC entry 1975 (class 2606 OID 84365)
-- Name: fk_nrrue4ugbqduga6hp1vuqnqyj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rating
    ADD CONSTRAINT fk_nrrue4ugbqduga6hp1vuqnqyj FOREIGN KEY (interaction) REFERENCES interaction(id);


--
-- TOC entry 1983 (class 2606 OID 91073)
-- Name: fk_t3a9d9lc3dsxoh8xbikdjq4sj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY notification
    ADD CONSTRAINT fk_t3a9d9lc3dsxoh8xbikdjq4sj FOREIGN KEY (target) REFERENCES person(id);


--
-- TOC entry 1965 (class 2606 OID 84285)
-- Name: fk_t4domjn2jm6vjgg6tnw2yps67; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY interaction
    ADD CONSTRAINT fk_t4domjn2jm6vjgg6tnw2yps67 FOREIGN KEY (borrower_rating) REFERENCES rating(id);


--
-- TOC entry 2101 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2015-06-23 10:53:49

--
-- PostgreSQL database dump complete
--


--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.0
-- Dumped by pg_dump version 9.4.0
-- Started on 2015-02-16 12:55:11

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

--
-- TOC entry 2030 (class 0 OID 58188)
-- Dependencies: 172
-- Data for Name: condition; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2032 (class 0 OID 58198)
-- Dependencies: 174
-- Data for Name: item; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO item VALUES (2, '2015-02-16 10:14:49.514', '2015-02-16 10:14:49.514', 0, 'borkborbk', 'såg', 1);
INSERT INTO item VALUES (3, '2015-02-16 10:14:58.242', '2015-02-16 10:14:58.242', 0, 'borkborbk', 'borr', 1);
INSERT INTO item VALUES (4, '2015-02-16 10:15:06.174', '2015-02-16 10:15:06.174', 0, 'borkborbk', 'stol', 1);


--
-- TOC entry 2031 (class 0 OID 58193)
-- Dependencies: 173
-- Data for Name: interaction; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2034 (class 0 OID 58219)
-- Dependencies: 177
-- Data for Name: pool; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO pool VALUES (5, '2015-02-16 10:16:07.874', '2015-02-16 10:16:07.874', 0, 'lund');
INSERT INTO pool VALUES (6, '2015-02-16 10:16:18.086', '2015-02-16 10:16:18.086', 0, 'malmö');
INSERT INTO pool VALUES (7, '2015-02-16 10:16:24.874', '2015-02-16 10:16:24.874', 0, 'bjärred');


--
-- TOC entry 2033 (class 0 OID 58206)
-- Dependencies: 175
-- Data for Name: membership; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO membership VALUES (8, '2015-02-16 11:47:36.856', '2015-02-16 11:47:36.856', 0, 1, 6);


--
-- TOC entry 2035 (class 0 OID 58224)
-- Dependencies: 178
-- Data for Name: share; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO share VALUES (9, '2015-02-16 11:53:16.036', '2015-02-16 11:53:16.036', 0, NULL, 2, 6);


-- Completed on 2015-02-16 12:55:11

--
-- PostgreSQL database dump complete
--


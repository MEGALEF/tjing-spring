--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.0
-- Dumped by pg_dump version 9.4.0
-- Started on 2015-02-16 11:20:45

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

--
-- TOC entry 2039 (class 0 OID 58099)
-- Dependencies: 172
-- Data for Name: condition; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2051 (class 0 OID 0)
-- Dependencies: 179
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 7, true);


--
-- TOC entry 2043 (class 0 OID 58122)
-- Dependencies: 176
-- Data for Name: person; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO person VALUES (1, '2015-02-16 10:13:44.797', '2015-02-16 10:13:44.797', 0, 'qwerqwer', 'Johannes', 'Andersson', 'qwerqwer');


--
-- TOC entry 2041 (class 0 OID 58109)
-- Dependencies: 174
-- Data for Name: item; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO item VALUES (2, '2015-02-16 10:14:49.514', '2015-02-16 10:14:49.514', 0, 'borkborbk', 'såg', 1);
INSERT INTO item VALUES (3, '2015-02-16 10:14:58.242', '2015-02-16 10:14:58.242', 0, 'borkborbk', 'borr', 1);
INSERT INTO item VALUES (4, '2015-02-16 10:15:06.174', '2015-02-16 10:15:06.174', 0, 'borkborbk', 'stol', 1);


--
-- TOC entry 2040 (class 0 OID 58104)
-- Dependencies: 173
-- Data for Name: interaction; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2044 (class 0 OID 58130)
-- Dependencies: 177
-- Data for Name: pool; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO pool VALUES (5, '2015-02-16 10:16:07.874', '2015-02-16 10:16:07.874', 0, 'lund');
INSERT INTO pool VALUES (6, '2015-02-16 10:16:18.086', '2015-02-16 10:16:18.086', 0, 'malmö');
INSERT INTO pool VALUES (7, '2015-02-16 10:16:24.874', '2015-02-16 10:16:24.874', 0, 'bjärred');


--
-- TOC entry 2042 (class 0 OID 58117)
-- Dependencies: 175
-- Data for Name: membership; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2045 (class 0 OID 58135)
-- Dependencies: 178
-- Data for Name: share; Type: TABLE DATA; Schema: public; Owner: postgres
--



-- Completed on 2015-02-16 11:20:45

--
-- PostgreSQL database dump complete
--


--
-- PostgreSQL database cluster dump
--

-- Started on 2024-10-31 17:12:43

SET default_transaction_read_only = off;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

--
-- Roles
--

CREATE ROLE postgres;
ALTER ROLE postgres WITH SUPERUSER INHERIT CREATEROLE CREATEDB LOGIN REPLICATION BYPASSRLS PASSWORD 'SCRAM-SHA-256$4096:32c0lDBGwtn/tDxNQdfowQ==$WsXhD0dcvrVTmyr26RvGAkUD0nVYsfEfXGhENwSCIjY=:RiRqvkRcZymjbS8yf3ZM3oUaup6++WOf1Zrag8so7JY=';

--
-- User Configurations
--








--
-- Databases
--

--
-- Database "template1" dump
--

\connect template1

--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

-- Started on 2024-10-31 17:12:43

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

-- Completed on 2024-10-31 17:12:44

--
-- PostgreSQL database dump complete
--

--
-- Database "postgres" dump
--

\connect postgres

--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

-- Started on 2024-10-31 17:12:44

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 7 (class 2615 OID 16704)
-- Name: pro16; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA pro16;


ALTER SCHEMA pro16 OWNER TO postgres;

--
-- TOC entry 2 (class 3079 OID 16384)
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- TOC entry 4916 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


--
-- TOC entry 233 (class 1255 OID 16672)
-- Name: check_library_card(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.check_library_card() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
    if not exists (select 1 from customer where custid = new.custid and librarycard = true) then
        raise exception 'customer must have a library card';
    end if;
    return new;
end;
$$;


ALTER FUNCTION public.check_library_card() OWNER TO postgres;

--
-- TOC entry 232 (class 1255 OID 16670)
-- Name: decrease_book_count_on_borrow(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.decrease_book_count_on_borrow() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
    update book set bookcount = bookcount - 1 where bookid = new.bookid;
    return new;
end;
$$;


ALTER FUNCTION public.decrease_book_count_on_borrow() OWNER TO postgres;

--
-- TOC entry 231 (class 1255 OID 16668)
-- Name: decrease_book_count_on_purchase(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.decrease_book_count_on_purchase() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
    update book set bookcount = bookcount - 1 where bookid = new.bookid;
    return new;
end;
$$;


ALTER FUNCTION public.decrease_book_count_on_purchase() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 230 (class 1259 OID 16705)
-- Name: pro; Type: TABLE; Schema: pro16; Owner: postgres
--

CREATE TABLE pro16.pro (
    description character varying(255),
    created_at timestamp without time zone
);


ALTER TABLE pro16.pro OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 16631)
-- Name: author; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.author (
    authid integer NOT NULL,
    authfname character varying(20) NOT NULL,
    authlname character varying(20) NOT NULL,
    authcountry character varying(20)
);


ALTER TABLE public.author OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16630)
-- Name: author_authid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.author_authid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.author_authid_seq OWNER TO postgres;

--
-- TOC entry 4917 (class 0 OID 0)
-- Dependencies: 225
-- Name: author_authid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.author_authid_seq OWNED BY public.author.authid;


--
-- TOC entry 218 (class 1259 OID 16603)
-- Name: book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book (
    bookid integer NOT NULL,
    bookname character varying(20) NOT NULL,
    bookavailable boolean NOT NULL,
    bookcount integer,
    empid integer,
    pubid integer,
    authid integer
);


ALTER TABLE public.book OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16602)
-- Name: book_bookid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_bookid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.book_bookid_seq OWNER TO postgres;

--
-- TOC entry 4918 (class 0 OID 0)
-- Dependencies: 217
-- Name: book_bookid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_bookid_seq OWNED BY public.book.bookid;


--
-- TOC entry 229 (class 1259 OID 16647)
-- Name: borrows; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.borrows (
    custid integer NOT NULL,
    bookid integer NOT NULL,
    startdate date,
    enddate date GENERATED ALWAYS AS ((startdate + 10)) STORED
);


ALTER TABLE public.borrows OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16617)
-- Name: customer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.customer (
    custid integer NOT NULL,
    custfname character varying(20),
    custlname character varying(20),
    librarycard boolean,
    custcontact character varying(20)
);


ALTER TABLE public.customer OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16616)
-- Name: customer_custid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.customer_custid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.customer_custid_seq OWNER TO postgres;

--
-- TOC entry 4919 (class 0 OID 0)
-- Dependencies: 221
-- Name: customer_custid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.customer_custid_seq OWNED BY public.customer.custid;


--
-- TOC entry 220 (class 1259 OID 16610)
-- Name: employee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employee (
    empid integer NOT NULL,
    empfname character varying(20) NOT NULL,
    emplname character varying(20) NOT NULL,
    empsalary double precision
);


ALTER TABLE public.employee OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16609)
-- Name: employee_empid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.employee_empid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.employee_empid_seq OWNER TO postgres;

--
-- TOC entry 4920 (class 0 OID 0)
-- Dependencies: 219
-- Name: employee_empid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.employee_empid_seq OWNED BY public.employee.empid;


--
-- TOC entry 227 (class 1259 OID 16637)
-- Name: feedback; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.feedback (
    description character varying(100) NOT NULL,
    rating_10 integer NOT NULL,
    custid integer
);


ALTER TABLE public.feedback OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16624)
-- Name: publisher; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.publisher (
    pubid integer NOT NULL,
    pubname character varying(20),
    pubcountry character varying(20)
);


ALTER TABLE public.publisher OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16623)
-- Name: publisher_pubid_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.publisher_pubid_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.publisher_pubid_seq OWNER TO postgres;

--
-- TOC entry 4921 (class 0 OID 0)
-- Dependencies: 223
-- Name: publisher_pubid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.publisher_pubid_seq OWNED BY public.publisher.pubid;


--
-- TOC entry 228 (class 1259 OID 16642)
-- Name: purchases; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.purchases (
    custid integer NOT NULL,
    bookid integer NOT NULL
);


ALTER TABLE public.purchases OWNER TO postgres;

--
-- TOC entry 4733 (class 2604 OID 16634)
-- Name: author authid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author ALTER COLUMN authid SET DEFAULT nextval('public.author_authid_seq'::regclass);


--
-- TOC entry 4729 (class 2604 OID 16606)
-- Name: book bookid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book ALTER COLUMN bookid SET DEFAULT nextval('public.book_bookid_seq'::regclass);


--
-- TOC entry 4731 (class 2604 OID 16620)
-- Name: customer custid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer ALTER COLUMN custid SET DEFAULT nextval('public.customer_custid_seq'::regclass);


--
-- TOC entry 4730 (class 2604 OID 16613)
-- Name: employee empid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee ALTER COLUMN empid SET DEFAULT nextval('public.employee_empid_seq'::regclass);


--
-- TOC entry 4732 (class 2604 OID 16627)
-- Name: publisher pubid; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.publisher ALTER COLUMN pubid SET DEFAULT nextval('public.publisher_pubid_seq'::regclass);


--
-- TOC entry 4910 (class 0 OID 16705)
-- Dependencies: 230
-- Data for Name: pro; Type: TABLE DATA; Schema: pro16; Owner: postgres
--

COPY pro16.pro (description, created_at) FROM stdin;
new pro	2024-10-28 00:00:00
old pro	2024-10-28 00:00:00
\.


--
-- TOC entry 4906 (class 0 OID 16631)
-- Dependencies: 226
-- Data for Name: author; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.author (authid, authfname, authlname, authcountry) FROM stdin;
1	Author1Fname	Author1Lname	USA
2	Author2Fname	Author2Lname	UK
3	Author3Fname	Author3Lname	Canada
4	Author4Fname	Author4Lname	Palestine
5	Author5Fname	Author5Lname	Jordan
\.


--
-- TOC entry 4898 (class 0 OID 16603)
-- Dependencies: 218
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book (bookid, bookname, bookavailable, bookcount, empid, pubid, authid) FROM stdin;
10	Book1	t	10	1	1	1
12	Book2	t	15	2	2	2
14	Book3	t	8	3	1	3
16	Book4	t	8	4	5	4
18	Book5	t	8	5	4	5
\.


--
-- TOC entry 4909 (class 0 OID 16647)
-- Dependencies: 229
-- Data for Name: borrows; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.borrows (custid, bookid, startdate) FROM stdin;
1	1	2024-10-20
2	2	2024-10-19
3	3	2024-11-01
4	5	2024-11-02
5	4	2024-11-04
\.


--
-- TOC entry 4902 (class 0 OID 16617)
-- Dependencies: 222
-- Data for Name: customer; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.customer (custid, custfname, custlname, librarycard, custcontact) FROM stdin;
4001	Alice	Johnson	t	123-456-7890
4002	Bob	Smith	f	987-654-3210
4003	Eva	Davis	t	456-789-0123
4005	Bob	Smith	f	987-654-3210
4006	Eva	Davis	t	456-789-0123
\.


--
-- TOC entry 4900 (class 0 OID 16610)
-- Dependencies: 220
-- Data for Name: employee; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.employee (empid, empfname, emplname, empsalary) FROM stdin;
1001	John	Doe	50000
1002	Jane	Smith	60000
1003	Michael	Johnson	55000
1004	Mic	Johnson	55000
1005	Jon	Johnson	55000
\.


--
-- TOC entry 4907 (class 0 OID 16637)
-- Dependencies: 227
-- Data for Name: feedback; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.feedback (description, rating_10, custid) FROM stdin;
Good book	8	1
Could be better	6	2
Excellent read	10	3
Excellent	10	4
Nice	10	5
\.


--
-- TOC entry 4904 (class 0 OID 16624)
-- Dependencies: 224
-- Data for Name: publisher; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.publisher (pubid, pubname, pubcountry) FROM stdin;
104	Publisher1	USA
105	Publisher2	UK
106	Publisher3	Canada
107	Publisher4	Palestine
108	Publisher5	Jordan
\.


--
-- TOC entry 4908 (class 0 OID 16642)
-- Dependencies: 228
-- Data for Name: purchases; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.purchases (custid, bookid) FROM stdin;
1	1
2	2
3	3
4	4
5	5
\.


--
-- TOC entry 4922 (class 0 OID 0)
-- Dependencies: 225
-- Name: author_authid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.author_authid_seq', 1, false);


--
-- TOC entry 4923 (class 0 OID 0)
-- Dependencies: 217
-- Name: book_bookid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.book_bookid_seq', 1, false);


--
-- TOC entry 4924 (class 0 OID 0)
-- Dependencies: 221
-- Name: customer_custid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.customer_custid_seq', 1, false);


--
-- TOC entry 4925 (class 0 OID 0)
-- Dependencies: 219
-- Name: employee_empid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.employee_empid_seq', 1, false);


--
-- TOC entry 4926 (class 0 OID 0)
-- Dependencies: 223
-- Name: publisher_pubid_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.publisher_pubid_seq', 1, false);


--
-- TOC entry 4744 (class 2606 OID 16636)
-- Name: author author_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_pkey PRIMARY KEY (authid);


--
-- TOC entry 4736 (class 2606 OID 16608)
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (bookid);


--
-- TOC entry 4750 (class 2606 OID 16652)
-- Name: borrows borrows_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.borrows
    ADD CONSTRAINT borrows_pkey PRIMARY KEY (custid, bookid);


--
-- TOC entry 4740 (class 2606 OID 16622)
-- Name: customer customer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.customer
    ADD CONSTRAINT customer_pkey PRIMARY KEY (custid);


--
-- TOC entry 4738 (class 2606 OID 16615)
-- Name: employee employee_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (empid);


--
-- TOC entry 4746 (class 2606 OID 16641)
-- Name: feedback feedback_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.feedback
    ADD CONSTRAINT feedback_pkey PRIMARY KEY (description);


--
-- TOC entry 4742 (class 2606 OID 16629)
-- Name: publisher publisher_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.publisher
    ADD CONSTRAINT publisher_pkey PRIMARY KEY (pubid);


--
-- TOC entry 4748 (class 2606 OID 16646)
-- Name: purchases purchases_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.purchases
    ADD CONSTRAINT purchases_pkey PRIMARY KEY (custid, bookid);


--
-- TOC entry 4752 (class 2620 OID 16673)
-- Name: borrows trg_check_library_card; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_check_library_card BEFORE INSERT OR UPDATE ON public.borrows FOR EACH ROW EXECUTE FUNCTION public.check_library_card();


--
-- TOC entry 4753 (class 2620 OID 16671)
-- Name: borrows trg_decrease_book_count_on_borrow; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_decrease_book_count_on_borrow AFTER INSERT ON public.borrows FOR EACH ROW EXECUTE FUNCTION public.decrease_book_count_on_borrow();


--
-- TOC entry 4751 (class 2620 OID 16669)
-- Name: purchases trg_decrease_book_count_on_purchase; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trg_decrease_book_count_on_purchase AFTER INSERT ON public.purchases FOR EACH ROW EXECUTE FUNCTION public.decrease_book_count_on_purchase();


-- Completed on 2024-10-31 17:12:44

--
-- PostgreSQL database dump complete
--

-- Completed on 2024-10-31 17:12:44

--
-- PostgreSQL database cluster dump complete
--


--
-- PostgreSQL database dump
--

\restrict gyill2Lijxd61msQoW5wlNF5jFebw9aFmvwgggBKGsgA5NhpB6dI9bRDlBcPPtD

-- Dumped from database version 14.22 (Homebrew)
-- Dumped by pg_dump version 14.22 (Homebrew)

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
-- Name: stackoverflow; Type: SCHEMA; Schema: -; Owner: alexmldvn
--

CREATE SCHEMA stackoverflow;


ALTER SCHEMA stackoverflow OWNER TO alexmldvn;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: answer_votes; Type: TABLE; Schema: public; Owner: alexmldvn
--

CREATE TABLE public.answer_votes (
    answer_id bigint NOT NULL,
    user_id bigint NOT NULL,
    vote_value integer NOT NULL
);


ALTER TABLE public.answer_votes OWNER TO alexmldvn;

--
-- Name: answers; Type: TABLE; Schema: public; Owner: alexmldvn
--

CREATE TABLE public.answers (
    id bigint NOT NULL,
    topic_id bigint NOT NULL,
    author_id bigint NOT NULL,
    text_content text NOT NULL,
    picture_url character varying(500),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.answers OWNER TO alexmldvn;

--
-- Name: answers_id_seq; Type: SEQUENCE; Schema: public; Owner: alexmldvn
--

CREATE SEQUENCE public.answers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.answers_id_seq OWNER TO alexmldvn;

--
-- Name: answers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alexmldvn
--

ALTER SEQUENCE public.answers_id_seq OWNED BY public.answers.id;


--
-- Name: roles; Type: TABLE; Schema: public; Owner: alexmldvn
--

CREATE TABLE public.roles (
    id bigint NOT NULL,
    role_name character varying(50) NOT NULL
);


ALTER TABLE public.roles OWNER TO alexmldvn;

--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: alexmldvn
--

CREATE SEQUENCE public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.roles_id_seq OWNER TO alexmldvn;

--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alexmldvn
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;


--
-- Name: tags; Type: TABLE; Schema: public; Owner: alexmldvn
--

CREATE TABLE public.tags (
    id bigint NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE public.tags OWNER TO alexmldvn;

--
-- Name: tags_id_seq; Type: SEQUENCE; Schema: public; Owner: alexmldvn
--

CREATE SEQUENCE public.tags_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tags_id_seq OWNER TO alexmldvn;

--
-- Name: tags_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alexmldvn
--

ALTER SEQUENCE public.tags_id_seq OWNED BY public.tags.id;


--
-- Name: topic_tags; Type: TABLE; Schema: public; Owner: alexmldvn
--

CREATE TABLE public.topic_tags (
    topic_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE public.topic_tags OWNER TO alexmldvn;

--
-- Name: topic_votes; Type: TABLE; Schema: public; Owner: alexmldvn
--

CREATE TABLE public.topic_votes (
    topic_id bigint NOT NULL,
    user_id bigint NOT NULL,
    vote_value integer NOT NULL
);


ALTER TABLE public.topic_votes OWNER TO alexmldvn;

--
-- Name: topics; Type: TABLE; Schema: public; Owner: alexmldvn
--

CREATE TABLE public.topics (
    id bigint NOT NULL,
    author_id bigint NOT NULL,
    title character varying(255) NOT NULL,
    text_content text NOT NULL,
    picture_url character varying(500),
    status character varying(50) DEFAULT 'RECEIVED'::character varying,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.topics OWNER TO alexmldvn;

--
-- Name: topics_id_seq; Type: SEQUENCE; Schema: public; Owner: alexmldvn
--

CREATE SEQUENCE public.topics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.topics_id_seq OWNER TO alexmldvn;

--
-- Name: topics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alexmldvn
--

ALTER SEQUENCE public.topics_id_seq OWNED BY public.topics.id;


--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: alexmldvn
--

CREATE TABLE public.user_roles (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.user_roles OWNER TO alexmldvn;

--
-- Name: users; Type: TABLE; Schema: public; Owner: alexmldvn
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    username character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    password_hash character varying(255) NOT NULL,
    score numeric(10,2) DEFAULT 0.00,
    is_banned boolean DEFAULT false
);


ALTER TABLE public.users OWNER TO alexmldvn;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: alexmldvn
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO alexmldvn;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: alexmldvn
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: answer_votes; Type: TABLE; Schema: stackoverflow; Owner: alexmldvn
--

CREATE TABLE stackoverflow.answer_votes (
    answer_id bigint NOT NULL,
    user_id bigint NOT NULL,
    vote_type character varying(20) NOT NULL,
    CONSTRAINT chk_answer_vote_type CHECK (((vote_type)::text = ANY ((ARRAY['UPVOTE'::character varying, 'DOWNVOTE'::character varying, 'LIKE'::character varying, 'DISLIKE'::character varying])::text[])))
);


ALTER TABLE stackoverflow.answer_votes OWNER TO alexmldvn;

--
-- Name: answers; Type: TABLE; Schema: stackoverflow; Owner: alexmldvn
--

CREATE TABLE stackoverflow.answers (
    id bigint NOT NULL,
    topic_id bigint NOT NULL,
    author_id bigint NOT NULL,
    text_content text NOT NULL,
    picture_url character varying(500),
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE stackoverflow.answers OWNER TO alexmldvn;

--
-- Name: answers_id_seq; Type: SEQUENCE; Schema: stackoverflow; Owner: alexmldvn
--

CREATE SEQUENCE stackoverflow.answers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE stackoverflow.answers_id_seq OWNER TO alexmldvn;

--
-- Name: answers_id_seq; Type: SEQUENCE OWNED BY; Schema: stackoverflow; Owner: alexmldvn
--

ALTER SEQUENCE stackoverflow.answers_id_seq OWNED BY stackoverflow.answers.id;


--
-- Name: roles; Type: TABLE; Schema: stackoverflow; Owner: alexmldvn
--

CREATE TABLE stackoverflow.roles (
    id bigint NOT NULL,
    role_name character varying(50) NOT NULL
);


ALTER TABLE stackoverflow.roles OWNER TO alexmldvn;

--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: stackoverflow; Owner: alexmldvn
--

CREATE SEQUENCE stackoverflow.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE stackoverflow.roles_id_seq OWNER TO alexmldvn;

--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: stackoverflow; Owner: alexmldvn
--

ALTER SEQUENCE stackoverflow.roles_id_seq OWNED BY stackoverflow.roles.id;


--
-- Name: tags; Type: TABLE; Schema: stackoverflow; Owner: alexmldvn
--

CREATE TABLE stackoverflow.tags (
    id bigint NOT NULL,
    name character varying(50) NOT NULL
);


ALTER TABLE stackoverflow.tags OWNER TO alexmldvn;

--
-- Name: tags_id_seq; Type: SEQUENCE; Schema: stackoverflow; Owner: alexmldvn
--

CREATE SEQUENCE stackoverflow.tags_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE stackoverflow.tags_id_seq OWNER TO alexmldvn;

--
-- Name: tags_id_seq; Type: SEQUENCE OWNED BY; Schema: stackoverflow; Owner: alexmldvn
--

ALTER SEQUENCE stackoverflow.tags_id_seq OWNED BY stackoverflow.tags.id;


--
-- Name: topic_tags; Type: TABLE; Schema: stackoverflow; Owner: alexmldvn
--

CREATE TABLE stackoverflow.topic_tags (
    topic_id bigint NOT NULL,
    tag_id bigint NOT NULL
);


ALTER TABLE stackoverflow.topic_tags OWNER TO alexmldvn;

--
-- Name: topic_votes; Type: TABLE; Schema: stackoverflow; Owner: alexmldvn
--

CREATE TABLE stackoverflow.topic_votes (
    topic_id bigint NOT NULL,
    user_id bigint NOT NULL,
    vote_type character varying(20) NOT NULL,
    CONSTRAINT chk_topic_vote_type CHECK (((vote_type)::text = ANY ((ARRAY['UPVOTE'::character varying, 'DOWNVOTE'::character varying, 'LIKE'::character varying, 'DISLIKE'::character varying])::text[])))
);


ALTER TABLE stackoverflow.topic_votes OWNER TO alexmldvn;

--
-- Name: topics; Type: TABLE; Schema: stackoverflow; Owner: alexmldvn
--

CREATE TABLE stackoverflow.topics (
    id bigint NOT NULL,
    author_id bigint NOT NULL,
    title character varying(255) NOT NULL,
    text_content text NOT NULL,
    picture_url character varying(500),
    status character varying(50) DEFAULT 'RECEIVED'::character varying,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_topic_status CHECK (((status)::text = ANY ((ARRAY['RECEIVED'::character varying, 'IN_PROGRESS'::character varying, 'SOLVED'::character varying])::text[])))
);


ALTER TABLE stackoverflow.topics OWNER TO alexmldvn;

--
-- Name: topics_id_seq; Type: SEQUENCE; Schema: stackoverflow; Owner: alexmldvn
--

CREATE SEQUENCE stackoverflow.topics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE stackoverflow.topics_id_seq OWNER TO alexmldvn;

--
-- Name: topics_id_seq; Type: SEQUENCE OWNED BY; Schema: stackoverflow; Owner: alexmldvn
--

ALTER SEQUENCE stackoverflow.topics_id_seq OWNED BY stackoverflow.topics.id;


--
-- Name: user_roles; Type: TABLE; Schema: stackoverflow; Owner: alexmldvn
--

CREATE TABLE stackoverflow.user_roles (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE stackoverflow.user_roles OWNER TO alexmldvn;

--
-- Name: users; Type: TABLE; Schema: stackoverflow; Owner: alexmldvn
--

CREATE TABLE stackoverflow.users (
    id bigint NOT NULL,
    username character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    password_hash character varying(255) NOT NULL,
    score numeric(10,2) DEFAULT 0.00,
    is_banned boolean DEFAULT false
);


ALTER TABLE stackoverflow.users OWNER TO alexmldvn;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: stackoverflow; Owner: alexmldvn
--

CREATE SEQUENCE stackoverflow.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE stackoverflow.users_id_seq OWNER TO alexmldvn;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: stackoverflow; Owner: alexmldvn
--

ALTER SEQUENCE stackoverflow.users_id_seq OWNED BY stackoverflow.users.id;


--
-- Name: answers id; Type: DEFAULT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.answers ALTER COLUMN id SET DEFAULT nextval('public.answers_id_seq'::regclass);


--
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- Name: tags id; Type: DEFAULT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.tags ALTER COLUMN id SET DEFAULT nextval('public.tags_id_seq'::regclass);


--
-- Name: topics id; Type: DEFAULT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.topics ALTER COLUMN id SET DEFAULT nextval('public.topics_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Name: answers id; Type: DEFAULT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.answers ALTER COLUMN id SET DEFAULT nextval('stackoverflow.answers_id_seq'::regclass);


--
-- Name: roles id; Type: DEFAULT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.roles ALTER COLUMN id SET DEFAULT nextval('stackoverflow.roles_id_seq'::regclass);


--
-- Name: tags id; Type: DEFAULT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.tags ALTER COLUMN id SET DEFAULT nextval('stackoverflow.tags_id_seq'::regclass);


--
-- Name: topics id; Type: DEFAULT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.topics ALTER COLUMN id SET DEFAULT nextval('stackoverflow.topics_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.users ALTER COLUMN id SET DEFAULT nextval('stackoverflow.users_id_seq'::regclass);


--
-- Data for Name: answer_votes; Type: TABLE DATA; Schema: public; Owner: alexmldvn
--

COPY public.answer_votes (answer_id, user_id, vote_value) FROM stdin;
\.


--
-- Data for Name: answers; Type: TABLE DATA; Schema: public; Owner: alexmldvn
--

COPY public.answers (id, topic_id, author_id, text_content, picture_url, created_at) FROM stdin;
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: alexmldvn
--

COPY public.roles (id, role_name) FROM stdin;
\.


--
-- Data for Name: tags; Type: TABLE DATA; Schema: public; Owner: alexmldvn
--

COPY public.tags (id, name) FROM stdin;
16	postgresql
\.


--
-- Data for Name: topic_tags; Type: TABLE DATA; Schema: public; Owner: alexmldvn
--

COPY public.topic_tags (topic_id, tag_id) FROM stdin;
\.


--
-- Data for Name: topic_votes; Type: TABLE DATA; Schema: public; Owner: alexmldvn
--

COPY public.topic_votes (topic_id, user_id, vote_value) FROM stdin;
\.


--
-- Data for Name: topics; Type: TABLE DATA; Schema: public; Owner: alexmldvn
--

COPY public.topics (id, author_id, title, text_content, picture_url, status, created_at) FROM stdin;
\.


--
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: alexmldvn
--

COPY public.user_roles (user_id, role_id) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: alexmldvn
--

COPY public.users (id, username, email, password_hash, score, is_banned) FROM stdin;
11	testuser1	testuser1@test.com	$2a$10$rhc8yvwnkR0hW7H6i2GOgu/BXav8CU/ecpzEXfnWio2Ihdy0rWkly	0.00	f
12	antonio	antonio@mail.com	$2a$10$XVaZQF.3HAp9wPk4NBRlfuEd8lUnCU7Jd88QDEgSeQHDld3l9bu1m	0.00	f
\.


--
-- Data for Name: answer_votes; Type: TABLE DATA; Schema: stackoverflow; Owner: alexmldvn
--

COPY stackoverflow.answer_votes (answer_id, user_id, vote_type) FROM stdin;
\.


--
-- Data for Name: answers; Type: TABLE DATA; Schema: stackoverflow; Owner: alexmldvn
--

COPY stackoverflow.answers (id, topic_id, author_id, text_content, picture_url, created_at) FROM stdin;
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: stackoverflow; Owner: alexmldvn
--

COPY stackoverflow.roles (id, role_name) FROM stdin;
\.


--
-- Data for Name: tags; Type: TABLE DATA; Schema: stackoverflow; Owner: alexmldvn
--

COPY stackoverflow.tags (id, name) FROM stdin;
\.


--
-- Data for Name: topic_tags; Type: TABLE DATA; Schema: stackoverflow; Owner: alexmldvn
--

COPY stackoverflow.topic_tags (topic_id, tag_id) FROM stdin;
\.


--
-- Data for Name: topic_votes; Type: TABLE DATA; Schema: stackoverflow; Owner: alexmldvn
--

COPY stackoverflow.topic_votes (topic_id, user_id, vote_type) FROM stdin;
\.


--
-- Data for Name: topics; Type: TABLE DATA; Schema: stackoverflow; Owner: alexmldvn
--

COPY stackoverflow.topics (id, author_id, title, text_content, picture_url, status, created_at) FROM stdin;
\.


--
-- Data for Name: user_roles; Type: TABLE DATA; Schema: stackoverflow; Owner: alexmldvn
--

COPY stackoverflow.user_roles (user_id, role_id) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: stackoverflow; Owner: alexmldvn
--

COPY stackoverflow.users (id, username, email, password_hash, score, is_banned) FROM stdin;
\.


--
-- Name: answers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: alexmldvn
--

SELECT pg_catalog.setval('public.answers_id_seq', 6, true);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: alexmldvn
--

SELECT pg_catalog.setval('public.roles_id_seq', 1, false);


--
-- Name: tags_id_seq; Type: SEQUENCE SET; Schema: public; Owner: alexmldvn
--

SELECT pg_catalog.setval('public.tags_id_seq', 16, true);


--
-- Name: topics_id_seq; Type: SEQUENCE SET; Schema: public; Owner: alexmldvn
--

SELECT pg_catalog.setval('public.topics_id_seq', 5, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: alexmldvn
--

SELECT pg_catalog.setval('public.users_id_seq', 14, true);


--
-- Name: answers_id_seq; Type: SEQUENCE SET; Schema: stackoverflow; Owner: alexmldvn
--

SELECT pg_catalog.setval('stackoverflow.answers_id_seq', 1, false);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: stackoverflow; Owner: alexmldvn
--

SELECT pg_catalog.setval('stackoverflow.roles_id_seq', 1, false);


--
-- Name: tags_id_seq; Type: SEQUENCE SET; Schema: stackoverflow; Owner: alexmldvn
--

SELECT pg_catalog.setval('stackoverflow.tags_id_seq', 1, false);


--
-- Name: topics_id_seq; Type: SEQUENCE SET; Schema: stackoverflow; Owner: alexmldvn
--

SELECT pg_catalog.setval('stackoverflow.topics_id_seq', 1, false);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: stackoverflow; Owner: alexmldvn
--

SELECT pg_catalog.setval('stackoverflow.users_id_seq', 1, false);


--
-- Name: answer_votes answer_votes_pkey; Type: CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.answer_votes
    ADD CONSTRAINT answer_votes_pkey PRIMARY KEY (answer_id, user_id);


--
-- Name: answers answers_pkey; Type: CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.answers
    ADD CONSTRAINT answers_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: roles roles_role_name_key; Type: CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_role_name_key UNIQUE (role_name);


--
-- Name: tags tags_name_key; Type: CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_name_key UNIQUE (name);


--
-- Name: tags tags_pkey; Type: CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.tags
    ADD CONSTRAINT tags_pkey PRIMARY KEY (id);


--
-- Name: topic_tags topic_tags_pkey; Type: CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.topic_tags
    ADD CONSTRAINT topic_tags_pkey PRIMARY KEY (topic_id, tag_id);


--
-- Name: topic_votes topic_votes_pkey; Type: CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.topic_votes
    ADD CONSTRAINT topic_votes_pkey PRIMARY KEY (topic_id, user_id);


--
-- Name: topics topics_pkey; Type: CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.topics
    ADD CONSTRAINT topics_pkey PRIMARY KEY (id);


--
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: answer_votes answer_votes_pkey; Type: CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.answer_votes
    ADD CONSTRAINT answer_votes_pkey PRIMARY KEY (answer_id, user_id);


--
-- Name: answers answers_pkey; Type: CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.answers
    ADD CONSTRAINT answers_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: roles roles_role_name_key; Type: CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.roles
    ADD CONSTRAINT roles_role_name_key UNIQUE (role_name);


--
-- Name: tags tags_name_key; Type: CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.tags
    ADD CONSTRAINT tags_name_key UNIQUE (name);


--
-- Name: tags tags_pkey; Type: CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.tags
    ADD CONSTRAINT tags_pkey PRIMARY KEY (id);


--
-- Name: topic_tags topic_tags_pkey; Type: CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.topic_tags
    ADD CONSTRAINT topic_tags_pkey PRIMARY KEY (topic_id, tag_id);


--
-- Name: topic_votes topic_votes_pkey; Type: CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.topic_votes
    ADD CONSTRAINT topic_votes_pkey PRIMARY KEY (topic_id, user_id);


--
-- Name: topics topics_pkey; Type: CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.topics
    ADD CONSTRAINT topics_pkey PRIMARY KEY (id);


--
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users users_username_key; Type: CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- Name: answer_votes answer_votes_answer_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.answer_votes
    ADD CONSTRAINT answer_votes_answer_id_fkey FOREIGN KEY (answer_id) REFERENCES public.answers(id) ON DELETE CASCADE;


--
-- Name: answer_votes answer_votes_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.answer_votes
    ADD CONSTRAINT answer_votes_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: answers answers_author_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.answers
    ADD CONSTRAINT answers_author_id_fkey FOREIGN KEY (author_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: answers answers_topic_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.answers
    ADD CONSTRAINT answers_topic_id_fkey FOREIGN KEY (topic_id) REFERENCES public.topics(id) ON DELETE CASCADE;


--
-- Name: topic_tags topic_tags_tag_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.topic_tags
    ADD CONSTRAINT topic_tags_tag_id_fkey FOREIGN KEY (tag_id) REFERENCES public.tags(id) ON DELETE CASCADE;


--
-- Name: topic_tags topic_tags_topic_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.topic_tags
    ADD CONSTRAINT topic_tags_topic_id_fkey FOREIGN KEY (topic_id) REFERENCES public.topics(id) ON DELETE CASCADE;


--
-- Name: topic_votes topic_votes_topic_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.topic_votes
    ADD CONSTRAINT topic_votes_topic_id_fkey FOREIGN KEY (topic_id) REFERENCES public.topics(id) ON DELETE CASCADE;


--
-- Name: topic_votes topic_votes_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.topic_votes
    ADD CONSTRAINT topic_votes_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: topics topics_author_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.topics
    ADD CONSTRAINT topics_author_id_fkey FOREIGN KEY (author_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: user_roles user_roles_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE;


--
-- Name: user_roles user_roles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: alexmldvn
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;


--
-- Name: answer_votes answer_votes_answer_id_fkey; Type: FK CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.answer_votes
    ADD CONSTRAINT answer_votes_answer_id_fkey FOREIGN KEY (answer_id) REFERENCES stackoverflow.answers(id) ON DELETE CASCADE;


--
-- Name: answer_votes answer_votes_user_id_fkey; Type: FK CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.answer_votes
    ADD CONSTRAINT answer_votes_user_id_fkey FOREIGN KEY (user_id) REFERENCES stackoverflow.users(id) ON DELETE CASCADE;


--
-- Name: answers answers_author_id_fkey; Type: FK CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.answers
    ADD CONSTRAINT answers_author_id_fkey FOREIGN KEY (author_id) REFERENCES stackoverflow.users(id) ON DELETE CASCADE;


--
-- Name: answers answers_topic_id_fkey; Type: FK CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.answers
    ADD CONSTRAINT answers_topic_id_fkey FOREIGN KEY (topic_id) REFERENCES stackoverflow.topics(id) ON DELETE CASCADE;


--
-- Name: topic_tags topic_tags_tag_id_fkey; Type: FK CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.topic_tags
    ADD CONSTRAINT topic_tags_tag_id_fkey FOREIGN KEY (tag_id) REFERENCES stackoverflow.tags(id) ON DELETE CASCADE;


--
-- Name: topic_tags topic_tags_topic_id_fkey; Type: FK CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.topic_tags
    ADD CONSTRAINT topic_tags_topic_id_fkey FOREIGN KEY (topic_id) REFERENCES stackoverflow.topics(id) ON DELETE CASCADE;


--
-- Name: topic_votes topic_votes_topic_id_fkey; Type: FK CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.topic_votes
    ADD CONSTRAINT topic_votes_topic_id_fkey FOREIGN KEY (topic_id) REFERENCES stackoverflow.topics(id) ON DELETE CASCADE;


--
-- Name: topic_votes topic_votes_user_id_fkey; Type: FK CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.topic_votes
    ADD CONSTRAINT topic_votes_user_id_fkey FOREIGN KEY (user_id) REFERENCES stackoverflow.users(id) ON DELETE CASCADE;


--
-- Name: topics topics_author_id_fkey; Type: FK CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.topics
    ADD CONSTRAINT topics_author_id_fkey FOREIGN KEY (author_id) REFERENCES stackoverflow.users(id) ON DELETE CASCADE;


--
-- Name: user_roles user_roles_role_id_fkey; Type: FK CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.user_roles
    ADD CONSTRAINT user_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES stackoverflow.roles(id) ON DELETE CASCADE;


--
-- Name: user_roles user_roles_user_id_fkey; Type: FK CONSTRAINT; Schema: stackoverflow; Owner: alexmldvn
--

ALTER TABLE ONLY stackoverflow.user_roles
    ADD CONSTRAINT user_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES stackoverflow.users(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict gyill2Lijxd61msQoW5wlNF5jFebw9aFmvwgggBKGsgA5NhpB6dI9bRDlBcPPtD


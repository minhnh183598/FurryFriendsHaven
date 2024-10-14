Create database FURRYFRIENDSHAVEN
USE FURRYFRIENDSHAVEN

CREATE SEQUENCE task_seq START WITH 1 INCREMENT BY 50
    GO

CREATE TABLE application
(
    application_id varchar(255) NOT NULL,
    pet_id         varchar(255),
    id             varchar(255),
    full_name      varchar(255),
    yob            int          NOT NULL,
    gender         varchar(255),
    address        varchar(255),
    city           varchar(255),
    job            varchar(255),
    phone          varchar(255),
    live_in        varchar(255),
    live_with      varchar(255),
    first_person   varchar(255),
    first_phone    varchar(255),
    second_person  varchar(255),
    second_phone   varchar(255),
    status         int          NOT NULL,
    create_at      datetime,
    CONSTRAINT pk_application PRIMARY KEY (application_id)
)
    GO

CREATE TABLE comment
(
    id                int IDENTITY (1, 1) NOT NULL,
    content           varchar(1000),
    created_date_time datetime NOT NULL,
    updated_at        datetime,
    user_id           varchar(255),
    issue_id          int,
    CONSTRAINT pk_comment PRIMARY KEY (id)
)
    GO

CREATE TABLE invalidated_token
(
    id          varchar(255) NOT NULL,
    expiry_time datetime,
    CONSTRAINT pk_invalidatedtoken PRIMARY KEY (id)
)
    GO

CREATE TABLE issue
(
    id          int IDENTITY (1, 1) NOT NULL,
    title       varchar(255),
    description varchar(255),
    status      varchar(255),
    taskid      int NOT NULL,
    priority    varchar(255),
    due_date    date,
    reporter_id varchar(255),
    task_id     int,
    CONSTRAINT pk_issue PRIMARY KEY (id)
)
    GO

CREATE TABLE issue_assignees
(
    issue_id     int          NOT NULL,
    assignees_id varchar(255) NOT NULL
)
    GO

CREATE TABLE issue_tags
(
    issue_id  int          NOT NULL,
    tags_name varchar(255) NOT NULL,
    CONSTRAINT pk_issue_tags PRIMARY KEY (issue_id, tags_name)
)
    GO

CREATE TABLE otp
(
    id            int IDENTITY (1, 1) NOT NULL,
    code          varchar(255),
    expire_time   datetime,
    refresh_count int NOT NULL,
    lockout_time  datetime,
    user_id       varchar(255),
    CONSTRAINT pk_otp PRIMARY KEY (id)
)
    GO

CREATE TABLE payment
(
    id             bigint IDENTITY (1, 1) NOT NULL,
    amount         varchar(255),
    bank_code      varchar(255),
    bank_tran_no   varchar(255),
    card_type      varchar(255),
    order_info     varchar(255),
    pay_date       varchar(255),
    response_code  varchar(255),
    transaction_no varchar(255),
    txn_ref        varchar(255),
    secure_hash    varchar(255),
    CONSTRAINT pk_payment PRIMARY KEY (id)
)
    GO

CREATE TABLE permission
(
    name        varchar(255) NOT NULL,
    description varchar(255),
    CONSTRAINT pk_permission PRIMARY KEY (name)
)
    GO

CREATE TABLE pet
(
    pet_id          varchar(255) NOT NULL,
    pet_name        varchar(255),
    pet_type        varchar(255),
    pet_age         varchar(255),
    pet_breed       varchar(255),
    pet_color       varchar(255),
    pet_description varchar(255),
    pet_size        varchar(255),
    pet_weight      float(53)    NOT NULL,
    pet_gender      varchar(255),
    pet_vaccin      varchar(255),
    pet_status      varchar(255),
    CONSTRAINT pk_pet PRIMARY KEY (pet_id)
)
    GO

CREATE TABLE refresh_token
(
    id                        bigint IDENTITY (1, 1) NOT NULL,
    refresh_token             varchar(255),
    token                     varchar(255),
    token_expiry_time         datetime,
    refresh_token_expiry_time datetime,
    user_id                   varchar(255),
    CONSTRAINT pk_refreshtoken PRIMARY KEY (id)
)
    GO

CREATE TABLE role
(
    name        varchar(100) NOT NULL,
    description varchar(max),
    CONSTRAINT pk_role PRIMARY KEY (name)
)
    GO

CREATE TABLE role_permissions
(
    role_name        varchar(100) NOT NULL,
    permissions_name varchar(100) NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (role_name, permissions_name)
)
    GO

CREATE TABLE tag
(
    name        varchar(255) NOT NULL,
    description varchar(max),
    type        varchar(255) NOT NULL,
    CONSTRAINT pk_tag PRIMARY KEY (name)
)
    GO

CREATE TABLE task
(
    id          int          NOT NULL,
    name        varchar(255) NOT NULL,
    description varchar(max),
    category    varchar(255),
    owner_id    varchar(255),
    CONSTRAINT pk_task PRIMARY KEY (id)
)
    GO

CREATE TABLE task_tags
(
    task_id   int          NOT NULL,
    tags_name varchar(255) NOT NULL,
    CONSTRAINT pk_task_tags PRIMARY KEY (task_id, tags_name)
)
    GO

CREATE TABLE task_team
(
    task_id int          NOT NULL,
    user_id varchar(255) NOT NULL
)
    GO

CREATE TABLE users
(
    id                     varchar(255) NOT NULL,
    username               varchar(100),
    password               varchar(255),
    firstname              varchar(100),
    lastname               varchar(100),
    email                  varchar(100),
    is_enabled             bit          NOT NULL,
    is_password_changeable bit          NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
)
    GO

CREATE TABLE users_roles
(
    user_id    varchar(255) NOT NULL,
    roles_name varchar(30) NOT NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (user_id, roles_name)
)
    GO

CREATE TABLE users_tasks
(
    user_id  varchar(255) NOT NULL,
    tasks_id int          NOT NULL
)
    GO

ALTER TABLE application
    ADD CONSTRAINT uc_application_firstphone UNIQUE (first_phone)
    GO

ALTER TABLE application
    ADD CONSTRAINT uc_application_phone UNIQUE (phone)
    GO

ALTER TABLE application
    ADD CONSTRAINT uc_application_secondphone UNIQUE (second_phone)
    GO

ALTER TABLE issue_assignees
    ADD CONSTRAINT uc_issue_assignees_assignees UNIQUE (assignees_id)
    GO

ALTER TABLE issue_tags
    ADD CONSTRAINT uc_issue_tags_tags_name UNIQUE (tags_name)
    GO

ALTER TABLE otp
    ADD CONSTRAINT uc_otp_user UNIQUE (user_id)
    GO

ALTER TABLE refresh_token
    ADD CONSTRAINT uc_refreshtoken_refreshtoken UNIQUE (refresh_token)
    GO

ALTER TABLE refresh_token
    ADD CONSTRAINT uc_refreshtoken_user UNIQUE (user_id)
    GO

ALTER TABLE task
    ADD CONSTRAINT uc_task_name UNIQUE (name)
    GO

ALTER TABLE task_tags
    ADD CONSTRAINT uc_task_tags_tags_name UNIQUE (tags_name)
    GO

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email)
    GO

ALTER TABLE application
    ADD CONSTRAINT FK_APPLICATION_ON_ID FOREIGN KEY (id) REFERENCES users (id)
    GO

ALTER TABLE application
    ADD CONSTRAINT FK_APPLICATION_ON_PETID FOREIGN KEY (pet_id) REFERENCES pet (pet_id)
    GO

ALTER TABLE comment
    ADD CONSTRAINT FK_COMMENT_ON_ISSUE FOREIGN KEY (issue_id) REFERENCES issue (id)
    GO

ALTER TABLE comment
    ADD CONSTRAINT FK_COMMENT_ON_USER FOREIGN KEY (user_id) REFERENCES users (id)
    GO

ALTER TABLE issue
    ADD CONSTRAINT FK_ISSUE_ON_REPORTER FOREIGN KEY (reporter_id) REFERENCES users (id)
    GO

ALTER TABLE issue
    ADD CONSTRAINT FK_ISSUE_ON_TASK FOREIGN KEY (task_id) REFERENCES task (id)
    GO

ALTER TABLE otp
    ADD CONSTRAINT FK_OTP_ON_USER FOREIGN KEY (user_id) REFERENCES users (id)
    GO

ALTER TABLE refresh_token
    ADD CONSTRAINT FK_REFRESHTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id)
    GO

ALTER TABLE task
    ADD CONSTRAINT FK_TASK_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id)
    GO

ALTER TABLE issue_assignees
    ADD CONSTRAINT fk_issass_on_issue FOREIGN KEY (issue_id) REFERENCES issue (id)
    GO

ALTER TABLE issue_assignees
    ADD CONSTRAINT fk_issass_on_user FOREIGN KEY (assignees_id) REFERENCES users (id)
    GO

ALTER TABLE issue_tags
    ADD CONSTRAINT fk_isstag_on_issue FOREIGN KEY (issue_id) REFERENCES issue (id)
    GO

ALTER TABLE issue_tags
    ADD CONSTRAINT fk_isstag_on_tag FOREIGN KEY (tags_name) REFERENCES tag (name)
    GO

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_permission FOREIGN KEY (permissions_name) REFERENCES permission (name)
    GO

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_role FOREIGN KEY (role_name) REFERENCES role (name)
    GO

ALTER TABLE task_tags
    ADD CONSTRAINT fk_task_tags_on_tag FOREIGN KEY (tags_name) REFERENCES tag (name)
    GO

ALTER TABLE task_tags
    ADD CONSTRAINT fk_task_tags_on_task FOREIGN KEY (task_id) REFERENCES task (id)
    GO

ALTER TABLE task_team
    ADD CONSTRAINT fk_task_team_on_task FOREIGN KEY (task_id) REFERENCES task (id)
    GO

ALTER TABLE task_team
    ADD CONSTRAINT fk_task_team_on_user FOREIGN KEY (user_id) REFERENCES users (id)
    GO

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (roles_name) REFERENCES role (name)
    GO

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id)
    GO

ALTER TABLE users_tasks
    ADD CONSTRAINT fk_usetas_on_task FOREIGN KEY (tasks_id) REFERENCES task (id)
    GO

ALTER TABLE users_tasks
    ADD CONSTRAINT fk_usetas_on_user FOREIGN KEY (user_id) REFERENCES users (id)
    GO

-- Insert role sample data
INSERT INTO role (name, description) VALUES
('ADMIN', 'Administrator with full access'),
('USER', 'Regular user with limited access'),
('STAFF', 'Staff member with moderate access'),
('VOLUNTEER', 'Volunteer with basic access'),
('ADOPTER', 'Adopter with rights to adopt pets'),
('DONOR', 'Donor who supports the organization');


-- Insert user sample data
INSERT INTO [dbo].[users] (is_enabled, is_password_changeable, email, firstname, id, lastname, password, username) VALUES
(1, 0, 'john.doe@example.com', 'John', 'c2c85bd1-9fae-4bdb-86c4-09b67118f02e', 'Doe', '$2a$10$e4Z0v.2b1e8cR3Rx3jIPMOcbv2zgZPdCiwwGF9WqkO2i8/tyqOJlS', 'john_doe'),
(1, 0, 'jane.smith@example.com', 'Jane', 'd2d8bde1-c9be-4a15-aa9d-4ae0f7352c2e', 'Smith', '$2a$10$GRz4/1oP5Xftl3Elkwm.uefO5ev1ykbkdZ4t/0UJTSlzTObXG/R9K', 'jane.smith'),
(1, 0, 'alice.brown@example.com', 'Alice', 'a28b11ad-b50a-4781-9b14-1caf1e83cffe', 'Brown', '$2a$10$zveWif5IYermc1o/DMk4pOeWOX/SCgHPbgAtaswYaFuv6qR4UeE8C', 'alice.brown'),
(1, 0, 'bob.johnson@example.com', 'Bob', 'd951cbcb-9a55-4c1b-85f7-f0d4e82b6cf0', 'Johnson', '$2a$10$Q9l7ukE/8L53hNlnFlT4AeTHGHyMN1OZNHm9czAYAdeDD8NyAH67C', 'bob.j'),
(1, 0, 'charlie.brown@example.com', 'Charlie', 'f89cca68-fe1c-4b9a-b2ca-2f65548b78f3', 'Brown', '$2a$10$ngq2I5j/pVVHKpCh8LhalexM5yXdWhnGSjOPwlzxHclTdKwjBCsuK', 'charlie.b'),
(1, 0, 'david.williams@example.com', 'David', 'aa07395f-889c-47c8-a465-f9b70b06a0f6', 'Williams', '$2a$10$Y5tm3yEKRfvnVxParYF9Te.cVMcG9jMeyuPja9.cIp3FAmItqRs0e', 'david.w'),
(1, 0, 'eya.thompson@example.com', 'Eya', 'b0269cbc-e7ef-4f47-988b-4e9064d5f776', 'Thompson', '$2a$10$udcpR9q4/0yTbwa8U5MeHuBHguzLoA7fX4A8D9/IkdgwkM3fAtm2K', 'eya.t'),
(1, 0, 'fay.miller@example.com', 'Fay', 'cfb01b74-84b0-48b3-b394-f3f2109aaa6e', 'Miller', '$2a$10$4y4CjElQn3F1BylU3GEZIeZICdh0C8OtFZ8Bje64BQ6LSPURp2KoC', 'fay.m'),
(1, 0, 'george.davis@example.com', 'George', '96397221-e542-4e38-b932-7154d805b33a', 'Davis', '$2a$10$Mv8NOB7p.Vo6LtvP41g0xuFTxXPP.clxyUvS/JS.z1a8OP5pWf4nK', 'george.d'),
(1, 0, 'hannah.garcia@example.com', 'Hannah', '5e329737-bc4a-4b57-b7a4-f28fbd2047eb', 'Garcia', '$2a$10$llz8OZGxks9sdOWhNI/aE.0Pcisrgg8JD8CqGTI0cm3hJ.3KY4Pa6', 'hannah.g'),
(1, 0, 'ian.martinez@example.com', 'Ian', '8987bb90-91e4-4c79-a426-da1517444b18', 'Martinez', '$2a$10$ZaCdoDrR8S6T.UJAbCoKcOHcRDLk50NOImB0Eu/HAc9tkdsSClzCq', 'ian.m'),
(1, 0, 'jacket.white@example.com', 'Jacket', '7d4fc2ff-f12a-43c3-8e6e-5b9b8b4b688a', 'White', '$2a$10$Uafi3.B.40EDcST3NxSzPeT6BBXcIt.HxXTxWM4xzTwBeMoyFGgTe', 'jacket.w'),
(1, 0, 'kevin.hall@example.com', 'Kevin', '012cb8be-8958-4a8c-94e5-ebce7463c5b3', 'Hall', '$2a$10$L7HkGsL5cQHwWojRNDfDle08D/A/FCYF6FeafORUVIktcAQi1G3My', 'kevin.h'),
(1, 0, 'linda.lewis@example.com', 'Linda', 'e0495233-f7be-45c4-8b41-e0cac4c8e1c6', 'Lewis', '$2a$10$ZH9jb8g4qWqWyCLOuiG2uOrADsf5Lz/XA0/c6A5P7r9u5CadastrarR698', 'linda.l'),
(1, 0, 'michael.thompson@example.com', 'Michael', 'f1183497-66a5-4c41-b3ef-e8cbf23c0752', 'Thompson', '$2a$10$7/tXseaFz7CIKvsTw/.0Ge.noBscM7C2Fr.E2MAMSGRkZhLOoeFpC', 'michael.t'),
(1, 0, 'nancy.white@example.com', 'Nancy', '16d74078-6cc8-401a-b34c-97d8733ab17d', 'White', '$2a$10$nYX2H06iRQ7iHn5guSePa.w0kA9P5HfntHxN4VPnM8bHbF3IQg2aO', 'nancy.w');

--Insert user_role sample data
INSERT INTO users_roles (user_id, roles_name) VALUES
('c2c85bd1-9fae-4bdb-86c4-09b67118f02e', 'USER'),  -- John
('d2d8bde1-c9be-4a15-aa9d-4ae0f7352c2e', 'USER'),  -- Jane
('a28b11ad-b50a-4781-9b14-1caf1e83cffe', 'USER'),  -- Alice
('d951cbcb-9a55-4c1b-85f7-f0d4e82b6cf0', 'USER'),  -- Bob
('f89cca68-fe1c-4b9a-b2ca-2f65548b78f3', 'USER'),  -- Charlie
('aa07395f-889c-47c8-a465-f9b70b06a0f6', 'USER'),  -- David
('b0269cbc-e7ef-4f47-988b-4e9064d5f776', 'USER'),  -- Eya
('cfb01b74-84b0-48b3-b394-f3f2109aaa6e', 'USER'),  -- Fay
('96397221-e542-4e38-b932-7154d805b33a', 'USER'),  -- George
('5e329737-bc4a-4b57-b7a4-f28fbd2047eb', 'USER'),  -- Hannah
('8987bb90-91e4-4c79-a426-da1517444b18', 'USER'),  -- Ian
('7d4fc2ff-f12a-43c3-8e6e-5b9b8b4b688a', 'USER'),  -- Jacket
('012cb8be-8958-4a8c-94e5-ebce7463c5b3', 'USER'),  -- Kevin
('e0495233-f7be-45c4-8b41-e0cac4c8e1c6', 'USER'),  -- Linda
('f1183497-66a5-4c41-b3ef-e8cbf23c0752', 'USER'),  -- Michael
('16d74078-6cc8-401a-b34c-97d8733ab17d', 'USER');  -- Nancy

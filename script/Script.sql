CREATE TABLE invalidated_token
(
    id          varchar(100) NOT NULL,
    expiry_time datetime,
    CONSTRAINT pk_invalidatedtoken PRIMARY KEY (id)
)
    GO

CREATE TABLE permission
(
    name        varchar(50) NOT NULL,
    description varchar(100),
    CONSTRAINT pk_permission PRIMARY KEY (name)
)
    GO

CREATE TABLE refresh_token
(
    id                        bigint IDENTITY (1, 1) NOT NULL,
    refresh_token             varchar(100) NOT NULL,
    token                     varchar(100) NOT NULL,
    token_expiry_time         datetime,
    refresh_token_expiry_time datetime,
    user_id                   varchar(100) NOT NULL,
    CONSTRAINT pk_refreshtoken PRIMARY KEY (id)
)
    GO

CREATE TABLE role
(
    name        varchar(50) NOT NULL,
    description varchar(255),
    CONSTRAINT pk_role PRIMARY KEY (name)
)
    GO

CREATE TABLE role_permissions
(
    role_name        varchar(50) NOT NULL,
    permissions_name varchar(50) NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (role_name, permissions_name)
)
    GO

CREATE TABLE users
(
    id        varchar(255) NOT NULL,
    username  varchar(255),
    password  varchar(255),
    firstname varchar(255),
    lastname  varchar(255),
    email     varchar(255),
    dob       date,
    CONSTRAINT pk_users PRIMARY KEY (id)
)
    GO

CREATE TABLE users_roles
(
    user_id    varchar(255) NOT NULL,
    roles_name varchar(255) NOT NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (user_id, roles_name)
)
    GO

ALTER TABLE refresh_token
    ADD CONSTRAINT uc_refreshtoken_refreshtoken UNIQUE (refresh_token)
    GO

ALTER TABLE refresh_token
    ADD CONSTRAINT uc_refreshtoken_user UNIQUE (user_id)
    GO

ALTER TABLE refresh_token
    ADD CONSTRAINT FK_REFRESHTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id)
    GO

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_permission FOREIGN KEY (permissions_name) REFERENCES permission (name)
    GO

ALTER TABLE role_permissions
    ADD CONSTRAINT fk_rolper_on_role FOREIGN KEY (role_name) REFERENCES role (name)
    GO

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (roles_name) REFERENCES role (name)
    GO

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id)
    GO
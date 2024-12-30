/* Roles creations directly in database */
INSERT INTO tb_roles (role_id, name) VALUES (1, 'API_ADMIN(1L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (2, 'API_BASIC(2L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (3, 'API_GROUP(3L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (4, 'API_GROUP_MANAGER(4L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (5, 'API_USER(5L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (6, 'API_USER_MANAGER(6L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (7, 'API_ROLE(7L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (8, 'API_ROLE_MANAGER(8L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (9, 'API_TICKET(9L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (10, 'API_TICKET_MANAGER(10L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (11, 'API_TICKET_TYPE(11L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (12, 'API_TICKET_TYPE_MANAGER(12L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (13, 'API_TICKET_MESSAGE(13L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (14, 'API_TICKET_MESSAGE_MANAGER(14L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (15, 'API_TICKET_LOG(15L)') ON CONFLICT (role_id) DO NOTHING;
INSERT INTO tb_roles (role_id, name) VALUES (16, 'API_TICKET_LOG_MANAGER(16L)') ON CONFLICT (role_id) DO NOTHING;
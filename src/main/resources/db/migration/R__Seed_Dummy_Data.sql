DELETE FROM user_roles;
DELETE FROM transaction_categories;
DELETE FROM transactions;
DELETE FROM users;


INSERT INTO users (id, created_at, email, first_name, last_name, password_digest, updated_at) VALUES(1, '2022-04-01 08:30:09.028', 'test1.test@gmail.com', 'Edward', 'Heaver', '$2a$10$IQSSBjSaXamKUXf/inyXpOxOkT9wTWR/hrNvyCQJspVn9UDB97syC', '2022-04-01 08:30:09.028');
INSERT INTO users (id, created_at, email, first_name, last_name, password_digest, updated_at) VALUES(2, '2022-04-01 08:30:09.028', 'test2.test@gmail.com', 'Edward', 'Heaver', '$2a$10$IQSSBjSaXamKUXf/inyXpOxOkT9wTWR/hrNvyCQJspVn9UDB97syC', '2022-04-01 08:30:09.028');

INSERT INTO user_roles (user_id, role_id) VALUES(1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES(2, 2);

INSERT INTO transactions (id, created_at, notes, title, total, updated_at, user_id) VALUES(1, '2022-04-01 08:30:09.612', 'And so he spoke, and so he spoke, that Lord of Castamere, but now the rains weep o''er his hall, with no one there to hear. Yes, now the rains weep o''er his hall, and not a soul to hear.', 'Bush Tomato', 9.54, '2022-04-01 08:30:09.612', 1);
INSERT INTO transactions (id, created_at, notes, title, total, updated_at, user_id) VALUES(2, '2022-04-01 08:30:09.622', 'Why is it that when one man builds a wall, the next man immediately needs to know what''s on the other side?', 'Papaya', 13.15, '2022-04-01 08:30:09.622', 1);
INSERT INTO transactions (id, created_at, notes, title, total, updated_at, user_id) VALUES(3, '2022-04-01 08:30:09.633', 'All dwarfs are bastards in their father''s eyes', 'Kiwi Fruit', 6.62, '2022-04-01 08:30:09.633', 1);
INSERT INTO transactions (id, created_at, notes, title, total, updated_at, user_id) VALUES(4, '2022-04-01 08:30:09.652', 'Do the dead frighten you?', 'Kiwi Fruit', 11.46, '2022-04-01 08:30:09.652', 1);
INSERT INTO transactions (id, created_at, notes, title, total, updated_at, user_id) VALUES(5, '2022-04-01 08:30:09.662', 'Every flight begins with a fall.', 'Aubergine', 8.15, '2022-04-01 08:30:09.662', 1);
INSERT INTO transactions (id, created_at, notes, title, total, updated_at, user_id) VALUES(6, '2022-04-01 08:30:09.686', 'Winter is coming.', 'Elderberry', 9.33, '2022-04-01 08:30:09.686', 1);
INSERT INTO transactions (id, created_at, notes, title, total, updated_at, user_id) VALUES(7, '2022-04-01 08:30:09.699', 'The things I do for love.', 'Currants', 7.31, '2022-04-01 08:30:09.699', 1);
INSERT INTO transactions (id, created_at, notes, title, total, updated_at, user_id) VALUES(8, '2022-04-01 08:30:09.716', 'When the snows fall and the white winds blow, the lone wolf dies but the pack survives.', 'Mangosteens', 13.73, '2022-04-01 08:30:09.716', 2);
INSERT INTO transactions (id, created_at, notes, title, total, updated_at, user_id) VALUES(9, '2022-04-01 08:30:09.727', 'The things I do for love.', 'Blackberries', 6.4, '2022-04-01 08:30:09.727', 2);
INSERT INTO transactions (id, created_at, notes, title, total, updated_at, user_id) VALUES(10, '2022-04-01 08:30:09.741', 'All dwarfs are bastards in their father''s eyes', 'Bush Tomato', 11.1, '2022-04-01 08:30:09.741', 2);
INSERT INTO transactions (id, created_at, notes, title, total, updated_at, user_id) VALUES(11, '2022-04-01 08:30:09.753', 'Things are not always as they seemed, much that may seem evil can be good.', 'Bush Tomato', 11.8, '2022-04-01 08:30:09.753', 2);

INSERT INTO transaction_categories (transaction_id, category_id) VALUES(3, 3);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(2, 1);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(4, 5);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(6, 7);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(7, 8);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(8, 9);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(10, 2);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(5, 1);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(11, 4);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(11, 3);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(11, 8);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(1, 4);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(1, 8);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(5, 8);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(6, 9);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(5, 5);
INSERT INTO transaction_categories (transaction_id, category_id) VALUES(9, 5);



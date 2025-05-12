-- Populate database with test data script
-- Oracle Database

-- Setting timestamp format
ALTER SESSION SET NLS_TIMESTAMP_FORMAT = 'YYYY-MM-DD HH24:MI:SS';

-- Populate Permissions table
INSERT INTO Permissions (permission_name) VALUES ('Create Topic');
INSERT INTO Permissions (permission_name) VALUES ('Edit Topic');
INSERT INTO Permissions (permission_name) VALUES ('Delete Topic');
INSERT INTO Permissions (permission_name) VALUES ('Create Comment');
INSERT INTO Permissions (permission_name) VALUES ('Edit Comment');
INSERT INTO Permissions (permission_name) VALUES ('Delete Comment');
INSERT INTO Permissions (permission_name) VALUES ('Moderate Users');
INSERT INTO Permissions (permission_name) VALUES ('View Admin Panel');
INSERT INTO Permissions (permission_name) VALUES ('Manage Categories');
INSERT INTO Permissions (permission_name) VALUES ('Send Messages');

-- Populate Roles_Permissions table
-- Administrator has all permissions
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 1);
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 2);
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 3);
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 7);
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 8);
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 9);
-- Moderator permissions
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (2, 3);
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (2, 6);
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (2, 7);
-- User permissions
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (3, 1);

-- Populate User table
INSERT INTO "User" (username, email, password, registration_date, role_id)
VALUES ('admin', 'admin@forum.com', 'AdminPass123!', TIMESTAMP '2024-01-01 10:00:00', 1);

INSERT INTO "User" (username, email, password, registration_date, role_id)
VALUES ('moderator1', 'mod1@forum.com', 'ModPass123!', TIMESTAMP '2024-01-05 14:30:00', 2);

INSERT INTO "User" (username, email, password, registration_date, role_id)
VALUES ('moderator2', 'mod2@forum.com', 'ModPass456!', TIMESTAMP '2024-01-07 09:15:00', 2);

INSERT INTO "User" (username, email, password, registration_date, role_id)
VALUES ('john_doe', 'john@example.com', 'UserPass123!', TIMESTAMP '2024-01-10 11:20:00', 3);

INSERT INTO "User" (username, email, password, registration_date, role_id)
VALUES ('jane_smith', 'jane@example.com', 'UserPass456!', TIMESTAMP '2024-01-12 13:45:00', 3);

INSERT INTO "User" (username, email, password, registration_date, role_id)
VALUES ('mike_wilson', 'mike@example.com', 'UserPass789!', TIMESTAMP '2024-01-15 16:30:00', 3);

INSERT INTO "User" (username, email, password, registration_date, role_id)
VALUES ('sarah_jones', 'sarah@example.com', 'UserPass101!', TIMESTAMP '2024-01-18 08:00:00', 3);

INSERT INTO "User" (username, email, password, registration_date, role_id)
VALUES ('tom_brown', 'tom@example.com', 'UserPass202!', TIMESTAMP '2024-01-20 17:15:00', 3);

INSERT INTO "User" (username, email, password, registration_date, role_id)
VALUES ('lisa_davis', 'lisa@example.com', 'UserPass303!', TIMESTAMP '2024-01-22 12:00:00', 3);

INSERT INTO "User" (username, email, password, registration_date, role_id)
VALUES ('alex_miller', 'alex@example.com', 'UserPass404!', TIMESTAMP '2024-01-25 10:30:00', 3);

-- Populate Category table
INSERT INTO Category (name, description) VALUES ('General Discussion', 'General topics and conversations');
INSERT INTO Category (name, description) VALUES ('Tech Support', 'Technical support and help');
INSERT INTO Category (name, description) VALUES ('Programming', 'Programming languages and development');
INSERT INTO Category (name, description) VALUES ('Hardware', 'Computer hardware discussions');
INSERT INTO Category (name, description) VALUES ('Gaming', 'Video games and gaming culture');
INSERT INTO Category (name, description) VALUES ('Movies & TV', 'Movies and television shows discussion');
INSERT INTO Category (name, description) VALUES ('Music', 'Music discussion and recommendations');
INSERT INTO Category (name, description) VALUES ('Sports', 'Sports news and discussions');
INSERT INTO Category (name, description) VALUES ('Science', 'Scientific discussions and discoveries');
INSERT INTO Category (name, description) VALUES ('Off-Topic', 'Random and off-topic conversations');

-- Populate Topic table
INSERT INTO Topic (title, content, created_at, updated_at, user_id, category_id)
VALUES ('Welcome to Our Forum!', 'This is the official welcome thread. Please introduce yourself!',
        TIMESTAMP '2024-02-01 09:00:00', TIMESTAMP '2024-02-01 09:00:00', 1, 1);

INSERT INTO Topic (title, content, created_at, updated_at, user_id, category_id)
VALUES ('Best Programming Languages in 2024', 'Let''s discuss the most popular programming languages this year.',
        TIMESTAMP '2024-02-02 10:30:00', TIMESTAMP '2024-02-02 10:30:00', 4, 3);

INSERT INTO Topic (title, content, created_at, updated_at, user_id, category_id)
VALUES ('PC Build Recommendations', 'Looking for advice on building a new gaming PC. Budget is $1500.',
        TIMESTAMP '2024-02-03 14:15:00', TIMESTAMP '2024-02-03 14:15:00', 5, 4);

INSERT INTO Topic (title, content, created_at, updated_at, user_id, category_id)
VALUES ('Favorite Games of All Time', 'Share your top 5 favorite games of all time!',
        TIMESTAMP '2024-02-04 11:00:00', TIMESTAMP '2024-02-04 11:00:00', 6, 5);

INSERT INTO Topic (title, content, created_at, updated_at, user_id, category_id)
VALUES ('Latest Marvel Movies Discussion', 'What do you think about the recent Marvel releases?',
        TIMESTAMP '2024-02-05 16:45:00', TIMESTAMP '2024-02-05 16:45:00', 7, 6);

INSERT INTO Topic (title, content, created_at, updated_at, user_id, category_id)
VALUES ('Rock Music Recommendations', 'Looking for some good rock bands to listen to.',
        TIMESTAMP '2024-02-06 13:30:00', TIMESTAMP '2024-02-06 13:30:00', 8, 7);

INSERT INTO Topic (title, content, created_at, updated_at, user_id, category_id)
VALUES ('NBA Season Discussion', 'Who do you think will win the championship this year?',
        TIMESTAMP '2024-02-07 18:00:00', TIMESTAMP '2024-02-07 18:00:00', 9, 8);

INSERT INTO Topic (title, content, created_at, updated_at, user_id, category_id)
VALUES ('Space Exploration Updates', 'Latest news about NASA and SpaceX missions.',
        TIMESTAMP '2024-02-08 09:45:00', TIMESTAMP '2024-02-08 09:45:00', 10, 9);

INSERT INTO Topic (title, content, created_at, updated_at, user_id, category_id)
VALUES ('Random Thoughts Thread', 'Share your random thoughts and musings here!',
        TIMESTAMP '2024-02-09 12:15:00', TIMESTAMP '2024-02-09 12:15:00', 4, 10);

-- Deleted topic for testing soft delete
INSERT INTO Topic (title, content, created_at, updated_at, user_id, category_id, deleted_at)
VALUES ('Deleted Topic', 'This topic has been deleted',
        TIMESTAMP '2024-02-10 10:00:00', TIMESTAMP '2024-02-10 10:00:00', 5, 1, TIMESTAMP '2024-02-11 11:00:00');

-- Populate Tag table
INSERT INTO Tag (name) VALUES ('announcement');
INSERT INTO Tag (name) VALUES ('help');
INSERT INTO Tag (name) VALUES ('discussion');
INSERT INTO Tag (name) VALUES ('tutorial');
INSERT INTO Tag (name) VALUES ('news');
INSERT INTO Tag (name) VALUES ('review');
INSERT INTO Tag (name) VALUES ('question');
INSERT INTO Tag (name) VALUES ('guide');
INSERT INTO Tag (name) VALUES ('tip');
INSERT INTO Tag (name) VALUES ('update');

-- Populate Topic_Tag table
INSERT INTO Topic_Tag (topic_id, tag_id) VALUES (1, 1);
INSERT INTO Topic_Tag (topic_id, tag_id) VALUES (1, 3);
INSERT INTO Topic_Tag (topic_id, tag_id) VALUES (2, 3);
INSERT INTO Topic_Tag (topic_id, tag_id) VALUES (2, 7);
INSERT INTO Topic_Tag (topic_id, tag_id) VALUES (3, 2);
INSERT INTO Topic_Tag (topic_id, tag_id) VALUES (3, 7);
INSERT INTO Topic_Tag (topic_id, tag_id) VALUES (4, 3);
INSERT INTO Topic_Tag (topic_id, tag_id) VALUES (5, 3);
INSERT INTO Topic_Tag (topic_id, tag_id) VALUES (5, 6);
INSERT INTO Topic_Tag (topic_id, tag_id) VALUES (8, 5);

-- Populate Comment table
INSERT INTO Comment_ (content, created_at, user_id, topic_id)
VALUES ('Welcome everyone! Glad to have you here.', TIMESTAMP '2024-02-01 10:00:00', 2, 1);

INSERT INTO Comment_ (content, created_at, user_id, topic_id)
VALUES ('I think Python is still the best for beginners.', TIMESTAMP '2024-02-02 11:00:00', 5, 2);

INSERT INTO Comment_ (content, created_at, user_id, topic_id)
VALUES ('JavaScript is essential for web development though.', TIMESTAMP '2024-02-02 11:30:00', 6, 2);

INSERT INTO Comment_ (content, created_at, user_id, topic_id)
VALUES ('For that budget, I recommend AMD Ryzen 7.', TIMESTAMP '2024-02-03 15:00:00', 7, 3);

INSERT INTO Comment_ (content, created_at, user_id, topic_id)
VALUES ('Don''t forget a good cooling system!', TIMESTAMP '2024-02-03 15:30:00', 8, 3);

INSERT INTO Comment_ (content, created_at, user_id, topic_id)
VALUES ('The Witcher 3 is definitely in my top 5.', TIMESTAMP '2024-02-04 12:00:00', 9, 4);

INSERT INTO Comment_ (content, created_at, user_id, topic_id)
VALUES ('I loved the latest Spider-Man movie!', TIMESTAMP '2024-02-05 17:00:00', 10, 5);

INSERT INTO Comment_ (content, created_at, user_id, topic_id)
VALUES ('Check out Led Zeppelin if you haven''t already.', TIMESTAMP '2024-02-06 14:00:00', 4, 6);

INSERT INTO Comment_ (content, created_at, user_id, topic_id)
VALUES ('Lakers are looking strong this season.', TIMESTAMP '2024-02-07 18:30:00', 5, 7);

-- Deleted comment for testing soft delete
INSERT INTO Comment_ (content, created_at, user_id, topic_id, deleted_at)
VALUES ('This comment has been deleted', TIMESTAMP '2024-02-08 10:00:00', 6, 8, TIMESTAMP '2024-02-08 11:00:00');

-- Populate Attachment table
INSERT INTO Attachment (file_path, uploaded_at, topic_id)
VALUES ('/uploads/welcome_banner.jpg', TIMESTAMP '2024-02-01 09:00:00', 1);

INSERT INTO Attachment (file_path, uploaded_at, topic_id)
VALUES ('/uploads/programming_chart.png', TIMESTAMP '2024-02-02 10:30:00', 2);

INSERT INTO Attachment (file_path, uploaded_at, topic_id)
VALUES ('/uploads/pc_specs.pdf', TIMESTAMP '2024-02-03 14:15:00', 3);

INSERT INTO Attachment (file_path, uploaded_at, comment_id)
VALUES ('/uploads/build_photo.jpg', TIMESTAMP '2024-02-03 15:00:00', 4);

INSERT INTO Attachment (file_path, uploaded_at, topic_id)
VALUES ('/uploads/game_list.txt', TIMESTAMP '2024-02-04 11:00:00', 4);

INSERT INTO Attachment (file_path, uploaded_at, comment_id)
VALUES ('/uploads/review_image.png', TIMESTAMP '2024-02-05 17:00:00', 7);

INSERT INTO Attachment (file_path, uploaded_at, topic_id)
VALUES ('/uploads/band_logo.jpg', TIMESTAMP '2024-02-06 13:30:00', 6);

INSERT INTO Attachment (file_path, uploaded_at, topic_id)
VALUES ('/uploads/stats_2024.xlsx', TIMESTAMP '2024-02-07 18:00:00', 7);

INSERT INTO Attachment (file_path, uploaded_at, topic_id)
VALUES ('/uploads/space_photo.jpg', TIMESTAMP '2024-02-08 09:45:00', 8);

INSERT INTO Attachment (file_path, uploaded_at, comment_id)
VALUES ('/uploads/diagram.png', TIMESTAMP '2024-02-08 10:00:00', 9);

-- Populate Message table
INSERT INTO Message (content, sent_at, sender_id, recipient_id)
VALUES ('Welcome to the forum!', TIMESTAMP '2024-02-01 09:30:00', 1, 4);

INSERT INTO Message (content, sent_at, sender_id, recipient_id)
VALUES ('Thanks for joining!', TIMESTAMP '2024-02-01 10:00:00', 2, 5);

INSERT INTO Message (content, sent_at, sender_id, recipient_id)
VALUES ('Please check the rules.', TIMESTAMP '2024-02-01 11:00:00', 2, 6);

INSERT INTO Message (content, sent_at, sender_id, recipient_id)
VALUES ('Great post!', TIMESTAMP '2024-02-02 12:00:00', 4, 5);

INSERT INTO Message (content, sent_at, sender_id, recipient_id)
VALUES ('Thanks for the help!', TIMESTAMP '2024-02-03 15:15:00', 5, 7);

INSERT INTO Message (content, sent_at, sender_id, recipient_id)
VALUES ('Let''s collaborate on a project.', TIMESTAMP '2024-02-04 14:00:00', 6, 7);

INSERT INTO Message (content, sent_at, sender_id, recipient_id)
VALUES ('Sure, sounds good!', TIMESTAMP '2024-02-04 14:30:00', 7, 6);

INSERT INTO Message (content, sent_at, sender_id, recipient_id)
VALUES ('Check out my latest post.', TIMESTAMP '2024-02-05 16:00:00', 8, 9);

INSERT INTO Message (content, sent_at, sender_id, recipient_id)
VALUES ('Nice article!', TIMESTAMP '2024-02-06 09:00:00', 9, 10);

INSERT INTO Message (content, sent_at, sender_id, recipient_id)
VALUES ('Meeting tomorrow at 3?', TIMESTAMP '2024-02-07 17:00:00', 10, 1);

-- Populate Notification table
INSERT INTO Notification (content, created_at, user_id)
VALUES ('Welcome to the forum!', TIMESTAMP '2024-02-01 09:00:00', 4);

INSERT INTO Notification (content, created_at, user_id)
VALUES ('You have a new message', TIMESTAMP '2024-02-01 09:30:00', 4);

INSERT INTO Notification (content, created_at, user_id)
VALUES ('Someone commented on your topic', TIMESTAMP '2024-02-02 11:00:00', 4);

INSERT INTO Notification (content, created_at, read_at, user_id)
VALUES ('Your post was liked', TIMESTAMP '2024-02-03 14:00:00', TIMESTAMP '2024-02-03 15:00:00', 5);

INSERT INTO Notification (content, created_at, user_id)
VALUES ('New reply to your comment', TIMESTAMP '2024-02-04 12:00:00', 6);

INSERT INTO Notification (content, created_at, read_at, user_id)
VALUES ('Weekly digest available', TIMESTAMP '2024-02-05 08:00:00', TIMESTAMP '2024-02-05 09:00:00', 7);

INSERT INTO Notification (content, created_at, user_id)
VALUES ('Your topic was featured', TIMESTAMP '2024-02-06 10:00:00', 8);

INSERT INTO Notification (content, created_at, user_id)
VALUES ('New follower', TIMESTAMP '2024-02-07 14:00:00', 9);

INSERT INTO Notification (content, created_at, read_at, user_id)
VALUES ('System maintenance scheduled', TIMESTAMP '2024-02-08 07:00:00', TIMESTAMP '2024-02-08 08:00:00', 10);

INSERT INTO Notification (content, created_at, user_id)
VALUES ('Password expires in 7 days', TIMESTAMP '2024-02-09 09:00:00', 1);

-- Populate Moderation_Log table
INSERT INTO Moderation_Log (action, created_at, user_id, target_id, target_type)
VALUES ('Deleted spam comment', TIMESTAMP '2024-02-02 14:00:00', 2, 10, 'comment');

INSERT INTO Moderation_Log (action, created_at, user_id, target_id, target_type)
VALUES ('Moved topic to correct category', TIMESTAMP '2024-02-03 09:00:00', 2, 3, 'topic');

INSERT INTO Moderation_Log (action, created_at, user_id, target_id, target_type)
VALUES ('Warned user for inappropriate content', TIMESTAMP '2024-02-04 16:00:00', 2, 7, 'user');

INSERT INTO Moderation_Log (action, created_at, user_id, target_id, target_type)
VALUES ('Closed duplicate topic', TIMESTAMP '2024-02-05 11:00:00', 3, 10, 'topic');

INSERT INTO Moderation_Log (action, created_at, user_id, target_id, target_type)
VALUES ('Banned user for spamming', TIMESTAMP '2024-02-06 13:00:00', 1, 11, 'user');

INSERT INTO Moderation_Log (action, created_at, user_id, target_id, target_type)
VALUES ('Restored deleted comment', TIMESTAMP '2024-02-07 10:00:00', 1, 10, 'comment');

INSERT INTO Moderation_Log (action, created_at, user_id, target_id, target_type)
VALUES ('Edited inappropriate content', TIMESTAMP '2024-02-08 15:00:00', 3, 5, 'topic');

INSERT INTO Moderation_Log (action, created_at, user_id, target_id, target_type)
VALUES ('Merged duplicate topics', TIMESTAMP '2024-02-09 12:00:00', 2, 6, 'topic');

INSERT INTO Moderation_Log (action, created_at, user_id, target_id, target_type)
VALUES ('Removed offensive image', TIMESTAMP '2024-02-10 09:00:00', 2, 7, 'attachment');

INSERT INTO Moderation_Log (action, created_at, user_id, target_id, target_type)
VALUES ('Issued temporary ban', TIMESTAMP '2024-02-11 14:00:00', 1, 8, 'user');

-- Populate Topic_Rating table
INSERT INTO Topic_Rating (score, user_id, topic_id) VALUES (5, 4, 1);
INSERT INTO Topic_Rating (score, user_id, topic_id) VALUES (4, 5, 2);
INSERT INTO Topic_Rating (score, user_id, topic_id) VALUES (5, 6, 2);
INSERT INTO Topic_Rating (score, user_id, topic_id) VALUES (3, 7, 3);
INSERT INTO Topic_Rating (score, user_id, topic_id) VALUES (4, 8, 3);
INSERT INTO Topic_Rating (score, user_id, topic_id) VALUES (5, 9, 4);
INSERT INTO Topic_Rating (score, user_id, topic_id) VALUES (4, 10, 4);
INSERT INTO Topic_Rating (score, user_id, topic_id) VALUES (5, 4, 5);
INSERT INTO Topic_Rating (score, user_id, topic_id) VALUES (3, 5, 6);
INSERT INTO Topic_Rating (score, user_id, topic_id) VALUES (4, 6, 7);

COMMIT;
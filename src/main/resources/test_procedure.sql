-- Test the InsertTopicWithDetails procedure
DECLARE
v_topic_id INTEGER;
    v_tags VARCHAR2_ARRAY;
    v_success NUMBER;
    v_error_msg VARCHAR2(4000);
BEGIN
    -- Test 1: Successful topic creation with tags
    v_tags := VARCHAR2_ARRAY('database', 'oracle', 'plsql');
    InsertTopicWithDetails(
        p_topic_id => v_topic_id,
        p_category_id => 3,
        p_user_id => 4,
        p_title => 'How to create stored procedures in Oracle',
        p_content => 'I am learning PL/SQL and want to understand how to create stored procedures.',
        p_tags_array => v_tags,
        p_attachment_filename => 'example.sql',
        p_attachment_path => '/uploads',
        p_success => v_success
    );

    IF v_success = 1 THEN
        -- Record success
        INSERT INTO Topic (title, content, user_id, category_id, created_at, deleted_at)
        VALUES ('TEST RESULT: Success! Topic ID: ' || v_topic_id, 'Test 1 passed', 1, 1, SYSTIMESTAMP, SYSTIMESTAMP);
COMMIT;
END IF;

    -- Test 2: Topic with bad words (should fail)
    v_tags := VARCHAR2_ARRAY('test', 'bad');
    InsertTopicWithDetails(
        p_topic_id => v_topic_id,
        p_category_id => 1,
        p_user_id => 5,
        p_title => 'This is spam content',
        p_content => 'This post contains spam and should be blocked.',
        p_tags_array => v_tags,
        p_attachment_filename => NULL,
        p_attachment_path => NULL,
        p_success => v_success
    );

    IF v_success = 0 THEN
        -- Record expected failure
        INSERT INTO Topic (title, content, user_id, category_id, created_at, deleted_at)
        VALUES ('TEST RESULT: Failed as expected (bad words)', 'Test 2 passed', 1, 1, SYSTIMESTAMP, SYSTIMESTAMP);
COMMIT;
END IF;

    -- Test 3: Topic without tags and attachment
    InsertTopicWithDetails(
        p_topic_id => v_topic_id,
        p_category_id => 1,
        p_user_id => 6,
        p_title => 'Simple topic without extras',
        p_content => 'This is a simple topic with no tags or attachments.',
        p_tags_array => NULL,
        p_attachment_filename => NULL,
        p_attachment_path => NULL,
        p_success => v_success
    );

    IF v_success = 1 THEN
        INSERT INTO Topic (title, content, user_id, category_id, created_at, deleted_at)
        VALUES ('TEST RESULT: Success! Topic ID: ' || v_topic_id, 'Test 3 passed', 1, 1, SYSTIMESTAMP, SYSTIMESTAMP);
COMMIT;
END IF;

EXCEPTION
    WHEN OTHERS THEN
        -- Capture error message
        v_error_msg := SUBSTR(SQLERRM, 1, 4000);
        -- Record error
INSERT INTO Topic (title, content, user_id, category_id, created_at, deleted_at)
VALUES ('TEST ERROR', v_error_msg, 1, 1, SYSTIMESTAMP, SYSTIMESTAMP);
COMMIT;
END;
/

-- View test results
SELECT title, content
FROM Topic
WHERE title LIKE 'TEST%'
  AND deleted_at IS NOT NULL
ORDER BY created_at DESC;

-- Verify the actual topics created by tests
SELECT t.topic_id, t.title, u.username, c.name as category,
       TO_CHAR(t.created_at, 'DD.MM.YYYY HH24:MI:SS') as created
FROM Topic t
         JOIN "User" u ON t.user_id = u.user_id
         JOIN Category c ON t.category_id = c.category_id
WHERE t.created_at >= SYSDATE - 1/24
  AND t.deleted_at IS NULL
ORDER BY t.created_at DESC;

-- Check tags for recent topics
SELECT t.topic_id, t.title, tg.name as tag_name
FROM Topic t
         JOIN Topic_Tag tt ON t.topic_id = tt.topic_id
         JOIN Tag tg ON tt.tag_id = tg.tag_id
WHERE t.created_at >= SYSDATE - 1/24
  AND t.deleted_at IS NULL
ORDER BY t.topic_id, tg.name;

-- Check comments for recent topics
SELECT t.topic_id, t.title, SUBSTR(c.content, 1, 50) as comment_preview
FROM Comment_ c
         JOIN Topic t ON c.topic_id = t.topic_id
WHERE c.created_at >= SYSDATE - 1/24
  AND t.deleted_at IS NULL
ORDER BY c.created_at DESC;

-- Check attachments for recent topics
SELECT t.topic_id, t.title, a.file_path
FROM Attachment a
         JOIN Topic t ON a.topic_id = t.topic_id
WHERE a.uploaded_at >= SYSDATE - 1/24
  AND t.deleted_at IS NULL
ORDER BY a.uploaded_at DESC;

-- Clean up test results (optional)
--DELETE FROM Topic WHERE title LIKE 'TEST%' AND deleted_at IS NOT NULL;
--COMMIT;
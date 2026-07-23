ALTER TABLE reviews
    ADD COLUMN updated_at TIMESTAMP;

UPDATE reviews
SET updated_at = created_at
WHERE updated_at IS NULL;
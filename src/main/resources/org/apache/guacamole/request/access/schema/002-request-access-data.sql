-- Request Access Extension Initial Data
-- This script populates the extension with initial configuration data

-- Insert default admin permissions if they don't exist
INSERT IGNORE INTO guacamole_system_permission (entity_id, permission) 
SELECT e.entity_id, 'ADMINISTER'
FROM guacamole_entity e
WHERE e.type = 'USER' AND e.name IN (SELECT username FROM guacamole_user WHERE disabled = 0)
LIMIT 1;

-- Note: The actual permission management for this extension will be handled
-- through the extension's own permission system and configuration.
-- This is a placeholder for any initial data that might be needed.
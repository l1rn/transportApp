-- user indexes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

-- route indexes
CREATE INDEX idx_routes_from ON routes(route_from);
CREATE INDEX idx_routes_to ON routes(route_to);
CREATE INDEX idx_routes_transport ON routes(transport);

-- token indexes
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_device_id ON refresh_tokens(device_id);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);
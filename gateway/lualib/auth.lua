
local jwt = require "resty.jwt"

local JWT_SECRET = "4ca9f2b1d0481c33acb8dcbeaf08a6d1aba6cc2e0a24401493365361308677998b291cda035f59e1587fa3b847e477b4e9baf200e9a748856845c3f65c71db98"

local function authenticate()
    local cookie_header = ngx.req.get_headers()['Cookie']
    if not cookie_header then
        return false, "No cookies found"
    end

    local accessToken = cookie_header:match("accessToken=([^;+])")
    local refreshToken = cookie_header:match("refreshToken=([^;+])")

    if not accessToken then
        return false, "Missing accessToken"
    end

    if not refreshToken then
        return false, "Missing refreshToken"
    end

    local jwt_access_obj = jwt:verify(JWT_SECRET, accessToken)
    local jwt_refresh_obj = jwt:verify(JWT_SECRET, refreshToken)

    if not jwt_access_obj.verified then
        return false, "Invalid access token: " .. (jwt_access_obj.reason or "unknown error")
    end

    if not jwt_refresh_obj.verified then
        return false, "Invalid refresh token: " .. (jwt_refresh_obj.verified or "unknown error")
    end

    return true, "Authentication successful"
end

local auth_ok, auth_msg = authenticate()
if not auth_ok then
    ngx.log(ngx.ERR, "Authentication failed: ", auth_msg)
    ngx.exit(401)
end

ngx.log(ngx.INFO, "Authentication passed: ", auth_msg)
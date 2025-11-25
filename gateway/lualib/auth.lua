
local jwt = require "resty.jwt"

local auth_cookies = ngx.req.get_headers()['Set-Cookie']
if not auth_cookies then
    return ngx.exit(401)
end

local _, accessToken = auth_cookies:match("accessToken")
local _, refreshToken = auth_cookies:match("refreshToken")

local jwt_access_obj = jwt:verify("4ca9f2b1d0481c33acb8dcbeaf08a6d1aba6cc2e0a24401493365361308677998b291cda035f59e1587fa3b847e477b4e9baf200e9a748856845c3f65c71db98", accessToken)
local jwt_refresh_obj = jwt:verify("4ca9f2b1d0481c33acb8dcbeaf08a6d1aba6cc2e0a24401493365361308677998b291cda035f59e1587fa3b847e477b4e9baf200e9a748856845c3f65c71db98", refreshToken)

if not jwt_access_obj or jwt_refresh_obj then
    return ngx.exit(401)
end
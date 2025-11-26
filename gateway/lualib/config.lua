local _M = {}

function _M.get_app_root()
    local env_root = os.getenv("GATEWAY_ROOT")
    if env_root then
        return env_root
    end

    local handle = io.popen("pwd")
    if (handle == nil or handle == '') then
        return nil
    end
    local result = handle.read("*a")
    handle:close()

    return result:gsub("\n", "") or "."
end

function _M.load_config()
    local root = _M.get_app_root()
    package.path = root .. "/lualib/?.lua;" .. package.path
    return root
end

return _M
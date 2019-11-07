local value = ARGV[1]
local oldValue = redis.call('get',KEYS[1])
if(not(oldValue)) then return 1 end
if(value == oldValue)
then
    redis.call('del',KEYS[1])
    return 1
else
    return 0
end


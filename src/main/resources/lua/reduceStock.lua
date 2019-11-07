local orderCount = ARGV[1]
local stock = redis.call('hget',KEYS[1],KEYS[2])
if(not(stock)) then return 0 end
if(stock >= orderCount)
then
    redis.call('hset',KEYS[1],KEYS[2],stock-orderCount)
    return 1
else
    return 0
end


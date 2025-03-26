//have functions to deal with path
const path = require('path')

//to make a path that will work on windows and linux
const makeP = path.join(__dirname, 'www', 'videos')// pwd/www/videos

//it contains tons of utility functions
const util = require('util')

util.debug('hupa')

//you can the statistics and other info related to heap
const v8 = require('v8')

util.debug(v8.getHeapCodeStatistics().toString())

//readline it's a wrapper around the stdin and stdout
const readLine = require('readline')

const rl = readLine.createInterface({
  input: process.stdin,
  output: process.stdout
})

rl.write('How are you doing')

rl.question('username', (input) => {
  util.debug(input)
})
const path = require('path')

console.log(__dirname)//current directory path
console.log(__filename)//current file path

console.log(`File name is ${path.basename(__filename)}}`)//only file name
//----------------Process--------------------------------------------------
//access the process
console.log(process.pid)
console.log(process.versions.node) //version of node used to run the process
console.log(process.argv)//args passed using terminal

//process.exit()// to forcefully exit the program

//-----------------StandardIn and StandardOut--------------------------------
process.stdout //to write to the terminal console.log uses it under the hood,
//but it doesn't cause line break like console.log
process.stdout.write('hukkaa')

process.stdin// to take input from the terminal

// reading the data from the terminal
process.stdin.on('data', input => {
  process.stdout.write(input.toString())
  process.exit()
})
//-----------------Timers---------------------------------------------------

//execute one time after specified the delay
const t = setTimeout(() => console.log('DONE'), 100)
clearTimeout(t)

//execute one time immediately(when stack is empty)
const i = setImmediate(() => console.log('SONE'))
clearImmediate(i)

//keeps on executing after specified the delay
const k = setInterval(() => console.log('INONE'), 1000)
clearInterval(k)
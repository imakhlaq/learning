const fs = require('fs')

//read all the files in a folder

//read all the files in synchronously
const files = fs.readdirSync('./')

//read all the files asynchronously
fs.readdir('./', (err, files) => console.log(files))

//to read content of a file
const content = fs.readFileSync('./01_node_Intro.js', { encoding: 'UTF8' })
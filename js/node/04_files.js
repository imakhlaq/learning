const fs = require('fs')

//-----read all the files in a folder---------------

//read all the files in synchronously
const files = fs.readdirSync('./')

//read all the files asynchronously
fs.readdir('./', (err, files) => console.log(files))

//-------to read content of a file (you can also read binary files too)--------

//all content of a file Sync
const content = fs.readFileSync('./01_node_Intro.js', { encoding: 'UTF8' })

//all content of a file Async
fs.readFile('./01_node_Intro.js', { encoding: 'utf8' }, (err, content) => console.log(content))

//all content of a binary file Async
fs.readFile('./earth.jpg', (image) => console.log(image))

//------- Rename a file and delete a file
fs.rename('./earth.txt', './mars.txt', err => console.log(err))

//------- delete a file
fs.unlink('./earth.txt', err => console.log(err))

//------Write to a file------------------------------------

fs.writeFile('./earth.txt', 'This is my earth', err => console.log(err))

//------Append to a file------------------------------------

fs.appendFile('/earth.txt', 'This is my earth', err => console.log(err))

//--------- create a directory or delete a directory

if (!fs.existsSync('./dad')) {
  fs.mkdir('./dad', err => console.log(err))//crate a directory
}
fs.rmdir('./dad', err => console.log(err))//to delete a directory